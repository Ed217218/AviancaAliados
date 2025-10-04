package Avianca.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import Avianca.Steps.ButtonPages;

public class SolicitudBloqueoPage {

    private ButtonPages buttonPages;

    @FindBy(how = How.XPATH, using = "//input[@id = 'mat-input-6']")
    private WebElement txtSolicitante;

    //esto es un constructor
    public SolicitudBloqueoPage(WebDriver driver) {
        this.buttonPages = new ButtonPages(driver);
        //instancia de clases
        PageFactory.initElements(driver, this);
    }
    
    public void llegarSolicitudDeBloqueo() {
        buttonPages.btnSolicitudDeBloqueo();
        buttonPages.btnNuevaSolicitud();
    }
/*
    public void agregarSolicitante(String solicitante) {
        txtSolicitante.sendKeys(solicitante);
    }
*/



}
