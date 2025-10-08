package Avianca.Pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Avianca.Steps.ButtonPages;
import groovyjarjarantlr4.v4.parse.ANTLRParser.sync_return;

public class SolicitudBloqueoPage {

    private ButtonPages buttonPages;
    private WebDriver driver;
    private WebDriverWait wait;

        //esto es un constructor
    public SolicitudBloqueoPage(WebDriver driver) {
        this.driver = driver;
        this.buttonPages = new ButtonPages(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
         //instancia de clases
        PageFactory.initElements(driver, this);
    }


    public void llegarSolicitudDeBloqueo() {
        buttonPages.btnSolicitudDeBloqueo();
        buttonPages.btnNuevaSolicitud();
      //  buttonPages.clickAgregarBloqueo();
       // buttonPages.clickEnviar();
      //  buttonPages.clickNuevaSolicitud(); 
      //  buttonPages.clickEliminacionMasiva();
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
     * @param solicitante Nombre del solicitante
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
     * @param tourOperador Nombre del tour operador a seleccionar
     */
    public void seleccionarTourOperador(String tourOperador) {
        try {
            // Esperar a que el select sea clickeable
            wait.until(ExpectedConditions.elementToBeClickable(selectTourOperador));
            
            // Hacer clic para desplegar el menú
            selectTourOperador.click();
            System.out.println("✅ Menú de Tour Operador desplegado");
            
            // Esperar 3 segundos después de desplegar el menú
            Thread.sleep(3000);
            System.out.println("⏱️ Esperando 5 segundos antes de seleccionar...");
            
            // Esperar a que aparezcan las opciones
            List<WebElement> opciones = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.xpath("//mat-option//span")
                ));
            
            // Seleccionar la opción deseada
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
     * @param negoNombre Nombre del nego
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
     * @param marcar true para marcar, false para desmarcar
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
     * @param aerolinea Nombre de la aerolínea a seleccionar
     */
    public void seleccionarAerolinea(String aerolinea) {
        try {
            System.out.println("🔍 Intentando seleccionar aerolínea: " + aerolinea);
            
            // Esperar a que el select sea clickeable
            wait.until(ExpectedConditions.elementToBeClickable(selectAerolinea));
            
            // Hacer clic para desplegar el menú
            selectAerolinea.click();
            System.out.println("✅ Menú de Aerolínea desplegado");
            
            // Esperar 5 segundos después de desplegar el menú
            Thread.sleep(3000);
            System.out.println("⏱️ Esperando 5 segundos antes de seleccionar...");
            
            // Esperar a que aparezcan las opciones
            List<WebElement> opciones = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//mat-option//span")
            ));
            
            System.out.println("📋 Opciones disponibles en el dropdown:");
            for (WebElement opcion : opciones) {
                System.out.println("   - " + opcion.getText());
            }
            
            // Seleccionar la opción deseada
            boolean encontrado = false;
            for (WebElement opcion : opciones) {
                String textoOpcion = opcion.getText().trim();
                if (textoOpcion.equalsIgnoreCase(aerolinea.trim())) {
                    // Scroll a la opción antes de hacer clic
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
            
            // Esperar a que el dropdown se cierre completamente
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
     * @param numeroVuelo Número de vuelo
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
     * @param origen Origen a seleccionar
     */
    public void seleccionarOrigen(String origen) {
        try {
            // Esperar a que el select sea clickeable
            wait.until(ExpectedConditions.elementToBeClickable(selectOrigen));
            
            // Hacer clic para desplegar el menú
            selectOrigen.click();
            System.out.println("✅ Menú de Origen desplegado");
            // Esperar 3 segundos después de desplegar el menú
            Thread.sleep(3000);
            System.out.println("⏱️ Esperando 3 segundos antes de seleccionar...");

            // Esperar a que aparezcan las opciones
            List<WebElement> opciones = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//mat-option//span")
            ));
            
            // Seleccionar la opción deseada
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
            
            // Esperar a que el dropdown se cierre completamente
            Thread.sleep(1000);
            
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
     * @param destino Destino a seleccionar
     */
    public void seleccionarDestino(String destino) {
        try {
            // Esperar a que el select sea clickeable
            wait.until(ExpectedConditions.elementToBeClickable(selectDestino));
            
            // Hacer clic para desplegar el menú
            selectDestino.click();
            System.out.println("✅ Menú de Destino desplegado");
            // Esperar 3 segundos después de desplegar el menú
            Thread.sleep(3000);
            System.out.println("⏱️ Esperando 3 segundos antes de seleccionar...");

            // Esperar a que aparezcan las opciones
            List<WebElement> opciones = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//mat-option//span")
            ));
            
            // Seleccionar la opción deseada
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
            
            // Esperar a que el dropdown se cierre completamente
            Thread.sleep(1000);
            
        } catch (InterruptedException e) {
            System.err.println("❌ Error en la espera de tiempo: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw new RuntimeException("Fallo en la espera de Destino", e);
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar destino: " + e.getMessage());
            throw new RuntimeException("Fallo al seleccionar el destino", e);
        }
    }

    /**
     * Método para ingresar la Fecha Inicial
     * @param fechaInicial Fecha inicial en formato adecuado
     */
    public void ingresarFechaInicial(String fechaInicial) {
        try {
            wait.until(ExpectedConditions.visibilityOf(txtFechaInicial));
            txtFechaInicial.clear();
            txtFechaInicial.sendKeys(fechaInicial);
            System.out.println("✅ Se ingresó la fecha inicial: " + fechaInicial);
        } catch (Exception e) {
            System.err.println("❌ Error al ingresar fecha inicial: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar fecha inicial", e);
        }
    }

    /**
     * Método para ingresar la Fecha Final
     * @param fechaFinal Fecha final en formato adecuado
     */
    public void ingresarFechaFinal(String fechaFinal) {
        try {
            wait.until(ExpectedConditions.visibilityOf(txtFechaFinal));
            txtFechaFinal.clear();
            txtFechaFinal.sendKeys(fechaFinal);
            System.out.println("✅ Se ingresó la fecha final: " + fechaFinal);
        } catch (Exception e) {
            System.err.println("❌ Error al ingresar fecha final: " + e.getMessage());
            throw new RuntimeException("Fallo al ingresar fecha final", e);
        }
    }


    /**
     * Método para ingresar el número de Asientos
     * @param asientos Número de asientos
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
     * Selecciona todos los días de la semana individualmente
     */
    public void seleccionarTodosLosDias() {
        try {
            wait.until(ExpectedConditions.visibilityOf(listFrecuenciaVuelo));
            
            // Array con los días de la semana
            String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
            
            for (String dia : dias) {
                WebElement opcion = listFrecuenciaVuelo.findElement(
                    By.xpath(".//mat-list-option[.//span[contains(text(),'" + dia + "')]]")
                );
                
                // Hacer clic en la opción si no está seleccionada
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
     * Marca el checkbox "Todos"
     */
    public void marcarTodos() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(chkTodos));
            if (!chkTodos.isSelected()) {
                chkTodos.click();
                System.out.println("✅ Checkbox 'Todos' marcado");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al marcar 'Todos': " + e.getMessage());
            throw new RuntimeException("Fallo al marcar 'Todos'", e);
        }
    }






 /*
    public void agregarSolicitante(String solicitante) {
        txtSolicitante.sendKeys(solicitante);
    }
*/
    /**
     * Método para llenar el formulario completo
     * @param solicitante Nombre del solicitante
     * @param tourOperador Tour operador
     * @param negoNombre Nombre del nego
     //* @param cargarArchivo Si se marca el checkbox de cargar archivo
     * @param aerolinea Aerolínea
     * @param numeroVuelo Número de vuelo
     * @param origen Origen
     * @param destino Destino
     * @param fechaInicial Fecha inicial
     * @param fechaFinal Fecha final
     * @param asientos Número de asientos
     //* @param dias Días de la semana
     //* @param marcarTodos Si se marca el checkbox "Todos"
     */
    
    public void llenarFormularioInformacionSolicitante(String solicitante, String tourOperador, String negoNombre) {

    // Información del Solicitante
        ingresarSolicitante(solicitante);
        seleccionarTourOperador(tourOperador);
        ingresarNegoNombre(negoNombre);
        //marcarCargarArchivo(cargarArchivo);
      
        System.out.println("✅ Formulario de Informacion del Solicitante llenado correctamente.");
    } 
          
    public void llenarFormularioInformacionVuelo(String aerolinea, String numeroVuelo, String origen, String destino, 
String fechaInicial, String fechaFinal, String asientos) {
     
    // Información del Vuelo     
    seleccionarAerolinea(aerolinea);
    ingresarNumeroVuelo(numeroVuelo);
    seleccionarOrigen(origen);
    
    // Ajustar el destino para agregar un espacio después de la coma
    String destinoAjustado = destino.replace(",", ", ");
    System.out.println("Seleccionando destino antes: " + destino);
       destino = destinoAjustado;
    seleccionarDestino(destino);
    System.out.println("Seleccionando destino después: " + destino);
    
    ingresarFechaInicial(fechaInicial);
    ingresarFechaFinal(fechaFinal);
    ingresarAsientos(asientos);

    System.out.println("✅ Formulario de Informacion del Vuelo llenado correctamente.");
    }
    
}