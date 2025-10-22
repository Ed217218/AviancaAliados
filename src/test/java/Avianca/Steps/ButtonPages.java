package Avianca.Steps;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;

import Avianca.Utils.ApiErrorCapture;
import Avianca.Utils.ElementInteractions;
import Avianca.Utils.CalendarUtil;
import Avianca.Utils.ElementFinder;
import Avianca.Utils.BrowserMobProxyHelper;

public class ButtonPages {

    private WebDriver driver;
    private WebDriverWait wait;
    private ElementInteractions elementInteractions;
    private ApiErrorCapture apiErrorCapture;
    private CalendarUtil calendarUtil;
    private ElementFinder elementFinder;
    private BrowserMobProxyHelper proxyHelper; // Helper para captura HTTP

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

    @FindBy(how = How.XPATH, using = "//span[normalize-space(text())='Panel']")
    private WebElement txtPanel;

    @FindBy(how = How.XPATH, using = "//span[normalize-space(text())='Modulo de nueva solicitud']")
    private WebElement txtModuloNuevaSolicitud;

    // Campo de fecha (ejemplo)
    @FindBy(how = How.XPATH, using = "//input[@formcontrolname='fecha']")
    private WebElement dateField;

    // Selector para el contenedor del calendario
    private final By calendarContainer = By.className("mat-datepicker-content-container");

    public ButtonPages(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.elementInteractions = new ElementInteractions(driver);
        this.apiErrorCapture = new ApiErrorCapture(driver);
        this.calendarUtil = new CalendarUtil(driver);
        this.elementFinder = new ElementFinder(driver, 20); // 20 segundos de espera
        PageFactory.initElements(driver, this);
    }
    
    /**
     * 🔧 Constructor alternativo con BrowserMobProxyHelper
     */
    public ButtonPages(WebDriver driver, BrowserMobProxyHelper proxyHelper) {
        this(driver);
        this.proxyHelper = proxyHelper;
        System.out.println("✅ ButtonPages iniciado con soporte de BrowserMob Proxy");
    }
    
    /**
     * 🔧 Setter para inyectar el proxyHelper después de la inicialización
     */
    public void setProxyHelper(BrowserMobProxyHelper proxyHelper) {
        this.proxyHelper = proxyHelper;
        System.out.println("✅ ProxyHelper configurado en ButtonPages");
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
        return elementFinder.encontrarElemento(localizadores);
    }

    /**
     * 🎯 MÉTODO MEJORADO: Clic en "Solicitudes de Bloqueo" para desplegar submenú
     */
    public void btnSolicitudDeBloqueo() {
        try {
            System.out.println("🔍 Elemento encontrado: " + txtPanel.getText());
            String menu = "PANEL";
            String txtPanelText = txtPanel.getText();

            if (txtPanelText.equals(menu)) {
                System.out.println("⏱️ Esperando 5 segundos para estabilización y recargar los artefactos...");    
                Thread.sleep(5000);         
                driver.navigate().refresh();
                System.out.println("🔄 Página refrescada");
                System.out.println("🔍 Inicia 'Solicitudes de Bloqueo'...");
                System.out.println("🔍 Buscando elemento 'Solicitudes de Bloqueo'...");
                WebElement elemento = encontrarSolicitudDeBloqueo();

                if (elemento != null) {                     
                    System.out.println("🔍 Elemento encontrado: " + elemento.getText());
                    elementInteractions.scrollToElement(elemento);
                    wait.until(ExpectedConditions.elementToBeClickable(elemento));
                    elemento.click();
                    System.out.println("✅ Clic realizado en 'Solicitudes de Bloqueo'");
                    
                    boolean subMenuDesplegado = esperarSubMenuDesplegado();
                    
                    if (subMenuDesplegado) {
                        System.out.println("✅ Submenú desplegado correctamente");
                        System.out.println("⏳ Esperando a que el menú se estabilice...");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        mantenerSubMenuAbierto();
                    } else {
                        System.out.println("⚠️ El submenú no se desplegó después del clic");
                        realizarClicConMultiplesEstrategias(elemento);
                        esperarSubMenuDesplegado();
                    }
                } else {
                    System.out.println("❌ No se encontró el elemento 'Solicitudes de Bloqueo'");
                }
            } else {
                System.out.println("❌ El menú no está en el estado esperado. Texto actual: " + txtPanelText);
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
        return elementFinder.encontrarElemento(localizadores);
    }

    /**
     * 🎯 MÉTODO MEJORADO: Clic en "Nueva Solicitud" del submenú
     */
    public void btnNuevaSolicitud() {
        try {
            System.out.println("🔍 Buscando elemento 'Nueva Solicitud'...");
            WebElement elemento = encontrarNuevaSolicitud();
            System.out.println("🔍 Elemento encontrado: " + elemento.getText());
                  
            if (elemento != null && elemento.isDisplayed()) {
                System.out.println("✅ Elemento 'Nueva Solicitud' encontrado y visible");
                realizarClicConMultiplesEstrategias(elemento);
                System.out.println("✅ Clic realizado en 'Nueva Solicitud'");
                
                wait.until(ExpectedConditions.urlContains("/OpeBlock/Index"));
                System.out.println("✅ Navegación exitosa a /OpeBlock/Index");
                
                System.out.println("🔍 Elemento menu: " + txtModuloNuevaSolicitud.getText());
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space(text())='Modulo de nueva solicitud']")));
                System.out.println("🔍 Elemento encontrado: " + txtModuloNuevaSolicitud.getText());
            } else {
                throw new RuntimeException("❌ No se encontró el elemento 'Nueva Solicitud' o no está visible");
            }
        } catch (Exception e) {
            System.err.println("❌ Error en clic sobre 'Nueva Solicitud': " + e.getMessage());
            throw new RuntimeException("Fallo al interactuar con 'Nueva Solicitud'", e);
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Espera a que el submenú se despliegue
     */
    private boolean esperarSubMenuDesplegado() {
        try {
            System.out.println("⏳ Esperando a que el submenú se despliegue...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("horizontal-sub-menu-103")));
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
            WebElement subMenu = driver.findElement(By.id("horizontal-sub-menu-103"));
            Actions actions = new Actions(driver);
            actions.moveToElement(subMenu, subMenu.getSize().width / 2, subMenu.getSize().height / 2).perform();
            System.out.println("✅ Mouse movido al submenú para mantenerlo abierto");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo mantener el submenú abierto: " + e.getMessage());
            throw new RuntimeException("Error al mantener el submenú abierto", e);
        }
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Selecciona una fecha usando el calendario
     */
    public void selectDateFromCalendar(int day, int month, int year) {
        try {
            System.out.println("🗓️ Seleccionando fecha: " + day + "/" + month + "/" + year);
            LocalDate targetDate = LocalDate.of(year, month, day);
            calendarUtil.openCalendar(dateField);
            calendarUtil.selectDate(targetDate);
            System.out.println("✅ Fecha seleccionada correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar fecha del calendario: " + e.getMessage());
            throw new RuntimeException("No se pudo seleccionar la fecha del calendario", e);
        }
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Verifica si una fecha está disponible en el calendario
     */
    public boolean isDateAvailable(int day, int month, int year) {
        try {
            System.out.println("🔍 Verificando disponibilidad de fecha: " + day + "/" + month + "/" + year);
            LocalDate targetDate = LocalDate.of(year, month, day);
            calendarUtil.openCalendar(dateField);
            boolean available = calendarUtil.isDateAvailable(targetDate);
            calendarUtil.closeCalendar();
            System.out.println("✅ Disponibilidad de fecha: " + available);
            return available;
        } catch (Exception e) {
            System.err.println("❌ Error al verificar disponibilidad de fecha: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Selecciona fecha inicial usando el calendario
     */
    public void seleccionarFechaInicial(int day, int month, int year) {
        try {
            System.out.println("🗓️ Seleccionando fecha inicial: " + day + "/" + month + "/" + year);
            System.out.println("🔍 Abriendo calendario de fecha inicial...");
            abrirCalendarioFechaInicial();
            LocalDate targetDate = LocalDate.of(year, month, day);
            System.out.println("📅 Fecha objetivo creada: " + targetDate);
            System.out.println("🖱️ Seleccionando fecha en el calendario...");
            calendarUtil.selectDate(targetDate);
            System.out.println("✅ Fecha inicial seleccionada correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar fecha inicial: " + e.getMessage());
            throw new RuntimeException("No se pudo seleccionar la fecha inicial", e);
        }
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Abre el calendario de fecha inicial
     */
    public void abrirCalendarioFechaInicial() {
        try {
            System.out.println("🔍 Buscando botón de calendario de fecha inicial...");
            WebElement botonCalendario = encontrarBotonCalendarioFechaInicial();
            
            if (botonCalendario != null) {
                System.out.println("📜 Haciendo scroll al botón de calendario...");
                elementInteractions.scrollToElement(botonCalendario);
                System.out.println("⏳ Esperando a que el botón sea clickeable...");
                wait.until(ExpectedConditions.elementToBeClickable(botonCalendario));
                System.out.println("🖱️ Intentando hacer clic en el botón...");
                if (!elementInteractions.intentarClicNormal(botonCalendario)) {
                    if (!elementInteractions.intentarClicConHoverYClick(botonCalendario)) {
                        if (!elementInteractions.intentarClickConJavaScript(botonCalendario)) {
                            throw new RuntimeException("❌ No se pudo hacer clic en el botón de calendario de fecha inicial");
                        }
                    }
                }
                System.out.println("✅ Clic realizado en el botón de calendario de fecha inicial");
                System.out.println("⏳ Esperando a que el calendario se abra...");
                Thread.sleep(1000);
                try {
                    WebElement calendarElement = driver.findElement(calendarContainer);
                    if (calendarElement.isDisplayed()) {
                        System.out.println("✅ Calendario de fecha inicial está visible");
                    } else {
                        throw new RuntimeException("❌ El calendario de fecha inicial no está visible");
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error al verificar visibilidad del calendario: " + e.getMessage());
                    throw e;
                }
            } else {
                throw new RuntimeException("❌ No se encontró el botón de calendario de fecha inicial");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al abrir calendario de fecha inicial: " + e.getMessage());
            throw new RuntimeException("Fallo al abrir calendario de fecha inicial", e);
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra el botón de calendario de fecha inicial con múltiples estrategias
     */
    private WebElement encontrarBotonCalendarioFechaInicial() {
        By[] localizadores = {
            By.cssSelector("mat-datepicker-toggle[data-mat-calendar='mat-datepicker-0'] button"),
            By.xpath("(//button[@aria-label='Open calendar'])[1]"),
            By.xpath("(//mat-datepicker-toggle/following-sibling::div//button)[1]"),
            By.xpath("//div[contains(@class, 'ng-tns-c1798928316-19')]//button[@aria-label='Open calendar']"),
            By.xpath("(//button[@aria-label='Open calendar']//svg)[1]/ancestor::button"),
            By.xpath("(//mat-datepicker-toggle)[1]//button"),
            By.xpath("(//div[contains(@class, 'mat-mdc-form-field-icon-suffix')])[1]//button"),
            By.xpath("(//button[contains(@class, 'mat-icon-button') and @aria-label='Open calendar'])[1]")
        };
        return elementFinder.encontrarElemento(localizadores);
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Selecciona fecha final usando el calendario
     */
    public void seleccionarFechaFinal(int day, int month, int year) {
        try {
            System.out.println("🗓️ Seleccionando fecha final: " + day + "/" + month + "/" + year);
            System.out.println("🔍 Abriendo calendario de fecha final...");
            abrirCalendarioFechaFinal();
            LocalDate targetDate = LocalDate.of(year, month, day);
            System.out.println("📅 Fecha objetivo creada: " + targetDate);
            System.out.println("🖱️ Seleccionando fecha en el calendario...");
            calendarUtil.selectDate(targetDate);
            System.out.println("✅ Fecha final seleccionada correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar fecha final: " + e.getMessage());
            throw new RuntimeException("No se pudo seleccionar la fecha final", e);
        }
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Abre el calendario de fecha final
     */
    public void abrirCalendarioFechaFinal() {
        try {
            System.out.println("🔍 Buscando botón de calendario de fecha final...");
            WebElement botonCalendario = encontrarBotonCalendarioFechaFinal();
            
            if (botonCalendario != null) {
                System.out.println("📜 Haciendo scroll al botón de calendario...");
                elementInteractions.scrollToElement(botonCalendario);
                System.out.println("⏳ Esperando a que el botón sea clickeable...");
                wait.until(ExpectedConditions.elementToBeClickable(botonCalendario));
                System.out.println("🖱️ Intentando hacer clic en el botón...");
                if (!elementInteractions.intentarClicNormal(botonCalendario)) {
                    if (!elementInteractions.intentarClicConHoverYClick(botonCalendario)) {
                        if (!elementInteractions.intentarClickConJavaScript(botonCalendario)) {
                            throw new RuntimeException("❌ No se pudo hacer clic en el botón de calendario de fecha final");
                        }
                    }
                }
                System.out.println("✅ Clic realizado en el botón de calendario de fecha final");
                System.out.println("⏳ Esperando a que el calendario se abra...");
                Thread.sleep(1000);
                try {
                    WebElement calendarElement = driver.findElement(calendarContainer);
                    if (calendarElement.isDisplayed()) {
                        System.out.println("✅ Calendario de fecha final está visible");
                    } else {
                        throw new RuntimeException("❌ El calendario de fecha final no está visible");
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error al verificar visibilidad del calendario: " + e.getMessage());
                    throw e;
                }
            } else {
                throw new RuntimeException("❌ No se encontró el botón de calendario de fecha final");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al abrir calendario de fecha final: " + e.getMessage());
            throw new RuntimeException("Fallo al abrir calendario de fecha final", e);
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra el botón de calendario de fecha final con múltiples estrategias
     */
    private WebElement encontrarBotonCalendarioFechaFinal() {
        By[] localizadores = {
            By.cssSelector("mat-datepicker-toggle[data-mat-calendar='mat-datepicker-1'] button"),
            By.xpath("(//button[@aria-label='Open calendar'])[2]"),
            By.xpath("(//mat-datepicker-toggle/following-sibling::div//button)[2]"),
            By.xpath("//div[contains(@class, 'ng-tns-c1798928316-20')]//button[@aria-label='Open calendar']"),
            By.xpath("(//button[@aria-label='Open calendar']//svg)[2]/ancestor::button"),
            By.xpath("(//mat-datepicker-toggle)[2]//button"),
            By.xpath("(//div[contains(@class, 'mat-mdc-form-field-icon-suffix')])[2]//button"),
            By.xpath("(//button[contains(@class, 'mat-icon-button') and @aria-label='Open calendar'])[2]")
        };
        return elementFinder.encontrarElemento(localizadores);
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Selecciona un rango de fechas
     */
    public void seleccionarRangoFechas(int startDay, int startMonth, int startYear, 
                                    int endDay, int endMonth, int endYear) {
        try {
            System.out.println("🗓️ Seleccionando rango de fechas:");
            System.out.println("   - Inicio: " + startDay + "/" + startMonth + "/" + startYear);
            System.out.println("   - Fin: " + endDay + "/" + endMonth + "/" + endYear);
            
            System.out.println("🔄 Iniciando selección de fecha inicial...");
            seleccionarFechaInicial(startDay, startMonth, startYear);
            
            System.out.println("⏳ Esperando a que el calendario inicial se cierre...");
            Thread.sleep(2000);
            
            System.out.println("🔄 Iniciando selección de fecha final...");
            seleccionarFechaFinal(endDay, endMonth, endYear);
            
            System.out.println("✅ Rango de fechas seleccionado correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar rango de fechas: " + e.getMessage());
            throw new RuntimeException("No se pudo seleccionar el rango de fechas", e);
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
        return elementFinder.encontrarElemento(localizadores);
    }

    /**
     * 🎯 MÉTODO MEJORADO: Clic en "Agregar Bloqueo"
     */
    public void clickAgregarBloqueo() {
        try {
            System.out.println("🔍 Buscando elemento 'Agregar Bloqueo'...");
            WebElement elemento = encontrarAgregarBloqueo();
            
            if (elemento != null) {
                elementInteractions.scrollToElement(elemento);
                wait.until(ExpectedConditions.elementToBeClickable(elemento));
                // Usar solo realizarClicConMultiplesEstrategias para evitar duplicados
                realizarClicConMultiplesEstrategias(elemento);              
                System.out.println("✅ Clic realizado en 'Agregar Bloqueo'");
            } else {
                throw new RuntimeException("❌ No se encontró el elemento 'Agregar Bloqueo'");
            }

            System.out.println("⏱️ Esperando 5 segundos para que se procese la acción...");
            Thread.sleep(5000);

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
        return elementFinder.encontrarElemento(localizadores);
    }

    /**
     * 🎯 MÉTODO MEJORADO: Clic en "Eliminación masiva de bloqueos"
     */
    public void clickEliminacionMasiva() {
        try {
            System.out.println("🔍 Buscando elemento 'Eliminación masiva de bloqueos'...");
            WebElement elemento = encontrarEliminacionMasiva();
            
            System.out.println("⏱️ Esperando 5 segundos para que se procese la acción...");
            Thread.sleep(5000);
            
            wait.until(ExpectedConditions.visibilityOf(elemento));

            if (elemento != null) {
                elementInteractions.scrollToElement(elemento);
                wait.until(ExpectedConditions.elementToBeClickable(elemento));
                realizarClicConMultiplesEstrategias(elemento);
                System.out.println("✅ Clic realizado en 'Eliminación masiva de bloqueos'");
            } else {
                throw new RuntimeException("❌ No se encontró el elemento 'Eliminación masiva de bloqueos'");
            }

            System.out.println("⏱️ Esperando 5 segundos para que se procese la acción...");
            Thread.sleep(5000);

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
        return elementFinder.encontrarElementoClickeable(localizadores);
    }



/**
 * 🎯 MÉTODO PRINCIPAL: Clic en "Enviar" - Versión con BrowserMob Proxy
 * Captura y valida la respuesta del servicio createListBlocks SIN usar CDP
 */
public void clickEnviar() {
    // Si hay proxy configurado, usar la versión con captura
    if (proxyHelper != null && proxyHelper.estaActivo()) {
        clickEnviarConCaptura();
    } else {
        clickEnviarSinCaptura();
    }
}

/**
 * 🎯 Versión con captura de HTTP usando BrowserMob Proxy
 */
private void clickEnviarConCaptura() {
    try {
        System.out.println("🔍 Buscando elemento 'Enviar'...");
        WebElement elemento = encontrarEnviar();
        
        if (elemento != null) {
            prepararElementoParaInteraccion(elemento);
            
            // Reiniciar captura para limpiar peticiones anteriores
            proxyHelper.reiniciarCaptura();
            System.out.println("🔄 Captura de tráfico iniciada");
            
            System.out.println("🖱️ Realizando clic en 'Enviar'...");
            boolean clicExitoso = elementInteractions.realizarClicHibrido(elemento);
            
            if (!clicExitoso) {
                throw new RuntimeException("❌ No se pudo realizar el clic en el botón 'Enviar' con ninguna estrategia");
            }
            
            System.out.println("✅ Clic realizado exitosamente en 'Enviar'");
            System.out.println("⏳ Esperando respuesta del servicio createListBlocks...");
            
            // Esperar a que la petición se complete
            Thread.sleep(5000);
            
            // Buscar la petición del servicio createListBlocks
            System.out.println("🔍 Buscando petición createListBlocks en el tráfico capturado...");
            int statusCode = proxyHelper.obtenerCodigoRespuesta("createListBlocks");
            
            if (statusCode == -1) {
                System.out.println("⚠️ No se encontró la petición createListBlocks");
                System.out.println("📊 Mostrando todas las peticiones capturadas:");
                proxyHelper.imprimirResumenPeticiones();
                throw new RuntimeException("No se capturó la petición al servicio createListBlocks");
            }
            
            // Obtener detalles de la respuesta
            String responseBody = proxyHelper.obtenerBodyRespuesta("createListBlocks");
            long tiempoRespuesta = proxyHelper.obtenerTiempoRespuesta("createListBlocks");
            
            System.out.println("\n📊 ANÁLISIS DE LA PETICIÓN:");
            System.out.println("=====================================");
            System.out.println("🔗 Servicio: createListBlocks");
            System.out.println("📊 Código de respuesta: " + statusCode);
            System.out.println("⏱️ Tiempo de respuesta: " + tiempoRespuesta + "ms");
            
            if (statusCode >= 200 && statusCode < 300) {
                System.out.println("✅ El servicio respondió exitosamente");
                
                if (responseBody != null && !responseBody.isEmpty()) {
                    System.out.println("📄 Cuerpo de la respuesta:");
                    System.out.println(responseBody);
                    
                    // Intentar extraer información relevante del JSON
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        if (jsonResponse.has("identifier")) {
                            String identifier = jsonResponse.getString("identifier");
                            System.out.println("🆔 Identifier obtenido: " + identifier);
                        }
                        if (jsonResponse.has("message")) {
                            String message = jsonResponse.getString("message");
                            System.out.println("💬 Mensaje: " + message);
                        }
                    } catch (JSONException e) {
                        System.out.println("ℹ️ La respuesta no es JSON o tiene un formato diferente");
                    }
                } else {
                    System.out.println("ℹ️ La respuesta no tiene cuerpo");
                }
            } else if (statusCode >= 400) {
                System.out.println("❌ El servicio respondió con error: " + statusCode);
                System.out.println("=====================================");
                if (responseBody != null && !responseBody.isEmpty()) {
                    System.out.println("📄 CUERPO COMPLETO DE LA RESPUESTA:");
                    System.out.println("-------------------------------------");
                    System.out.println(responseBody);
                    System.out.println("-------------------------------------");
                    
                    // Intentar parsear como JSON para extraer información estructurada
                    try {
                        JSONObject errorJson = new JSONObject(responseBody);
                        System.out.println("\n🔎 DATOS ESTRUCTURADOS DEL ERROR:");
                        
                        // Buscar campos comunes de error
                        if (errorJson.has("error")) {
                            System.out.println("   • Error: " + errorJson.get("error"));
                        }
                        if (errorJson.has("message")) {
                            System.out.println("   • Mensaje: " + errorJson.getString("message"));
                        }
                        if (errorJson.has("status")) {
                            System.out.println("   • Status: " + errorJson.get("status"));
                        }
                        if (errorJson.has("path")) {
                            System.out.println("   • Path: " + errorJson.getString("path"));
                        }
                        if (errorJson.has("timestamp")) {
                            System.out.println("   • Timestamp: " + errorJson.get("timestamp"));
                        }
                        if (errorJson.has("trace")) {
                            System.out.println("   • Trace disponible: Sí");
                        }
                        
                        // Imprimir todos los campos del JSON
                        System.out.println("\n📋 TODOS LOS CAMPOS DEL ERROR:");
                        for (String key : errorJson.keySet()) {
                            if (!key.equals("trace")) { // Omitir trace si es muy largo
                                System.out.println("   • " + key + ": " + errorJson.get(key));
                            }
                        }
                        
                    } catch (JSONException e) {
                        System.out.println("ℹ️ El cuerpo del error no es JSON válido o tiene formato diferente");
                    }
                } else {
                    System.out.println("⚠️ El servicio no devolvió cuerpo en la respuesta de error");
                }
                System.out.println("=====================================");
                throw new RuntimeException("El servicio createListBlocks respondió con error: " + statusCode);
            }
            
            System.out.println("=====================================\n");
            System.out.println("✅ Procesamiento completado exitosamente");
            
        } else {
            throw new RuntimeException("❌ No se encontró el elemento 'Enviar'");
        }
    } catch (Exception e) {
        System.err.println("❌ Error en clic sobre 'Enviar': " + e.getMessage());
        throw new RuntimeException("Fallo al interactuar con 'Enviar'", e);
    }
}

/**
 * 🎯 Versión sin captura (usando indicadores DOM)
 */
private void clickEnviarSinCaptura() {
    try {
        System.out.println("🔍 Buscando elemento 'Enviar'...");
        WebElement elemento = encontrarEnviar();
        
        if (elemento != null) {
            prepararElementoParaInteraccion(elemento);
            
            System.out.println("🖱️ Realizando clic en 'Enviar'...");
            boolean clicExitoso = elementInteractions.realizarClicHibrido(elemento);
            
            if (!clicExitoso) {
                throw new RuntimeException("❌ No se pudo realizar el clic en el botón 'Enviar' con ninguna estrategia");
            }
            
            System.out.println("✅ Clic realizado exitosamente en 'Enviar'");
            System.out.println("⏳ Esperando a que el servicio createListBlocks procese la información...");
            
            // Esperar a que la solicitud se procese (tiempo estimado)
            esperarProcesamientoServicio();
            
            System.out.println("✅ Procesamiento completado");
            
        } else {
            throw new RuntimeException("❌ No se encontró el elemento 'Enviar'");
        }
    } catch (Exception e) {
        System.err.println("❌ Error en clic sobre 'Enviar': " + e.getMessage());
        throw new RuntimeException("Fallo al interactuar con 'Enviar'", e);
    }
}

/**
 * 🔧 MÉTODO AUXILIAR: Espera a que el servicio procese la información
 * Detecta indicadores visuales de que el servicio respondió
 */
private void esperarProcesamientoServicio() {
    try {
        // Estrategia 1: Buscar spinner/loader que aparece durante el procesamiento
        System.out.println("🔄 Verificando si hay indicador de carga...");
        List<By> loadingIndicators = Arrays.asList(
            By.cssSelector(".mat-progress-spinner"),
            By.cssSelector(".spinner"),
            By.cssSelector("[role='progressbar']"),
            By.xpath("//*[contains(@class, 'loading')]"),
            By.xpath("//*[contains(@class, 'spinner')]")
        );
        
        boolean loaderDetectado = false;
        for (By locator : loadingIndicators) {
            try {
                WebElement loader = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                if (loader.isDisplayed()) {
                    System.out.println("⏳ Indicador de carga detectado, esperando a que desaparezca...");
                    wait.until(ExpectedConditions.invisibilityOf(loader));
                    loaderDetectado = true;
                    break;
                }
            } catch (Exception ignored) {
                // Continuar con el siguiente localizador
            }
        }
        
        if (!loaderDetectado) {
            System.out.println("ℹ️ No se detectó indicador de carga, usando espera fija...");
            // Espera fija de 5 segundos si no hay loader
            Thread.sleep(5000);
        }
        
        // Estrategia 2: Verificar si apareció mensaje de éxito
        System.out.println("🔍 Verificando mensaje de confirmación...");
        List<By> successIndicators = Arrays.asList(
            By.xpath("//*[contains(text(), 'exitosa') or contains(text(), 'éxito') or contains(text(), 'correctamente')]"),
            By.cssSelector(".success-message"),
            By.cssSelector(".mat-snack-bar-container"),
            By.xpath("//simple-snack-bar"),
            By.xpath("//*[contains(@class, 'success')]")
        );
        
        boolean mensajeExitoDetectado = false;
        for (By locator : successIndicators) {
            try {
                WebElement mensaje = driver.findElement(locator);
                if (mensaje.isDisplayed()) {
                    System.out.println("✅ Mensaje de éxito detectado: " + mensaje.getText());
                    mensajeExitoDetectado = true;
                    break;
                }
            } catch (Exception ignored) {
                // Continuar con el siguiente localizador
            }
        }
        
        if (!mensajeExitoDetectado) {
            System.out.println("ℹ️ No se detectó mensaje de éxito visual, asumiendo procesamiento completado");
        }
        
        // Estrategia 3: Verificar cambios en el DOM (botón deshabilitado/habilitado)
        System.out.println("🔍 Verificando estado del botón 'Enviar'...");
        WebElement botonEnviar = encontrarEnviar();
        if (botonEnviar != null) {
            // Si el botón se deshabilitó y luego se habilitó, indica que el proceso terminó
            try {
                wait.until(ExpectedConditions.attributeToBe(botonEnviar, "disabled", "true"));
                System.out.println("⏳ Botón deshabilitado durante procesamiento...");
                wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(botonEnviar, "disabled", "true")));
                System.out.println("✅ Botón habilitado nuevamente - procesamiento completado");
            } catch (Exception e) {
                System.out.println("ℹ️ El botón no cambió de estado durante el procesamiento");
            }
        }
        
        System.out.println("✅ Espera de procesamiento completada");
        
    } catch (Exception e) {
        System.out.println("⚠️ Error al esperar procesamiento: " + e.getMessage());
        // Espera de fallback
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}






    /**
     * 🔧 MÉTODO AUXILIAR: Prepara el elemento para la interacción
     */
    private void prepararElementoParaInteraccion(WebElement elemento) throws InterruptedException {
        Thread.sleep(5000);
        wait.until(ExpectedConditions.visibilityOf(elemento));
        elementInteractions.scrollToElement(elemento);
        Thread.sleep(5000);
        
        if (!elemento.isEnabled()) {
            System.out.println("⚠️ El botón 'Enviar' está deshabilitado, esperando a que se habilite...");
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
        }
        
        System.out.println("✅ El botón 'Enviar' está habilitado y clickeable");
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Realiza el clic usando múltiples estrategias
     */
    private void realizarClicConMultiplesEstrategias(WebElement elemento) {
        if (!elementInteractions.intentarClicNormal(elemento)) {
            System.out.println("❌ No se pudo hacer clic con clic normal");
            
            if (!elementInteractions.intentarClicConHoverYClick(elemento)) {
                System.out.println("❌ No se pudo hacer clic con hover tradicional");
                
                if (!elementInteractions.intentarClicConHoverYClickAngular(elemento)) {
                    System.out.println("❌ No se pudo hacer clic con hover para Angular");
                    
                    if (!elementInteractions.intentarClickConJavaScript(elemento)) {
                        System.out.println("❌ No se pudo hacer clic con JavaScript");
                        throw new RuntimeException("❌ No se pudo hacer clic");
                    }
                }
            }
        }
    }





    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra "Nueva Solicitud" con múltiples estrategias
     
    private WebElement encontrarNuevaSolicitudButton() {
        By[] localizadores = {
            By.xpath("//button[.//span[normalize-space()='Nueva Solicitud']]"),
            By.xpath("//button[@type='button' and .//span[contains(text(), 'Nueva Solicitud')]]"),
            By.xpath("//button[contains(@class, 'mat-primary') and .//span[contains(text(), 'Nueva Solicitud')]]")
        };
        return encontrarElemento(localizadores);
    }
*/

    /**
     * 🎯 MÉTODO MEJORADO: Clic en "Nueva Solicitud"
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

   */




}