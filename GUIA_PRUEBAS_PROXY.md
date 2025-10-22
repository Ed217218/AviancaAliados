# 🧪 GUÍA DE PRUEBA - BrowserMob Proxy

## 📝 PRUEBA 1: Sin Proxy (Comportamiento Original)

### Comando
```bash
mvn clean test -Dtest=RunnersFeature
```

### Resultado Esperado
```
ℹ️ BrowserMob Proxy deshabilitado (enableProxy=false)
✅ Navegador abierto sin proxy
...
🔍 Haciendo clic en el botón Enviar...
⏳ Esperando a que el DOM se actualice después del clic...
✅ Clic en Enviar completado (sin captura de HTTP)
```

---

## 🧪 PRUEBA 2: Con Proxy Habilitado (Nueva Funcionalidad)

### Comando
```bash
mvn clean test -Dtest=RunnersFeature -DenableProxy=true
```

### Resultado Esperado
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
📈 Código de respuesta: 200 (o código real)
⏱️  Tiempo de respuesta: XXXX ms
✅ RESPUESTA EXITOSA (2xx)

📄 Body de respuesta:
{...respuesta real del servicio...}

🔎 Datos extraídos del JSON:
   • Identifier: XXXXX
   • Mensaje: XXXXX
========================================

✅ Navegador cerrado
✅ BrowserMob Proxy detenido
🧹 Proxy limpiado de WebDriverConfig
```

---

## 🧪 PRUEBA 3: Verificar Captura de Petición Específica

### Crear test manual en `ButtonPages.java`

Agregar método temporal después de `clickEnviarConCaptura()`:

```java
public void probarCapturaDePeticiones() {
    if (proxyHelper != null) {
        System.out.println("\n🧪 ===== PRUEBA DE CAPTURA =====");
        
        // Iniciar captura
        proxyHelper.capturarTodasLasPeticiones();
        
        // Navegar a una página
        driver.get("https://aliadosqa.aro.avtest.ink/");
        
        try {
            Thread.sleep(3000); // Esperar carga
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Buscar peticiones capturadas
        System.out.println("🔍 Buscando peticiones capturadas...");
        
        if (proxyHelper.buscarPeticionPorUrl("aliadosqa") != null) {
            System.out.println("✅ Petición encontrada!");
            System.out.println("📈 Código: " + proxyHelper.obtenerCodigoRespuesta("aliadosqa"));
            System.out.println("⏱️ Tiempo: " + proxyHelper.obtenerTiempoRespuesta("aliadosqa") + " ms");
        } else {
            System.out.println("❌ No se encontraron peticiones");
        }
        
        System.out.println("================================\n");
    }
}
```

### Ejecutar
```bash
mvn clean test -DenableProxy=true
```

---

## 🧪 PRUEBA 4: Verificar HAR Completo

### Modificar temporalmente `BrowserMobProxyHelper.java`

Agregar después del método `detenerProxy()`:

```java
public void imprimirResumenHAR() {
    try {
        Har har = proxy.getHar();
        List<HarEntry> entries = har.getLog().getEntries();
        
        System.out.println("\n📊 ===== RESUMEN HAR =====");
        System.out.println("Total de peticiones capturadas: " + entries.size());
        System.out.println("\n🌐 URLs capturadas:");
        
        for (int i = 0; i < Math.min(10, entries.size()); i++) {
            HarEntry entry = entries.get(i);
            String url = entry.getRequest().getUrl();
            int status = entry.getResponse().getStatus();
            long time = entry.getTime();
            
            System.out.println((i + 1) + ". [" + status + "] " + url + " (" + time + " ms)");
        }
        
        if (entries.size() > 10) {
            System.out.println("... y " + (entries.size() - 10) + " más");
        }
        
        System.out.println("===========================\n");
        
    } catch (Exception e) {
        System.err.println("Error al imprimir HAR: " + e.getMessage());
    }
}
```

### Llamar en `Conexion.cerrarNavegador()` antes de detener:

```java
public void cerrarNavegador() {
    try {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Navegador cerrado");
        }
        
        if (proxyHelper != null) {
            proxyHelper.imprimirResumenHAR(); // AGREGAR ESTA LÍNEA
            proxyHelper.detenerProxy();
            System.out.println("✅ BrowserMob Proxy detenido");
        }
        
        WebDriverConfig.clearProxy();
        
    } catch (Exception e) {
        System.err.println("⚠️ Error al cerrar navegador/proxy: " + e.getMessage());
    }
}
```

---

## 🧪 PRUEBA 5: Guardar HAR en Archivo

### Modificar `BrowserMobProxyHelper.java`

Agregar método después de `detenerProxy()`:

```java
/**
 * Guarda el HAR capturado en un archivo
 * @param rutaArchivo Ruta donde guardar el archivo HAR
 */
public void guardarHarEnArchivo(String rutaArchivo) {
    try {
        Har har = proxy.getHar();
        File archivoHar = new File(rutaArchivo);
        
        // Crear directorio si no existe
        archivoHar.getParentFile().mkdirs();
        
        har.writeTo(archivoHar);
        
        System.out.println("✅ HAR guardado en: " + archivoHar.getAbsolutePath());
        System.out.println("📊 Tamaño: " + (archivoHar.length() / 1024) + " KB");
        
    } catch (IOException e) {
        System.err.println("❌ Error al guardar HAR: " + e.getMessage());
    }
}
```

### Modificar `Conexion.cerrarNavegador()`

```java
if (proxyHelper != null) {
    // Guardar HAR antes de detener
    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    proxyHelper.guardarHarEnArchivo("target/har/traffic_" + timestamp + ".har");
    
    proxyHelper.detenerProxy();
    System.out.println("✅ BrowserMob Proxy detenido");
}
```

### Ejecutar
```bash
mvn clean test -DenableProxy=true
```

### Verificar archivo generado
```bash
ls -l target/har/
```

Deberías ver archivos como:
```
traffic_20251022_094512.har
```

Puedes abrir estos archivos en:
- [HAR Viewer Online](http://www.softwareishard.com/har/viewer/)
- Chrome DevTools → Network → Import HAR
- Firefox DevTools → Network → Import HAR

---

## 🧪 PRUEBA 6: Validar Respuesta del Servicio createListBlocks

### Caso 1: Respuesta Exitosa (200)

**Resultado esperado:**
```
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

### Caso 2: Error del Cliente (400)

**Resultado esperado:**
```
📊 ===== ANÁLISIS DE PETICIÓN HTTP =====
🔗 URL buscada: createListBlocks
📍 URL completa: https://aliadosqa.aro.avtest.ink/api/web-pa-holds/tempBlocks/createListBlocks
📈 Código de respuesta: 400
⏱️  Tiempo de respuesta: 523 ms
❌ ERROR DEL CLIENTE (4xx)

📄 Body de respuesta:
{"error":"Datos inválidos","message":"El campo 'asientos' es requerido"}

🔎 Datos extraídos del JSON:
   • Error: Datos inválidos
   • Mensaje: El campo 'asientos' es requerido
========================================
```

### Caso 3: Error del Servidor (500)

**Resultado esperado:**
```
📊 ===== ANÁLISIS DE PETICIÓN HTTP =====
🔗 URL buscada: createListBlocks
📍 URL completa: https://aliadosqa.aro.avtest.ink/api/web-pa-holds/tempBlocks/createListBlocks
📈 Código de respuesta: 500
⏱️  Tiempo de respuesta: 2134 ms
❌ ERROR DEL SERVIDOR (5xx)

📄 Body de respuesta:
{"error":"Internal Server Error","message":"Base de datos no disponible"}
========================================
```

---

## 🧪 PRUEBA 7: Verificar Limpieza de Recursos

### Verificar que el puerto del proxy se libere correctamente

```bash
# Antes de la prueba (no debe haber nada)
netstat -ano | findstr :8080

# Durante la prueba (debe aparecer el proxy)
mvn clean test -DenableProxy=true
# En otra terminal mientras corre:
netstat -ano | findstr :8080

# Después de la prueba (debe estar libre de nuevo)
netstat -ano | findstr :8080
```

---

## 📊 CHECKLIST DE VALIDACIÓN

- [ ] **Prueba 1:** Test corre sin `-DenableProxy` (comportamiento original)
- [ ] **Prueba 2:** Test corre con `-DenableProxy=true` (nueva funcionalidad)
- [ ] **Prueba 3:** Se captura tráfico HTTP correctamente
- [ ] **Prueba 4:** Se imprimen todas las peticiones del HAR
- [ ] **Prueba 5:** Se guarda archivo HAR en `target/har/`
- [ ] **Prueba 6:** Se analiza respuesta de `createListBlocks` correctamente
- [ ] **Prueba 7:** Puerto del proxy se libera después de la prueba
- [ ] Logs muestran inicio del proxy
- [ ] Logs muestran configuración del WebDriver con proxy
- [ ] Logs muestran análisis detallado del servicio HTTP
- [ ] Logs muestran cierre del proxy
- [ ] No hay errores de compilación
- [ ] No hay errores de ejecución
- [ ] Archivos HAR son válidos y se pueden abrir en visualizadores

---

## 🚨 PROBLEMAS COMUNES Y SOLUCIONES

### Problema 1: "Address already in use"
**Causa:** Puerto del proxy no se liberó
**Solución:**
```powershell
# Encontrar proceso en puerto 8080
netstat -ano | findstr :8080
# Matar proceso
taskkill /PID <PID> /F
```

### Problema 2: "Proxy connection refused"
**Causa:** Proxy no se inició correctamente
**Solución:** Verificar logs de inicio, reintentar test

### Problema 3: "No se encuentra la petición createListBlocks"
**Causa:** Petición no se realizó o URL incorrecta
**Solución:** 
- Aumentar tiempo de espera en `clickEnviarConCaptura()`
- Verificar que el botón Enviar funcione
- Revisar HAR completo para ver qué peticiones se capturaron

### Problema 4: "SSL certificate problem"
**Causa:** Certificado no aceptado
**Solución:** Ya está incluido `setAcceptInsecureCerts(true)`, verificar configuración

---

## 📚 RECURSOS ADICIONALES

### Visualizar HAR
- **Online:** http://www.softwareishard.com/har/viewer/
- **Chrome DevTools:** F12 → Network → Import HAR file
- **Firefox DevTools:** F12 → Network → Import HAR file

### Analizar JSON
- **Online:** https://jsonformatter.org/
- **VS Code:** Extensión "JSON Viewer"

### Verificar Servicios
- **Postman:** Importar petición desde HAR
- **cURL:** Generar comando desde HAR

---

**✅ LISTO PARA PRUEBAS**

Ejecuta la Prueba 2 primero para verificar la funcionalidad básica:

```bash
mvn clean test -Dtest=RunnersFeature -DenableProxy=true
```
