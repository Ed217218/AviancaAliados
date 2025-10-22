package Avianca.Utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
//import org.openqa.selenium.devtools.v85.network.model.Request;
//import org.openqa.selenium.devtools.v85.network.model.Response;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ApiErrorCapture {
    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(ApiErrorCapture.class);
    private DevTools devTools;
    private ConcurrentMap<String, ApiRequest> solicitudesPendientes = new ConcurrentHashMap<>();
    private List<NetworkInteraction> interaccionesCapturadas = new CopyOnWriteArrayList<>();
    private boolean cdpInicializado = false;
    
    // Configuración para habilitar/deshabilitar CDP mediante propiedad del sistema
    private static final boolean CDP_ENABLED = Boolean.parseBoolean(
        System.getProperty("cdp.enabled", "false")
    );

    public ApiErrorCapture(WebDriver driver) {
        this.driver = driver;
        
        // Verificar si CDP está habilitado por configuración
        if (!CDP_ENABLED) {
            logger.info("ℹ️ CDP deshabilitado por configuración (cdp.enabled=false)");
            cdpInicializado = false;
            return;
        }
        
        // Intentar inicializar CDP si es Chrome - NO BLOQUEANTE
        if (driver instanceof ChromeDriver) {
            try {
                this.devTools = ((ChromeDriver) driver).getDevTools();
                inicializarCDP();
            } catch (Exception e) {
                logger.info("ℹ️ CDP no disponible para esta versión de Chrome. Las pruebas continuarán normalmente.");
                logger.debug("Detalle del error CDP: {}", e.getMessage());
                cdpInicializado = false;
            }
        } else {
            logger.debug("⚠️ El navegador no es Chrome. CDP no estará disponible.");
            cdpInicializado = false;
        }
    }

    /**
     * Inicializa Chrome DevTools para capturar solicitudes de red
     */
    private void inicializarCDP() {
        try {
            if (devTools == null) {
                logger.error("❌ DevTools no está disponible");
                return;
            }
            
            // Crear sesión de DevTools
            devTools.createSession();
            logger.info("✅ Sesión de DevTools creada");
            
            // Habilitar el dominio Network
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            logger.info("✅ Dominio Network habilitado");
            
            // Escuchar solicitudes de red
            devTools.addListener(Network.requestWillBeSent(), request -> {
                try {
                    ApiRequest apiRequest = new ApiRequest();
                    apiRequest.setUrl(request.getRequest().getUrl());
                    apiRequest.setMethod(request.getRequest().getMethod());
                    apiRequest.setRequestId(request.getRequestId().toString());
                    
                    // Guardar la solicitud pendiente
                    solicitudesPendientes.put(request.getRequestId().toString(), apiRequest);
                    
                    logger.debug("📤 Solicitud capturada: {} {}", request.getRequest().getMethod(), request.getRequest().getUrl());
                } catch (Exception e) {
                    logger.error("❌ Error al procesar solicitud: {}", e.getMessage());
                }
            });
            
            // Escuchar respuestas de red
            devTools.addListener(Network.responseReceived(), response -> {
                try {
                    ApiResponse apiResponse = new ApiResponse();
                    apiResponse.setUrl(response.getResponse().getUrl());
                    apiResponse.setStatus(response.getResponse().getStatus());
                    apiResponse.setStatusText(response.getResponse().getStatusText());
                    apiResponse.setRequestId(response.getRequestId().toString());
                    
                    // Asociar con la solicitud correspondiente
                    if (solicitudesPendientes.containsKey(response.getRequestId().toString())) {
                        apiResponse.setRequest(solicitudesPendientes.get(response.getRequestId().toString()));
                        solicitudesPendientes.remove(response.getRequestId().toString());
                    }
                    
                    // Obtener el cuerpo de la respuesta
                    try {
                        devTools.send(Network.getResponseBody(response.getRequestId()));
                    } catch (Exception e) {
                        logger.debug("⚠️ No se pudo obtener el cuerpo de la respuesta: {}", e.getMessage());
                    }
                    
                    // Agregar a las interacciones capturadas
                    NetworkInteraction interaccion = new NetworkInteraction(
                        apiResponse.getRequest() != null ? apiResponse.getRequest() : new ApiRequest(),
                        apiResponse
                    );
                    interaccionesCapturadas.add(interaccion);
                    
                    logger.debug("📥 Respuesta capturada: {} - {}", response.getResponse().getStatus(), response.getResponse().getUrl());
                } catch (Exception e) {
                    logger.error("❌ Error al procesar respuesta: {}", e.getMessage());
                }
            });
            
            // Escuchar cuerpos de respuesta
            devTools.addListener(Network.responseReceived(), response -> {
                try {
                    // Intentar obtener el cuerpo de la respuesta
                    String responseBody = devTools.send(Network.getResponseBody(response.getRequestId())).getBody();
                    if (responseBody != null && !responseBody.isEmpty()) {
                        // Buscar la interacción correspondiente y actualizar el cuerpo
                        for (NetworkInteraction interaccion : interaccionesCapturadas) {
                            if (interaccion.getResponse().getRequestId().equals(response.getRequestId().toString())) {
                                interaccion.getResponse().setBody(responseBody);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.debug("⚠️ No se pudo obtener el cuerpo de la respuesta: {}", e.getMessage());
                }
            });
            
            cdpInicializado = true;
            logger.info("✅ Chrome DevTools inicializado correctamente");
        } catch (Exception e) {
            // CDP no disponible para Chrome 141+ - esto es normal y esperado
            logger.debug("⚠️ CDP no disponible (Chrome 141 requiere selenium-devtools-v141): {}", e.getMessage());
            cdpInicializado = false;
        }
    }

    /**
     * Limpia los logs de red antes de la prueba
     */
    public void limpiarLogsRed() {
        try {
            if (devTools != null && cdpInicializado) {
                interaccionesCapturadas.clear();
                solicitudesPendientes.clear();
                logger.info("✅ Logs de CDP limpiados");
            }
            
            try {
                driver.manage().logs().get(LogType.PERFORMANCE);
                logger.info("✅ Logs de rendimiento limpiados");
            } catch (Exception e) {
                logger.debug("⚠️ No se pudieron limpiar los logs de rendimiento: {}", e.getMessage());
            }
        } catch (Exception e) {
            logger.error("⚠️ No se pudieron limpiar los logs de red: {}", e.getMessage());
        }
    }

    /**
     * Verifica si los logs de rendimiento están disponibles
     * @return true si están disponibles, false si no
     */
    public boolean verificarLogsRendimientoDisponibles() {
        try {
            // Si tenemos CDP inicializado, consideramos que los logs están disponibles
            if (cdpInicializado) {
                logger.info("✅ CDP está inicializado y disponible");
                return true;
            }
            
            // Verificar logs tradicionales
            boolean disponibles = driver.manage().logs().getAvailableLogTypes().contains(LogType.PERFORMANCE);
            logger.info("Logs de rendimiento disponibles: {}", disponibles);
            return disponibles;
        } catch (Exception e) {
            logger.error("❌ No se pudieron verificar los logs disponibles: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Captura todas las interacciones de red (solicitudes y respuestas)
     * @return Lista de todas las interacciones de red
     */
    public List<NetworkInteraction> capturarInteraccionesRed() {
        try {
            // Si tenemos CDP inicializado y con datos, usar esos
            if (cdpInicializado && !interaccionesCapturadas.isEmpty()) {
                logger.info("🔍 Se capturaron {} interacciones de red mediante CDP", interaccionesCapturadas.size());
                return new ArrayList<>(interaccionesCapturadas);
            }
            
            // Si no, usar el método tradicional
            if (!verificarLogsRendimientoDisponibles()) {
                logger.error("❌ Los logs de rendimiento no están disponibles. Verifica la configuración del WebDriver.");
                return new ArrayList<>();
            }
            
            LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);
            List<NetworkInteraction> interacciones = new ArrayList<>();
            ConcurrentMap<String, ApiRequest> solicitudesPendientes = new ConcurrentHashMap<>();
            
            for (LogEntry entry : logs) {
                try {
                    JSONObject json = new JSONObject(entry.getMessage());
                    
                    if (json.has("message")) {
                        JSONObject message = json.getJSONObject("message");
                        String method = message.getString("method");
                        
                        if ("Network.requestWillBeSent".equals(method)) {
                            // Capturar solicitud
                            JSONObject params = message.getJSONObject("params");
                            String requestId = params.getString("requestId");
                            
                            ApiRequest request = new ApiRequest();
                            request.setUrl(params.getJSONObject("request").getString("url"));
                            request.setMethod(params.getJSONObject("request").getString("method"));
                            request.setRequestId(requestId);
                            
                            // Capturar cuerpo de la solicitud si existe
                            if (params.getJSONObject("request").has("postData")) {
                                request.setBody(params.getJSONObject("request").getString("postData"));
                            }
                            
                            solicitudesPendientes.put(requestId, request);
                            
                        } else if ("Network.responseReceived".equals(method)) {
                            // Capturar respuesta
                            JSONObject params = message.getJSONObject("params");
                            String requestId = params.getString("requestId");
                            JSONObject response = params.getJSONObject("response");
                            
                            ApiResponse apiResponse = new ApiResponse();
                            apiResponse.setUrl(response.getString("url"));
                            apiResponse.setStatus(response.getInt("status"));
                            apiResponse.setStatusText(response.getString("statusText"));
                            apiResponse.setRequestId(requestId);
                            
                            // Asociar con la solicitud correspondiente
                            if (solicitudesPendientes.containsKey(requestId)) {
                                apiResponse.setRequest(solicitudesPendientes.get(requestId));
                                solicitudesPendientes.remove(requestId);
                            }
                            
                            // Obtener cuerpo de la respuesta
                            obtenerCuerpoRespuesta(apiResponse, requestId);
                            
                            interacciones.add(new NetworkInteraction(
                                apiResponse.getRequest() != null ? apiResponse.getRequest() : new ApiRequest(),
                                apiResponse
                            ));
                        }
                    }
                } catch (JSONException e) {
                    logger.debug("Entrada de log no procesable: {}", e.getMessage());
                }
            }
            
            logger.info("🔍 Se capturaron {} interacciones de red", interacciones.size());
            return interacciones;
        } catch (Exception e) {
            logger.error("⚠️ Error al capturar interacciones de red: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el cuerpo de la respuesta de error
     * @param apiResponse Objeto ApiResponse donde se almacenará el cuerpo
     * @param requestId ID de la solicitud para buscar en los logs
     */
    private void obtenerCuerpoRespuesta(ApiResponse apiResponse, String requestId) {
        try {
            LogEntries responseLogs = driver.manage().logs().get(LogType.PERFORMANCE);
            for (LogEntry responseEntry : responseLogs) {
                try {
                    JSONObject responseJson = new JSONObject(responseEntry.getMessage());
                    if (responseJson.has("message")) {
                        JSONObject responseMessage = responseJson.getJSONObject("message");
                        if (responseMessage.has("method") && 
                            "Network.getResponseBody".equals(responseMessage.getString("method")) &&
                            responseMessage.getJSONObject("params").getString("requestId").equals(requestId)) {
                            
                            JSONObject responseBody = responseMessage.getJSONObject("params").getJSONObject("body");
                            apiResponse.setBody(responseBody.toString());
                            return;
                        }
                    }
                } catch (JSONException e) {
                    logger.debug("Entrada de log de respuesta no procesable: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.debug("No se pudo obtener el cuerpo de la respuesta: {}", e.getMessage());
        }
    }

    /**
     * Filtra interacciones por URL específica
     * @param interacciones Lista de interacciones de red
     * @param urlFragment Fragmento de URL a buscar
     * @return Lista filtrada de interacciones
     */
    public List<NetworkInteraction> filtrarInteraccionesPorUrl(List<NetworkInteraction> interacciones, String urlFragment) {
        List<NetworkInteraction> filtradas = new ArrayList<>();
        for (NetworkInteraction interaccion : interacciones) {
            if (interaccion.getResponse().getUrl().contains(urlFragment)) {
                filtradas.add(interaccion);
            }
        }
        return filtradas;
    }

    /**
     * Muestra las interacciones de red en la consola
     * @param interacciones Lista de interacciones a mostrar
     */
    public void mostrarInteraccionesEnConsola(List<NetworkInteraction> interacciones) {
        logger.info("📡 INTERACCIONES DE RED CAPTURADAS:");
        logger.info("=====================================");
        
        for (NetworkInteraction interaccion : interacciones) {
            logger.info(interaccion.toString());
            logger.info("---");
        }
        
        logger.info("=====================================");
    }

    /**
     * Clase para representar una interacción de red completa (solicitud + respuesta)
     */
    public static class NetworkInteraction {
        private ApiRequest request;
        private ApiResponse response;

        public NetworkInteraction(ApiRequest request, ApiResponse response) {
            this.request = request;
            this.response = response;
        }

        public ApiRequest getRequest() { return request; }
        public ApiResponse getResponse() { return response; }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            
            if (request != null) {
                sb.append("📤 SOLICITUD:\n");
                sb.append("  URL: ").append(request.getUrl()).append("\n");
                sb.append("  Método: ").append(request.getMethod()).append("\n");
                if (request.getBody() != null && !request.getBody().isEmpty()) {
                    sb.append("  Cuerpo: ");
                    try {
                        JSONObject jsonBody = new JSONObject(request.getBody());
                        sb.append(jsonBody.toString(2));
                    } catch (JSONException e) {
                        sb.append(request.getBody());
                    }
                    sb.append("\n");
                }
            }
            
            sb.append("📥 RESPUESTA:\n");
            sb.append("  URL: ").append(response.getUrl()).append("\n");
            sb.append("  Estado: ").append(response.getStatus()).append(" ").append(response.getStatusText()).append("\n");
            
            if (response.getBody() != null && !response.getBody().isEmpty()) {
                sb.append("  Cuerpo: ");
                try {
                    JSONObject jsonBody = new JSONObject(response.getBody());
                    sb.append(jsonBody.toString(2));
                } catch (JSONException e) {
                    sb.append(response.getBody());
                }
                sb.append("\n");
            }
            
            return sb.toString();
        }
    }

    /**
     * Clase para representar una respuesta de API
     */
    public static class ApiResponse {
        private String url;
        private String method;
        private int status;
        private String statusText;
        private String body;
        private ApiRequest request;
        private String requestId;

        // Getters y setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        
        public String getStatusText() { return statusText; }
        public void setStatusText(String statusText) { this.statusText = statusText; }
        
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
        
        public ApiRequest getRequest() { return request; }
        public void setRequest(ApiRequest request) { this.request = request; }
        
        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
    }

    /**
     * Clase para representar una solicitud de API
     */
    public static class ApiRequest {
        private String url;
        private String method;
        private String body;
        private String requestId;

        // Getters y setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
        
        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
    }
}