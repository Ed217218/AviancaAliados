package Avianca.Definitions;

import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import Avianca.Pages.LoginPage;
import Avianca.Pages.SolicitudBloqueoPage;
import Avianca.Steps.Conexion;
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
    
    @When("El usuario navega a Nueva Solicitud")
    public void navegarANuevaSolicitud() {
        // Inicializar solicitudBloqueoPage después de tener el driver
        this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);
        this.solicitudBloqueoPage.llegarSolicitudDeBloqueo();
    }
}