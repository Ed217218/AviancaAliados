package Avianca.Pages;

//import static org.mockito.Mockito.timeout;
//import static org.mockito.Mockito.when;

import java.time.Duration;
//import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Avianca.Steps.ButtonPages;
//import Avianca.Pages.SolicitudBloqueoPage;
//import Avianca.Utils.BrowserMobProxyHelper;
import Avianca.Utils.ElementFinder;
import Avianca.Utils.ElementInteractions;
import Avianca.Utils.FormatoFecha;
import Avianca.Utils.JavascriptResaltaHtml;
//import lombok.var;

public class AdminBloqueoPage {


    private ButtonPages buttonPages;
    private WebDriver driver;
    private WebDriverWait wait;
   // private SolicitudBloqueoPage solicitudBloqueoPage;
    private JavascriptResaltaHtml resaltador;
    private ElementFinder elementFinder;
    private ElementInteractions elementInteractions;


 private static String numeroSolicitudCreada = null;


    public AdminBloqueoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.buttonPages = new ButtonPages(driver);
  //      this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
        this.resaltador = new JavascriptResaltaHtml(driver);  
        this.elementFinder = new ElementFinder(driver, 5);  
        this.elementInteractions = new ElementInteractions(driver);
        PageFactory.initElements(driver, this);
    }

    public void bandejaDeSolicitudesDeBloqueo() {
        this.buttonPages.btnAdministracionDeBloqueos();
    }

    public void hacerClicBandejaDeSolicitudes() {
        this.buttonPages.btnBandejaDeSolicitudes();
    }

    public void hacerClicCrearBloqueos() {
        this.buttonPages.btnCrearBloqueos();
    }

    public void hacerClicAgregarBloqueo() {
        buttonPages.clickAgregarBloqueo();
    }


//=== Creacion de bloqueos
    @FindBy(how = How.ID, using = "mat-input-0")
    private WebElement textnegocio;

    @FindBy(how = How.ID, using = "mat-input-1")
    private WebElement textSolicitante;

    @FindBy(how = How.ID, using = "mat-input-2")
    private WebElement textOperador;


/**
 * 🔧 MÉTODO AUXILIAR: Encuentra campo "Nombre del Tour Operador" con múltiples estrategias
 */
private WebElement encontrarNombreTourOperador() {
    By[] localizadores = {
        By.xpath("//mat-label[contains(text(), 'Nombre del Tour Operador')]"),
        By.xpath("//label[@id='mat-mdc-form-field-label-4']"),
        By.xpath("//div[contains(@class, 'mdc-notched-outline')]//mat-label[contains(text(), 'Nombre del Tour Operador')]"),
        By.xpath("//label[@for='mat-input-2']"),
        By.xpath("//label[contains(@class, 'mat-mdc-floating-label') and contains(., 'Nombre del Tour Operador')]"),
        By.xpath("//span[contains(@class, 'mat-mdc-form-field-required-marker')]/ancestor::label[contains(., 'Nombre del Tour Operador')]"),
        By.xpath("//mat-form-field//mat-label[contains(text(), 'Nombre del Tour Operador')]"),
        By.xpath("//input[@id='mat-input-2']/preceding::label[contains(., 'Nombre del Tour Operador')]")
    };
    return elementFinder.encontrarElemento(localizadores);
}


//=== app-pop-up-tour-operador
    @FindBy(how = How.ID, using = "mat-input-3")
    private WebElement textFiltrar;

/**
 * 🔧 MÉTODO AUXILIAR: Encuentra botón con icono "check_circle" con múltiples estrategias
 */
private WebElement encontrarBotonCheckCircle() {
    By[] localizadores = {
        By.xpath("//mat-icon[text()='check_circle']/ancestor::a"),
        By.cssSelector("a.mat-mdc-outlined-button.mat-accent"),
        By.xpath("//a[contains(@class, 'mat-mdc-outlined-button') and .//mat-icon[text()='check_circle']]"),
        By.xpath("//a[@mat-stroked-button and .//mat-icon[text()='check_circle']]"),
        By.xpath("//a[@color='accent' and .//mat-icon[text()='check_circle']]"),
        By.xpath("//a[@aria-disabled='false' and .//mat-icon[text()='check_circle']]"),
        By.xpath("//a[contains(@class, 'mdc-button--outlined') and .//mat-icon[text()='check_circle']]"),
        By.xpath("//mat-icon[@data-mat-icon-type='font' and text()='check_circle']/ancestor::a")
    };
    return elementFinder.encontrarElemento(localizadores);
}

//=== Frecuencia del vuelo

    @FindBy(how = How.ID, using = "airlinea")
    private WebElement selectAerolinea;

    @FindBy(how = How.ID, using = "mat-input-4")
    private WebElement textNumeroVuelo;

    @FindBy(how = How.ID, using = "mat-input-5")
    private WebElement textAsientos;

    @FindBy(how = How.ID, using = "clase")
    private WebElement textClase;

    @FindBy(how = How.ID, using = "origen")
    private WebElement textOrigen;

    @FindBy(how = How.ID, using = "destino")
    private WebElement textDestino;

    @FindBy(how = How.ID, using = "fechaInicial")
    private WebElement textFechaInicial;

    @FindBy(how = How.ID, using = "fechaFinal")
    private WebElement textFechaFinal;


        // ===== FRECUENCIA DEL VUELO =====

    @FindBy(how = How.XPATH, using = "//mat-selection-list[@formcontrolname='paramFrecuenciaVuelo']")
    private WebElement listFrecuenciaVuelo;







/**
 * 🎯 MÉTODO MEJORADO: Gestiona la solicitud de bloqueo creada
 * Obtiene el número de solicitud GUARDADO EN MEMORIA desde ButtonPages
 */
public void gestionarLaSolicitudDeBloqueoCreada() {
    try {
        System.out.println("🚀 Iniciando gestión de solicitud de bloqueo...");
        
        // ✅ PASO 1: Obtener el número de solicitud DESDE LA MEMORIA de ButtonPages
        System.out.println("📋 Obteniendo número de solicitud guardado en memoria...");
        
        String numeroSolicitud = this.buttonPages.obtenerNumeroSolicitud();
        
        if (numeroSolicitud == null || numeroSolicitud.isEmpty()) {
            throw new RuntimeException("❌ No se pudo obtener el número de solicitud de la memoria");
        }
        
        System.out.println("✅ Número de solicitud recuperado: " + numeroSolicitud);
        System.out.println("📋 ========================================");
        System.out.println("🔍 BUSCANDO SOLICITUD EN BANDEJA");
        System.out.println("🆔 Número a buscar: " + numeroSolicitud);
        System.out.println("📋 ========================================");
        
        // PASO 2: Navegar a la bandeja de solicitudes (si no estamos ahí)
        System.out.println("🔄 Verificando navegación a Bandeja de Solicitudes...");
        String urlActual = driver.getCurrentUrl();
        if (!urlActual.contains("/Inbox/Index")) {
            System.out.println("⚠️ No estamos en Bandeja de Solicitudes, navegando...");
            navegarABandejaDeSolicitudes();
        } else {
            System.out.println("✅ Ya estamos en Bandeja de Solicitudes");
        }
        
        // PASO 3: Esperar a que la tabla se cargue completamente
        System.out.println("⏳ Esperando carga de la tabla...");
        esperarCargaTabla();
        
        // PASO 4: Buscar y resaltar la fila con el número de solicitud
        System.out.println("🔍 Buscando solicitud " + numeroSolicitud + " en la tabla...");
        WebElement filaEncontrada = buscarYResaltarSolicitudEnTabla(numeroSolicitud);
        
        if (filaEncontrada == null) {
            throw new RuntimeException("❌ No se encontró la solicitud " + numeroSolicitud + " en la tabla");
        }
        
        System.out.println("✅ Solicitud encontrada en la tabla");
        
        // PASO 5: Hacer clic en el botón arrow_forward de esa fila
        System.out.println("🖱️ Haciendo clic en el botón de acción de la fila...");
        boolean clickExitoso = clickBotonArrowForward(filaEncontrada);
        
        if (clickExitoso) {
            System.out.println("✅ Gestión de solicitud completada exitosamente");
            Thread.sleep(2000);
        } else {
            throw new RuntimeException("❌ No se pudo hacer clic en el botón arrow_forward");
        }
        
    } catch (Exception e) {
        System.err.println("❌ Error en la gestión de solicitud: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Fallo en la gestión de solicitud de bloqueo", e);
    }
}

/**
 * 🔧 MÉTODO AUXILIAR: Espera a que la tabla Material Angular se cargue
 */
private void esperarCargaTabla() {
    try {
        System.out.println("⏳ Esperando a que la tabla se cargue...");
        
        // Esperar a que la tabla esté presente
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("mat-table.mat-mdc-table")
        ));
        
        // Esperar a que al menos una fila esté visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("mat-row.mat-mdc-row")
        ));
        
        // Pequeña pausa para asegurar que todos los elementos se renderizaron
        Thread.sleep(1500);
        
        System.out.println("✅ Tabla cargada correctamente");
        
    } catch (Exception e) {
        System.err.println("⚠️ Error esperando la tabla: " + e.getMessage());
    }
}

/**
 * 🎯 MÉTODO MEJORADO: Busca el número de solicitud en la tabla Material Angular y resalta la fila
 * @param numeroSolicitud Número de solicitud a buscar (ej: "S80799")
 * @return WebElement de la fila encontrada, o null si no existe
 */
public WebElement buscarYResaltarSolicitudEnTabla(String numeroSolicitud) {
    try {
        System.out.println("🔍 Buscando solicitud en la tabla Material: " + numeroSolicitud);
        
        // Múltiples estrategias para encontrar la fila en Material Angular
        By[] localizadoresFila = {
            // Buscar por mat-cell que contenga el ID exacto
            By.xpath("//mat-cell[contains(@class, 'mat-column-id') and contains(normalize-space(.), '" + numeroSolicitud + "')]/parent::mat-row"),
            
            // Buscar por mat-row que contenga el número
            By.xpath("//mat-row[contains(., '" + numeroSolicitud + "')]"),
            
            // Buscar por mat-cell con clase específica
            By.xpath("//mat-cell[contains(@class, 'cdk-column-id') and normalize-space()='" + numeroSolicitud + "']/ancestor::mat-row"),
            
            // Búsqueda más flexible
            By.xpath("//mat-row[.//mat-cell[contains(text(), '" + numeroSolicitud + "')]]"),
            
            // Búsqueda por clase completa
            By.xpath("//mat-cell[@class='mat-mdc-cell mdc-data-table__cell cdk-cell cdk-column-id mat-column-id ng-star-inserted' and contains(., '" + numeroSolicitud + "')]/parent::mat-row")
        };
        
        WebElement filaEncontrada = null;
        
        // Intentar cada localizador
        for (int i = 0; i < localizadoresFila.length; i++) {
            try {
                List<WebElement> elementos = driver.findElements(localizadoresFila[i]);
                if (!elementos.isEmpty()) {
                    filaEncontrada = elementos.get(0);
                    System.out.println("✅ Fila encontrada con localizador #" + (i + 1));
                    break;
                }
            } catch (Exception e) {
                // Continuar con el siguiente localizador
            }
        }
        
        if (filaEncontrada != null) {
            System.out.println("🎯 ¡Solicitud encontrada en la tabla!");
            
            // ✅ USANDO JavascriptResaltaHtml
            // 1. Hacer scroll hasta el elemento y centrarlo
            resaltador.scrollYCentrarElemento(filaEncontrada);
            
            // 2. Resaltar con parpadeo (3 veces) para llamar la atención
            resaltador.resaltarConParpadeo(filaEncontrada, 3);
            
            // 3. Dejar resaltado por 2 segundos
            resaltador.resaltarElemento(filaEncontrada, 2000);
            
            return filaEncontrada;
            
        } else {
            System.out.println("❌ No se encontró la solicitud " + numeroSolicitud + " en la tabla");
            
            // Debug: Imprimir todas las solicitudes visibles
            imprimirSolicitudesVisibles();
            
            return null;
        }
        
    } catch (Exception e) {
        System.err.println("❌ Error al buscar solicitud en tabla: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}

/**
 * 🔧 MÉTODO AUXILIAR DEBUG: Imprime todas las solicitudes visibles en la tabla
 */
private void imprimirSolicitudesVisibles() {
    try {
        System.out.println("📋 Solicitudes visibles en la tabla:");
        
        List<WebElement> celdasId = driver.findElements(
            By.cssSelector("mat-cell.mat-column-id")
        );
        
        for (int i = 0; i < celdasId.size(); i++) {
            String id = celdasId.get(i).getText().trim();
            System.out.println("   " + (i + 1) + ". " + id);
        }
        
    } catch (Exception e) {
        System.out.println("⚠️ No se pudieron listar las solicitudes");
    }
}

/**
 * 🎯 MÉTODO ESPECÍFICO: Hace clic en el botón arrow_forward de la fila
 * @param filaEncontrada Fila que contiene la solicitud
 * @return true si se hizo clic exitosamente, false en caso contrario
 */
public boolean clickBotonArrowForward(WebElement filaEncontrada) {
    try {
        System.out.println("🔍 Buscando botón 'arrow_forward' en la fila...");
        
        // Múltiples estrategias para encontrar el botón arrow_forward
        By[] localizadoresBoton = {
            // Buscar el primer botón <a> con icono arrow_forward
            By.xpath(".//a[@mat-stroked-button]//mat-icon[text()='arrow_forward']/ancestor::a"),
            
            // Buscar por clase y contenido
            By.xpath(".//a[contains(@class, 'mat-mdc-outlined-button')]//mat-icon[normalize-space()='arrow_forward']/parent::*/parent::a"),
            
            // Buscar directamente el mat-icon arrow_forward y subir al botón
            By.xpath(".//mat-icon[contains(text(), 'arrow_forward')]/ancestor::a[@mat-stroked-button]"),
            
            // Buscar por mat-cell de acción
            By.xpath(".//mat-cell[contains(@class, 'mat-column-accion')]//a[.//mat-icon[text()='arrow_forward']]"),
            
            // Búsqueda más genérica
            By.xpath(".//a[contains(@class, 'mdc-button--outlined')][1]")
        };
        
        WebElement boton = null;
        
        // Buscar el botón dentro de la fila
        for (int i = 0; i < localizadoresBoton.length; i++) {
            try {
                List<WebElement> botones = filaEncontrada.findElements(localizadoresBoton[i]);
                if (!botones.isEmpty()) {
                    boton = botones.get(0);
                    System.out.println("✅ Botón 'arrow_forward' encontrado con localizador #" + (i + 1));
                    break;
                }
            } catch (Exception e) {
                // Continuar con el siguiente localizador
            }
        }
        
        if (boton != null) {
            // ✅ USANDO JavascriptResaltaHtml
            // Resaltar el botón antes de hacer clic
            resaltador.resaltarElemento(boton, 1000);
            
            // Hacer clic usando múltiples estrategias
            try {
                // Estrategia 1: Clic normal con wait
                wait.until(ExpectedConditions.elementToBeClickable(boton));
                boton.click();
                System.out.println("✅ Clic realizado en el botón 'arrow_forward' (método normal)");
                
            } catch (Exception e1) {
                try {
                    // Estrategia 2: Clic con JavaScript
                    System.out.println("⚠️ Clic normal falló, intentando con JavaScript...");
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].click();", boton);
                    System.out.println("✅ Clic realizado con JavaScript");
                    
                } catch (Exception e2) {
                    // Estrategia 3: Actions
                    System.out.println("⚠️ JavaScript falló, intentando con Actions...");
                    Actions actions = new Actions(driver);
                    actions.moveToElement(boton).click().perform();
                    System.out.println("✅ Clic realizado con Actions");
                }
            }
            
            // Esperar a que se procese la acción
            Thread.sleep(1500);
            
            return true;
            
        } else {
            System.out.println("❌ No se encontró el botón 'arrow_forward' en la fila");
            return false;
        }
        
    } catch (Exception e) {
        System.err.println("❌ Error al hacer clic en el botón: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

/**
 * 🎯 MÉTODO: Navega a la Bandeja de Solicitudes desde el menú
 */
public void navegarABandejaDeSolicitudes() {
    try {
        System.out.println("🔍 Iniciando navegación a Bandeja de Solicitudes...");
        
        // Paso 1: Abrir el menú principal
        this.buttonPages.btnAdministracionDeBloqueos();
        
        // Paso 2: Hacer clic en el submenú
        this.buttonPages.btnBandejaDeSolicitudes();
        
        // Paso 3: Esperar a que cargue la página
        wait.until(ExpectedConditions.urlContains("/Inbox/Index"));
        
        System.out.println("✅ Navegación exitosa a Bandeja de Solicitudes");
        
    } catch (Exception e) {
        System.err.println("❌ Error al navegar a Bandeja de Solicitudes: " + e.getMessage());
        throw new RuntimeException("Fallo en la navegación", e);
    }
}


/**
 * 🎯 MÉTODO COMPLETO: Aprueba la solicitud de bloqueo creada
 * 1. Marca el checkbox "marcar como procesado"
 * 2. Hace clic en el botón "Ejecutar" de la página principal
 * 3. Espera el popup de confirmación
 * 4. Hace clic en el botón "Ejecutar" del popup
 */
public void aprobarLaSolicitudDeBloqueoCreada() {
    try {
        System.out.println("🚀 Iniciando aprobación de solicitud de bloqueo...");
        
        // Paso 1: Obtener el número de solicitud desde ButtonPages
        String numeroSolicitud = this.buttonPages.obtenerNumeroSolicitud();
        System.out.println("📋 Número de solicitud obtenido para aprobación: " + numeroSolicitud);
        
        // Paso 2: Esperar a que la página se cargue completamente
        System.out.println("⏳ Esperando a que la página de aprobación se cargue...");
        Thread.sleep(2000);
        
        // Paso 3: Marcar el checkbox "marcar como procesado"
        System.out.println("☑️ Marcando checkbox 'marcar como procesado'...");
        boolean checkboxMarcado = marcarCheckboxProcesado();
        
        if (!checkboxMarcado) {
            throw new RuntimeException("❌ No se pudo marcar el checkbox 'marcar como procesado'");
        }
        
        System.out.println("✅ Checkbox 'marcar como procesado' marcado exitosamente");
        
        // Paso 4: Hacer clic en el botón "Ejecutar" de la página principal
        System.out.println("🖱️ Haciendo clic en el botón 'Ejecutar' de la página principal...");
        boolean botonClickeado = clickBotonEjecutar();
        
        if (!botonClickeado) {
            throw new RuntimeException("❌ No se pudo hacer clic en el botón 'Ejecutar' principal");
        }
        
        System.out.println("✅ Botón 'Ejecutar' principal clickeado exitosamente");
        
        // Paso 5: Esperar a que aparezca el popup de confirmación
        System.out.println("⏳ Esperando popup de confirmación 'Detalles del Bloqueo'...");
        Thread.sleep(2000);
        
        // Paso 6: Hacer clic en el botón "Ejecutar" del popup
        System.out.println("🖱️ Haciendo clic en el botón 'Ejecutar' del popup de confirmación...");
        boolean popupConfirmado = clickBotonEjecutarPopup();
        
        if (!popupConfirmado) {
            throw new RuntimeException("❌ No se pudo confirmar en el popup");
        }
        
        System.out.println("✅ Popup confirmado exitosamente");
        
        // Paso 7: Esperar confirmación final o cambio de estado
        System.out.println("⏳ Esperando confirmación final de aprobación...");
        Thread.sleep(3000);
        
        System.out.println("🎉 ========================================");
        System.out.println("✅ SOLICITUD APROBADA EXITOSAMENTE");
        System.out.println("🆔 Número de Solicitud: " + numeroSolicitud);
        System.out.println("🎉 ========================================");
        
    } catch (Exception e) {
        System.err.println("❌ Error al aprobar la solicitud de bloqueo: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Fallo al aprobar la solicitud de bloqueo", e);
    }
}

/**
 * 🔧 MÉTODO AUXILIAR: Marca el checkbox "marcar como procesado"
 * @return true si se marcó exitosamente, false en caso contrario
 */
private boolean marcarCheckboxProcesado() {
    try {
        System.out.println("🔍 Buscando checkbox 'marcar como procesado'...");
        
        // Múltiples estrategias para encontrar el checkbox
        By[] localizadoresCheckbox = {
            // Estrategia 1: Por ID del checkbox
            By.id("mat-mdc-checkbox-35"),
            
            // Estrategia 2: Por ID del input interno
            By.id("mat-mdc-checkbox-35-input"),
            
            // Estrategia 3: Por label text "marcar como procesado"
            By.xpath("//label[contains(text(), 'marcar como procesado')]/parent::div/parent::mat-checkbox"),
            
            // Estrategia 4: Por label text y buscar el input
            By.xpath("//label[contains(text(), 'marcar como procesado')]/preceding-sibling::div//input[@type='checkbox']"),
            
            // Estrategia 5: Por clase mat-mdc-checkbox que contenga el label
            By.xpath("//mat-checkbox[.//label[contains(text(), 'marcar como procesado')]]"),
            
            // Estrategia 6: Por input checkbox con label específico
            By.xpath("//input[@type='checkbox' and @id='mat-mdc-checkbox-35-input']"),
            
            // Estrategia 7: Por estructura completa de Material checkbox
            By.xpath("//mat-checkbox[@id='mat-mdc-checkbox-35']//input[@type='checkbox']"),
            
            // Estrategia 8: Por clase y orden (primer checkbox en el contenedor)
            By.xpath("//div[@class='check']//mat-checkbox[1]//input[@type='checkbox']"),
            
            // Estrategia 9: CSS Selector por ID
            By.cssSelector("#mat-mdc-checkbox-35 input[type='checkbox']"),
            
            // Estrategia 10: CSS Selector por label
            By.cssSelector("label[for='mat-mdc-checkbox-35-input']")
        };
        
        WebElement checkbox = null;
        int estrategiaExitosa = -1;
        
        // Intentar cada localizador
        for (int i = 0; i < localizadoresCheckbox.length; i++) {
            try {
                WebElement elemento = wait.until(ExpectedConditions.presenceOfElementLocated(localizadoresCheckbox[i]));
                if (elemento != null) {
                    checkbox = elemento;
                    estrategiaExitosa = i + 1;
                    System.out.println("✅ Checkbox encontrado con localizador #" + estrategiaExitosa);
                    break;
                }
            } catch (Exception e) {
                System.out.println("⚠️ Localizador #" + (i + 1) + " falló, probando siguiente...");
            }
        }
        
        if (checkbox == null) {
            System.err.println("❌ No se encontró el checkbox con ningún localizador");
            return false;
        }
        
        // Verificar si ya está marcado
        boolean yaEstaMarcado = checkbox.isSelected() || 
                                checkbox.getAttribute("class").contains("checked") ||
                                checkbox.getAttribute("class").contains("mdc-checkbox--selected");
        
        if (yaEstaMarcado) {
            System.out.println("ℹ️ El checkbox ya está marcado");
            return true;
        }
        
        // ✅ USANDO JavascriptResaltaHtml
        // Resaltar el checkbox antes de hacer clic
        resaltador.resaltarElemento(checkbox, 1000);
        
        // Hacer clic en el checkbox con múltiples estrategias
        boolean clickExitoso = false;
        
        try {
            // Estrategia 1: Clic normal
            wait.until(ExpectedConditions.elementToBeClickable(checkbox));
            checkbox.click();
            System.out.println("✅ Checkbox marcado con clic normal");
            clickExitoso = true;
            
        } catch (Exception e1) {
            try {
                // Estrategia 2: Clic con JavaScript
                System.out.println("⚠️ Clic normal falló, intentando con JavaScript...");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", checkbox);
                System.out.println("✅ Checkbox marcado con JavaScript");
                clickExitoso = true;
                
            } catch (Exception e2) {
                try {
                    // Estrategia 3: Clic en el label asociado
                    System.out.println("⚠️ JavaScript falló, intentando clic en el label...");
                    WebElement label = driver.findElement(By.xpath("//label[@for='mat-mdc-checkbox-35-input']"));
                    label.click();
                    System.out.println("✅ Checkbox marcado haciendo clic en el label");
                    clickExitoso = true;
                    
                } catch (Exception e3) {
                    // Estrategia 4: Actions
                    System.out.println("⚠️ Label falló, intentando con Actions...");
                    Actions actions = new Actions(driver);
                    actions.moveToElement(checkbox).click().perform();
                    System.out.println("✅ Checkbox marcado con Actions");
                    clickExitoso = true;
                }
            }
        }
        
        // Esperar un momento para que se actualice el estado
        Thread.sleep(500);
        
        // Verificar que se marcó correctamente
        boolean estaSeleccionado = checkbox.isSelected() || 
                                    checkbox.getAttribute("class").contains("checked") ||
                                    checkbox.getAttribute("class").contains("mdc-checkbox--selected");
        
        if (estaSeleccionado) {
            System.out.println("✅ Verificación: Checkbox marcado correctamente");
        } else {
            System.out.println("⚠️ Advertencia: No se pudo verificar que el checkbox se marcó");
        }
        
        return clickExitoso;
        
    } catch (Exception e) {
        System.err.println("❌ Error al marcar checkbox: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

/**
 * 🔧 MÉTODO AUXILIAR: Hace clic en el botón "Ejecutar" de la página principal
 * @return true si se hizo clic exitosamente, false en caso contrario
 */
    public boolean clickBotonEjecutar() {
    try {
        System.out.println("🔍 Buscando botón 'Ejecutar' en la página principal...");
        
        // Múltiples estrategias para encontrar el botón "Ejecutar"
        By[] localizadoresBoton = {
            // Estrategia 1: Por texto exacto del botón
            By.xpath("//button[contains(@class, 'mat-mdc-raised-button') and contains(., 'Ejecutar')]"),
            
            // Estrategia 2: Por span con texto "Ejecutar"
            By.xpath("//span[@class='mdc-button__label' and normalize-space()='Ejecutar']/parent::button"),
            
            // Estrategia 3: Por clase mat-raised-button y color accent
            By.xpath("//button[@mat-raised-button and @color='accent' and contains(., 'Ejecutar')]"),
            
            // Estrategia 4: Por clase completa y texto
            By.xpath("//button[@class='mdc-button mdc-button--raised mat-mdc-raised-button mat-accent mat-mdc-button-base' and contains(., 'Ejecutar')]"),
            
            // Estrategia 5: Por span interno
            By.xpath("//span[normalize-space()='Ejecutar']/ancestor::button"),
            
            // Estrategia 6: Por tipo button y texto
            By.xpath("//button[@type='button' and contains(normalize-space(), 'Ejecutar')]"),
            
            // Estrategia 7: CSS Selector por clase y texto
            By.cssSelector("button.mat-mdc-raised-button.mat-accent"),
            
            // Estrategia 8: Por float right y texto
            By.xpath("//button[@style='float: right;' and contains(., 'Ejecutar')]"),
            
            // Estrategia 9: Búsqueda más genérica por mat-raised-button
            By.xpath("//button[@mat-raised-button]//span[contains(text(), 'Ejecutar')]/parent::button"),
            
            // Estrategia 10: Por cualquier botón con texto Ejecutar
            By.xpath("//button[normalize-space()='Ejecutar' or .//span[normalize-space()='Ejecutar']]")
        };
        
        WebElement boton = null;
        int estrategiaExitosa = -1;
        
        // Intentar cada localizador
        for (int i = 0; i < localizadoresBoton.length; i++) {
            try {
                WebElement elemento = wait.until(ExpectedConditions.presenceOfElementLocated(localizadoresBoton[i]));
                if (elemento != null && elemento.isDisplayed()) {
                    boton = elemento;
                    estrategiaExitosa = i + 1;
                    System.out.println("✅ Botón 'Ejecutar' encontrado con localizador #" + estrategiaExitosa);
                    break;
                }
            } catch (Exception e) {
                System.out.println("⚠️ Localizador #" + (i + 1) + " falló, probando siguiente...");
            }
        }
        
        if (boton == null) {
            System.err.println("❌ No se encontró el botón 'Ejecutar' con ningún localizador");
            return false;
        }
        
        // ✅ USANDO JavascriptResaltaHtml
        // Hacer scroll hasta el botón
        resaltador.scrollYCentrarElemento(boton);
        
        // Resaltar el botón antes de hacer clic
        resaltador.resaltarElemento(boton, 1000);
        
        // Hacer clic en el botón con múltiples estrategias
        boolean clickExitoso = false;
        
        try {
            // Estrategia 1: Clic normal con wait
            wait.until(ExpectedConditions.elementToBeClickable(boton));
            boton.click();
            System.out.println("✅ Clic en botón 'Ejecutar' realizado con método normal");
            clickExitoso = true;
            
        } catch (Exception e1) {
            try {
                // Estrategia 2: Clic con JavaScript
                System.out.println("⚠️ Clic normal falló, intentando con JavaScript...");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", boton);
                System.out.println("✅ Clic en botón 'Ejecutar' realizado con JavaScript");
                clickExitoso = true;
                
            } catch (Exception e2) {
                // Estrategia 3: Actions
                System.out.println("⚠️ JavaScript falló, intentando con Actions...");
                Actions actions = new Actions(driver);
                actions.moveToElement(boton).click().perform();
                System.out.println("✅ Clic en botón 'Ejecutar' realizado con Actions");
                clickExitoso = true;
            }
        }
        
        // Esperar a que se procese la acción
        Thread.sleep(1500);
        
        return clickExitoso;
        
    } catch (Exception e) {
        System.err.println("❌ Error al hacer clic en botón 'Ejecutar': " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

/**
 * 🔧 MÉTODO AUXILIAR: Hace clic en el botón "Ejecutar" del popup de confirmación
 * @return true si se hizo clic exitosamente, false en caso contrario
 */
private boolean clickBotonEjecutarPopup() {
    try {
        System.out.println("🔍 Buscando botón 'Ejecutar' en el popup de confirmación...");
        
        // Múltiples estrategias para encontrar el botón "Ejecutar" del popup
        By[] localizadoresBotonPopup = {
            // Estrategia 1: Por type="submit" y texto "Ejecutar" en el popup
            By.xpath("//button[@type='submit' and contains(., 'Ejecutar')]"),
            
            // Estrategia 2: Por span con texto "Ejecutar" y type submit
            By.xpath("//button[@type='submit']//span[@class='mdc-button__label' and normalize-space()='Ejecutar']"),
            
            // Estrategia 3: Por atributo cdkfocusinitial (específico del popup)
            By.xpath("//button[@cdkfocusinitial and contains(., 'Ejecutar')]"),
            
            // Estrategia 4: Por mat-card-title "Detalles del Bloqueo" y buscar botón Ejecutar
            By.xpath("//mat-card-title[contains(text(), 'Detalles del Bloqueo')]/ancestor::mat-card//button[@type='submit' and contains(., 'Ejecutar')]"),
            
            // Estrategia 5: Por mensaje de confirmación y botón submit
            By.xpath("//div[contains(text(), '¿Está seguro que desea continuar')]/ancestor::mat-card//button[@type='submit']"),
            
            // Estrategia 6: Por clase y type submit con color accent
            By.xpath("//button[@type='submit' and @color='accent' and contains(@class, 'mat-mdc-raised-button')]"),
            
            // Estrategia 7: Por float right y type submit
            By.xpath("//button[@type='submit' and @style='float: right;' and contains(., 'Ejecutar')]"),
            
            // Estrategia 8: CSS Selector por type submit y clase accent
            By.cssSelector("button[type='submit'].mat-accent"),
            
            // Estrategia 9: Por estructura completa del popup con mat-card-content
            By.xpath("//mat-card[.//mat-card-title[contains(text(), 'Detalles del Bloqueo')]]//button[@type='submit']"),
            
            // Estrategia 10: Por span interno "Ejecutar" en botón submit
            By.xpath("//span[normalize-space()='Ejecutar']/parent::button[@type='submit']"),
            
            // Estrategia 11: Por cualquier botón submit en mat-card con título específico
            By.xpath("//mat-card-title[text()='Detalles del Bloqueo']/ancestor::div//button[@type='submit']//span[text()=' Ejecutar ']"),
            
            // Estrategia 12: Búsqueda genérica por type submit (última opción)
            By.xpath("//button[@type='submit' and @mat-raised-button]")
        };
        
        WebElement botonPopup = null;
        int estrategiaExitosa = -1;
        
        // Intentar cada localizador
        for (int i = 0; i < localizadoresBotonPopup.length; i++) {
            try {
                WebElement elemento = wait.until(ExpectedConditions.presenceOfElementLocated(localizadoresBotonPopup[i]));
                if (elemento != null && elemento.isDisplayed()) {
                    botonPopup = elemento;
                    estrategiaExitosa = i + 1;
                    System.out.println("✅ Botón 'Ejecutar' del popup encontrado con localizador #" + estrategiaExitosa);
                    break;
                }
            } catch (Exception e) {
                System.out.println("⚠️ Localizador popup #" + (i + 1) + " falló, probando siguiente...");
            }
        }
        
        if (botonPopup == null) {
            System.err.println("❌ No se encontró el botón 'Ejecutar' del popup con ningún localizador");
            
            // Debug: Intentar listar todos los botones visibles
            try {
                List<WebElement> botonesVisibles = driver.findElements(By.tagName("button"));
                System.out.println("🐛 DEBUG: Botones visibles en la página:");
                for (int i = 0; i < botonesVisibles.size(); i++) {
                    if (botonesVisibles.get(i).isDisplayed()) {
                        System.out.println("   " + (i+1) + ". Texto: '" + botonesVisibles.get(i).getText() + "'");
                        System.out.println("      Type: " + botonesVisibles.get(i).getAttribute("type"));
                        System.out.println("      Class: " + botonesVisibles.get(i).getAttribute("class"));
                    }
                }
            } catch (Exception ex) {
                System.out.println("⚠️ No se pudo listar botones para debug");
            }
            
            return false;
        }
        
        // ✅ USANDO JavascriptResaltaHtml
        // Hacer scroll hasta el botón del popup
        resaltador.scrollYCentrarElemento(botonPopup);
        
        // Resaltar el botón antes de hacer clic
        resaltador.resaltarElemento(botonPopup, 1000);
        
        // Hacer clic en el botón del popup con múltiples estrategias
        boolean clickExitoso = false;
        
        try {
            // Estrategia 1: Clic normal con wait
            wait.until(ExpectedConditions.elementToBeClickable(botonPopup));
            botonPopup.click();
            System.out.println("✅ Clic en botón 'Ejecutar' del popup realizado con método normal");
            clickExitoso = true;
            
        } catch (Exception e1) {
            try {
                // Estrategia 2: Clic con JavaScript
                System.out.println("⚠️ Clic normal falló, intentando con JavaScript...");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", botonPopup);
                System.out.println("✅ Clic en botón 'Ejecutar' del popup realizado con JavaScript");
                clickExitoso = true;
                
            } catch (Exception e2) {
                // Estrategia 3: Actions
                System.out.println("⚠️ JavaScript falló, intentando con Actions...");
                Actions actions = new Actions(driver);
                actions.moveToElement(botonPopup).click().perform();
                System.out.println("✅ Clic en botón 'Ejecutar' del popup realizado con Actions");
                clickExitoso = true;
            }
        }
        
        // Esperar a que se procese la confirmación
        Thread.sleep(2000);
        
        return clickExitoso;
        
    } catch (Exception e) {
        System.err.println("❌ Error al hacer clic en botón 'Ejecutar' del popup: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

    public void diligenciarBloqueos(String negocio, String solicitante, String operador) {      
        try {
            System.out.println("🚀 Iniciando diligenciamiento de bloqueos...");
            
            // Seleccionar Negocio
            seleccionarNegocio(negocio);
            System.out.println("✅ Negocio seleccionado: " + negocio);
            
            // Ingresar Solicitante
            ingresarSolicitante(solicitante);
            System.out.println("✅ Solicitante ingresado: " + solicitante);
            
            // Ingresar Operador
            ingresarOperador(operador);
            System.out.println("✅ Operador ingresado: " + operador);
            
            System.out.println("🎉 Diligenciamiento de bloqueos completado exitosamente");
            
        } catch (Exception e) {
            System.err.println("❌ Error al diligenciar bloqueos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Fallo al diligenciar bloqueos", e);
        }

    }

    public void seleccionarNegocio(String negocio) throws InterruptedException {

        try {
            
            wait.until(ExpectedConditions.visibilityOf(textnegocio));
            wait.until(ExpectedConditions.elementToBeClickable(textnegocio));
            textnegocio.clear();
            Thread.sleep(2000);
            textnegocio.sendKeys(negocio);

            System.out.println("🔍 Seleccionando negocio: " + negocio);
        } catch (Exception e) {
            System.err.println("⚠️ Error seleccionando negocio: " + e.getMessage());
            throw new RuntimeException("Fallo al seleccionar negocio", e);

        }   

    }

    public void ingresarSolicitante(String solicitante) throws InterruptedException {

        try {
            
            wait.until(ExpectedConditions.visibilityOf(textSolicitante));
            wait.until(ExpectedConditions.elementToBeClickable(textSolicitante));
            textSolicitante.clear();
             Thread.sleep(2000);
            textSolicitante.sendKeys(solicitante);

            System.out.println("🔍 Ingresando solicitante: " + solicitante);
        } catch (Exception e) {
            System.err.println("⚠️ Error ingresando solicitante: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar solicitante", e);

        }   

    }


    public void ingresarOperador(String operador) throws InterruptedException {

        try {

            WebElement elemento = encontrarNombreTourOperador();
            wait.until(ExpectedConditions.visibilityOf(elemento));
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            Thread.sleep(2000);


           if (elemento != null) {
               elementInteractions.scrollToElement(elemento);
                wait.until(ExpectedConditions.elementToBeClickable(elemento));
                realizarClicConMultiplesEstrategias(elemento);
                System.out.println("✅ Clic realizado en 'Nombre del Tour Operador'");
           }else {
                throw new RuntimeException("❌ No se encontró el elemento 'Nombre del Tour Operador'");
            }


            Thread.sleep(1500);

            textFiltrar.clear();
            textFiltrar.sendKeys(operador);
            Thread.sleep(1500);


            WebElement elementos = encontrarBotonCheckCircle();
            elemento=elementos;
            wait.until(ExpectedConditions.visibilityOf(elemento));
            wait.until(ExpectedConditions.elementToBeClickable(elemento));

        


     if (elemento != null) {
               elementInteractions.scrollToElement(elemento);
                wait.until(ExpectedConditions.elementToBeClickable(elemento));
                realizarClicConMultiplesEstrategias(elemento);
                System.out.println("✅ Clic realizado en el boton 'check'");
           }else {
                throw new RuntimeException("❌ No se encontró el boton 'check'");
            }


            System.out.println("🔍 Ingresando operador: " + operador);
        } catch (Exception e) {
            System.err.println("⚠️ Error ingresando operador: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar operador", e);

        }   

    }


    public void diligenciarVuelo(String aerolinea, String numeroVuelo, String asientos, String clase, String origen, 
    String destino, String fechaInicial, String fechaFinal){

        try {
            
            seleccionarAerolinea(aerolinea);
            System.out.println("✅ Aerolínea seleccionada: " + aerolinea);

            ingresarNumeroVuelo(numeroVuelo);
            System.out.println("✅ Número de vuelo ingresado: " + numeroVuelo);

            ingresarAsientos(asientos);
            System.out.println("✅ Asientos ingresados: " + asientos);

            seleccionarClase(clase);
            System.out.println("✅ Clase seleccionada: " + clase);

            ingresarOrigen(origen);
            System.out.println("✅ Origen ingresado: " + origen);

            ingresarDestino(destino);
            System.out.println("✅ Destino ingresado: " + destino);

            ingresarFechaInicial(fechaInicial);
            System.out.println("✅ Fecha inicial ingresada: " + fechaInicial);

            ingresarFechaFinal(fechaFinal);
            System.out.println("✅ Fecha final ingresada: " + fechaFinal);
        } catch (Exception e) {
            System.err.println("⚠️ Error diligenciando vuelo: " + e.getMessage());
            throw new RuntimeException("Fallo al diligenciar vuelo", e);
        }
    }


    /**
     * Método para seleccionar la Aerolínea
     */
    public void seleccionarAerolinea(String aerolinea) {
        try {
            System.out.println("🔍 Intentando seleccionar aerolínea: " + aerolinea);
            
            wait.until(ExpectedConditions.elementToBeClickable(selectAerolinea));
            selectAerolinea.click();
            System.out.println("✅ Menú de Aerolínea desplegado");
            Thread.sleep(2000);
            System.out.println("⏱️ Esperando 2 segundos antes de seleccionar...");

            List<WebElement> opciones = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//mat-option//span")
            ));
            
            System.out.println("📋 Opciones disponibles en el dropdown:");
            for (WebElement opcion : opciones) {
                System.out.println("   - " + opcion.getText());
            }
            
            boolean encontrado = false;
            for (WebElement opcion : opciones) {
                String textoOpcion = opcion.getText().trim();
                if (textoOpcion.equalsIgnoreCase(aerolinea.trim())) {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", opcion);
                    Thread.sleep(300);
                    
                    opcion.click();
                    System.out.println("✅ Se seleccionó la aerolínea: " + aerolinea);
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado) {
                System.err.println("❌ No se encontró la aerolínea: '" + aerolinea + "' en las opciones disponibles");
                throw new RuntimeException("No se encontró la aerolínea: " + aerolinea + ". Revisa las opciones disponibles en el log.");
            }
            
            Thread.sleep(1000);
            System.out.println("✅ Dropdown de aerolínea cerrado correctamente");
            
        } catch (InterruptedException e) {
            System.err.println("❌ Error en la espera de tiempo: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw new RuntimeException("Fallo en la espera de Aerolínea", e);
        } catch (org.openqa.selenium.TimeoutException e) {
            System.err.println("❌ Timeout esperando el dropdown de aerolínea: " + e.getMessage());
            throw new RuntimeException("Timeout al esperar el dropdown de aerolínea. Verifica que el elemento esté visible.", e);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.err.println("❌ Elemento no encontrado: " + e.getMessage());
            throw new RuntimeException("No se encontró el elemento del dropdown de aerolínea", e);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al seleccionar aerolínea: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Fallo al seleccionar la aerolínea: " + e.getMessage(), e);
        }
    }

        /**
     * Método para ingresar el Número de Vuelo
     */
    public void ingresarNumeroVuelo(String numeroVuelo) {
        try {
            wait.until(ExpectedConditions.visibilityOf(textNumeroVuelo));
            textNumeroVuelo.clear();
            textNumeroVuelo.sendKeys(numeroVuelo);
            System.out.println("✅ Se ingresó el número de vuelo: " + numeroVuelo);
        } catch (Exception e) {
            System.err.println("❌ Error al ingresar número de vuelo: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar número de vuelo", e);
        }
    }

        /**
     * Método para ingresar el número de Asientos
     */
    public void ingresarAsientos(String asientos) {
        try {
            wait.until(ExpectedConditions.visibilityOf(textAsientos));
            textAsientos.clear();
            textAsientos.sendKeys(asientos);
            System.out.println("✅ Se ingresaron los asientos: " + asientos);
        } catch (Exception e) {
            System.err.println("❌ Error al ingresar asientos: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar asientos", e);
        }
    }



        /**
     * Método para seleccionar la Clase
     */
    public void seleccionarClase(String clase) {
        try {
            System.out.println("🔍 Intentando seleccionar clase: " + clase);

            wait.until(ExpectedConditions.elementToBeClickable(textClase));
            textClase.click();
            System.out.println("✅ Menú de Clase desplegado");
            Thread.sleep(2000);
            System.out.println("⏱️ Esperando 2 segundos antes de seleccionar...");

            List<WebElement> opciones = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//mat-option//span")
            ));
            
            System.out.println("📋 Opciones disponibles en el dropdown:");
            for (WebElement opcion : opciones) {
                System.out.println("   - " + opcion.getText());
            }
            
            boolean encontrado = false;
            for (WebElement opcion : opciones) {
                String textoOpcion = opcion.getText().trim();
                if (textoOpcion.equalsIgnoreCase(clase.trim())) {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", opcion);
                    Thread.sleep(300);
                    
                    opcion.click();
                    System.out.println("✅ Se seleccionó la clase: " + clase);
                    encontrado = true;
                    break;
                }
            }       
            
            if (!encontrado) {
                System.err.println("❌ No se encontró la clase: '" + clase + "' en las opciones disponibles");
                throw new RuntimeException("No se encontró la clase: " + clase + ". Revisa las opciones disponibles en el log.");
            }
            
            Thread.sleep(1000);
            System.out.println("✅ Dropdown de clase cerrado correctamente");

        } catch (InterruptedException e) {
            System.err.println("❌ Error en la espera de tiempo: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw new RuntimeException("Fallo en la espera de Clase", e);
        } catch (org.openqa.selenium.TimeoutException e) {
            System.err.println("❌ Timeout esperando el dropdown de clase: " + e.getMessage());
            throw new RuntimeException("Timeout al esperar el dropdown de clase. Verifica que el elemento esté visible.", e);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.err.println("❌ Elemento no encontrado: " + e.getMessage());
            throw new RuntimeException("No se encontró el elemento del dropdown de clase", e);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al seleccionar clase: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Fallo al seleccionar la clase: " + e.getMessage(), e);
        }
    }

    /**
     * Método para seleccionar el Origen
     */
    public void ingresarOrigen(String origen) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(textOrigen));
            textOrigen.click();
            System.out.println("✅ Menú de Origen desplegado");
            Thread.sleep(2000);
            System.out.println("⏱️ Esperando 2 segundos antes de seleccionar...");

            List<WebElement> opciones = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//mat-option//span")
            ));
            
            boolean encontrado = false;
            for (WebElement opcion : opciones) {
                if (opcion.getText().equalsIgnoreCase(origen)) {
                    opcion.click();
                    System.out.println("✅ Se seleccionó el origen: " + origen);
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado) {
                throw new RuntimeException("No se encontró el origen: " + origen);
            }
            
            Thread.sleep(2000);
            
        } catch (InterruptedException e) {
            System.err.println("❌ Error en la espera de tiempo: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw new RuntimeException("Fallo en la espera de Origen", e);
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar origen: " + e.getMessage());
            throw new RuntimeException("Fallo al seleccionar el origen", e);
        }
    }

    /**
     * Método para seleccionar el Destino
     */
    public void ingresarDestino(String destino) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(textDestino));
            textDestino.click();
            System.out.println("✅ Menú de Destino desplegado");
            Thread.sleep(2000);
            System.out.println("⏱️ Esperando 2 segundos antes de seleccionar...");

            List<WebElement> opciones = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//mat-option//span")
            ));
            
            String destinoAjustado = destino.replace("\"", "");
            destinoAjustado = destinoAjustado.replace(",", ", ");
            destino = destinoAjustado;

            boolean encontrado = false;
            for (WebElement opcion : opciones) {
                if (opcion.getText().equalsIgnoreCase(destino)) {
                    opcion.click();
                    System.out.println("✅ Se seleccionó el destino: " + destino);
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado) {
                throw new RuntimeException("No se encontró el destino: " + destino);
            }
            
            Thread.sleep(2000);
            
        } catch (InterruptedException e) {
            System.err.println("❌ Error en la espera de tiempo: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw new RuntimeException("Fallo en la espera de Destino", e);
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar destino: " + e.getMessage());
            throw new RuntimeException("Fallo al seleccionar el destino", e);
        }
    }


        // ===== MÉTODOS MODIFICADOS PARA MANEJO DE CALENDARIO =====
    
    /**
     * Método para ingresar la Fecha Inicial usando el calendario
     */
    public void ingresarFechaInicial(String fechaInicial) {
        try {
            String fechaLimpia = fechaInicial.replace("\"", "").trim();
            
            System.out.println("📅 Fecha inicial original: " + fechaInicial);
            
            FormatoFecha.ComponentesFecha componentes = FormatoFecha.extraerComponentesFecha(fechaLimpia);
            int dia = componentes.getDia();
            int mes = componentes.getMes();
            int año = componentes.getAño();
            
            System.out.println("📅 Componentes extraídos: Día=" + dia + ", Mes=" + mes + ", Año=" + año);
            
            buttonPages.seleccionarFechaInicial(dia, mes, año);
            
            System.out.println("✅ Se seleccionó la fecha inicial en el calendario: " + dia + "/" + mes + "/" + año);
            
        } catch (Exception e) {
            System.err.println("❌ Error al ingresar fecha inicial: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar fecha inicial", e);
        }
    }

    /**
     * Método para ingresar la Fecha Final usando el calendario
     */
    public void ingresarFechaFinal(String fechaFinal) {
        try {
            String fechaLimpia = fechaFinal.replace("\"", "").trim();
            
            System.out.println("📅 Fecha final original: " + fechaFinal);
            
            FormatoFecha.ComponentesFecha componentes = FormatoFecha.extraerComponentesFecha(fechaLimpia);
            int dia = componentes.getDia();
            int mes = componentes.getMes();
            int año = componentes.getAño();
            
            System.out.println("📅 Componentes extraídos: Día=" + dia + ", Mes=" + mes + ", Año=" + año);
            
            buttonPages.seleccionarFechaFinal(dia, mes, año);
            
            System.out.println("✅ Se seleccionó la fecha final en el calendario: " + dia + "/" + mes + "/" + año);
            
        } catch (Exception e) {
            System.err.println("❌ Error al ingresar fecha final: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar fecha final", e);
        }
    }

    
    /**
     * Método para seleccionar días de la semana de L a D
     */
    public void seleccionarTodosLosDias() {
        try {
            wait.until(ExpectedConditions.visibilityOf(listFrecuenciaVuelo));
            
            String[] dias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
            
            for (String dia : dias) {
                WebElement opcion = listFrecuenciaVuelo.findElement(
                    By.xpath(".//mat-list-option[.//span[contains(text(),'" + dia + "')]]")
                );
                
                WebElement box = opcion.findElement(By.cssSelector("input[type='checkbox']"));
                if (!box.isSelected()) {
                    opcion.click();
                    System.out.println("✅ Día seleccionado: " + dia);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar todos los días: " + e.getMessage());
            throw new RuntimeException("Fallo al seleccionar todos los días", e);
        }
    }



/**
 * 🔧 MÉTODO AUXILIAR: Encuentra el elemento con el número de solicitud usando múltiples estrategias
 */
private WebElement encontrarNumeroSolicitud() {
    By[] localizadores = {
        By.xpath("//mat-card-subtitle[contains(text(), 'Solicitud de bloqueos')]"),
        By.xpath("//mat-card-subtitle[@class='mat-mdc-card-subtitle' and contains(text(), 'Solicitud de bloqueos')]"),
        By.cssSelector("mat-card-subtitle.mat-mdc-card-subtitle"),
        By.xpath("//div[@class='mat-mdc-card-header-text']//mat-card-subtitle"),
        By.xpath("//mat-card-subtitle[contains(@class, 'mat-mdc-card-subtitle')]"),
        By.xpath("//*[contains(@class, 'mat-mdc-card-subtitle') and contains(text(), 'Solicitud de bloqueos')]")
    };
    return elementFinder.encontrarElemento(localizadores);
}

/**
 * 🎯 MÉTODO MEJORADO: Valida que la solicitud fue exitosa y extrae el número de solicitud
 * ✅ GUARDA el número en memoria para uso posterior
 * @return Número de solicitud (ej: "S80767")
 */
public String VerificarBloqueoCreado() {
    try {
        System.out.println("🔍 Validando que el bloqueo fue creado exitosamente...");
        
        // Esperar un momento para que el elemento aparezca
        Thread.sleep(3000);
        
        // Buscar el elemento con el número de solicitud
        WebElement elementoSolicitud = encontrarNumeroSolicitud();
        
        if (elementoSolicitud != null && elementoSolicitud.isDisplayed()) {
            // Obtener el texto completo: "Solicitud de bloqueos - S80767"
            String textoCompleto = elementoSolicitud.getText().trim();
            System.out.println("📄 Texto encontrado: " + textoCompleto);
            
            // Validar que contiene "Solicitud de bloqueos"
            if (!textoCompleto.contains("Creacion de bloqueos")) {
                throw new RuntimeException("❌ El texto no contiene 'Creacion de bloqueos': " + textoCompleto);
            }
            
            // Extraer el número de solicitud usando expresión regular
            String numeroSolicitud = extraerNumeroSolicitud(textoCompleto);
            
            if (numeroSolicitud != null && !numeroSolicitud.isEmpty()) {
                // ✅ GUARDAR EN VARIABLE STATIC (persiste entre sesiones)
                AdminBloqueoPage.numeroSolicitudCreada = numeroSolicitud;
                
                System.out.println("✅ Validación exitosa");
                System.out.println("📋 ========================================");
                System.out.println("📄 BLOQUEO CREADO EXITOSAMENTE");
                System.out.println("🆔 Número de Bloqueo: " + numeroSolicitud);
                System.out.println("💾 Número guardado en MEMORIA STATIC");
                System.out.println("🔒 Persistirá incluso después de cerrar sesión");
                System.out.println("📋 ========================================");
                
                return numeroSolicitud;
            } else {
                throw new RuntimeException("❌ No se pudo extraer el número de bloqueo del texto: " + textoCompleto);
            }
            
        } else {
            throw new RuntimeException("❌ No se encontró el elemento con el número de bloqueo");
        }
        
    } catch (Exception e) {
        System.err.println("❌ Error al validar bloqueo exitoso: " + e.getMessage());
        throw new RuntimeException("Fallo al validar la creación del bloqueo", e);
    }
}

/**
 * 🔧 MÉTODO AUXILIAR: Extrae el número de solicitud del texto usando regex
 * @param textoCompleto Texto completo que contiene el número (ej: "Solicitud de bloqueos - S80767")
 * @return Número de solicitud (ej: "S80767")
 */
private String extraerNumeroSolicitud(String textoCompleto) {
    try {
        // Método 1: Split por guion (más simple)
        if (textoCompleto.contains("-")) {
            String numeroSolicitud = textoCompleto.split("-")[1].trim();
            System.out.println("✅ Número extraído con split: " + numeroSolicitud);
            return numeroSolicitud;
        }
        
        // Método 2: Expresión regular (más robusto)
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("S\\d+");
        java.util.regex.Matcher matcher = pattern.matcher(textoCompleto);
        
        if (matcher.find()) {
            String numeroSolicitud = matcher.group();
            System.out.println("✅ Número extraído con regex: " + numeroSolicitud);
            return numeroSolicitud;
        }
        
        System.err.println("⚠️ No se pudo extraer el número de bloqueo");
        return "";
        
    } catch (Exception e) {
        System.err.println("❌ Error al extraer número de bloqueo: " + e.getMessage());
        return "";
    }
}

/**
 * 🎯 MÉTODO MEJORADO: Obtiene el número de solicitud guardado en memoria STATIC
 * Este método puede ser llamado desde cualquier clase después de validarSolicitudExitosa()
 * @return Número de solicitud guardado (ej: "S80767")
 */
public String obtenerNumeroSolicitud() {
    if (AdminBloqueoPage.numeroSolicitudCreada != null && !AdminBloqueoPage.numeroSolicitudCreada.isEmpty()) {
        System.out.println("📋 Número de bloqueo recuperado de MEMORIA STATIC: " + AdminBloqueoPage.numeroSolicitudCreada);
        System.out.println("🔒 Variable persistió después de cerrar sesión");
        return AdminBloqueoPage.numeroSolicitudCreada;
    } else {
        System.err.println("❌ ERROR: No hay número de bloqueo guardado en memoria");
        System.err.println("⚠️ Asegúrate de llamar VerificarBloqueoCreado() antes de obtenerNumeroSolicitud()");
        System.err.println("💡 El número de bloqueo se guarda cuando se valida la creación exitosa");
        throw new RuntimeException("❌ No se pudo obtener el número de bloqueo porque no fue guardado previamente");
    }
}

/**
 * 🔧 MÉTODO AUXILIAR OPCIONAL: Limpia el número de solicitud guardado
 * Útil para empezar un nuevo test desde cero
 */
public void limpiarNumeroSolicitud() {
    System.out.println("🧹 Limpiando número de bloqueo de memoria STATIC...");
    AdminBloqueoPage.numeroSolicitudCreada = null;
    System.out.println("✅ Número de bloqueo limpiado");
}

/**
 * 🔧 MÉTODO AUXILIAR OPCIONAL: Verifica si hay un número de solicitud guardado
 * @return true si hay número guardado, false si no
 */
public boolean tieneNumeroSolicitudGuardado() {
    boolean tiene = AdminBloqueoPage.numeroSolicitudCreada != null && !AdminBloqueoPage.numeroSolicitudCreada.isEmpty();
    System.out.println("🔍 ¿Tiene número guardado?: " + tiene);
    if (tiene) {
        System.out.println("   Número guardado en STATIC: " + AdminBloqueoPage.numeroSolicitudCreada);
    }
    return tiene;
}






    /**
     * 🔧 MÉTODO AUXILIAR: Realiza el clic usando múltiples estrategias
     */
    private void realizarClicConMultiplesEstrategias(WebElement elemento ) {
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










}
