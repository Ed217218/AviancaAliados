package Avianca.Definitions;

import io.cucumber.java.en.When;

//import org.checkerframework.checker.units.qual.s;
import org.openqa.selenium.WebDriver;

import io.cucumber.java.en.And;
//import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import Avianca.Steps.Conexion;
import Avianca.Utils.BrowserMobProxyHelper;

import Avianca.Pages.LoginPage;
import Avianca.Pages.SolicitudBloqueoPage;
import Avianca.Pages.AdminBloqueoPage;
import Avianca.Pages.BloqueoPages;

public class DefinitionsSteps {

    private WebDriver driver;
    private Conexion conexion;
    private LoginPage loginPage;
    private SolicitudBloqueoPage solicitudBloqueoPage;
    private BrowserMobProxyHelper proxyHelper;
    private AdminBloqueoPage adminBloqueoPage;
    private BloqueoPages bloqueoPages;

    @Given("^abrir el navegador$")
    public void abrir_navegador() {
        try {
            this.conexion = new Conexion();
            this.driver = this.conexion.abrirNavegador();
            
            // Obtener el proxy helper si está habilitado
            this.proxyHelper = this.conexion.getProxyHelper();
            
            if (this.proxyHelper != null) {
                System.out.println("✅ Navegador abierto con BrowserMob Proxy habilitado");
            } else {
                System.out.println("✅ Navegador abierto sin proxy");
            }
            
        } catch (Exception e) {
            System.err.println("Error al abrir navegador: " + e.getMessage());
            throw new RuntimeException("Fallo al inicializar el navegador", e);
        }
    }
    
    // ⚠️ COMENTADO: Ahora Hooks.java se encarga de cerrar el navegador automáticamente
    /*
    @After
    public void cerrarRecursos() {
        try {
            if (this.conexion != null) {
                this.conexion.cerrarNavegador();
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al cerrar recursos: " + e.getMessage());
        }
    }
    */

    @When("^el usuario diligenica usuario (.*) diligencia password (.*)$")
    public void llenarLogin(String email, String password) {
        // Inicializar loginPage después de tener el driver
        this.loginPage = new LoginPage(driver);
        this.loginPage.llenarLogin(email, password);
    } 


    @When("^Iniciar sesion como administrador (.*) (.*)$")
    public void iniciarSesionComoAdministrador(String emailAdmin, String passwordAdmin) {
              // Inicializar loginPage después de tener el driver
        this.loginPage = new LoginPage(driver);
        this.loginPage.emailAdmin(emailAdmin);
        this.loginPage.passwordAdmin(passwordAdmin);


    }

    
    @When("^El usuario navega a Nueva Solicitud$")
    public void navegarANuevaSolicitud() {
        // Inicializar solicitudBloqueoPage con proxy si está disponible
        if (this.proxyHelper != null) {
            this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver, proxyHelper);
        } else {
            this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
        }
        this.solicitudBloqueoPage.SolicitudDeBloqueo();
        this.solicitudBloqueoPage.hacerClicNuevaSolicitud();
    }

    @When("^El usuario diligencia el formulario de solicitud de bloqueos (.*) (.*) (.*)$")
    public void diligenciarFormularioSolicitudBloqueo(String solicitante, String tourOperador, String negocio) {
        if (this.solicitudBloqueoPage == null) {
            if (this.proxyHelper != null) {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver, proxyHelper);
            } else {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
            }
        }
        this.solicitudBloqueoPage.llenarFormularioInformacionSolicitante(solicitante, tourOperador, negocio);
    }

   @When("^El usuario diligencia el formulario de informacion del vuelo (.*) (.*) (.*) (.*) (.*) (.*) (.*)$")
    public void diligenciarFormularioVuelo(
        String aerolinea,      // [A-Z]+ captura letras mayúsculas (ej: AV)
        String numeroVuelo,    // \d+ captura números (ej: 1632)
        String origen,         // [^\s]+ captura sin espacios (ej: UIO,Quito)
        String destino,        // [\w\s,]+ captura con espacios (ej: GPS, Baltra)
        String fechaInicial,   // \d{4}-\d{2}-\d{2} formato fecha
        String fechaFinal,     // \d{4}-\d{2}-\d{2} formato fecha
        String asientos        // \d+ captura números
        ) 
            {

                    System.out.println("🔍 DEBUG - Parámetros recibidos:");
                    System.out.println("   Aerolínea: '" + aerolinea + "'");
                    System.out.println("   Número de Vuelo: '" + numeroVuelo + "'");
                    System.out.println("   Origen: '" + origen + "'");
                    System.out.println("   Destino: '" + destino + "'");
                    System.out.println("   Fecha Inicial: '" + fechaInicial + "'");
                    System.out.println("   Fecha Final: '" + fechaFinal + "'");
                    System.out.println("   Asientos: '" + asientos + "'");

                    if (this.solicitudBloqueoPage == null) {
                        if (this.proxyHelper != null) {
                            this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver, proxyHelper);
                        } else {
                            this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
                        }
                    }
            
                    this.solicitudBloqueoPage.llenarFormularioInformacionVuelo(
                    aerolinea,
                    numeroVuelo,
                    origen,
                    destino,
                    fechaInicial,
                    fechaFinal,
                    asientos
                        );
            }



    @When("^Diligenciar frcuencia de vuelo$")
    public void diligenciarFrecuenciaDeVuelo() {
        if (this.solicitudBloqueoPage == null) {
            if (this.proxyHelper != null) {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver, proxyHelper);
            } else {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
            }
        }
        this.solicitudBloqueoPage.seleccionarTodosLosDias();
        this.solicitudBloqueoPage.marcarTodos();
    }

    @When("^Agregar bloqueo$")
    public void agregarBloqueo() {
        if (this.solicitudBloqueoPage == null) {
            if (this.proxyHelper != null) {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver, proxyHelper);
            } else {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
            }
        }
        this.solicitudBloqueoPage.hacerClicAgregarBloqueo();
    }

    @When("^Eliminacion masiva de bloqueos$")
    public void eliminacionMasivaDeBloqueos() {
        if (this.solicitudBloqueoPage == null) {
            if (this.proxyHelper != null) {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver, proxyHelper);
            } else {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
            }
        }
        this.solicitudBloqueoPage.hacerClicEliminacionMasiva();
    }

    @When("^Agregar nuevamente bloqueo$")
    public void agregarNuevamenteBloqueo() {
        if (this.solicitudBloqueoPage == null) {
            if (this.proxyHelper != null) {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver, proxyHelper);
            } else {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
            }
        }
        this.solicitudBloqueoPage.hacerClicAgregarBloqueo();
    }



    @When("^El usuario hace clic en Enviar$")
    public void hacerClicEnviar() {
        if (this.solicitudBloqueoPage == null) {
            if (this.proxyHelper != null) {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver, proxyHelper);
            } else {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
            }
        }
        this.solicitudBloqueoPage.hacerClicEnviar();    
    }

    @When("^El usuario valida que la solicitud de bloqueo fue creada exitosamente$")
    public void validarSolicitudBloqueoExitosa() {
        if (this.solicitudBloqueoPage == null) {
            if (this.proxyHelper != null) {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver, proxyHelper);
            } else {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
            }
        }
        this.solicitudBloqueoPage.validarSolicitudExitosa();
    }

    @When("^Cerrar sesion$")
    public void cerrarSesion() {
        if (this.solicitudBloqueoPage == null) {
            if (this.proxyHelper != null) {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver, proxyHelper);
            } else {
                this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
            }
        }
        this.solicitudBloqueoPage.cerrarSesion();
    }



    @When("^Bandeja de solicitudes de bloqueo$")
    public void bandejaDeSolicitudesDeBloqueo() {
        this.adminBloqueoPage = new AdminBloqueoPage(driver);  
        this.adminBloqueoPage.bandejaDeSolicitudesDeBloqueo();
        this.adminBloqueoPage.hacerClicBandejaDeSolicitudes();
    }

    

    @When("^Gestionar la solicitud de bloqueo creada$")
    public void gestionarLaSolicitudDeBloqueoCreada() {
        this.adminBloqueoPage = new AdminBloqueoPage(driver);
        this.adminBloqueoPage.gestionarLaSolicitudDeBloqueoCreada();
    }

    
    @When("^Aprobar la solicitud de bloqueo creada$")
    public void aprobarLaSolicitudDeBloqueoCreada() {
        this.adminBloqueoPage = new AdminBloqueoPage(driver);
        this.adminBloqueoPage.aprobarLaSolicitudDeBloqueoCreada();
    }

    @When("^Hacer clic en crear bloqueos$")
    public void hacerClicCrearBloqueos() {
        this.adminBloqueoPage = new AdminBloqueoPage(driver);
        this.adminBloqueoPage.bandejaDeSolicitudesDeBloqueo();
        this.adminBloqueoPage.hacerClicCrearBloqueos();
    }

    @When("^Diligenciar bloqueos (.*) (.*) (.*)$")
    public void diligenciarBloqueos(String negocio, String solicitante, String operador) {
        this.adminBloqueoPage = new AdminBloqueoPage(driver); 

        System.out.println("🔍 DEBUG - Parámetros recibidos:");
        System.out.println("   Negocio: '" + negocio + "'");
        System.out.println("   Solicitante: '" + solicitante + "'");
        System.out.println("   Operador: '" + operador + "'");

        this.adminBloqueoPage.diligenciarBloqueos(negocio, solicitante, operador);
    }

    @When("^Diligenciar vuelo (.*) (.*) (.*) (.*) (.*) (.*) (.*) (.*)$")
    public void diligenciarVuelo(String aerolinea, String numeroVuelo, String asientos, String clase, String origen, 
    String destino, String fechaInicial, String fechaFinal) {
        this.adminBloqueoPage = new AdminBloqueoPage(driver);
        
                    System.out.println("🔍 DEBUG - Parámetros recibidos:");
                    System.out.println("   Aerolínea: '" + aerolinea + "'");
                    System.out.println("   Número de Vuelo: '" + numeroVuelo + "'");
                    System.out.println("   Asientos: '" + asientos + "'");
                    System.out.println("   Clase: '" + clase + "'");
                    System.out.println("   Origen: '" + origen + "'");
                    System.out.println("   Destino: '" + destino + "'");
                    System.out.println("   Fecha Inicial: '" + fechaInicial + "'");
                    System.out.println("   Fecha Final: '" + fechaFinal + "'");
                   
        
        this.adminBloqueoPage.diligenciarVuelo(aerolinea, numeroVuelo, asientos, clase, origen, destino, fechaInicial, fechaFinal);
    }


    @When("^Diligenciar frcuencia de vuelo admin$")
        public void diligenciarFrecuenciaDeVueloAdmin() {
        this.adminBloqueoPage = new AdminBloqueoPage(driver);
        this.adminBloqueoPage.seleccionarTodosLosDias();
    }


    @When("^Agregar bloqueo admin$")
        public void agregarBloqueoAdmin() {
        this.adminBloqueoPage = new AdminBloqueoPage(driver);
        this.adminBloqueoPage.hacerClicAgregarBloqueo();
    }


    
    @When("^El usuario hace clic en ejecutar$")
    public void elUsuarioHaceClicEnEjecutar() {
        this.adminBloqueoPage = new AdminBloqueoPage(driver);
        boolean clickEjecutar = this.adminBloqueoPage.clickBotonEjecutar();
        if (!clickEjecutar) {
            throw new RuntimeException("No se pudo hacer clic en el botón 'Ejecutar'");
        }
    }

    @When("^El usuario valida que la solicitud de bloqueo fue creada exitosamente admin$")
    public void elUsuarioValidaQueLaSolicitudDeBloqueoFueCreadaExitosamenteAdmin() {
        this.adminBloqueoPage = new AdminBloqueoPage(driver);
        this.adminBloqueoPage.VerificarBloqueoCreado();
    }

    @When("^El usuario navega a bloqueos$")
    public void elUsuarioNavegaABloqueos() {
        this.bloqueoPages = new BloqueoPages(driver);
        this.bloqueoPages.navagarAdminBloqueos();
        this.bloqueoPages.navegarBloqueos();

    }


    @When("^El usuario busca el bloqueo creado previamente con fechas (.*) (.*)$")
    public void elUsuarioBuscaElBloqueoCreadoPreviamenteConFechas(String fechaInicio, String fechaFin) {
        this.bloqueoPages = new BloqueoPages(driver);
        this.bloqueoPages.navegarBusqueda();
        this.bloqueoPages.buscarBloqueoPorFechas(fechaInicio, fechaFin);
        this.bloqueoPages.buscarBloqueo();
    }   






    /**
     * 🎯 STEP: El usuario selecciona el bloqueo
     * 
     * Busca filas con estado Aprobado (#6BCF00) en la tabla
     * Si no encuentra en la página actual, navega entre páginas
     * Hace clic en el botón "edit" del primer registro encontrado
     */
    @And("^El usuario selecciona el bloqueo$")
    public void elUsuarioSeleccionaElBloqueo() {
        try {
            System.out.println("🎯 ===== EJECUTANDO: El usuario selecciona el bloqueo =====");
            bloqueoPages.seleccionarBloqueoAprobado();
            System.out.println("✅ Bloqueo seleccionado exitosamente");
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar el bloqueo: " + e.getMessage());
            throw new RuntimeException("Fallo al seleccionar el bloqueo", e);
        }
    }

    /**
     * 🎯 STEP: El usuario modifica el bloqueo con asientos
     * 
     * Modifica el campo "Asientos adicionales" en el popup
     * Guarda los cambios y cierra el popup
     * 
     * @param asientos Número de asientos adicionales a ingresar
     */
    @When("^El usuario modifica el bloqueo con asientos (.*)$")
    public void elUsuarioModificaElBloqueoConAsientos(String asientos) {
        try {
            System.out.println("🎯 ===== EJECUTANDO: El usuario modifica el bloqueo con asientos " + asientos + " =====");
            bloqueoPages.modificarAsientosDelBloqueo(asientos);
            System.out.println("✅ Bloqueo modificado exitosamente");
        } catch (Exception e) {
            System.err.println("❌ Error al modificar el bloqueo: " + e.getMessage());
            throw new RuntimeException("Fallo al modificar el bloqueo", e);
        }
    }

    /**
     * 🎯 STEP: El usuario valida que la modificación del bloqueo fue exitosa
     * 
     * Busca la fila con N° Solicitud y RecLoc guardados previamente
     * Valida que el estado cambió a Amarillo (#F6B113 o #FFD414)
     * Resalta los datos encontrados con JavaScript
     */
    @Then("^El usuario valida que la modificacion del bloqueo fue exitosa$")
    public void elUsuarioValidaQuelaModificacionDelBloqueoFueExitosa() {
        try {
            System.out.println("🎯 ===== EJECUTANDO: El usuario valida que la modificación del bloqueo fue exitosa =====");
            bloqueoPages.validarModificacionBloqueoExitosa();
            System.out.println("✅✅✅ Validación exitosa: El bloqueo cambió a estado Amarillo (En Revisión)");
        } catch (Exception e) {
            System.err.println("❌ Error al validar la modificación: " + e.getMessage());
            throw new RuntimeException("Fallo en la validación de la modificación", e);
        }
    }





















}

