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

    // Localizador principal para login
    @FindBy(how = How.XPATH, using = "//button[@type='submit']")
    private WebElement btnLogin;

    



    
    // ===== BOTONES DE ACCIÓN =====

        // Botón "Agregar Bloqueo"
        @FindBy(how = How.XPATH, using = "//button[.//span[normalize-space()='Agregar Bloqueo']]")
        private WebElement btnAgregarBloqueo;

        // Botón "Eliminación masiva de bloqueos"
        @FindBy(how = How.XPATH, using = "//button[.//span[normalize-space()='Eliminacion masiva de bloqueos']]")
        private WebElement btnEliminacionMasiva;

        // Botón "Enviar"
        @FindBy(how = How.XPATH, using = "//button[.//span[normalize-space()='Enviar']]")
        private WebElement btnEnviar;

        // Botón "Nueva Solicitud"
        @FindBy(how = How.XPATH, using = "//button[.//span[normalize-space()='Nueva Solicitud']]")
        private WebElement NuevaSolicitud;





    public ButtonPages(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Aumentamos a 20 segundos
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
            throw new RuntimeException("Fallo en login", e);
        }
    }


    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra "Solicitudes de Bloqueo" con múltiples estrategias
     */
    private WebElement encontrarSolicitudDeBloqueo() {
        By[] localizadores = {
            By.id("horizontal-menu-item-103"),
            By.xpath("//span[@class='horizontal-menu-title' and text()='Solicitudes de Bloqueo']"),
            By.xpath("//span[text()='Solicitudes de Bloqueo']"),
            By.xpath("//*[contains(text(), 'Solicitudes de Bloqueo')]")
        };
        return encontrarElemento(localizadores);
    }

       /**
     * 🔧 MÉTODO AUXILIAR: Scroll a un elemento
     */
    private void scrollToElement(WebElement elemento) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", elemento);
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo hacer scroll al elemento: " + e.getMessage());
            throw new RuntimeException("Error al hacer scroll al elemento", e);
        }
    }


    /**
     * 🎯 MÉTODO MEJORADO: Clic en "Solicitudes de Bloqueo" para desplegar submenú
     * Ahora aseguramos que el menú se mantenga abierto el tiempo suficiente
     */
    public void btnSolicitudDeBloqueo() {
        try {
            System.out.println("🔍 Buscando elemento 'Solicitudes de Bloqueo'...");
            
            WebElement elemento = encontrarSolicitudDeBloqueo();
            
            if (elemento != null) {
                // Hacer scroll al elemento si es necesario
                scrollToElement(elemento);
                
                // Esperar que sea visible y clickeable
                wait.until(ExpectedConditions.elementToBeClickable(elemento));
                
                // HACER CLIC PARA DESPLEGAR EL MENÚ
                elemento.click();
                System.out.println("✅ Clic realizado en 'Solicitudes de Bloqueo'");
                
                // Esperar a que se despliegue el submenú y se estabilice
                boolean subMenuDesplegado = esperarSubMenuDesplegado();
                
                if (subMenuDesplegado) {
                    System.out.println("✅ Submenú desplegado correctamente");
                    
                    // Esperar adicional para asegurar que el menú no se cierre
                    System.out.println("⏳ Esperando a que el menú se estabilice...");
                    try {
                        Thread.sleep(5000); // Espera de 5 segundos para estabilización
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    // Mover el mouse al submenú para mantenerlo abierto
                    mantenerSubMenuAbierto();
                    
                } else {
                    System.out.println("⚠️ El submenú no se desplegó después del clic");
                    // Intentar alternativa: JavaScript
                    intentarClickConJavaScript(elemento);
                    esperarSubMenuDesplegado();
                }
                
            } else {
                throw new RuntimeException("❌ No se encontró el elemento 'Solicitudes de Bloqueo'");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error en clic sobre 'Solicitudes de Bloqueo': " + e.getMessage());
            throw new RuntimeException("Fallo al interactuar con 'Solicitudes de Bloqueo'", e);
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra "Nueva Solicitud" con múltiples estrategias
     */
    private WebElement encontrarNuevaSolicitud() {
        By[] localizadores = {
            By.id("horizontal-menu-item-102"),
            By.xpath("//span[@class='horizontal-menu-title' and text()='Nueva Solicitud']"),
            By.xpath("//span[text()='Nueva Solicitud']"),
            By.xpath("//*[contains(text(), 'Nueva Solicitud')]"),
            By.xpath("//a[@href='/OpeBlock/Index']")
        };
        return encontrarElemento(localizadores);
    }

    /**
     * 🎯 MÉTODO MEJORADO: Clic en "Nueva Solicitud" del submenú
     * Ahora aseguramos que el clic se realice correctamente
     */
    public void btnNuevaSolicitud() {
        try {
            System.out.println("🔍 Buscando elemento 'Nueva Solicitud'...");
            
            
            // Esperar a que el elemento esté disponible
            WebElement elemento = encontrarNuevaSolicitud();
        /*   
            WebElement elemento = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[@class='horizontal-menu-title' and text()='Nueva Solicitud']")
            ));
         */
            
            if (elemento != null && elemento.isDisplayed()) {
                System.out.println("✅ Elemento 'Nueva Solicitud' encontrado y visible");
                
                // Intentar múltiples estrategias para hacer clic
                if (!intentarClicNormal(elemento)) {
                    System.out.println("⚠️ Clic normal falló, intentando con Actions...");
                    if (!intentarClicConActions(elemento)) {
                        System.out.println("⚠️ Clic con Actions falló, intentando con JavaScript...");
                        if (!intentarClickConJavaScript(elemento)) {
                            throw new RuntimeException("❌ No se pudo hacer clic en 'Nueva Solicitud' con ningún método");
                        }
                    }
                }
                
                System.out.println("✅ Clic realizado en 'Nueva Solicitud'");
                
                // Verificar navegación
                wait.until(ExpectedConditions.urlContains("/OpeBlock/Index"));
                System.out.println("✅ Navegación exitosa a /OpeBlock/Index");
                
            } else {
                throw new RuntimeException("❌ No se encontró el elemento 'Nueva Solicitud' o no está visible");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error en clic sobre 'Nueva Solicitud': " + e.getMessage());
            throw new RuntimeException("Fallo al interactuar con 'Nueva Solicitud'", e);
        }
    }



    /**
     * 🔧 MÉTODO AUXILIAR GENÉRICO: Encuentra un elemento con múltiples estrategias
     */
    private WebElement encontrarElemento(By[] localizadores) {
        for (By localizador : localizadores) {
            try {
                List<WebElement> elementos = driver.findElements(localizador);
                if (!elementos.isEmpty() && elementos.get(0).isDisplayed()) {
                    System.out.println("✅ Encontrado con localizador: " + localizador);
                    return elementos.get(0);
                }
            } catch (Exception e) {
                System.out.println("⚠️ Falló localizador: " + localizador);
                throw new RuntimeException("Error al buscar elemento con localizador: " + localizador, e);
            }
        }
        return null;
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Espera a que el submenú se despliegue
     * @return true si el submenú se desplegó correctamente
     */
    private boolean esperarSubMenuDesplegado() {
        try {
            System.out.println("⏳ Esperando a que el submenú se despliegue...");
            // Esperar explícitamente a que el submenú sea visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("horizontal-sub-menu-103")));
            // Verificar que "Nueva Solicitud" esté visible dentro del submenú
            WebElement nuevaSolicitud = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(
                By.id("horizontal-sub-menu-103"),
                By.xpath(".//span[text()='Nueva Solicitud']")
            )).get(0);
            
            return nuevaSolicitud.isDisplayed();
            
        } catch (Exception e) {
            System.out.println("⚠️ Error esperando submenú: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Mantiene el submenú abierto moviendo el mouse
     */
    private void mantenerSubMenuAbierto() {
        try {
            // Encontrar el submenú
            WebElement subMenu = driver.findElement(By.id("horizontal-sub-menu-103"));
            
            // Crear objeto Actions
            Actions actions = new Actions(driver);
            
            // Mover el mouse al centro del submenú para mantenerlo abierto
            actions.moveToElement(subMenu, subMenu.getSize().width / 2, subMenu.getSize().height / 2).perform();
            System.out.println("✅ Mouse movido al submenú para mantenerlo abierto");
            
            // Pequeña pausa para asegurar que el menú se mantenga abierto
            Thread.sleep(500);
            
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo mantener el submenú abierto: " + e.getMessage());
            throw new RuntimeException("Error al mantener el submenú abierto", e);
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Intenta hacer clic normal
     * @return true si el clic fue exitoso
     */
    private boolean intentarClicNormal(WebElement elemento) {
        try {
            // Esperar a que sea clickeable
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            elemento.click();
            return true;
        } catch (Exception e) {
            System.out.println("⚠️ Clic normal falló: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Intenta hacer clic con Actions
     * @return true si el clic fue exitoso
     */
    private boolean intentarClicConActions(WebElement elemento) {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(elemento).click().perform();
            return true;
        } catch (Exception e) {
            System.out.println("⚠️ Clic con Actions falló: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Clic con JavaScript como alternativa
     * @param elemento Elemento sobre el que se hará clic
     * @return true si el clic fue exitoso, false en caso contrario
     */
    private boolean intentarClickConJavaScript(WebElement elemento) {
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
     * 🔧 MÉTODO AUXILIAR: Encuentra "Agregar Bloqueo" con múltiples estrategias
     */
    private WebElement encontrarAgregarBloqueo() {
        By[] localizadores = {
            By.xpath("//button[.//span[normalize-space()='Agregar Bloqueo']]"),
            By.xpath("//button[@type='submit' and .//span[contains(text(), 'Agregar Bloqueo')]]"),
            By.xpath("//button[contains(@class, 'mat-accent') and .//span[contains(text(), 'Agregar Bloqueo')]]"),
            By.xpath("//button[.//mat-icon[contains(@class, 'add')]]")
        };
        return encontrarElemento(localizadores);
    }

    /**
     * 🎯 MÉTODO MEJORADO: Clic en "Agregar Bloqueo"
     */
    public void clickAgregarBloqueo() {
        try {
            System.out.println("🔍 Buscando elemento 'Agregar Bloqueo'...");
            
            WebElement elemento = encontrarAgregarBloqueo();
            
            if (elemento != null) {
                // Hacer scroll al elemento si es necesario
                scrollToElement(elemento);
                
                // Esperar que sea visible y clickeable
                wait.until(ExpectedConditions.elementToBeClickable(elemento));
                
                // Intentar múltiples estrategias para hacer clic
                if (!intentarClicNormal(elemento)) {
                    System.out.println("⚠️ Clic normal falló, intentando con Actions...");
                    if (!intentarClicConActions(elemento)) {
                        System.out.println("⚠️ Clic con Actions falló, intentando con JavaScript...");
                        if (!intentarClickConJavaScript(elemento)) {
                            throw new RuntimeException("❌ No se pudo hacer clic en 'Agregar Bloqueo' con ningún método");
                        }
                    }
                }
                
                System.out.println("✅ Clic realizado en 'Agregar Bloqueo'");
            } else {
                throw new RuntimeException("❌ No se encontró el elemento 'Agregar Bloqueo'");
            }
        } catch (Exception e) {
            System.err.println("❌ Error en clic sobre 'Agregar Bloqueo': " + e.getMessage());
            throw new RuntimeException("Fallo al interactuar con 'Agregar Bloqueo'", e);
        }
    }




    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra "Eliminación masiva de bloqueos" con múltiples estrategias
     */
    private WebElement encontrarEliminacionMasiva() {
        By[] localizadores = {
            By.xpath("//button[.//span[normalize-space()='Eliminacion masiva de bloqueos']]"),
            By.xpath("//button[@type='button' and .//span[contains(text(), 'Eliminacion masiva de bloqueos')]]"),
            By.xpath("//button[contains(@class, 'mat-warn') and .//span[contains(text(), 'Eliminacion masiva de bloqueos')]]"),
            By.xpath("//button[.//mat-icon[contains(@class, 'delete')]]")
        };
        return encontrarElemento(localizadores);
    }



    /**
     * 🎯 MÉTODO MEJORADO: Clic en "Eliminación masiva de bloqueos"
     */
    public void clickEliminacionMasiva() {
        try {
            System.out.println("🔍 Buscando elemento 'Eliminación masiva de bloqueos'...");
            
            WebElement elemento = encontrarEliminacionMasiva();
            
            if (elemento != null) {
                scrollToElement(elemento);
                wait.until(ExpectedConditions.elementToBeClickable(elemento));
                
                if (!intentarClicNormal(elemento)) {
                    if (!intentarClicConActions(elemento)) {
                        if (!intentarClickConJavaScript(elemento)) {
                            throw new RuntimeException("❌ No se pudo hacer clic en 'Eliminación masiva de bloqueos'");
                        }
                    }
                }
                
                System.out.println("✅ Clic realizado en 'Eliminación masiva de bloqueos'");
            } else {
                throw new RuntimeException("❌ No se encontró el elemento 'Eliminación masiva de bloqueos'");
            }
        } catch (Exception e) {
            System.err.println("❌ Error en clic sobre 'Eliminación masiva de bloqueos': " + e.getMessage());
            throw new RuntimeException("Fallo al interactuar con 'Eliminación masiva de bloqueos'", e);
        }
    }



    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra "Enviar" con múltiples estrategias
     */
    private WebElement encontrarEnviar() {
        By[] localizadores = {
            By.xpath("//button[.//span[normalize-space()='Enviar']]"),
            By.xpath("//button[@type='button' and .//span[contains(text(), 'Enviar')]]"),
            By.xpath("//button[contains(@class, 'mat-accent') and .//span[contains(text(), 'Enviar')]]")
        };
        return encontrarElemento(localizadores);
    }

    /**
     * 🎯 MÉTODO MEJORADO: Clic en "Enviar"
     */
    public void clickEnviar() {
        try {
            System.out.println("🔍 Buscando elemento 'Enviar'...");
            
            WebElement elemento = encontrarEnviar();
            
            if (elemento != null) {
                scrollToElement(elemento);
                wait.until(ExpectedConditions.elementToBeClickable(elemento));
                
                if (!intentarClicNormal(elemento)) {
                    if (!intentarClicConActions(elemento)) {
                        if (!intentarClickConJavaScript(elemento)) {
                            throw new RuntimeException("❌ No se pudo hacer clic en 'Enviar'");
                        }
                    }
                }
                
                System.out.println("✅ Clic realizado en 'Enviar'");
            } else {
                throw new RuntimeException("❌ No se encontró el elemento 'Enviar'");
            }
        } catch (Exception e) {
            System.err.println("❌ Error en clic sobre 'Enviar': " + e.getMessage());
            throw new RuntimeException("Fallo al interactuar con 'Enviar'", e);
        }
    }


    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra "Nueva Solicitud" con múltiples estrategias
     */
    private WebElement encontrarNuevaSolicitudButton() {
        By[] localizadores = {
            By.xpath("//button[.//span[normalize-space()='Nueva Solicitud']]"),
            By.xpath("//button[@type='button' and .//span[contains(text(), 'Nueva Solicitud')]]"),
            By.xpath("//button[contains(@class, 'mat-primary') and .//span[contains(text(), 'Nueva Solicitud')]]")
        };
        return encontrarElemento(localizadores);
    }

    /**
     * 🎯 MÉTODO MEJORADO: Clic en "Nueva Solicitud"
     */
    public void clickNuevaSolicitud() {
        try {
            System.out.println("🔍 Buscando elemento 'Nueva Solicitud'...");
            
            WebElement elemento = encontrarNuevaSolicitudButton();
            
            if (elemento != null) {
                scrollToElement(elemento);
                
                // Verificar si el botón está habilitado antes de intentar hacer clic
                if (!elemento.isEnabled()) {
                    System.out.println("⚠️ El botón 'Nueva Solicitud' está deshabilitado, esperando a que se habilite...");
                    wait.until(ExpectedConditions.elementToBeClickable(elemento));
                }
                
                if (!intentarClicNormal(elemento)) {
                    if (!intentarClicConActions(elemento)) {
                        if (!intentarClickConJavaScript(elemento)) {
                            throw new RuntimeException("❌ No se pudo hacer clic en 'Nueva Solicitud'");
                        }
                    }
                }
                
                System.out.println("✅ Clic realizado en 'Nueva Solicitud'");
            } else {
                throw new RuntimeException("❌ No se encontró el elemento 'Nueva Solicitud'");
            }
        } catch (Exception e) {
            System.err.println("❌ Error en clic sobre 'Nueva Solicitud': " + e.getMessage());
            throw new RuntimeException("Fallo al interactuar con 'Nueva Solicitud'", e);
        }
    }








}
