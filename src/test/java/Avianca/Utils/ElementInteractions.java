package Avianca.Utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ElementInteractions {
    private WebDriver driver;
    private WebDriverWait wait;
    public AngularInteractions angularInteractions; // Cambiado a public para acceso desde ButtonPages

    public ElementInteractions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.angularInteractions = new AngularInteractions(driver);
    }

    /**
     * 🔧 Hace scroll a un elemento
     * @param elemento Elemento al que se hará scroll
     */
    public void scrollToElement(WebElement elemento) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", elemento);
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo hacer scroll al elemento: " + e.getMessage());
        }
    }

    /**
     * 🔧 Intenta hacer clic normal
     * @param elemento Elemento sobre el que se hará clic
     * @return true si el clic fue exitoso, false en caso contrario
     */
    public boolean intentarClicNormal(WebElement elemento) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            elemento.click();
            System.out.println("✅ Clic normal realizado con éxito");
            return true;
        } catch (Exception e) {
            System.out.println("⚠️ Clic normal falló: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔧 Intenta hacer hover y luego clic con Actions
     * @param elemento Elemento sobre el que se hará hover y clic
     * @return true si el clic fue exitoso, false en caso contrario
     */
    public boolean intentarClicConHoverYClick(WebElement elemento) {
        try {
            System.out.println("🖱️ Intentando hacer hover sobre el elemento...");
            
            Actions actions = new Actions(driver);
            actions.moveToElement(elemento).perform();
            System.out.println("✅ Hover realizado sobre el elemento");
            
            Thread.sleep(2000);
            
            wait.until(ExpectedConditions.visibilityOf(elemento));
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            
            actions.click().perform();
            System.out.println("✅ Clic realizado después del hover");
            
            return true;
        } catch (Exception e) {
            System.out.println("⚠️ Falló el intento de hover y clic: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔧 Clic con JavaScript como alternativa
     * @param elemento Elemento sobre el que se hará clic
     * @return true si el clic fue exitoso, false en caso contrario
     */
    public boolean intentarClickConJavaScript(WebElement elemento) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
            System.out.println("✅ Clic realizado con JavaScript");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Falló el clic con JavaScript: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔧 MÉTODO MEJORADO: Intenta hacer hover y luego clic con soporte para Angular
     */
    public boolean intentarClicConHoverYClickAngular(WebElement elemento) {
        try {
            System.out.println("🖱️ Intentando hacer hover y clic con soporte para Angular...");
            
            // Paso 1: Simular hover específico para Angular
            if (!angularInteractions.simularHoverAngular(elemento)) {
                System.out.println("❌ No se pudo simular el hover en Angular");
                return false;
            }
            
            // Paso 2: Realizar clic después del hover
            if (!angularInteractions.realizarClicDespuesHover(elemento)) {
                System.out.println("❌ No se pudo realizar el clic después del hover");
                return false;
            }
            
            System.out.println("✅ Hover y clic realizados exitosamente en Angular");
            return true;
        } catch (Exception e) {
            System.out.println("⚠️ Falló el intento de hover y clic en Angular: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔧 Clic con soporte completo para Angular (NgZone)
     * @param elemento WebElement sobre el que se hará clic
     * @return true si el clic fue exitoso, false en caso contrario
     */
    public boolean intentarClicConSoporteAngular(WebElement elemento) {
        try {
            System.out.println("🔄 Intentando clic con soporte completo para Angular...");
            
            // Paso 1: Esperar a que Angular esté listo
            angularInteractions.waitForAngularReady();
            
            // Paso 2: Esperar a que el elemento sea visible y clickeable
            wait.until(ExpectedConditions.visibilityOf(elemento));
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            
            // Paso 3: Hacer scroll al elemento
            scrollToElement(elemento);
            
            // Paso 4: Esperar un momento después del scroll
            Thread.sleep(500);
            
            // Paso 5: Disparar eventos de mouse completos
            angularInteractions.dispatchMouseEvents(elemento);
            
            // Paso 6: Esperar a que Angular procese los eventos
            angularInteractions.waitForAngularReady();
            
            System.out.println("✅ Clic con soporte Angular realizado");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Falló el clic con soporte Angular: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔧 Clic dentro de la zona de Angular (NgZone)
     * @param elemento WebElement sobre el que se hará clic
     * @return true si el clic fue exitoso, false en caso contrario
     */
    public boolean intentarClicDentroNgZone(WebElement elemento) {
        try {
            System.out.println("🔄 Intentando clic dentro de NgZone...");
            
            // Esperar a que Angular esté listo
            angularInteractions.waitForAngularReady();
            
            // Esperar a que el elemento sea visible
            wait.until(ExpectedConditions.visibilityOf(elemento));
            
            // Hacer scroll al elemento
            scrollToElement(elemento);
            
            // Realizar clic dentro de NgZone
            angularInteractions.clickInsideNgZone(elemento);
            
            // Esperar a que Angular procese el clic
            angularInteractions.waitForAngularReady();
            
            System.out.println("✅ Clic dentro de NgZone realizado");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Falló el clic dentro de NgZone: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔧 Clic con forzado de detección de cambios de Angular
     * @param elemento WebElement sobre el que se hará clic
     * @return true si el clic fue exitoso, false en caso contrario
     */
    public boolean intentarClicConDeteccionForzada(WebElement elemento) {
        try {
            System.out.println("🔄 Intentando clic con detección forzada de Angular...");
            
            // Esperar a que el elemento sea visible y clickeable
            wait.until(ExpectedConditions.visibilityOf(elemento));
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            
            // Hacer scroll al elemento
            scrollToElement(elemento);
            
            // Forzar la detección de cambios antes del clic
            angularInteractions.forceAngularChangeDetection();
            
            // Realizar clic con JavaScript
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
            
            // Forzar la detección de cambios después del clic
            angularInteractions.forceAngularChangeDetection();
            
            // Esperar a que Angular procese
            angularInteractions.waitForAngularReady();
            
            System.out.println("✅ Clic con detección forzada realizado");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Falló el clic con detección forzada: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔄 Realiza clic en un elemento usando múltiples estrategias
     * @param elemento WebElement sobre el que se hará clic
     * @return true si el clic fue exitoso, false en caso contrario
     */
    public boolean realizarClicConMultiplesEstrategias(WebElement elemento) {
        try {
            System.out.println("🔄 Intentando clic con múltiples estrategias...");
            
            // Estrategia 1: Clic normal
            if (intentarClicNormal(elemento)) {
                return true;
            }
            
            // Estrategia 2: Clic con hover y Actions
            if (intentarClicConHoverYClick(elemento)) {
                return true;
            }
            
            // Estrategia 3: Clic con JavaScript
            if (intentarClickConJavaScript(elemento)) {
                return true;
            }
            
            // Estrategia 4: Clic con Angular
            if (intentarClicConHoverYClickAngular(elemento)) {
                return true;
            }
            
            // Si todas las estrategias fallan
            System.out.println("❌ Todas las estrategias de clic fallaron");
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Error al realizar clic con múltiples estrategias: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🖱️ Realiza clic en un elemento usando JavaScriptExecutor (prioridad)
     * @param elemento WebElement sobre el que se hará clic
     * @return true si el clic fue exitoso, false en caso contrario
     */
    public boolean realizarClicConJavaScript(WebElement elemento) {
        try {
            System.out.println("🔧 Ejecutando clic con JavaScriptExecutor...");
            
            // Método 1: Usando click() de JavaScript
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
            
            System.out.println("✅ Clic con JavaScript ejecutado correctamente");
            
            // Pequeña espera para asegurar que se procese el clic
            Thread.sleep(500);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error al realizar clic con JavaScript: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔄 Realiza clic en un elemento usando un enfoque híbrido (JavaScript + estrategias tradicionales)
     * @param elemento WebElement sobre el que se hará clic
     * @return true si el clic fue exitoso, false en caso contrario
     */
    public boolean realizarClicHibrido(WebElement elemento) {
        try {
            System.out.println("🔄 Intentando clic con enfoque híbrido...");
            
            // Estrategia 1: JavaScriptExecutor (prioridad)
            if (realizarClicConJavaScript(elemento)) {
                return true;
            }
            
            // Estrategia 2: Clic normal
            if (intentarClicNormal(elemento)) {
                return true;
            }
            
            // Estrategia 3: Clic con hover y Actions
            if (intentarClicConHoverYClick(elemento)) {
                return true;
            }
            
            // Estrategia 4: Clic con Angular
            if (intentarClicConHoverYClickAngular(elemento)) {
                return true;
            }
            
            // Si todas las estrategias fallan
            System.out.println("❌ Todas las estrategias de clic fallaron");
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Error al realizar clic con enfoque híbrido: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔄 Realiza clic en un elemento usando un enfoque híbrido con soporte Angular
     * @param elemento WebElement sobre el que se hará clic
     * @return true si el clic fue exitoso, false en caso contrario
     */
    public boolean realizarClicHibridoConAngular(WebElement elemento) {
        try {
            System.out.println("🔄 Intentando clic con enfoque híbrido y soporte Angular...");
            
            // Estrategia 1: Clic con soporte completo para Angular
            if (intentarClicConSoporteAngular(elemento)) {
                return true;
            }
            
            // Estrategia 2: Clic dentro de NgZone
            if (intentarClicDentroNgZone(elemento)) {
                return true;
            }
            
            // Estrategia 3: Clic con detección forzada
            if (intentarClicConDeteccionForzada(elemento)) {
                return true;
            }
            
            // Estrategia 4: Clic normal
            if (intentarClicNormal(elemento)) {
                return true;
            }
            
            // Estrategia 5: Clic con hover y Actions
            if (intentarClicConHoverYClick(elemento)) {
                return true;
            }
            
            // Estrategia 6: Clic con JavaScript tradicional
            if (intentarClickConJavaScript(elemento)) {
                return true;
            }
            
            // Estrategia 7: Clic con Angular (el método que ya tenías)
            if (intentarClicConHoverYClickAngular(elemento)) {
                return true;
            }
            
            // Si todas las estrategias fallan
            System.out.println("❌ Todas las estrategias de clic fallaron");
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Error al realizar clic con enfoque híbrido Angular: " + e.getMessage());
            return false;
        }
    }
}