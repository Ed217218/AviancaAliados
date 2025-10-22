package Avianca.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

/**
 * 🎯 HELPER para validar respuestas de APIs y servicios web
 * 
 * Este helper funciona CON o SIN CDP:
 * - Con CDP: Captura todas las llamadas de red en tiempo real
 * - Sin CDP: Valida respuestas mediante otros métodos (timeouts, elementos en pantalla, etc.)
 */
public class ApiValidationHelper {
    
    private static final Logger logger = LogManager.getLogger(ApiValidationHelper.class);
    private ApiErrorCapture apiErrorCapture;
    
    public ApiValidationHelper(ApiErrorCapture apiErrorCapture) {
        this.apiErrorCapture = apiErrorCapture;
    }
    
    /**
     * Valida que un servicio específico haya respondido exitosamente
     * 
     * @param urlFragment Fragmento de URL del servicio (ej: "createListBlocks")
     * @param expectedStatus Código HTTP esperado (ej: 200, 201)
     * @return true si el servicio respondió correctamente, false si no se pudo validar
     * @throws AssertionError si el servicio falló
     */
    public boolean validarServicioExitoso(String urlFragment, int expectedStatus) {
        logger.info("🔍 Validando servicio: {}", urlFragment);
        
        try {
            List<ApiErrorCapture.NetworkInteraction> interacciones = 
                apiErrorCapture.capturarInteraccionesRed();
            
            if (interacciones.isEmpty()) {
                logger.warn("⚠️ No se capturaron interacciones de red. CDP podría estar deshabilitado.");
                return false;
            }
            
            // Filtrar por URL específica
            List<ApiErrorCapture.NetworkInteraction> servicioFiltrado = 
                apiErrorCapture.filtrarInteraccionesPorUrl(interacciones, urlFragment);
            
            if (servicioFiltrado.isEmpty()) {
                logger.error("❌ No se encontró el servicio: {}", urlFragment);
                logger.info("📋 Servicios capturados:");
                for (ApiErrorCapture.NetworkInteraction interaccion : interacciones) {
                    logger.info("  - {}", interaccion.getResponse().getUrl());
                }
                throw new AssertionError(
                    String.format("Servicio no encontrado: %s", urlFragment)
                );
            }
            
            // Validar el primer match
            ApiErrorCapture.NetworkInteraction servicio = servicioFiltrado.get(0);
            ApiErrorCapture.ApiResponse response = servicio.getResponse();
            int status = response.getStatus();
            
            if (status != expectedStatus) {
                logger.error("❌ Servicio {} falló:", urlFragment);
                logger.error("   URL: {}", response.getUrl());
                logger.error("   Status: {} {}", status, response.getStatusText());
                logger.error("   Respuesta: {}", response.getBody());
                
                throw new AssertionError(
                    String.format(
                        "Servicio %s falló con status %d (esperado: %d). Respuesta: %s",
                        urlFragment,
                        status,
                        expectedStatus,
                        response.getBody()
                    )
                );
            }
            
            logger.info("✅ Servicio {} ejecutado exitosamente (Status: {})", urlFragment, status);
            return true;
            
        } catch (Exception e) {
            logger.warn("⚠️ No se pudo validar el servicio via CDP: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca errores HTTP (4xx, 5xx) en todas las llamadas de red
     * 
     * @return Lista de errores encontrados
     */
    public List<ApiErrorCapture.NetworkInteraction> buscarErroresHttp() {
        logger.info("🔍 Buscando errores HTTP en las llamadas de red...");
        
        try {
            List<ApiErrorCapture.NetworkInteraction> interacciones = 
                apiErrorCapture.capturarInteraccionesRed();
            
            List<ApiErrorCapture.NetworkInteraction> errores = interacciones.stream()
                .filter(i -> i.getResponse().getStatus() >= 400)
                .collect(java.util.stream.Collectors.toList());
            
            if (!errores.isEmpty()) {
                logger.error("❌ Se encontraron {} errores HTTP:", errores.size());
                for (ApiErrorCapture.NetworkInteraction error : errores) {
                    ApiErrorCapture.ApiResponse response = error.getResponse();
                    logger.error("   {} - Status: {} {}", 
                        response.getUrl(),
                        response.getStatus(),
                        response.getStatusText()
                    );
                }
            } else {
                logger.info("✅ No se encontraron errores HTTP");
            }
            
            return errores;
            
        } catch (Exception e) {
            logger.warn("⚠️ No se pudieron buscar errores via CDP: {}", e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
    
    /**
     * Genera un reporte de todas las APIs llamadas durante la prueba
     */
    public void generarReporteApis() {
        logger.info("\n📊 REPORTE DE APIS EJECUTADAS:");
        logger.info("=====================================");
        
        try {
            List<ApiErrorCapture.NetworkInteraction> interacciones = 
                apiErrorCapture.capturarInteraccionesRed();
            
            if (interacciones.isEmpty()) {
                logger.info("⚠️ No se capturaron llamadas de red (CDP deshabilitado)");
                logger.info("=====================================\n");
                return;
            }
            
            int exitosas = 0;
            int fallidas = 0;
            
            for (ApiErrorCapture.NetworkInteraction interaccion : interacciones) {
                ApiErrorCapture.ApiResponse response = interaccion.getResponse();
                ApiErrorCapture.ApiRequest request = interaccion.getRequest();
                
                // Solo mostrar APIs REST (ignorar recursos estáticos)
                String url = response.getUrl();
                if (url.contains("/api/") || url.contains("/service/") || 
                    url.endsWith(".json") || url.contains("/OpeBlock/")) {
                    
                    int status = response.getStatus();
                    String emoji = status < 400 ? "✅" : "❌";
                    
                    if (status < 400) {
                        exitosas++;
                    } else {
                        fallidas++;
                    }
                    
                    logger.info("{} {} {} - Status: {} {}", 
                        emoji,
                        request.getMethod(),
                        url,
                        status,
                        response.getStatusText()
                    );
                }
            }
            
            logger.info("=====================================");
            logger.info("📈 Resumen: {} exitosas | {} fallidas", exitosas, fallidas);
            logger.info("=====================================\n");
            
        } catch (Exception e) {
            logger.warn("⚠️ No se pudo generar el reporte: {}", e.getMessage());
            logger.info("=====================================\n");
        }
    }
    
    /**
     * Valida que NO haya errores HTTP durante la ejecución
     * Útil para usar en @After de Cucumber
     * 
     * @throws AssertionError si se encuentran errores HTTP
     */
    public void validarSinErroresHttp() {
        List<ApiErrorCapture.NetworkInteraction> errores = buscarErroresHttp();
        
        if (!errores.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append(String.format("Se encontraron %d errores HTTP:\n", errores.size()));
            
            for (ApiErrorCapture.NetworkInteraction error : errores) {
                ApiErrorCapture.ApiResponse response = error.getResponse();
                mensaje.append(String.format(
                    "  - %s: Status %d %s\n",
                    response.getUrl(),
                    response.getStatus(),
                    response.getStatusText()
                ));
            }
            
            throw new AssertionError(mensaje.toString());
        }
    }
    
    /**
     * Verifica si CDP está activo y funcionando
     * 
     * @return true si CDP está capturando tráfico de red
     */
    public boolean isCdpActivo() {
        try {
            List<ApiErrorCapture.NetworkInteraction> interacciones = 
                apiErrorCapture.capturarInteraccionesRed();
            return !interacciones.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
