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
import Avianca.Steps.ButtonBloqueoPages;
//import Avianca.Pages.SolicitudBloqueoPage;
//import Avianca.Utils.BrowserMobProxyHelper;
import Avianca.Utils.ElementFinder;
import Avianca.Utils.ElementInteractions;
import Avianca.Utils.FormatoFecha;
import Avianca.Utils.JavascriptResaltaHtml;
//import lombok.var;


public class BloqueoPages {

    // ===== Objetos ===== 
    private ButtonPages buttonPages;
    private ButtonBloqueoPages buttonBloqueoPages;
    private WebDriver driver;
    private WebDriverWait wait;
   // private SolicitudBloqueoPage solicitudBloqueoPage;
    private JavascriptResaltaHtml resaltador;
    private ElementFinder elementFinder;
    private ElementInteractions elementInteractions;



   // ===== BOTONES DE ACCIÓN =====
    
    // ===== Xpath / Localizadores =====

    @FindBy(how = How.ID, using = "fechaInicial")
    private WebElement textFechaInicial;

    @FindBy(how = How.ID, using = "fechaFinal")
    private WebElement textFechaFinal;

// Constructor
    public BloqueoPages(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.buttonPages = new ButtonPages(driver);
        this.buttonBloqueoPages = new ButtonBloqueoPages(driver);
  //      this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
        this.resaltador = new JavascriptResaltaHtml(driver);  
        this.elementFinder = new ElementFinder(driver, 5);  
        this.elementInteractions = new ElementInteractions(driver);
        PageFactory.initElements(driver, this);
    }

// ===== VARIABLES ESTÁTICAS PARA ALMACENAR DATOS =====
    private static String nSolicitudGuardado = null;
    private static String recLocGuardado = null;



    public void navagarAdminBloqueos() {
        this.buttonBloqueoPages.clickAdminBloqueos();
    }

    public void navegarBloqueos() {
        this.buttonBloqueoPages.clickBloqueos();
    }

    public void navegarBusqueda() {
        this.buttonBloqueoPages.clickTabBusqueda();  
    }

    public void buscarBloqueo() {
        this.buttonBloqueoPages.clickBotonBuscar();
    }





    public void buscarBloqueoPorFechas(String fechaInicial, String fechaFinal) {
  try {
            
            ingresarFechaInicial(fechaInicial);
            System.out.println("✅ Fecha inicial ingresada: " + fechaInicial);

            ingresarFechaFinal(fechaFinal);
            System.out.println("✅ Fecha final ingresada: " + fechaFinal);
        } catch (Exception e) {
            System.err.println("⚠️ Error al buscar vuelo: " + e.getMessage());
            throw new RuntimeException("Fallo al buscar vuelo", e);
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
            
            buttonBloqueoPages.seleccionarFechaInicial(dia, mes, año);
            
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
            
            buttonBloqueoPages.seleccionarFechaFinal(dia, mes, año);
            
            System.out.println("✅ Se seleccionó la fecha final en el calendario: " + dia + "/" + mes + "/" + año);
            
        } catch (Exception e) {
            System.err.println("❌ Error al ingresar fecha final: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar fecha final", e);
        }
    }




/**
 * 🎯 MÉTODO PÚBLICO: Selecciona un bloqueo con estado Aprobado (#6BCF00)
 * 
 * Este método:
 * - Busca filas con estado Aprobado (verde)
 * - Navega entre páginas si es necesario
 * - Hace clic en el botón "edit" del primer registro
 * - Abre el popup de edición
 * - Extrae y guarda N° Solicitud y RecLoc en variables estáticas
 */
        public void seleccionarBloqueoAprobado() {
            try {
                System.out.println("🔍 ====== INICIANDO SELECCIÓN DE BLOQUEO APROBADO ======");
                
                // PASO 1: Buscar filas con estado Aprobado (#6BCF00)
                List<WebElement> filasAprobadas = buttonBloqueoPages.buscarFilasConEstadoAprobado();
                int paginaActual = 1;
                final int MAX_PAGINAS = 20;
                
                // PASO 2: Si no encuentra, navegar entre páginas
                while ((filasAprobadas == null || filasAprobadas.isEmpty()) && paginaActual <= MAX_PAGINAS) {
                    System.out.println("⚠️ No se encontraron registros aprobados en página " + paginaActual);
                    System.out.println("🔄 Navegando a la siguiente página...");
                    
                    boolean navegacionExitosa = buttonBloqueoPages.navegarSiguientePagina();
                    if (navegacionExitosa) {
                        Thread.sleep(3000);
                        paginaActual++;
                        filasAprobadas = buttonBloqueoPages.buscarFilasConEstadoAprobado();
                    } else {
                        throw new RuntimeException("❌ No se encontró ningún bloqueo Aprobado (#6BCF00) en " + paginaActual + " páginas");
                    }
                }
                
                // PASO 3: Hacer clic en el botón "edit" del primer registro
                if (filasAprobadas != null && !filasAprobadas.isEmpty()) {
                    System.out.println("✅ Se encontraron " + filasAprobadas.size() + " registros aprobados en página " + paginaActual);
                    
                    WebElement primeraFila = filasAprobadas.get(0);
                    buttonBloqueoPages.hacerClicEnEditarFila(primeraFila);
                    
                    Thread.sleep(3000); // Esperar apertura del popup
                }
                
                // PASO 4: Verificar que el popup se abrió
                if (!buttonBloqueoPages.verificarPopupEditarVisible()) {
                    throw new RuntimeException("❌ El popup de edición no se abrió correctamente");
                }
                System.out.println("✅ Popup de edición abierto correctamente");
                
                // PASO 5: Extraer y guardar N° Solicitud y RecLoc
                String[] datosBloqueo = buttonBloqueoPages.extraerDatosBloqueoDelPopup();
                if (datosBloqueo == null || datosBloqueo.length < 2) {
                    throw new RuntimeException("❌ No se pudieron extraer los datos del bloqueo");
                }
                
                nSolicitudGuardado = datosBloqueo[0];
                recLocGuardado = datosBloqueo[1];
                
                System.out.println("📋 Datos guardados - N° Solicitud: " + nSolicitudGuardado + " | RecLoc: " + recLocGuardado);
                System.out.println("✅ ====== SELECCIÓN DE BLOQUEO COMPLETADA ======");
                
            } catch (Exception e) {
                System.err.println("❌ Error al seleccionar bloqueo aprobado: " + e.getMessage());
                throw new RuntimeException("Fallo al seleccionar bloqueo aprobado", e);
            }
        }

/**
 * 🎯 MÉTODO PÚBLICO: Modifica los asientos adicionales del bloqueo
 * 
 * Este método:
 * - Ingresa el nuevo valor en "Asientos adicionales"
 * - Guarda los cambios
 * - Cierra el popup
 * 
 * @param asientos Número de asientos adicionales a ingresar
 */
        public void modificarAsientosDelBloqueo(String asientos) {
            try {
                System.out.println("📝 ====== INICIANDO MODIFICACIÓN DE ASIENTOS ======");
                
                // PASO 1: Ingresar nuevo valor en "Asientos adicionales"
                buttonBloqueoPages.ingresarAsientosAdicionales(asientos);
                
                // PASO 2: Guardar cambios
                buttonBloqueoPages.guardarCambiosBloqueo();
                
                // PASO 3: Cerrar popup
                buttonBloqueoPages.cerrarPopupEdicion();
                
                System.out.println("✅ ====== MODIFICACIÓN DE ASIENTOS COMPLETADA ======");
                
            } catch (Exception e) {
                System.err.println("❌ Error al modificar asientos del bloqueo: " + e.getMessage());
                throw new RuntimeException("Fallo al modificar asientos del bloqueo", e);
            }
        }

/**
 * 🎯 MÉTODO PÚBLICO: Valida que la modificación del bloqueo fue exitosa
 * 
 * Este método:
 * - Busca la fila con N° Solicitud y RecLoc guardados
 * - Valida que el estado cambió a Amarillo (#F6B113 o #FFD414)
 * - Resalta los datos encontrados con JavaScript
 */
        public void validarModificacionBloqueoExitosa() {
            try {
                System.out.println("🔍 ====== INICIANDO VALIDACIÓN DE MODIFICACIÓN ======");
                
                if (nSolicitudGuardado == null || recLocGuardado == null) {
                    throw new RuntimeException("❌ No hay datos guardados para validar. Ejecuta primero 'seleccionarBloqueoAprobado()'");
                }
                
                System.out.println("📋 Buscando fila con N° Solicitud: " + nSolicitudGuardado + " y RecLoc: " + recLocGuardado);
                
                // Esperar actualización de la tabla
                Thread.sleep(3000);
                
                // Buscar y validar la fila con color amarillo
                boolean validacionExitosa = buttonBloqueoPages.validarFilaConColorAmarillo(nSolicitudGuardado, recLocGuardado);
                
                if (validacionExitosa) {
                    System.out.println("✅✅✅ ====== VALIDACIÓN EXITOSA: ESTADO CAMBIÓ A AMARILLO ======");
                } else {
                    throw new RuntimeException("❌ No se encontró la fila con estado Amarillo (#F6B113 o #FFD414)");
                }
                
            } catch (Exception e) {
                System.err.println("❌ Error al validar modificación: " + e.getMessage());
                throw new RuntimeException("Fallo en la validación de la modificación", e);
            }
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
