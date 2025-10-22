# 🌐 Guía de Uso de BrowserMob Proxy

## 📋 Descripción

BrowserMob Proxy se ha integrado en el proyecto para capturar tráfico HTTP sin necesidad de Chrome DevTools Protocol (CDP). La implementación permite capturar y analizar respuestas del servicio `createListBlocks` de manera transparente.

---

## 🚀 Activación del Proxy

### Opción 1: Por línea de comandos (Maven)

```bash
mvn clean test -DenableProxy=true
```

### Opción 2: Por línea de comandos (configuración del sistema)

```bash
mvn clean test -Dtest=RunnersFeature -DenableProxy=true
```

### Opción 3: En IDE (IntelliJ IDEA / Eclipse)

**Configurar en Run Configuration:**

1. Clic derecho en `RunnersFeature.java` → Run As → Run Configurations
2. Pestaña **Arguments**
3. En **VM arguments** agregar:
   ```
   -DenableProxy=true
   ```
4. Apply → Run

---

## 📂 Archivos Modificados/Creados

### ✅ Archivos Nuevos

1. **`BrowserMobProxyHelper.java`**
   - Ubicación: `src/test/java/Avianca/Utils/`
   - Función: Gestiona el ciclo de vida del proxy y la captura de tráfico HTTP

### ✅ Archivos Modificados

2. **`pom.xml`**
   - Agregada dependencia de BrowserMob Proxy 2.1.5

3. **`WebDriverConfig.java`**
   - Soporte para configurar proxy en Chrome, Firefox y Edge
   - Métodos: `setSeleniumProxy()`, `clearProxy()`

4. **`Conexion.java`**
   - Inicialización automática del proxy si `enableProxy=true`
   - Método `getProxyHelper()` para obtener instancia del proxy
   - Método `cerrarNavegador()` que limpia recursos del proxy

5. **`DefinitionsSteps.java`**
   - Inyección del proxy en todas las instancias de `SolicitudBloqueoPage`
   - Hook `@After` para cerrar navegador y proxy

6. **`SolicitudBloqueoPage.java`**
   - Nuevo constructor que acepta `BrowserMobProxyHelper`
   - Método `setProxyHelper()` para inyección tardía

7. **`ButtonPages.java`**
   - Campo `proxyHelper`
   - Tres versiones del método `clickEnviar()`:
     - `clickEnviar()` - Dispatcher (elige automáticamente)
     - `clickEnviarConCaptura()` - Con análisis HTTP
     - `clickEnviarSinCaptura()` - Sin captura (comportamiento original)

---

## 🔍 Funcionamiento

### Sin Proxy (`enableProxy=false` o no especificado)

```
Usuario hace clic en Enviar
    ↓
clickEnviar() detecta que proxyHelper == null
    ↓
Llama a clickEnviarSinCaptura()
    ↓
Espera DOM del Angular (comportamiento original)
```

### Con Proxy (`enableProxy=true`)

```
Usuario hace clic en Enviar
    ↓
clickEnviar() detecta que proxyHelper != null
    ↓
Llama a clickEnviarConCaptura()
    ↓
1. Reinicializa captura HAR
2. Hace clic en botón Enviar
3. Espera 5 segundos para la respuesta
4. Busca petición "createListBlocks" en HAR
5. Extrae: código HTTP, body, tiempo de respuesta
6. Valida éxito (200-299) o error (400+)
7. Intenta parsear JSON para obtener identifier y mensaje
8. Imprime análisis detallado en consola
```

---

## 📊 Ejemplo de Salida con Proxy Habilitado

```
🔧 Iniciando BrowserMob Proxy...
✅ BrowserMob Proxy iniciado y configurado
ℹ️ Proxy activo en puerto: 8080
🌐 ChromeDriver configurado con BrowserMob Proxy
✅ Navegador abierto con BrowserMob Proxy habilitado
...
🔍 Haciendo clic en el botón Enviar...
⏳ Esperando respuesta del servicio (5 segundos)...

📊 ===== ANÁLISIS DE PETICIÓN HTTP =====
🔗 URL buscada: createListBlocks
📍 URL completa: https://aliadosqa.aro.avtest.ink/api/web-pa-holds/tempBlocks/createListBlocks
📈 Código de respuesta: 200
⏱️  Tiempo de respuesta: 1247 ms
✅ RESPUESTA EXITOSA (2xx)

📄 Body de respuesta:
{"identifier":"12345","status":"success","message":"Bloqueos creados exitosamente"}

🔎 Datos extraídos del JSON:
   • Identifier: 12345
   • Mensaje: Bloqueos creados exitosamente
========================================
```

---

## 🛠️ Métodos Disponibles en BrowserMobProxyHelper

| Método | Descripción | Retorno |
|--------|-------------|---------|
| `iniciarProxy()` | Inicia el servidor proxy | void |
| `obtenerConfiguracionSelenium()` | Obtiene configuración para WebDriver | Proxy |
| `capturarTodasLasPeticiones()` | Inicia captura HAR nueva | void |
| `buscarPeticionPorUrl(String)` | Busca petición por URL parcial | HarEntry |
| `obtenerCodigoRespuesta(String)` | Obtiene código HTTP (ej: 200) | int |
| `obtenerBodyRespuesta(String)` | Obtiene cuerpo de respuesta | String |
| `obtenerTiempoRespuesta(String)` | Obtiene tiempo en ms | long |
| `peticionExitosa(String)` | Valida si código 200-299 | boolean |
| `detenerProxy()` | Detiene el servidor proxy | void |

---

## 🧪 Ejemplo de Uso Manual

```java
// Iniciar proxy
BrowserMobProxyHelper proxyHelper = new BrowserMobProxyHelper();
proxyHelper.iniciarProxy();

Proxy seleniumProxy = proxyHelper.obtenerConfiguracionSelenium();

// Configurar Chrome con proxy
ChromeOptions options = new ChromeOptions();
options.setProxy(seleniumProxy);
options.setAcceptInsecureCerts(true);
WebDriver driver = new ChromeDriver(options);

// Navegar y capturar
driver.get("https://aliadosqa.aro.avtest.ink/");

// Hacer acciones...
// ...

// Analizar petición específica
if (proxyHelper.buscarPeticionPorUrl("createListBlocks") != null) {
    int codigo = proxyHelper.obtenerCodigoRespuesta("createListBlocks");
    String body = proxyHelper.obtenerBodyRespuesta("createListBlocks");
    long tiempo = proxyHelper.obtenerTiempoRespuesta("createListBlocks");
    
    System.out.println("Código: " + codigo);
    System.out.println("Body: " + body);
    System.out.println("Tiempo: " + tiempo + " ms");
}

// Limpiar
proxyHelper.detenerProxy();
driver.quit();
```

---

## ⚙️ Configuración Avanzada

### Cambiar Puerto del Proxy

Por defecto usa puerto automático. Para forzar un puerto:

```java
// En BrowserMobProxyHelper.java línea 25:
proxy = new BrowserMobProxyServer();
proxy.start(8888); // Puerto fijo
```

### Habilitar Logs de Proxy

```java
// En BrowserMobProxyHelper.java después de iniciar:
proxy.enableHarCaptureTypes(CaptureType.REQUEST_HEADERS, 
                            CaptureType.RESPONSE_HEADERS, 
                            CaptureType.REQUEST_CONTENT, 
                            CaptureType.RESPONSE_CONTENT);
```

### Guardar HAR en Archivo

```java
// Después de capturar tráfico:
Har har = proxy.getHar();
File harFile = new File("target/traffic.har");
har.writeTo(harFile);
System.out.println("✅ HAR guardado en: " + harFile.getAbsolutePath());
```

---

## 🐛 Troubleshooting

### Problema: "Puerto ya en uso"

**Solución:** El proxy no se detuvo correctamente. Reiniciar o cambiar puerto.

```bash
# Windows: Matar proceso en puerto 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Problema: "Certificado SSL no confiable"

**Solución:** Ya incluido en configuración con `setAcceptInsecureCerts(true)`.

### Problema: "No se captura tráfico HTTPS"

**Solución:** Verificar que `setAcceptInsecureCerts(true)` esté en opciones del driver.

### Problema: "Petición no encontrada en HAR"

**Solución:** 
- Aumentar tiempo de espera en `clickEnviarConCaptura()` (línea 716)
- Verificar que la URL parcial sea correcta
- Revisar que el servicio se esté llamando realmente

---

## 📈 Ventajas sobre CDP

| Característica | BrowserMob Proxy | CDP |
|----------------|------------------|-----|
| **Cross-browser** | ✅ Chrome, Firefox, Edge | ❌ Solo Chrome/Chromium |
| **Formato estándar** | ✅ HAR (HTTP Archive) | ❌ Formato propietario |
| **Complejidad** | ✅ Fácil configuración | ⚠️ Requiere API avanzada |
| **Estabilidad** | ✅ Maduro y probado | ⚠️ API experimental |
| **CSP Issues** | ✅ No afectado | ⚠️ Puede ser bloqueado |

---

## 📚 Referencias

- [BrowserMob Proxy GitHub](https://github.com/lightbody/browsermob-proxy)
- [HAR Specification](http://www.softwareishard.com/blog/har-12-spec/)
- [Selenium Proxy Configuration](https://www.selenium.dev/documentation/webdriver/drivers/options/#proxy)

---

## 🔄 Actualización de Dependencias

Si necesitas actualizar BrowserMob Proxy:

```xml
<dependency>
    <groupId>net.lightbody.bmp</groupId>
    <artifactId>browsermob-core</artifactId>
    <version>2.1.5</version> <!-- Actualizar versión aquí -->
</dependency>
```

Luego ejecutar:

```bash
mvn clean install -U
```

---

**✅ Implementación completada y lista para usar**
