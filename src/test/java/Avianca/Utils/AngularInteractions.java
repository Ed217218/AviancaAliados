package Avianca.Utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * 🎯 Clase SIMPLIFICADA para manejar interacciones con aplicaciones Angular
 * VERSIÓN 2.0 - Sin dependencia de AngularJS APIs
 * 
 * Esta versión NO asume que la aplicación usa AngularJS (Angular 1.x)
 * Funciona con Angular moderno (2+) y con cualquier aplicación web
 */
public class AngularInteractions {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor jsExecutor;

    public AngularInteractions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.jsExecutor = (JavascriptExecutor) driver;
    }

    // ========================================
    // MÉTODOS PRINCIPALES SIMPLIFICADOS
    // ========================================

    /**
     * 🔧 Espera simple para que Angular procese cambios
     * NO usa APIs específicas de Angular para evitar incompatibilidades
     */
    public void esperarAngularEstable() {
        try {
            System.out.println("⏳ Esperando procesamiento...");
            Thread.sleep(1000); // Espera fija de 1 segundo
            System.out.println("✅ Espera completada");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 🔧 Espera a que Angular esté listo (versión básica)
     * Solo verifica que el documento esté listo
     */
    public void waitForAngularReady() {
        try {
            System.out.println("⏳ Esperando a que la página esté lista...");
            
            String script = "return document.readyState === 'complete';";
            
            wait.until(driver -> {
                try {
                    Object result = jsExecutor.executeScript(script);
                    return result != null && (Boolean) result;
                } catch (Exception e) {
                    return true;
                }
            });
            
            Thread.sleep(500); // Espera adicional para asegurar
            System.out.println("✅ Página lista");
        } catch (Exception e) {
            System.out.println("⚠️ Timeout esperando página: " + e.getMessage());
        }
    }

    /**
     * 🔧 Espera genérica para peticiones HTTP
     * Usa espera fija en lugar de detectar peticiones pendientes
     */
    public void waitForHttpRequests() {
        try {
            System.out.println("⏳ Esperando peticiones HTTP...");
            Thread.sleep(1500); // Espera fija de 1.5 segundos
            System.out.println("✅ Espera HTTP completada");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 🔧 Fuerza un re-render esperando un momento
     * Reemplaza la detección de cambios de Angular
     */
    public void forceChangeDetection() {
        try {
            System.out.println("🔄 Esperando re-render...");
            Thread.sleep(500);
            System.out.println("✅ Re-render completado");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 🔧 OBSOLETO: Mantenido por compatibilidad, usa forceChangeDetection()
     */
    @Deprecated
    public void forceAngularChangeDetection() {
        forceChangeDetection();
    }

    /**
     * 🔧 OBSOLETO: Mantenido por compatibilidad, usa waitForHttpRequests()
     */
    @Deprecated
    public void waitForAngularHttpRequests() {
        waitForHttpRequests();
    }

    /**
     * 🔧 Fuerza un ciclo de digestión (versión simplificada)
     */
    public void forzarCicloDigestionAngular() {
        forceChangeDetection();
    }

    // ========================================
    // MÉTODOS DE CLIC OPTIMIZADOS
    // ========================================

    /**
     * 🔧 Clic con eventos nativos completos (RECOMENDADO)
     * Compatible con Angular moderno y cualquier framework
     */
    public void clickWithNativeEvents(WebElement element) {
        try {
            System.out.println("🖱️ Ejecutando clic con eventos nativos...");
            
            String script = 
                "var element = arguments[0];" +
                // Asegurar que el elemento esté visible
                "element.scrollIntoView({block: 'center'});" +
                // Disparar eventos completos de mouse en secuencia
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
                // Disparar evento 'change' si es input o select
                "if (element.tagName === 'INPUT' || element.tagName === 'SELECT') {" +
                "  var changeEvent = new Event('change', { bubbles: true });" +
                "  element.dispatchEvent(changeEvent);" +
                "}";
            
            jsExecutor.executeScript(script, element);
            Thread.sleep(300); // Espera breve para que procese
            System.out.println("✅ Clic con eventos nativos ejecutado");
        } catch (Exception e) {
            System.err.println("❌ Error en clic con eventos nativos: " + e.getMessage());
            throw new RuntimeException("No se pudo hacer clic con eventos nativos", e);
        }
    }

    /**
     * 🔧 OBSOLETO: clickInsideNgZone ahora usa eventos nativos
     * Mantenido por compatibilidad con código existente
     */
    @Deprecated
    public void clickInsideNgZone(WebElement element) {
        clickWithNativeEvents(element);
    }

    /**
     * 🔧 Dispara eventos de mouse completos
     */
    public void dispatchMouseEvents(WebElement element) {
        try {
            System.out.println("🖱️ Disparando eventos de mouse...");
            
            String script = 
                "var element = arguments[0];" +
                "var events = ['mouseover', 'mouseenter', 'mousedown', 'mouseup', 'click'];" +
                "events.forEach(function(eventType) {" +
                "  var event = new MouseEvent(eventType, {" +
                "    view: window," +
                "    bubbles: true," +
                "    cancelable: true" +
                "  });" +
                "  element.dispatchEvent(event);" +
                "});";
            
            jsExecutor.executeScript(script, element);
            System.out.println("✅ Eventos de mouse disparados");
        } catch (Exception e) {
            System.err.println("❌ Error al disparar eventos de mouse: " + e.getMessage());
            throw new RuntimeException("No se pudieron disparar eventos de mouse", e);
        }
    }

    /**
     * 🔧 Realiza clic después de un hover exitoso
     */
    public boolean realizarClicDespuesHover(WebElement elemento) {
        try {
            System.out.println("🖱️ Realizando clic después de hover...");
            
            // Esperar que el elemento esté clickeable
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            
            // Clic con eventos nativos
            clickWithNativeEvents(elemento);
            
            // Esperar procesamiento
            Thread.sleep(300);
            
            System.out.println("✅ Clic después de hover ejecutado correctamente");
            return true;
        } catch (Exception e) {
            System.out.println("⚠️ Error al realizar clic después de hover: " + e.getMessage());
            return false;
        }
    }

    // ========================================
    // MÉTODOS DE HOVER (SIMPLIFICADOS)
    // ========================================

    /**
     * 🎯 Simula hover en Angular (versión simplificada)
     */
    public boolean simularHoverAngular(WebElement elemento) {
        System.out.println("🖱️ Simulando hover...");
        
        try {
            // Disparar eventos de mouse over
            String script = 
                "var element = arguments[0];" +
                "var mouseoverEvent = new MouseEvent('mouseover', {" +
                "  view: window," +
                "  bubbles: true," +
                "  cancelable: true" +
                "});" +
                "element.dispatchEvent(mouseoverEvent);" +
                "var mouseenterEvent = new MouseEvent('mouseenter', {" +
                "  view: window," +
                "  bubbles: true," +
                "  cancelable: true" +
                "});" +
                "element.dispatchEvent(mouseenterEvent);";
            
            jsExecutor.executeScript(script, elemento);
            Thread.sleep(500); // Espera para que procese el hover
            
            System.out.println("✅ Hover simulado correctamente");
            return true;
        } catch (Exception e) {
            System.out.println("⚠️ Error simulando hover: " + e.getMessage());
            return false;
        }
    }
}
