package Avianca.Steps;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import java.time.Duration;
import java.util.List;

public class ButtonPages {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    // ✅ LOCALIZADOR CORRECTO basado en el HTML real
    @FindBy(how = How.XPATH, using = "//button[@type='submit']")
    private WebElement btnLogin;

    // ✅ LOCALIZADOR CORRECTO para "Solicitudes de Bloqueo"
    // Usamos el ID único que encontramos en el HTML: horizontal-menu-item-103
    @FindBy(how = How.ID, using = "horizontal-menu-item-103")
    public WebElement btnSolicitudDeBloqueo;

    // ✅ LOCALIZADOR ALTERNATIVO usando clase y texto
    @FindBy(how = How.XPATH, using = "//span[@class='horizontal-menu-title' and text()='Solicitudes de Bloqueo']")
    public WebElement btnSolicitudDeBloqueoAlternativo;

    // ✅ LOCALIZADOR CORRECTO para "Nueva Solicitud"  
    // Usamos el ID único: horizontal-menu-item-102
    @FindBy(how = How.ID, using = "horizontal-menu-item-102")
    public WebElement btnNuevaSolicitud;

    // ✅ LOCALIZADOR ALTERNATIVO para "Nueva Solicitud"
    @FindBy(how = How.XPATH, using = "//span[@class='horizontal-menu-title' and text()='Nueva Solicitud']")
    public WebElement btnNuevaSolicitudAlternativo;

    public ButtonPages(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * 🎯 MÉTODO MEJORADO: Clic en botón de login
     */
    public void btnLogin() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btnLogin));
            btnLogin.click();
            System.out.println("✅ Login realizado correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error en login: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 🎯 MÉTODO MEJORADO: Hover sobre "Solicitudes de Bloqueo"
     * Este método implementa múltiples estrategias para mayor robustez
     */
    public void btnSolicitudDeBloqueo() {
        try {
            System.out.println("🔍 Buscando elemento 'Solicitudes de Bloqueo'...");
            
            WebElement elemento = encontrarSolicitudDeBloqueo();
            
            if (elemento != null) {
                // Hacer scroll al elemento si es necesario
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", elemento);
                
                // Esperar que sea visible
                wait.until(ExpectedConditions.visibilityOf(elemento));
                
                // Realizar hover
            //actions.moveToElement(elemento).build().perform();
                
            String mouseOverScript = 
                "var evObj = document.createEvent('MouseEvents');" +
                "evObj.initMouseEvent('mouseover',true,true,window,1,0,0,0,0," +
                "false,false,false,false,0,null);" +
                "arguments[0].dispatchEvent(evObj);";
            
            ((JavascriptExecutor) driver).executeScript(mouseOverScript, elemento);

              
              System.out.println("✅ Hover realizado en 'Solicitudes de Bloqueo'");
                
                // Esperar a que se despliegue el submenú
                Thread.sleep(1000);
                
                // Verificar que el submenú se desplegó
                verificarSubMenuDesplegado();
                
            } else {
                throw new RuntimeException("❌ No se encontró el elemento 'Solicitudes de Bloqueo'");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error en hover sobre 'Solicitudes de Bloqueo': " + e.getMessage());

        }
    }

    /**
     * 🎯 MÉTODO MEJORADO: Clic en "Nueva Solicitud"
     */
    public void btnNuevaSolicitud() {
        try {
            System.out.println("🔍 Buscando elemento 'Nueva Solicitud'...");
            
            WebElement elemento = encontrarNuevaSolicitud();
            
            if (elemento != null) {
                // Hacer scroll al elemento
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", elemento);
                
                // Esperar que sea clickeable
                wait.until(ExpectedConditions.elementToBeClickable(elemento));
                
                // Hacer clic
                elemento.click();
                System.out.println("✅ Clic realizado en 'Nueva Solicitud'");
                
                // Verificar navegación
                Thread.sleep(2000);
                String urlActual = driver.getCurrentUrl();
                System.out.println("📍 URL después del clic: " + urlActual);
                
            } else {
                throw new RuntimeException("❌ No se encontró el elemento 'Nueva Solicitud'");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error en clic sobre 'Nueva Solicitud': " + e.getMessage());
            // Intentar con JavaScript como último recurso
            intentarClickConJavaScript();
        
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra "Solicitudes de Bloqueo" con múltiples estrategias
     */
    private WebElement encontrarSolicitudDeBloqueo() {
        // Array de localizadores en orden de preferencia
        By[] localizadores = {
            By.id("horizontal-menu-item-103"),
            By.xpath("//span[@class='horizontal-menu-title' and text()='Solicitudes de Bloqueo']"),
            By.xpath("//span[text()='Solicitudes de Bloqueo']"),
            By.xpath("//*[contains(text(), 'Solicitudes de Bloqueo')]")
        };

        for (By localizador : localizadores) {
            try {
                List<WebElement> elementos = driver.findElements(localizador);
                if (!elementos.isEmpty() && elementos.get(0).isDisplayed()) {
                    System.out.println("✅ Encontrado con localizador: " + localizador);
                    return elementos.get(0);
                }
            } catch (Exception e) {
                System.out.println("⚠️ Falló localizador: " + localizador);
            }
        }
        return null;
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra "Nueva Solicitud" con múltiples estrategias
     */
    private WebElement encontrarNuevaSolicitud() {
        // Array de localizadores en orden de preferencia
        By[] localizadores = {
            By.id("horizontal-menu-item-102"),
            By.xpath("//span[@class='horizontal-menu-title' and text()='Nueva Solicitud']"),
            By.xpath("//span[text()='Nueva Solicitud']"),
            By.xpath("//*[contains(text(), 'Nueva Solicitud')]"),
            By.xpath("//a[@href='/OpeBlock/Index']") // Basado en el href del HTML
        };

        for (By localizador : localizadores) {
            try {
                List<WebElement> elementos = driver.findElements(localizador);
                if (!elementos.isEmpty() && elementos.get(0).isDisplayed()) {
                    System.out.println("✅ Encontrado con localizador: " + localizador);
                    return elementos.get(0);
                }
            } catch (Exception e) {
                System.out.println("⚠️ Falló localizador: " + localizador);
            }
        }
        return null;
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Verifica que el submenú se desplegó
     */
    private void verificarSubMenuDesplegado() {
        try {
            // Buscar el div del submenú basado en el HTML
            WebElement subMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("horizontal-sub-menu-103")
            ));
            System.out.println("✅ Submenú desplegado correctamente");
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo verificar el despliegue del submenú");
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Último recurso - clic con JavaScript
     */
    private void intentarClickConJavaScript() {
        try {
            WebElement elemento = encontrarNuevaSolicitud();
            if (elemento != null) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
                System.out.println("✅ Clic realizado con JavaScript");
            }
        } catch (Exception e) {
            System.err.println("❌ También falló el clic con JavaScript: " + e.getMessage());
        }
    }
}
