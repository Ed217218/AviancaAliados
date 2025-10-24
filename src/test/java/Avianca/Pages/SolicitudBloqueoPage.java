package Avianca.Pages;

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
import Avianca.Utils.BrowserMobProxyHelper;
import Avianca.Utils.FormatoFecha;
//import Avianca.Utils.ElementFinder;
//import Avianca.Utils.ElementInteractions;

public class SolicitudBloqueoPage {

    private ButtonPages buttonPages;
    private WebDriver driver;
    private WebDriverWait wait;
    //private ElementFinder elementFinder;
    //private ElementInteractions elementInteractions;
    
    public SolicitudBloqueoPage(WebDriver driver) {
        this.driver = driver;
        this.buttonPages = new ButtonPages(driver);
      //  this.elementInteractions = new ElementInteractions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
      //  this.elementFinder = new ElementFinder(driver, 5);
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Constructor que acepta un BrowserMobProxyHelper para captura de tráfico HTTP
     * @param driver WebDriver de Selenium
     * @param proxyHelper Helper del proxy para captura de peticiones HTTP
     */
    public SolicitudBloqueoPage(WebDriver driver, BrowserMobProxyHelper proxyHelper) {
        this.driver = driver;
        this.buttonPages = new ButtonPages(driver, proxyHelper);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Inyecta el proxy helper después de la construcción
     * @param proxyHelper Helper del proxy para captura de peticiones HTTP
     */
    public void setProxyHelper(BrowserMobProxyHelper proxyHelper) {
        if (this.buttonPages != null) {
            this.buttonPages.setProxyHelper(proxyHelper);
        }
    }

    // ===== MÉTODOS EXISTENTES =====
    
    public void SolicitudDeBloqueo() {
        buttonPages.btnSolicitudDeBloqueo();
    }

    public void hacerClicNuevaSolicitud() {
        buttonPages.btnNuevaSolicitud();
    }

    public void hacerClicAgregarBloqueo() {
        buttonPages.clickAgregarBloqueo();
    }

    public void hacerClicEliminacionMasiva() {
        buttonPages.clickEliminacionMasiva();
    } 

    public void hacerClicEnviar() {
        buttonPages.clickEnviar();
    }

    public void validarSolicitudExitosa() { 
        buttonPages.validarSolicitudExitosa();
    }

    public void cerrarSesion() {
        buttonPages.cerrarSesion();
    }   




    // ===== INFORMACIÓN DEL SOLICITANTE =====    

    @FindBy(how = How.ID, using = "mat-input-0")
    private WebElement txtSolicitante;

    @FindBy(how = How.ID, using = "tourOperador")
    private WebElement selectTourOperador;

    @FindBy(how = How.ID, using = "mat-input-1")    
    private WebElement txtNegoNombre;

    @FindBy(how = How.ID, using = "mat-mdc-checkbox-1")
    private WebElement chkCargarArchivo;

    // ===== INFORMACIÓN DEL VUELO =====

    @FindBy(how = How.ID, using = "paramAerolinea")
    private WebElement selectAerolinea;

    @FindBy(how = How.ID, using = "mat-input-2")
    private WebElement txtNumeroVuelo;

    @FindBy(how = How.ID, using = "paramOrigen")
    private WebElement selectOrigen;

    @FindBy(how = How.ID, using = "destino")
    private WebElement selectDestino;

    @FindBy(how = How.ID, using = "startDate")
    private WebElement txtFechaInicial;

    @FindBy(how = How.ID, using = "endDate")
    private WebElement txtFechaFinal;

    @FindBy(how = How.ID, using = "mat-input-5")
    private WebElement txtAsientos;

    // ===== FRECUENCIA DEL VUELO =====

    @FindBy(how = How.XPATH, using = "//mat-selection-list[@formcontrolname='paramFrecuenciaVuelo']")
    private WebElement listFrecuenciaVuelo;

    @FindBy(how = How.ID, using = "mat-mdc-checkbox-2")
    private WebElement chkTodos;

    // ===== MÉTODOS DE INTERACCIÓN =====
    
    /**
     * Método para llenar el campo Solicitante
     */
    public void ingresarSolicitante(String solicitante) {
        try {
            wait.until(ExpectedConditions.visibilityOf(txtSolicitante));
            txtSolicitante.clear();
            txtSolicitante.sendKeys(solicitante);
            System.out.println("✅ Solicitante ingresado correctamente: " + solicitante);
        } catch (Exception e) {
            System.err.println("❌ Error al ingresar el solicitante: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar el solicitante", e);        
        }
    }

    /**
     * Método para seleccionar el Tour Operador
     */
    public void seleccionarTourOperador(String tourOperador) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(selectTourOperador));
            selectTourOperador.click();
            System.out.println("✅ Menú de Tour Operador desplegado");
            Thread.sleep(2000);
            System.out.println("⏱️ Esperando 2 segundos antes de seleccionar...");

            List<WebElement> opciones = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.xpath("//mat-option//span")
                ));
            
            boolean encontrado = false;
            for(WebElement opcion : opciones) {
                if(opcion.getText().equalsIgnoreCase(tourOperador)) {
                    opcion.click();
                    System.out.println("✅ Seleccionó el Tour Operador: " + tourOperador);
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado) {
                throw new RuntimeException("No se encontró el Tour Operador: " + tourOperador);
            }

        } catch (InterruptedException e) {
            System.err.println("❌ Error en la espera de tiempo: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw new RuntimeException("Fallo en la espera del Tour Operador", e);
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar el Tour Operador: " + e.getMessage());
            throw new RuntimeException("Fallo al seleccionar el Tour Operador", e);        
        }    
    }

    /**
     * Método para ingresar el Nombre del Nego
     */
    public void ingresarNegoNombre(String negoNombre) {
        try {
            wait.until(ExpectedConditions.visibilityOf(txtNegoNombre));
            txtNegoNombre.clear();
            txtNegoNombre.sendKeys(negoNombre);
            System.out.println("✅ Nombre del Nego ingresado correctamente: " + negoNombre);
        } catch (Exception e) {
            System.err.println("❌ Error al ingresar el Nombre del Nego: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar el Nombre del Nego", e);
        }
    }

    /**
     * Método para marcar o desmarcar el checkbox "Cargar desde el archivo"
     */
    public void marcarCargarArchivo(boolean marcar) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(chkCargarArchivo));
            boolean estaSeleccionado = chkCargarArchivo.isSelected();
            if(marcar != estaSeleccionado) {
                chkCargarArchivo.click();
                System.out.println("✅ Checkbox 'Cargar desde el archivo' configurado a: " + marcar);
            } else {
                System.out.println("ℹ️ Checkbox 'Cargar desde el archivo' ya está en el estado deseado: " + marcar);
            }
        } catch (Exception e) {
            System.err.println("❌ Error al configurar el checkbox 'Cargar desde el archivo': " + e.getMessage());
            throw new RuntimeException("Fallo al configurar el checkbox 'Cargar desde el archivo'", e);        
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
            wait.until(ExpectedConditions.visibilityOf(txtNumeroVuelo));
            txtNumeroVuelo.clear();
            txtNumeroVuelo.sendKeys(numeroVuelo);
            System.out.println("✅ Se ingresó el número de vuelo: " + numeroVuelo);
        } catch (Exception e) {
            System.err.println("❌ Error al ingresar número de vuelo: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar número de vuelo", e);
        }
    }

    /**
     * Método para seleccionar el Origen
     */
    public void seleccionarOrigen(String origen) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(selectOrigen));
            selectOrigen.click();
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
    public void seleccionarDestino(String destino) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(selectDestino));
            selectDestino.click();
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
     * Método para ingresar el número de Asientos
     */
    public void ingresarAsientos(String asientos) {
        try {
            wait.until(ExpectedConditions.visibilityOf(txtAsientos));
            txtAsientos.clear();
            txtAsientos.sendKeys(asientos);
            System.out.println("✅ Se ingresaron los asientos: " + asientos);
        } catch (Exception e) {
            System.err.println("❌ Error al ingresar asientos: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar asientos", e);
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
                
                WebElement checkbox = opcion.findElement(By.cssSelector("input[type='checkbox']"));
                if (!checkbox.isSelected()) {
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
     * Marca el checkbox "Todos" - VERSIÓN MEJORADA PARA MANEJAR INTERCEPTACIÓN
     */
    public void marcarTodos() {
        try {
            System.out.println("🔍 Intentando marcar checkbox 'Todos'...");
            
            // Estrategia 1: Verificar si ya está marcado
            wait.until(ExpectedConditions.visibilityOf(chkTodos));
            if (chkTodos.isSelected()) {
                System.out.println("ℹ️ Checkbox 'Todos' ya está marcado");
                return;
            }
            
            // Estrategia 2: Hacer scroll para asegurar visibilidad
            System.out.println("📜 Haciendo scroll al checkbox 'Todos'...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", chkTodos);
            Thread.sleep(500);
            
            // Estrategia 3: Esperar a que sea clickeable y verificar si hay elementos superpuestos
            try {
                wait.until(ExpectedConditions.elementToBeClickable(chkTodos));
                System.out.println("✅ Checkbox 'Todos' es clickeable");
                
                // Intentar clic normal
                try {
                    System.out.println("🖱️ Intentando clic normal en checkbox 'Todos'...");
                    chkTodos.click();
                    System.out.println("✅ Checkbox 'Todos' marcado con clic normal");
                    return;
                } catch (Exception e) {
                    System.out.println("⚠️ Clic normal falló, intentando JavaScript...");
                }
                
            } catch (Exception e) {
                System.out.println("⚠️ Checkbox no es clickeable directamente, usando estrategias alternativas...");
            }
            
            // Estrategia 4: Clic con JavaScript (evita problemas de superposición)
            try {
                System.out.println("🖱️ Intentando clic con JavaScript...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", chkTodos);
                
                // Verificar si se marcó
                Thread.sleep(300);
                if (chkTodos.isSelected()) {
                    System.out.println("✅ Checkbox 'Todos' marcado con JavaScript");
                    return;
                }
            } catch (Exception e) {
                System.out.println("⚠️ Clic con JavaScript falló, intentando Actions...");
            }
            
            // Estrategia 5: Clic con Actions (simula interacción humana)
            try {
                System.out.println("🖱️ Intentando clic con Actions...");
                Actions actions = new Actions(driver);
                actions.moveToElement(chkTodos).click().perform();
                
                // Verificar si se marcó
                Thread.sleep(300);
                if (chkTodos.isSelected()) {
                    System.out.println("✅ Checkbox 'Todos' marcado con Actions");
                    return;
                }
            } catch (Exception e) {
                System.out.println("⚠️ Clic con Actions falló, intentando clic en el label...");
            }
            
            // Estrategia 6: Hacer clic en el label asociado
            try {
                System.out.println("🖱️ Intentando clic en el label del checkbox...");
                WebElement label = chkTodos.findElement(By.xpath(".//label"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", label);
                
                // Verificar si se marcó
                Thread.sleep(300);
                if (chkTodos.isSelected()) {
                    System.out.println("✅ Checkbox 'Todos' marcado mediante clic en label");
                    return;
                }
            } catch (Exception e) {
                System.out.println("⚠️ Clic en label falló, intentando ocultar elementos superpuestos...");
            }
            
            // Estrategia 7: Ocultar temporalmente elementos superpuestos
            try {
                System.out.println("🔍 Intentando ocultar elementos superpuestos...");
                
                // Buscar y ocultar el footer que está causando la interferencia
                List<WebElement> footers = driver.findElements(By.xpath("//div[contains(@class, 'footer')]"));
                for (WebElement footer : footers) {
                    if (footer.isDisplayed()) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility='hidden'", footer);
                        System.out.println("👻 Footer ocultado temporalmente");
                    }
                }
                
                // Esperar un momento y hacer clic
                Thread.sleep(300);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", chkTodos);
                
                // Verificar si se marcó
                Thread.sleep(300);
                if (chkTodos.isSelected()) {
                    System.out.println("✅ Checkbox 'Todos' marcado después de ocultar elementos superpuestos");
                    
                    // Restaurar visibilidad de los footers
                    for (WebElement footer : footers) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility='visible'", footer);
                    }
                    System.out.println("👁️ Visibilidad de elementos restaurada");
                    return;
                }
                
                // Restaurar visibilidad aunque no haya funcionado
                for (WebElement footer : footers) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility='visible'", footer);
                }
                
            } catch (Exception e) {
                System.out.println("⚠️ No se pudieron ocultar elementos superpuestos");
            }
            
            // Si llegamos aquí, todas las estrategias fallaron
            throw new RuntimeException("No se pudo marcar el checkbox 'Todos' después de intentar múltiples estrategias");
            
        } catch (Exception e) {
            System.err.println("❌ Error al marcar 'Todos': " + e.getMessage());
            throw new RuntimeException("Fallo al marcar 'Todos'", e);
        }
    }


















    // ===== MÉTODOS DE LLENADO DE FORMULARIOS =====
    
    /**
     * Método para llenar el formulario completo de información del solicitante
     */
    public void llenarFormularioInformacionSolicitante(String solicitante, String tourOperador, String negoNombre) {
        ingresarSolicitante(solicitante);
        seleccionarTourOperador(tourOperador);
        ingresarNegoNombre(negoNombre);
        
        System.out.println("✅ Formulario de Informacion del Solicitante llenado correctamente.");
    } 
          
    /**
     * Método para llenar el formulario completo de información del vuelo
     */
    public void llenarFormularioInformacionVuelo(String aerolinea, String numeroVuelo, String origen, String destino, 
            String fechaInicial, String fechaFinal, String asientos) {
        
        seleccionarAerolinea(aerolinea);
        ingresarNumeroVuelo(numeroVuelo);
        seleccionarOrigen(origen);
        seleccionarDestino(destino);    
        
        ingresarFechaInicial(fechaInicial);
        ingresarFechaFinal(fechaFinal);    
        ingresarAsientos(asientos);

        System.out.println("✅ Formulario de Informacion del Vuelo llenado correctamente.");
   }


/**
 * 🚪 Cierra el navegador
 */
public void cerrarNavegador() {
    try {
        if (driver != null) {
            System.out.println("🚪 Cerrando navegador...");
            driver.quit();
            System.out.println("✅ Navegador cerrado exitosamente");
        } else {
            System.out.println("⚠️ El driver ya estaba cerrado o no se inicializó");
        }
    } catch (Exception e) {
        System.err.println("❌ Error al cerrar el navegador: " + e.getMessage());
    }
}







}



