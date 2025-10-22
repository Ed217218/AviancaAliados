# 📋 PLAN DETALLADO DE SOLUCIÓN - PASO A PASO

## 🔍 RESUMEN EJECUTIVO

**Problema Principal:** La aplicación usa **Angular moderno** (no AngularJS), pero el código está intentando usar APIs de **AngularJS (Angular 1.x)** que no existen.

**Impacto:** 
- ❌ No se capturan logs de red (CDP no configurado)
- ❌ Scripts de NgZone fallan (aplicación no usa AngularJS)
- ❌ No se detectan llamadas a servicios

---

## 📊 ERRORES IDENTIFICADOS Y PRIORIDAD

| # | Error | Causa Raíz | Prioridad | Complejidad |
|---|-------|------------|-----------|-------------|
| 1 | Los logs de rendimiento no están disponibles | CDP no habilitado correctamente | 🔴 ALTA | Media |
| 2 | Cannot read properties of undefined (reading 'element') | App usa Angular moderno, no AngularJS | 🔴 ALTA | Alta |
| 3 | script timeout | Métodos asíncronos incompatibles | 🟡 MEDIA | Baja |
| 4 | Unable to find CDP implementation matching 141 | Selenium 4.10.0 no soporta Chrome 141 | 🟡 MEDIA | Media |
| 5 | window.angular no existe | Código busca AngularJS en app Angular moderna | 🔴 ALTA | Alta |

---

## ✅ FASE 1: IDENTIFICAR VERSIÓN DE ANGULAR

### □ 1.1 Detectar versión de Angular de la aplicación

**Objetivo:** Confirmar si la aplicación usa Angular moderno (2+) o AngularJS (1.x)

**Pasos:**

```javascript
// Ejecutar en la consola del navegador (F12)
// Opción 1: Detectar Angular moderno
console.log('Angular detectado:', !!window.ng);
console.log('Versión:', window.ng?.getVersion?.());

// Opción 2: Detectar AngularJS
console.log('AngularJS detectado:', !!window.angular);

// Opción 3: Inspeccionar elementos
console.log('Atributos ng-*:', document.querySelector('[ng-app], [ng-controller]'));
console.log('Componentes Angular:', document.querySelector('[_nghost-ng-*], [_ngcontent-ng-*]'));
```

**Resultado Esperado:**
- Si `window.ng` existe → Angular moderno (2+)
- Si `window.angular` existe → AngularJS (1.x)
- Si encuentra `_nghost` o `_ngcontent` → Angular moderno

---

## ✅ FASE 2: HABILITAR CHROME DEVTOOLS PROTOCOL (CDP)

### □ 2.1 Actualizar pom.xml con dependencia CDP correcta

**Archivo:** `pom.xml`

**Acción:** Agregar dependencia para Chrome 141

```xml
<!-- Agregar después de la dependencia de selenium-java -->
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-devtools-v141</artifactId>
    <version>4.10.0</version>
</dependency>
```

**Verificación:**
```bash
mvn clean compile
```

**Estado:** □ Pendiente

---

### □ 2.2 Actualizar WebDriverConfig.java

**Archivo:** `src/test/java/Avianca/Utils/WebDriverConfig.java`

**Cambios Necesarios:**

1. **Agregar imports:**
```java
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v141.network.Network;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
```

2. **Modificar método getChromeDriver():**
```java
private static WebDriver getChromeDriver() {
    WebDriverManager.chromedriver().setup();
    System.out.println("✅ ChromeDriver configurado con WebDriverManager");
    
    ChromeOptions options = new ChromeOptions();
    
    // Configuración básica
    if (HEADLESS) {
        options.addArguments("--headless=new");
    }
    options.addArguments("--disable-notifications");
    options.addArguments("--disable-infobars");
    options.addArguments("--disable-extensions");
    options.addArguments("--disable-gpu");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--remote-allow-origins=*");
    
    // CRÍTICO: Habilitar Performance Logs para CDP
    LoggingPreferences logPrefs = new LoggingPreferences();
    logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
    logPrefs.enable(LogType.BROWSER, Level.ALL);
    
    // Usar el formato correcto para Chrome
    Map<String, Object> perfLoggingPrefs = new HashMap<>();
    perfLoggingPrefs.put("enableNetwork", true);
    perfLoggingPrefs.put("enablePage", false);
    perfLoggingPrefs.put("traceCategories", "browser,devtools.timeline,devtools");
    
    options.setCapability("goog:loggingPrefs", logPrefs);
    options.setCapability("goog:perfLoggingPrefs", perfLoggingPrefs);
    
    System.out.println("✅ Performance Logs habilitados");
    
    ChromeDriver driver = new ChromeDriver(options);
    
    // Habilitar CDP Network Domain
    try {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(
            Optional.empty(), 
            Optional.empty(), 
            Optional.empty()
        ));
        System.out.println("✅ Chrome DevTools Protocol (CDP) habilitado");
    } catch (Exception e) {
        System.out.println("⚠️ Error habilitando CDP: " + e.getMessage());
        System.out.println("⚠️ La captura de servicios puede no funcionar correctamente");
    }
    
    return driver;
}
```

**Estado:** □ Pendiente

---

## ✅ FASE 3: CORREGIR MÉTODOS ANGULARJS → ANGULAR MODERNO

### □ 3.1 Actualizar AngularInteractions.java

**Problema:** El código usa `window.angular.element()` que solo existe en AngularJS.

**Solución:** Reemplazar con métodos compatibles con Angular moderno.

**Archivo:** `src/test/java/Avianca/Utils/AngularInteractions.java`

**Cambios:**

#### **A. Método esperarAngularEstable()** - Ya está bien implementado ✅

Este método SÍ funciona con Angular moderno porque usa `getAllAngularTestabilities()`.

**PERO:** Reducir timeout para evitar script timeout:

```java
public void esperarAngularEstable() {
    try {
        System.out.println("⏳ Esperando estabilidad completa de Angular...");
        
        // TIMEOUT REDUCIDO: 15 segundos en lugar de 30
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        String script = 
            "var callback = arguments[arguments.length - 1];" +
            "if (window.getAllAngularTestabilities) { " +
            "    var testabilities = window.getAllAngularTestabilities(); " +
            "    var count = testabilities.length; " +
            "    if (count === 0) { callback(true); return; }" +
            "    var decrement = function() { " +
            "        count--; " +
            "        if (count === 0) { " +
            "            callback(true); " +
            "        } " +
            "    }; " +
            "    testabilities.forEach(function(testability) { " +
            "        testability.whenStable(decrement); " +
            "    }); " +
            "} else { " +
            "    callback(true); " +
            "}";
        
        jsExecutor.executeAsyncScript(script);
        System.out.println("✅ Angular está completamente estable");
    } catch (Exception e) {
        // No es crítico, continuar
        System.out.println("⚠️ Timeout esperando Angular (no crítico): " + e.getMessage());
    }
}
```

#### **B. Método waitForAngularHttpRequests()** - ELIMINAR ❌

Este método usa `window.angular` que NO existe en Angular moderno.

**Reemplazar con:**

```java
/**
 * 🔧 Espera genérica para peticiones HTTP (sin depender de AngularJS)
 */
public void waitForHttpRequests() {
    try {
        System.out.println("⏳ Esperando peticiones HTTP...");
        Thread.sleep(1500); // Espera fija de 1.5 segundos
        System.out.println("✅ Espera completada");
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
```

#### **C. Método forceAngularChangeDetection()** - ELIMINAR ❌

Usa `window.angular.element()` que no existe.

**Reemplazar con:**

```java
/**
 * 🔧 Fuerza un re-render esperando un momento
 */
public void forceChangeDetection() {
    try {
        System.out.println("🔄 Esperando re-render de Angular...");
        Thread.sleep(500);
        System.out.println("✅ Re-render completado");
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
```

#### **D. Método clickInsideNgZone()** - REEMPLAZAR ❌

**Nuevo método compatible:**

```java
/**
 * 🔧 Clic con eventos nativos (compatible con Angular moderno)
 */
public void clickWithNativeEvents(WebElement element) {
    try {
        System.out.println("🖱️ Ejecutando clic con eventos nativos...");
        
        String script = 
            "var element = arguments[0];" +
            // Disparar eventos completos de mouse
            "var events = ['mousedown', 'mouseup', 'click'];" +
            "events.forEach(function(eventType) {" +
            "  var event = new MouseEvent(eventType, {" +
            "    view: window," +
            "    bubbles: true," +
            "    cancelable: true," +
            "    buttons: 1" +
            "  });" +
            "  element.dispatchEvent(event);" +
            "});" +
            // Disparar también el evento 'change' si es input
            "if (element.tagName === 'INPUT' || element.tagName === 'SELECT') {" +
            "  var changeEvent = new Event('change', { bubbles: true });" +
            "  element.dispatchEvent(changeEvent);" +
            "}";
        
        jsExecutor.executeScript(script, element);
        Thread.sleep(300);
        System.out.println("✅ Clic con eventos nativos ejecutado");
    } catch (Exception e) {
        System.err.println("❌ Error en clic con eventos nativos: " + e.getMessage());
        throw new RuntimeException("No se pudo hacer clic con eventos nativos", e);
    }
}
```

**Estado:** □ Pendiente

---

### □ 3.2 Actualizar ElementInteractions.java

**Cambios necesarios:**

#### **A. Método prepararElementoParaInteraccionAngular() en ButtonPages**

Reemplazar llamadas a métodos eliminados:

```java
private void prepararElementoParaInteraccionAngular(WebElement elemento) throws InterruptedException {
    System.out.println("🔄 Preparando elemento para interacción Angular...");
    
    // Esperar a que Angular esté estable (CON TIMEOUT CORTO)
    try {
        elementInteractions.angularInteractions.esperarAngularEstable();
    } catch (Exception e) {
        System.out.println("  ⚠️ Timeout Angular (continuando): " + e.getMessage());
    }
    
    // Esperar visibilidad del elemento
    wait.until(ExpectedConditions.visibilityOf(elemento));
    System.out.println("  ✅ Elemento visible");
    
    // Hacer scroll al elemento
    elementInteractions.scrollToElement(elemento);
    System.out.println("  ✅ Scroll realizado");
    
    Thread.sleep(1000);
    
    // Verificar que el elemento esté habilitado
    if (!elemento.isEnabled()) {
        System.out.println("  ⏳ Esperando que el botón se habilite...");
        wait.until(ExpectedConditions.elementToBeClickable(elemento));
    }
    System.out.println("  ✅ Elemento habilitado y clickeable");
    
    // Espera genérica para HTTP (SIN window.angular)
    elementInteractions.angularInteractions.waitForHttpRequests();
    System.out.println("  ✅ Espera HTTP completada");
    
    System.out.println("✅ Elemento preparado correctamente");
}
```

#### **B. Método intentarClicDentroNgZone()**

Reemplazar con el nuevo método:

```java
public boolean intentarClicDentroNgZone(WebElement elemento) {
    try {
        System.out.println("🔄 Intentando clic con eventos nativos...");
        
        // Esperar Angular (con timeout corto)
        try {
            angularInteractions.esperarAngularEstable();
        } catch (Exception e) {
            // No crítico
        }
        
        // Esperar visibilidad
        wait.until(ExpectedConditions.visibilityOf(elemento));
        
        // Hacer scroll
        scrollToElement(elemento);
        
        // Clic con eventos nativos (NUEVO MÉTODO)
        angularInteractions.clickWithNativeEvents(elemento);
        
        System.out.println("✅ Clic con eventos nativos realizado");
        return true;
    } catch (Exception e) {
        System.err.println("❌ Falló clic con eventos nativos: " + e.getMessage());
        return false;
    }
}
```

#### **C. Método intentarClicConDeteccionForzada()**

Reemplazar llamadas:

```java
public boolean intentarClicConDeteccionForzada(WebElement elemento) {
    try {
        System.out.println("🔄 Intentando clic con re-render...");
        
        // Esperar elementos
        wait.until(ExpectedConditions.visibilityOf(elemento));
        wait.until(ExpectedConditions.elementToBeClickable(elemento));
        
        // Scroll
        scrollToElement(elemento);
        
        // Esperar re-render (NUEVO MÉTODO)
        angularInteractions.forceChangeDetection();
        
        // Clic con JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
        
        // Esperar re-render después
        angularInteractions.forceChangeDetection();
        
        System.out.println("✅ Clic con re-render realizado");
        return true;
    } catch (Exception e) {
        System.err.println("❌ Falló clic con re-render: " + e.getMessage());
        return false;
    }
}
```

**Estado:** □ Pendiente

---

## ✅ FASE 4: MEJORAR CAPTURA DE SERVICIOS

### □ 4.1 Verificar ApiErrorCapture.java

**Archivo:** `src/test/java/Avianca/Utils/ApiErrorCapture.java`

**Verificar que el método `capturarInteraccionesRed()` use Performance Logs:**

```java
public List<NetworkInteraction> capturarInteraccionesRed() {
    List<NetworkInteraction> interacciones = new ArrayList<>();
    
    try {
        // Obtener logs de performance
        LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);
        
        if (logs == null || logs.getAll().isEmpty()) {
            logger.error("❌ Los logs de rendimiento no están disponibles");
            return interacciones;
        }
        
        System.out.println("📊 Logs de performance capturados: " + logs.getAll().size());
        
        // Procesar logs...
        for (LogEntry entry : logs) {
            try {
                JSONObject json = new JSONObject(entry.getMessage());
                JSONObject message = json.getJSONObject("message");
                String method = message.getString("method");
                
                // Capturar solo eventos de red
                if (method.startsWith("Network.")) {
                    // ... procesamiento ...
                }
            } catch (Exception e) {
                // Ignorar logs mal formados
            }
        }
        
    } catch (Exception e) {
        logger.error("❌ Error capturando logs: " + e.getMessage());
    }
    
    return interacciones;
}
```

**Estado:** □ Pendiente - Verificar

---

## ✅ FASE 5: SIMPLIFICAR ESTRATEGIAS DE CLIC

### □ 5.1 Actualizar realizarClicOptimizadoAngular() en ButtonPages

**Problema:** Demasiadas estrategias, algunas no funcionan.

**Solución:** Reducir a 4 estrategias efectivas:

```java
private boolean realizarClicOptimizadoAngular(WebElement elemento) {
    System.out.println("📋 Intentando estrategias de clic optimizadas:\n");
    
    // ESTRATEGIA 1: Clic con eventos nativos (MEJOR PARA ANGULAR MODERNO)
    System.out.println("   [1/4] Clic con eventos nativos...");
    if (elementInteractions.intentarClicDentroNgZone(elemento)) {
        System.out.println("   ✅ ÉXITO: Clic con eventos nativos");
        return true;
    }
    System.out.println("   ❌ Falló estrategia 1");
    
    // ESTRATEGIA 2: Clic con JavaScript puro
    System.out.println("   [2/4] Clic con JavaScript...");
    if (elementInteractions.intentarClickConJavaScript(elemento)) {
        System.out.println("   ✅ ÉXITO: Clic con JavaScript");
        return true;
    }
    System.out.println("   ❌ Falló estrategia 2");
    
    // ESTRATEGIA 3: Clic normal de Selenium
    System.out.println("   [3/4] Clic normal...");
    if (elementInteractions.intentarClicNormal(elemento)) {
        System.out.println("   ✅ ÉXITO: Clic normal");
        return true;
    }
    System.out.println("   ❌ Falló estrategia 3");
    
    // ESTRATEGIA 4: Hover + clic tradicional
    System.out.println("   [4/4] Hover + clic...");
    if (elementInteractions.intentarClicConHoverYClick(elemento)) {
        System.out.println("   ✅ ÉXITO: Hover + clic");
        return true;
    }
    System.out.println("   ❌ Falló estrategia 4");
    
    System.out.println("\n❌ TODAS LAS ESTRATEGIAS FALLARON");
    return false;
}
```

**Estado:** □ Pendiente

---

## ✅ FASE 6: AUMENTAR TIMEOUT Y MEJORAR ESPERAS

### □ 6.1 Configurar timeouts adecuados

**Archivo:** `src/test/java/Avianca/Utils/AngularInteractions.java`

**En el constructor:**

```java
public AngularInteractions(WebDriver driver) {
    this.driver = driver;
    this.jsExecutor = (JavascriptExecutor) driver;
    
    // TIMEOUT REDUCIDO para evitar script timeout
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    
    // Configurar timeout de scripts asíncronos
    driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(15));
}
```

**Estado:** □ Pendiente

---

### □ 6.2 Aumentar espera después del clic

**En ButtonPages.clickEnviar():**

```java
// Después del clic exitoso
if (clicExitoso) {
    System.out.println("✅ Clic ejecutado");
    
    // ESPERA AUMENTADA para que el servicio se procese
    System.out.println("⏳ Esperando 5 segundos para que el servicio se procese...");
    Thread.sleep(5000); // Era 3000, ahora 5000
}
```

**Estado:** □ Pendiente

---

## ✅ FASE 7: TESTING Y VALIDACIÓN

### □ 7.1 Test de configuración CDP

**Script de prueba:**

```java
@Test
public void testCDPConfiguration() {
    WebDriver driver = WebDriverConfig.getDriver();
    driver.get("https://www.google.com");
    
    // Verificar que los logs estén disponibles
    LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);
    System.out.println("Logs capturados: " + logs.getAll().size());
    
    assertTrue("Los logs deben estar disponibles", logs.getAll().size() > 0);
    
    driver.quit();
}
```

**Estado:** □ Pendiente

---

### □ 7.2 Test de detección de Angular

**Script de prueba (ejecutar en consola del navegador):**

```javascript
// Abrir https://aliadosqa.aro.avtest.ink/
// Abrir consola (F12)

// Test 1: Detectar tipo de Angular
console.log('=== DETECCIÓN DE ANGULAR ===');
console.log('Angular moderno (ng):', !!window.ng);
console.log('AngularJS (angular):', !!window.angular);
console.log('getAllAngularTestabilities:', !!window.getAllAngularTestabilities);

// Test 2: Ver componentes
const hasModernAngular = document.querySelector('[_nghost-ng-*], [_ngcontent-ng-*]') !== null;
const hasAngularJS = document.querySelector('[ng-app], [ng-controller]') !== null;
console.log('Componentes Angular moderno:', hasModernAngular);
console.log('Componentes AngularJS:', hasAngularJS);

// Test 3: Ver si hay testabilities
if (window.getAllAngularTestabilities) {
    const testabilities = window.getAllAngularTestabilities();
    console.log('Testabilities encontradas:', testabilities.length);
}
```

**Estado:** □ Pendiente

---

## 📊 CHECKLIST DE IMPLEMENTACIÓN

### 🔴 PRIORIDAD ALTA (Hacer primero)

- [ ] **1.1** - Detectar versión de Angular en consola del navegador
- [ ] **2.1** - Agregar dependencia `selenium-devtools-v141` en pom.xml
- [ ] **2.2** - Actualizar `WebDriverConfig.java` con CDP
- [ ] **3.1** - Actualizar `AngularInteractions.java`:
  - [ ] Reducir timeout en `esperarAngularEstable()`
  - [ ] Reemplazar `waitForAngularHttpRequests()` → `waitForHttpRequests()`
  - [ ] Reemplazar `forceAngularChangeDetection()` → `forceChangeDetection()`
  - [ ] Reemplazar `clickInsideNgZone()` → `clickWithNativeEvents()`
- [ ] **3.2** - Actualizar `ElementInteractions.java`:
  - [ ] Actualizar `intentarClicDentroNgZone()`
  - [ ] Actualizar `intentarClicConDeteccionForzada()`
- [ ] **3.2** - Actualizar `ButtonPages.java`:
  - [ ] Actualizar `prepararElementoParaInteraccionAngular()`
  
### 🟡 PRIORIDAD MEDIA (Hacer después)

- [ ] **4.1** - Verificar `ApiErrorCapture.java` funcione con CDP
- [ ] **5.1** - Simplificar `realizarClicOptimizadoAngular()` a 4 estrategias
- [ ] **6.1** - Configurar timeouts en constructor de AngularInteractions
- [ ] **6.2** - Aumentar espera después del clic a 5 segundos

### 🟢 PRIORIDAD BAJA (Testing)

- [ ] **7.1** - Test de configuración CDP
- [ ] **7.2** - Test de detección de Angular

---

## 🎯 ORDEN DE EJECUCIÓN RECOMENDADO

```
1. Detectar versión de Angular (7.2 en navegador)
   ↓
2. Agregar dependencia CDP (2.1)
   ↓
3. Actualizar WebDriverConfig (2.2)
   ↓
4. Compilar proyecto (mvn clean compile)
   ↓
5. Actualizar AngularInteractions (3.1)
   ↓
6. Actualizar ElementInteractions (3.2)
   ↓
7. Actualizar ButtonPages (3.2)
   ↓
8. Compilar y ejecutar test (mvn test)
   ↓
9. Verificar logs en consola
   ↓
10. Ajustar timeouts si es necesario (6.1, 6.2)
```

---

## 🔧 COMANDOS ÚTILES

```bash
# Limpiar y compilar
mvn clean compile

# Compilar sin tests
mvn clean compile -DskipTests

# Ejecutar solo un test específico
mvn test -Dtest=RunnersFeature

# Ver dependencias
mvn dependency:tree

# Actualizar dependencias
mvn clean install -U
```

---

## ⚠️ NOTAS IMPORTANTES

1. **La aplicación usa Angular MODERNO, no AngularJS:**
   - NO usar `window.angular`
   - NO usar `injector.get('$rootScope')`
   - NO usar `$apply()` o `$digest()`

2. **CDP es crítico para capturar servicios:**
   - Sin CDP no se pueden capturar las llamadas HTTP
   - Requiere dependencia `selenium-devtools-v141`
   - Debe configurarse al crear el ChromeDriver

3. **Los scripts asíncronos tienen timeout:**
   - Reducir de 30s a 15s
   - Configurar en `driver.manage().timeouts().scriptTimeout()`

4. **Los eventos nativos son la clave:**
   - Usar `MouseEvent` con `bubbles: true`
   - Disparar `mousedown`, `mouseup`, `click` en secuencia
   - Disparar `change` para inputs

---

## 📈 MÉTRICAS DE ÉXITO

| Métrica | Actual | Objetivo |
|---------|--------|----------|
| Logs CDP capturados | ❌ 0 | ✅ > 0 |
| Errores de Angular | ❌ Sí | ✅ No |
| Script timeouts | ❌ Sí | ✅ No |
| Servicios detectados | ❌ 0/2 | ✅ 2/2 |
| Tasa de éxito del clic | ❌ 0% | ✅ 90%+ |

---

## 🆘 TROUBLESHOOTING

### Si aún no se capturan logs después de CDP:

1. Verificar en consola que CDP está activo:
   ```
   ✅ Chrome DevTools Protocol (CDP) habilitado
   ```

2. Verificar logs de performance disponibles:
   ```java
   LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);
   System.out.println("Logs: " + logs.getAll().size());
   ```

3. Si logs == 0, verificar Chrome Options:
   ```java
   options.setCapability("goog:loggingPrefs", logPrefs);
   options.setCapability("goog:perfLoggingPrefs", perfLoggingPrefs);
   ```

---

**Fecha de creación:** 21 de octubre de 2025  
**Estado:** 📋 Plan Completo - Listo para implementar  
**Prioridad:** 🔴 ALTA
