package Avianca.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class ElementFinder {
    private WebDriver driver;
    private WebDriverWait wait;
    
    /**
     * Constructor de la clase ElementFinder
     * @param driver WebDriver de Selenium
     */
    public ElementFinder(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }
    
    /**
     * Constructor de la clase ElementFinder con tiempo de espera personalizado
     * @param driver WebDriver de Selenium
     * @param timeoutInSeconds Tiempo de espera en segundos
     */
    public ElementFinder(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }
    
    /**
     * 🎯 MÉTODO PRINCIPAL: Encuentra un elemento usando múltiples estrategias
     * @param localizadores Array de localizadores a probar
     * @return WebElement si se encuentra, null en caso contrario
     */
    public WebElement encontrarElemento(By[] localizadores) {
        return encontrarElemento(localizadores, false);
    }
    
    /**
     * 🎯 MÉTODO SOBRECARGADO: Encuentra un elemento usando múltiples estrategias con opciones
     * @param localizadores Array de localizadores a probar
     * @param esperarHabilitado Si es true, espera a que el elemento esté habilitado
     * @return WebElement si se encuentra, null en caso contrario
     */
    public WebElement encontrarElemento(By[] localizadores, boolean esperarHabilitado) {
        for (By localizador : localizadores) {
            try {
                // Esperar a que los elementos estén presentes
                List<WebElement> elementos = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(localizador));
                
                if (!elementos.isEmpty()) {
                    WebElement elemento = elementos.get(0);
                    
                    // Verificar si el elemento es visible
                    if (elemento.isDisplayed()) {
                        // Si se solicita, verificar que esté habilitado
                        if (!esperarHabilitado || elemento.isEnabled()) {
                            System.out.println("✅ Encontrado con localizador: " + localizador);
                            return elemento;
                        } else {
                            System.out.println("⚠️ Elemento encontrado pero no habilitado: " + localizador);
                        }
                    } else {
                        System.out.println("⚠️ Elemento encontrado pero no visible: " + localizador);
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Falló localizador: " + localizador + " - " + e.getMessage());
            }
        }
        System.out.println("❌ No se encontró el elemento con ningún localizador");
        return null;
    }
    
    /**
     * 🎯 MÉTODO ALTERNATIVO: Encuentra un elemento esperando explícitamente su visibilidad
     * @param localizadores Array de localizadores a probar
     * @return WebElement si se encuentra, null en caso contrario
     */
    public WebElement encontrarElementoVisible(By[] localizadores) {
        for (By localizador : localizadores) {
            try {
                WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
                System.out.println("✅ Encontrado con localizador (visible): " + localizador);
                return elemento;
            } catch (Exception e) {
                System.out.println("⚠️ Falló localizador (visible): " + localizador + " - " + e.getMessage());
            }
        }
        System.out.println("❌ No se encontró el elemento visible con ningún localizador");
        return null;
    }
    
    /**
     * 🎯 MÉTODO ALTERNATIVO: Encuentra un elemento esperando que sea clickeable
     * @param localizadores Array de localizadores a probar
     * @return WebElement si se encuentra, null en caso contrario
     */
    public WebElement encontrarElementoClickeable(By[] localizadores) {
        for (By localizador : localizadores) {
            try {
                WebElement elemento = wait.until(ExpectedConditions.elementToBeClickable(localizador));
                System.out.println("✅ Encontrado con localizador (clickeable): " + localizador);
                return elemento;
            } catch (Exception e) {
                System.out.println("⚠️ Falló localizador (clickeable): " + localizador + " - " + e.getMessage());
            }
        }
        System.out.println("❌ No se encontró el elemento clickeable con ningún localizador");
        return null;
    }
    
    /**
     * 🎯 MÉTODO ÚTIL: Encuentra el primer elemento visible de una lista
     * @param localizadores Array de localizadores a probar
     * @return Primer WebElement visible encontrado, null si ninguno es visible
     */
    public WebElement encontrarPrimerElementoVisible(By[] localizadores) {
        for (By localizador : localizadores) {
            try {
                List<WebElement> elementos = driver.findElements(localizador);
                for (WebElement elemento : elementos) {
                    if (elemento.isDisplayed()) {
                        System.out.println("✅ Encontrado elemento visible con localizador: " + localizador);
                        return elemento;
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Falló localizador: " + localizador + " - " + e.getMessage());
            }
        }
        System.out.println("❌ No se encontró ningún elemento visible");
        return null;
    }
    
    /**
     * 🎯 MÉTODO ÚTIL: Verifica si existe al menos un elemento con los localizadores
     * @param localizadores Array de localizadores a probar
     * @return true si encuentra al menos un elemento, false en caso contrario
     */
    public boolean existeElemento(By[] localizadores) {
        return encontrarElemento(localizadores) != null;
    }
    
    /**
     * 🎯 MÉTODO ÚTIL: Cuenta cuántos elementos existen con los localizadores
     * @param localizadores Array de localizadores a probar
     * @return Número total de elementos encontrados
     */
    public int contarElementos(By[] localizadores) {
        int total = 0;
        for (By localizador : localizadores) {
            try {
                List<WebElement> elementos = driver.findElements(localizador);
                total += elementos.size();
                System.out.println("🔍 Encontrados " + elementos.size() + " elementos con localizador: " + localizador);
            } catch (Exception e) {
                System.out.println("⚠️ Falló localizador: " + localizador + " - " + e.getMessage());
            }
        }
        System.out.println("📊 Total de elementos encontrados: " + total);
        return total;
    }
}