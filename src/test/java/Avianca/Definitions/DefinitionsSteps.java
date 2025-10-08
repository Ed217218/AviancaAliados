package Avianca.Definitions;

import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import Avianca.Pages.LoginPage;
import Avianca.Pages.SolicitudBloqueoPage;
import Avianca.Steps.Conexion;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class DefinitionsSteps {

    private WebDriver driver;
    private Conexion conexion;
    private LoginPage loginPage;
    private SolicitudBloqueoPage solicitudBloqueoPage;

    @Given("^abrir el navegador$")
    public void abrir_navegador() {
        try {
            this.conexion = new Conexion();
            this.driver = this.conexion.abrirNavegador();
            System.out.println("Navegador abierto correctamente");
        } catch (Exception e) {
            System.err.println("Error al abrir navegador: " + e.getMessage());
            throw new RuntimeException("Fallo al inicializar el navegador", e);
        }
    }

    @When("^el usuario diligenica usuario (.*) diligencia password (.*)$")
    public void llenarLogin(String email, String password) {
        // Inicializar loginPage después de tener el driver
        this.loginPage = new LoginPage(driver);
        this.loginPage.llenarLogin(email, password);
    } 
    
    @When("^El usuario navega a Nueva Solicitud$")
    public void navegarANuevaSolicitud() {
        // Inicializar solicitudBloqueoPage después de tener el driver
        this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
        this.solicitudBloqueoPage.llegarSolicitudDeBloqueo();
    }

    @When("^El usuario diligencia el formulario de solicitud de bloqueos (.*) (.*) (.*)$")
    public void diligenciarFormularioSolicitudBloqueo(String solicitante, String tourOperador, String negocio) {
        this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
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
) {
    System.out.println("🔍 DEBUG - Parámetros recibidos:");
    System.out.println("   Aerolínea: '" + aerolinea + "'");
    System.out.println("   Número de Vuelo: '" + numeroVuelo + "'");
    System.out.println("   Origen: '" + origen + "'");
    System.out.println("   Destino: '" + destino + "'");
    System.out.println("   Fecha Inicial: '" + fechaInicial + "'");
    System.out.println("   Fecha Final: '" + fechaFinal + "'");
    System.out.println("   Asientos: '" + asientos + "'");

    this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
    
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



}