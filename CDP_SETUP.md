# 🔧 Configuración de Chrome DevTools Protocol (CDP)

## 📌 Estado Actual

- ✅ **CDP está DESHABILITADO por defecto** - Las pruebas funcionan normalmente
- ⚠️ Chrome 141 no tiene soporte CDP en Selenium 4.10.0
- ✅ El código está preparado para usar CDP cuando esté disponible

---

## 🚀 Cómo Habilitar CDP

### Opción 1: Usar Chrome Versión Compatible (RECOMENDADO)

#### Paso 1: Instalar Chrome versión compatible

Descargar e instalar una versión de Chrome con soporte CDP:
- **Chrome 85**: Más antiguo pero totalmente compatible
- **Chrome 120**: Versión reciente con soporte
- **Chrome 130**: Última versión con soporte en Selenium 4.10.0

Links de descarga:
```
https://www.google.com/chrome/browser/desktop/index.html?extra=devchannel
```

#### Paso 2: Agregar dependencia en pom.xml

```xml
<!-- Agregar DESPUÉS de serenity-cucumber -->
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-devtools-v85</artifactId>
    <version>4.10.0</version>
</dependency>
```

**Cambiar v85 por la versión de tu Chrome:**
- Chrome 85-89 → selenium-devtools-v85
- Chrome 120-124 → selenium-devtools-v120
- Chrome 130-134 → selenium-devtools-v130

#### Paso 3: Configurar ChromeDriver específico

En `WebDriverConfig.java` o `Conexion.java`:

```java
// Forzar versión específica de ChromeDriver
WebDriverManager.chromedriver()
    .driverVersion("130.0.6723.58")  // Versión compatible con Chrome 130
    .setup();
```

#### Paso 4: Habilitar CDP en las pruebas

**Método A: Por línea de comandos (temporal)**
```bash
mvn clean test "-Dtest=RunnersFeature" "-Dcdp.enabled=true"
```

**Método B: En serenity.properties (permanente)**
```properties
# serenity.properties
system.properties.cdp.enabled=true
```

**Método C: En código Java**
```java
// Antes de ejecutar las pruebas
System.setProperty("cdp.enabled", "true");
```

---

### Opción 2: Actualizar Todo el Stack (Más Trabajo)

⚠️ **Requiere cambios mayores**

#### Paso 1: Actualizar Serenity a versión 4.x

```xml
<properties>
    <serenity.version>4.2.0</serenity.version>
    <serenity.maven.version>4.2.0</serenity.maven.version>
    <serenity.cucumber.version>4.2.0</serenity.cucumber.version>
</properties>
```

#### Paso 2: Esto traerá Selenium 4.27+ automáticamente

#### Paso 3: Agregar selenium-devtools-v141

```xml
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-devtools-v141</artifactId>
    <version>4.27.0</version>
</dependency>
```

⚠️ **Problemas potenciales:**
- Serenity 4.x requiere Java 11+
- Posibles breaking changes en APIs
- Necesitas revisar todo el código

---

## 📊 Uso de CDP en tus Pruebas

Una vez habilitado CDP, puedes capturar errores de APIs:

### Ejemplo 1: Capturar errores HTTP en pasos de Cucumber

```java
// En DefinitionsSteps.java
@Given("abrir el navegador")
public void abrir_navegador() {
    driver = conexion.abrirNavegador();
    
    // Inicializar captura de errores de API
    apiErrorCapture = new ApiErrorCapture(driver);
    apiErrorCapture.limpiarLogsRed();
}

@When("El usuario hace clic en Enviar")
public void usuario_hace_clic_en_enviar() {
    buttonPages.btnEnviar();
    
    // Capturar interacciones de red
    List<NetworkInteraction> interacciones = apiErrorCapture.capturarInteraccionesRed();
    
    // Filtrar solo errores (códigos 4xx y 5xx)
    for (NetworkInteraction interaccion : interacciones) {
        int status = interaccion.getResponse().getStatus();
        if (status >= 400) {
            System.out.println("❌ ERROR EN API:");
            System.out.println("URL: " + interaccion.getResponse().getUrl());
            System.out.println("Status: " + status);
            System.out.println("Respuesta: " + interaccion.getResponse().getBody());
        }
    }
}
```

### Ejemplo 2: Validar respuesta específica de API

```java
@Then("el servicio createListBlocks debe responder exitosamente")
public void validar_servicio_create_list_blocks() {
    List<NetworkInteraction> interacciones = apiErrorCapture.capturarInteraccionesRed();
    
    // Filtrar por URL específica
    List<NetworkInteraction> servicioBlocks = apiErrorCapture.filtrarInteraccionesPorUrl(
        interacciones, 
        "createListBlocks"
    );
    
    if (servicioBlocks.isEmpty()) {
        throw new AssertionError("❌ No se encontró llamada al servicio createListBlocks");
    }
    
    NetworkInteraction servicio = servicioBlocks.get(0);
    int status = servicio.getResponse().getStatus();
    
    if (status != 200 && status != 201) {
        String errorBody = servicio.getResponse().getBody();
        throw new AssertionError(
            String.format("❌ Servicio falló con status %d: %s", status, errorBody)
        );
    }
    
    System.out.println("✅ Servicio createListBlocks ejecutado exitosamente");
}
```

### Ejemplo 3: Generar reporte de todas las APIs llamadas

```java
@After
public void generarReporteApis() {
    if (apiErrorCapture != null) {
        List<NetworkInteraction> interacciones = apiErrorCapture.capturarInteraccionesRed();
        
        System.out.println("\n📊 REPORTE DE APIS EJECUTADAS:");
        System.out.println("=====================================");
        
        for (NetworkInteraction interaccion : interacciones) {
            ApiResponse response = interaccion.getResponse();
            
            // Solo mostrar APIs REST (ignorar recursos estáticos)
            if (response.getUrl().contains("/api/") || response.getUrl().contains("/service/")) {
                String emoji = response.getStatus() < 400 ? "✅" : "❌";
                System.out.printf("%s %s %s - Status: %d%n", 
                    emoji,
                    interaccion.getRequest().getMethod(),
                    response.getUrl(),
                    response.getStatus()
                );
            }
        }
        
        System.out.println("=====================================\n");
    }
}
```

---

## 🧪 Verificar que CDP Funciona

Ejecutar prueba con CDP habilitado:

```bash
mvn clean test "-Dtest=RunnersFeature" "-Dcdp.enabled=true"
```

Deberías ver en los logs:
```
✅ Sesión de DevTools creada
✅ Dominio Network habilitado
✅ Chrome DevTools inicializado correctamente
📤 Solicitud capturada: POST https://aliadosqa.aro.avtest.ink/api/createListBlocks
📥 Respuesta capturada: 200 - https://aliadosqa.aro.avtest.ink/api/createListBlocks
```

Si ves:
```
ℹ️ CDP deshabilitado por configuración (cdp.enabled=false)
```
Significa que CDP está deshabilitado (comportamiento por defecto).

---

## ⚡ Ventajas de esta Implementación

1. ✅ **No rompe las pruebas existentes** - CDP es opcional
2. ✅ **Fácil de habilitar/deshabilitar** - Solo cambiar una propiedad
3. ✅ **Logs claros** - Sabes exactamente qué está pasando
4. ✅ **Preparado para el futuro** - Cuando Chrome 141 tenga soporte, solo habilitar
5. ✅ **Captura errores de red** - Puedes detectar fallos de APIs antes que fallen las pruebas

---

## 🎯 Roadmap Sugerido

### Corto Plazo (Esta semana)
- ✅ CDP deshabilitado por defecto
- ✅ Pruebas funcionando al 100%
- ✅ Código preparado para CDP

### Mediano Plazo (Próximo mes)
- 🔄 Instalar Chrome 130 en ambiente de QA
- 🔄 Agregar `selenium-devtools-v130` al pom.xml
- 🔄 Habilitar CDP en ambiente de QA: `-Dcdp.enabled=true`
- 🔄 Validar captura de errores de APIs

### Largo Plazo (3-6 meses)
- 🔄 Evaluar actualización a Serenity 4.x + Selenium 4.27+
- 🔄 Soporte nativo para Chrome 141+
- 🔄 Integrar reportes de APIs en Serenity Reports

---

## 📞 Soporte

Si tienes dudas:
1. Revisa los logs - son muy descriptivos
2. Verifica la versión de Chrome: `chrome://version/`
3. Confirma que CDP está habilitado: busca en logs `✅ Chrome DevTools inicializado`
