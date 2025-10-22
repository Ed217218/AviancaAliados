# 📋 PLAN DE IMPLEMENTACIÓN CDP - Chrome DevTools Protocol

## 🎯 Objetivo
Implementar Chrome DevTools Protocol (CDP) para capturar errores del servicio web, peticiones HTTP fallidas, errores de consola JavaScript y respuestas del servidor en tiempo real durante las pruebas automatizadas.

---

## ⚠️ LIMITACIONES IDENTIFICADAS

### Problema Principal: Versión de Selenium vs Chrome
- **Selenium actual**: 4.10.0 (incluido en Serenity BDD 3.9.8)
- **Chrome instalado**: 141.0.7390.108
- **Versiones CDP soportadas**: v85, v112, v113, v114
- **CDP necesario**: v141

### Consecuencia
El CDP **NO funcionará completamente** con Chrome 141 usando Selenium 4.10.0. Las características avanzadas requerirán actualización.

---

## 🔍 ANÁLISIS DE DOCUMENTACIÓN CDP

### Funcionalidades CDP Disponibles:

#### 1. **Network Monitoring** (Lo que necesitamos)
```java
// Captura peticiones HTTP
devTools.send(Network.enable(...));
devTools.addListener(Network.responseReceived(), response -> {
    // Capturar respuestas del servidor
});
devTools.addListener(Network.requestWillBeSent(), request -> {
    // Capturar peticiones enviadas
});
devTools.addListener(Network.loadingFailed(), failed -> {
    // Capturar peticiones fallidas
});
```

#### 2. **Console Logging** (Errores JavaScript)
```java
((HasLogEvents) driver).onLogEvent(consoleEvent(e -> {
    messages.add(e.getMessages().get(0));
}));
```

#### 3. **JavaScript Exceptions**
```java
// Captura excepciones no manejadas
devTools.getDomains().events().addJavascriptExceptionListener();
```

---

## 📝 PLAN PASO A PASO DE IMPLEMENTACIÓN

### **FASE 1: PREPARACIÓN DEL ENTORNO** ⏱️ 15 min

#### Paso 1.1: Verificar versión de Selenium
```bash
# Ya verificado: Selenium 4.10.0 (limitado)
```

#### Paso 1.2: Decidir estrategia
**OPCIÓN A** (Recomendada): Actualizar Selenium a 4.27+ (última versión)
- ✅ Soporte completo para Chrome 141
- ✅ Todas las funciones CDP disponibles
- ⚠️ Requiere actualizar serenity.properties

**OPCIÓN B** (Temporal): Usar funcionalidades básicas con Selenium 4.10.0
- ⚠️ CDP limitado (no todas las funciones)
- ⚠️ Warnings de incompatibilidad
- ✅ Sin cambios en dependencias

**OPCIÓN C**: Downgrade Chrome a versión 114
- ❌ No recomendado (perder funcionalidades del navegador)

---

### **FASE 2: CREAR CLASE INTERCEPTOR DE RED** ⏱️ 30 min

#### Paso 2.1: Crear `NetworkErrorCapture.java`
**Ubicación**: `src/test/java/Avianca/Utils/NetworkErrorCapture.java`

**Responsabilidades**:
- Inicializar DevTools
- Habilitar Network domain
- Capturar peticiones HTTP
- Capturar respuestas del servidor
- Detectar errores 4xx y 5xx
- Almacenar errores para análisis

#### Paso 2.2: Estructura de datos para errores
```java
public class NetworkError {
    private String url;
    private String method;
    private int statusCode;
    private String statusText;
    private String responseBody;
    private LocalDateTime timestamp;
    private Map<String, String> headers;
}
```

---

### **FASE 3: CREAR CLASE DE CAPTURA DE CONSOLA** ⏱️ 20 min

#### Paso 3.1: Crear `ConsoleErrorCapture.java`
**Ubicación**: `src/test/java/Avianca/Utils/ConsoleErrorCapture.java`

**Responsabilidades**:
- Capturar `console.error()` del navegador
- Capturar excepciones JavaScript
- Filtrar mensajes relevantes
- Asociar errores con acciones del test

---

### **FASE 4: INTEGRAR EN WebDriverConfig** ⏱️ 25 min

#### Paso 4.1: Modificar `WebDriverConfig.java`
**Cambios**:
1. Inicializar DevTools al crear el driver
2. Configurar listeners de Network y Console
3. Inyectar interceptores en el contexto global
4. Proveer métodos para consultar errores capturados

#### Paso 4.2: Crear método de inicialización
```java
public static void enableCDPMonitoring(ChromeDriver driver) {
    DevTools devTools = driver.getDevTools();
    devTools.createSession();
    
    // Habilitar Network
    devTools.send(Network.enable(...));
    
    // Habilitar Log
    devTools.send(Log.enable());
    
    // Agregar listeners
    NetworkErrorCapture.attachListeners(devTools);
    ConsoleErrorCapture.attachListeners(driver);
}
```

---

### **FASE 5: MODIFICAR ButtonPages.java** ⏱️ 20 min

#### Paso 5.1: Actualizar método `clickEnviar()`
**Cambios**:
1. Limpiar errores antes del clic
2. Ejecutar clic en "Enviar"
3. Ejecutar espera inteligente de 5 pasos
4. **NUEVO PASO 6**: Consultar errores CDP capturados
5. Si hay errores HTTP, reportarlos con detalles

#### Paso 5.2: Nuevo método `verificarErroresHTTP()`
```java
private void verificarErroresHTTP() {
    List<NetworkError> errors = NetworkErrorCapture.getErrors();
    
    if (!errors.isEmpty()) {
        for (NetworkError error : errors) {
            System.out.println("❌ ERROR HTTP CAPTURADO:");
            System.out.println("   URL: " + error.getUrl());
            System.out.println("   Status: " + error.getStatusCode());
            System.out.println("   Response: " + error.getResponseBody());
            
            // Si es error del servicio de bloqueos
            if (error.getUrl().contains("OpeBlock") || 
                error.getUrl().contains("block")) {
                // Parsear respuesta JSON
                parseServerError(error.getResponseBody());
            }
        }
    }
}
```

---

### **FASE 6: CREAR REPORTES DE ERRORES** ⏱️ 15 min

#### Paso 6.1: Crear `CDPErrorReporter.java`
**Funcionalidades**:
- Formatear errores capturados
- Generar logs estructurados
- Integrar con Serenity BDD reports
- Exportar a archivo JSON (opcional)

#### Paso 6.2: Formato de reporte
```json
{
  "test": "Solicitud de bloqueos",
  "timestamp": "2025-10-21T15:50:23",
  "errors": [
    {
      "type": "HTTP_ERROR",
      "url": "https://aliadosqa.aro.avtest.ink/api/blocks",
      "status": 500,
      "method": "POST",
      "errorCode": "PA-HOLDS-00007",
      "errorMessage": "ORA-00001: unique constraint violated",
      "stackTrace": "..."
    }
  ]
}
```

---

### **FASE 7: PRUEBAS Y VALIDACIÓN** ⏱️ 30 min

#### Paso 7.1: Crear test unitario para CDP
**Archivo**: `src/test/java/Avianca/Tests/CDPNetworkTest.java`

**Casos de prueba**:
1. Verificar inicialización de DevTools
2. Capturar petición exitosa (200 OK)
3. Capturar error 500 Internal Server Error
4. Capturar error 400 Bad Request
5. Validar parseo de respuesta JSON

#### Paso 7.2: Ejecutar test de solicitud de bloqueos
```bash
mvn test -Dtest=RunnersFeature
```

**Validaciones**:
- ✅ CDP se inicializa sin errores
- ✅ Captura petición POST a /api/blocks
- ✅ Detecta error ORA-00001
- ✅ Muestra respuesta completa del servidor
- ✅ Test reporta error con contexto

---

## 🚀 CRONOGRAMA DE IMPLEMENTACIÓN

| Fase | Tiempo | Prioridad | Dependencias |
|------|--------|-----------|--------------|
| Fase 1: Preparación | 15 min | Alta | Ninguna |
| Fase 2: NetworkErrorCapture | 30 min | Alta | Fase 1 |
| Fase 3: ConsoleErrorCapture | 20 min | Media | Fase 1 |
| Fase 4: Integración WebDriver | 25 min | Alta | Fase 2, 3 |
| Fase 5: Modificar ButtonPages | 20 min | Alta | Fase 4 |
| Fase 6: Reportes | 15 min | Baja | Fase 5 |
| Fase 7: Pruebas | 30 min | Alta | Todas |
| **TOTAL** | **2h 35min** | | |

---

## 📊 BENEFICIOS ESPERADOS

### Antes (Sin CDP)
```
❌ Test falla
❌ Mensaje: "No se encontró mensaje de respuesta"
❌ Timeout después de 5 segundos
❌ Sin información del error real
❌ Necesitas abrir manualmente DevTools
```

### Después (Con CDP)
```
✅ Test captura error en tiempo real
✅ Muestra: "POST /api/blocks → 500 Internal Server Error"
✅ Response body: {"errorCode": "PA-HOLDS-00007", "message": "ORA-00001: unique constraint violated"}
✅ Detalles completos del error del servidor
✅ Log automático sin intervención manual
```

---

## ⚙️ CONFIGURACIÓN RECOMENDADA

### Opción A: Actualizar Selenium (RECOMENDADO)

**Modificar `pom.xml`**:
```xml
<properties>
    <serenity.version>4.2.9</serenity.version> <!-- Última versión -->
    <selenium.version>4.27.0</selenium.version> <!-- Última versión -->
</properties>
```

**Ventajas**:
- ✅ Soporte completo CDP v141
- ✅ Sin warnings de incompatibilidad
- ✅ Todas las funcionalidades disponibles

**Desventajas**:
- ⚠️ Requiere pruebas de regresión
- ⚠️ Posibles cambios en APIs

---

### Opción B: Usar Selenium 4.10.0 actual (TEMPORAL)

**Sin cambios en pom.xml**

**Ventajas**:
- ✅ Sin riesgo de breaking changes
- ✅ Implementación inmediata

**Desventajas**:
- ⚠️ CDP limitado
- ⚠️ Warnings en consola
- ⚠️ Algunas funciones pueden fallar

---

## 🎓 RECURSOS DE APRENDIZAJE

1. **Documentación oficial**:
   - https://www.selenium.dev/documentation/webdriver/bidi/cdp/
   - https://chromedevtools.github.io/devtools-protocol/

2. **Ejemplos de código**:
   - https://github.com/SeleniumHQ/seleniumhq.github.io/tree/trunk/examples/java/src/test/java/dev/selenium/bidi/cdp

3. **CDP Domains**:
   - Network: Captura HTTP
   - Log: Captura console
   - Runtime: Captura excepciones JS
   - Performance: Métricas de rendimiento

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

- [ ] **Fase 1**: Decidir estrategia (Actualizar vs Usar actual)
- [ ] **Fase 2**: Crear NetworkErrorCapture.java
- [ ] **Fase 3**: Crear ConsoleErrorCapture.java
- [ ] **Fase 4**: Integrar en WebDriverConfig.java
- [ ] **Fase 5**: Modificar ButtonPages.java (agregar paso 6)
- [ ] **Fase 6**: Crear CDPErrorReporter.java
- [ ] **Fase 7**: Crear CDPNetworkTest.java
- [ ] **Fase 8**: Ejecutar test de validación
- [ ] **Fase 9**: Verificar captura de error ORA-00001
- [ ] **Fase 10**: Documentar resultados

---

## 🔧 CÓDIGO DE EJEMPLO RÁPIDO

### Inicialización básica CDP:
```java
ChromeDriver driver = (ChromeDriver) WebDriverConfig.getDriver();
DevTools devTools = driver.getDevTools();
devTools.createSession();

// Habilitar Network
devTools.send(Network.enable(
    Optional.empty(),
    Optional.empty(),
    Optional.empty()
));

// Capturar respuestas con error
devTools.addListener(Network.responseReceived(), response -> {
    Response res = response.getResponse();
    if (res.getStatus() >= 400) {
        System.out.println("❌ ERROR HTTP: " + res.getStatus() + " - " + res.getUrl());
        
        // Obtener body de la respuesta
        devTools.send(Network.getResponseBody(response.getRequestId()))
            .ifPresent(body -> {
                System.out.println("Response Body: " + body);
            });
    }
});
```

---

## 🎯 PRÓXIMOS PASOS INMEDIATOS

### Para comenzar HOY:
1. **Decidir**: ¿Actualizar Selenium o usar versión actual?
2. **Crear**: NetworkErrorCapture.java (Fase 2)
3. **Probar**: Con un test simple de captura HTTP
4. **Integrar**: En ButtonPages.clickEnviar()
5. **Validar**: Con test de solicitud de bloqueos

### ¿Por dónde empezamos?
Recomiendo comenzar con **Fase 1** (decisión de estrategia) y luego **Fase 2** (NetworkErrorCapture).

---

## 📞 SOPORTE

Si durante la implementación encuentras:
- Errores de compilación
- Incompatibilidades de versiones
- Funcionalidades CDP que no responden
- Warnings de DevTools

**Documentaremos y resolveremos cada caso específico.**

---

**Fecha de creación**: 21 de octubre de 2025
**Autor**: GitHub Copilot
**Proyecto**: AviancaAliados - Automatización
**Objetivo**: Capturar errores del servicio web con CDP
