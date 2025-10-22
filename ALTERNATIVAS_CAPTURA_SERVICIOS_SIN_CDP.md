# 🔍 ALTERNATIVAS PARA CAPTURAR SERVICIOS WEB SIN CDP

## 📊 COMPARATIVA DE ALTERNATIVAS

| Método | Complejidad | Captura | Ventajas | Desventajas |
|--------|-------------|---------|----------|-------------|
| **Espera DOM** ✅ | Baja | ❌ No | Simple, sin dependencias | No captura respuesta real |
| **BrowserMob Proxy** ⭐ | Media | ✅ Sí | Captura completa HTTP | Requiere proxy |
| **Selenium Network Logs** | Baja | ⚠️ Parcial | Integrado en Selenium | Limitado, solo Chrome |
| **REST Assured** | Media | ✅ Sí | Validación completa | Requiere llamadas paralelas |
| **JavaScript Injection** | Media | ✅ Sí | Sin dependencias extra | Puede ser bloqueado |

---

## ✅ SOLUCIÓN ACTUAL: ESPERA POR DOM

### Lo que hace:
1. Hace clic en "Enviar"
2. Detecta indicadores visuales (spinner, mensajes, estado de botón)
3. Espera a que el procesamiento termine
4. **NO captura la respuesta del API**

### Ventajas:
- ✅ Sin dependencias adicionales
- ✅ Simple de mantener
- ✅ No requiere CDP
- ✅ Funciona en todos los navegadores

### Desventajas:
- ❌ No valida la respuesta del servicio
- ❌ No captura errores HTTP específicos
- ❌ Depende de indicadores visuales

---

## 🌟 OPCIÓN A: BROWSERMOB PROXY (RECOMENDADA)

### ¿Qué es?
Un proxy HTTP que intercepta todas las peticiones entre el navegador y el servidor.

### Instalación:

#### 1. Agregar dependencia en `pom.xml`:
\`\`\`xml
<dependency>
    <groupId>net.lightbody.bmp</groupId>
    <artifactId>browsermob-core</artifactId>
    <version>2.1.5</version>
</dependency>
\`\`\`

#### 2. Crear clase `BrowserMobProxyHelper.java`:

\`\`\`java
package Avianca.Utils;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.stream.Collectors;

public class BrowserMobProxyHelper {
    
    private BrowserMobProxy proxy;
    private WebDriver driver;
    
    /**
     * Inicia el proxy y configura el navegador
     */
    public WebDriver iniciarConProxy() {
        // Iniciar el proxy
        proxy = new BrowserMobProxyServer();
        proxy.start(0);
        
        // Configurar captura de contenido
        proxy.enableHarCaptureTypes(
            CaptureType.REQUEST_CONTENT, 
            CaptureType.RESPONSE_CONTENT
        );
        
        // Crear nuevo HAR
        proxy.newHar("AviancaAliados");
        
        // Configurar Selenium con el proxy
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        
        ChromeOptions options = new ChromeOptions();
        options.setProxy(seleniumProxy);
        options.setAcceptInsecureCerts(true);
        
        driver = new ChromeDriver(options);
        return driver;
    }
    
    /**
     * Captura todas las peticiones HTTP
     */
    public List<HarEntry> capturarPeticiones() {
        Har har = proxy.getHar();
        return har.getLog().getEntries();
    }
    
    /**
     * Busca una petición específica por URL
     */
    public HarEntry buscarPeticionPorUrl(String urlParcial) {
        return capturarPeticiones().stream()
            .filter(entry -> entry.getRequest().getUrl().contains(urlParcial))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Obtiene el código de respuesta de una petición
     */
    public int obtenerCodigoRespuesta(String urlParcial) {
        HarEntry entry = buscarPeticionPorUrl(urlParcial);
        return entry != null ? entry.getResponse().getStatus() : -1;
    }
    
    /**
     * Obtiene el body de la respuesta
     */
    public String obtenerBodyRespuesta(String urlParcial) {
        HarEntry entry = buscarPeticionPorUrl(urlParcial);
        return entry != null ? entry.getResponse().getContent().getText() : null;
    }
    
    /**
     * Cierra el proxy
     */
    public void cerrar() {
        if (proxy != null) {
            proxy.stop();
        }
    }
}
\`\`\`

#### 3. Modificar `clickEnviar()` para usar BrowserMob:

\`\`\`java
public void clickEnviar(BrowserMobProxyHelper proxyHelper) {
    try {
        System.out.println("🖱️ Realizando clic en 'Enviar'...");
        WebElement elemento = encontrarEnviar();
        
        if (elemento != null) {
            prepararElementoParaInteraccion(elemento);
            boolean clicExitoso = elementInteractions.realizarClicHibrido(elemento);
            
            if (!clicExitoso) {
                throw new RuntimeException("❌ No se pudo realizar el clic");
            }
            
            System.out.println("⏳ Esperando respuesta del servicio...");
            Thread.sleep(5000);
            
            // Buscar la petición específica
            int statusCode = proxyHelper.obtenerCodigoRespuesta("createListBlocks");
            String responseBody = proxyHelper.obtenerBodyRespuesta("createListBlocks");
            
            System.out.println("📊 Código de respuesta: " + statusCode);
            System.out.println("📄 Respuesta: " + responseBody);
            
            if (statusCode == 200) {
                System.out.println("✅ Servicio ejecutado correctamente");
            } else {
                throw new RuntimeException("❌ El servicio respondió con error: " + statusCode);
            }
        }
    } catch (Exception e) {
        System.err.println("❌ Error: " + e.getMessage());
        throw new RuntimeException("Fallo al interactuar con 'Enviar'", e);
    }
}
\`\`\`

### Ventajas de BrowserMob:
- ✅ Captura completa de HTTP (headers, body, status)
- ✅ Funciona con cualquier navegador
- ✅ No requiere CDP
- ✅ Puede modificar peticiones/respuestas en tiempo real

### Desventajas:
- ❌ Requiere configuración de proxy
- ❌ Puede afectar el rendimiento
- ❌ Dependencia adicional

---

## 🔧 OPCIÓN B: SELENIUM NETWORK LOGS

### Solo para Chrome, usando capacidades de logging:

\`\`\`java
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

public class NetworkLogsHelper {
    
    public WebDriver iniciarConLogs() {
        ChromeOptions options = new ChromeOptions();
        
        // Habilitar logs de performance (similar a CDP pero más limitado)
        options.setCapability("goog:loggingPrefs", 
            Map.of("performance", "ALL", "browser", "ALL"));
        
        return new ChromeDriver(options);
    }
    
    public List<LogEntry> capturarLogs(WebDriver driver) {
        LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);
        return logs.getAll();
    }
    
    public void buscarPeticion(WebDriver driver, String url) {
        List<LogEntry> logs = capturarLogs(driver);
        
        for (LogEntry entry : logs) {
            String message = entry.getMessage();
            if (message.contains(url) && message.contains("Network.response")) {
                System.out.println("📡 Petición encontrada: " + message);
                // Parsear JSON del log
                JSONObject json = new JSONObject(message);
                // Extraer información...
            }
        }
    }
}
\`\`\`

### Ventajas:
- ✅ Integrado en Selenium
- ✅ No requiere dependencias adicionales
- ✅ Más ligero que BrowserMob

### Desventajas:
- ❌ Solo funciona en Chrome
- ❌ Logs en formato JSON complejo
- ❌ No captura body de respuesta completo

---

## 📡 OPCIÓN C: JAVASCRIPT INJECTION

### Interceptar XMLHttpRequest/Fetch con JavaScript:

\`\`\`java
public class JavaScriptNetworkCapture {
    
    private WebDriver driver;
    
    public void inyectarCapturaRed() {
        String script = 
            "window.capturedRequests = [];" +
            
            // Interceptar XMLHttpRequest
            "var originalXHR = window.XMLHttpRequest;" +
            "window.XMLHttpRequest = function() {" +
            "  var xhr = new originalXHR();" +
            "  var originalOpen = xhr.open;" +
            "  var originalSend = xhr.send;" +
            "  " +
            "  xhr.open = function(method, url) {" +
            "    xhr._url = url;" +
            "    xhr._method = method;" +
            "    originalOpen.apply(xhr, arguments);" +
            "  };" +
            "  " +
            "  xhr.send = function() {" +
            "    xhr.addEventListener('load', function() {" +
            "      window.capturedRequests.push({" +
            "        url: xhr._url," +
            "        method: xhr._method," +
            "        status: xhr.status," +
            "        response: xhr.responseText" +
            "      });" +
            "    });" +
            "    originalSend.apply(xhr, arguments);" +
            "  };" +
            "  " +
            "  return xhr;" +
            "};" +
            
            // Interceptar Fetch
            "var originalFetch = window.fetch;" +
            "window.fetch = function() {" +
            "  var url = arguments[0];" +
            "  return originalFetch.apply(this, arguments).then(function(response) {" +
            "    response.clone().text().then(function(body) {" +
            "      window.capturedRequests.push({" +
            "        url: url," +
            "        method: 'GET'," +
            "        status: response.status," +
            "        response: body" +
            "      });" +
            "    });" +
            "    return response;" +
            "  });" +
            "};";
        
        ((JavascriptExecutor) driver).executeScript(script);
        System.out.println("✅ Script de captura inyectado");
    }
    
    public List<Map<String, Object>> obtenerPeticionesCapturadas() {
        String script = "return window.capturedRequests || [];";
        return (List<Map<String, Object>>) ((JavascriptExecutor) driver).executeScript(script);
    }
    
    public Map<String, Object> buscarPeticion(String urlParcial) {
        List<Map<String, Object>> requests = obtenerPeticionesCapturadas();
        
        return requests.stream()
            .filter(req -> req.get("url").toString().contains(urlParcial))
            .findFirst()
            .orElse(null);
    }
}

// Uso en clickEnviar():
public void clickEnviar(JavaScriptNetworkCapture capture) {
    // Antes de navegar a la página, inyectar el script
    capture.inyectarCapturaRed();
    
    // Realizar clic
    elementInteractions.realizarClicHibrido(elemento);
    
    // Esperar
    Thread.sleep(3000);
    
    // Obtener petición
    Map<String, Object> request = capture.buscarPeticion("createListBlocks");
    
    if (request != null) {
        int status = (int) request.get("status");
        String response = (String) request.get("response");
        
        System.out.println("📊 Status: " + status);
        System.out.println("📄 Response: " + response);
    }
}
\`\`\`

### Ventajas:
- ✅ Sin dependencias externas
- ✅ Funciona en cualquier navegador
- ✅ Captura completa de peticiones

### Desventajas:
- ❌ Puede ser bloqueado por CSP (Content Security Policy)
- ❌ Requiere inyección en cada página
- ❌ No captura peticiones previas a la inyección

---

## 🧪 OPCIÓN D: REST ASSURED (VALIDACIÓN PARALELA)

### Hacer la misma llamada API desde el test:

\`\`\`java
import io.restassured.RestAssured;
import io.restassured.response.Response;

public void validarServicioConRestAssured() {
    // Hacer la misma petición que hace el navegador
    Response response = RestAssured
        .given()
            .baseUri("https://aliadosqa.aro.avtest.ink/api")
            .header("Content-Type", "application/json")
            .body("{\"data\": \"...\"}")  // Datos del formulario
        .when()
            .post("/web-pa-holds/tempBlocks/createListBlocks")
        .then()
            .extract().response();
    
    int statusCode = response.getStatusCode();
    String body = response.getBody().asString();
    
    System.out.println("📊 Status: " + statusCode);
    System.out.println("📄 Body: " + body);
    
    // Validar
    if (statusCode == 200) {
        System.out.println("✅ Servicio OK");
    }
}
\`\`\`

### Ventajas:
- ✅ Validación completa del API
- ✅ Independiente del navegador
- ✅ Puede probar edge cases

### Desventajas:
- ❌ Requiere conocer los datos exactos del formulario
- ❌ No prueba la integración real UI -> API
- ❌ Puede requerir autenticación adicional

---

## 📋 RECOMENDACIÓN

### Para tu caso (solo esperar):
✅ **Solución actual (Espera por DOM)** - Ya implementada

### Si necesitas capturar y validar:
⭐ **BrowserMob Proxy** - La mejor opción completa

### Si quieres algo rápido sin dependencias:
🔧 **JavaScript Injection** - Funcional y simple

---

## 🚀 IMPLEMENTACIÓN SUGERIDA

Te recomiendo **mantener la solución actual** (espera por DOM) porque:
1. ✅ Es simple y funcional
2. ✅ No requiere dependencias adicionales
3. ✅ Cumple con el requisito de "solo esperar"
4. ✅ Es fácil de mantener

Si más adelante necesitas **capturar y validar** la respuesta real del API, implementa **BrowserMob Proxy**.

---

**Archivo creado:** ALTERNATIVAS_CAPTURA_SERVICIOS_SIN_CDP.md  
**Solución actual:** Espera por indicadores DOM ✅  
**Mejor alternativa futura:** BrowserMob Proxy ⭐
