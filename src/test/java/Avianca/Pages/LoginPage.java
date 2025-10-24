package Avianca.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import Avianca.Steps.ButtonPages;

public class LoginPage {

    //llamado de una clase en otro paquete
    private ButtonPages buttonPages;

    //Esto es encontrar un elemeto en la pagina
    @FindBy(how = How.ID, using = "email")
    private WebElement txtEmail;

    @FindBy(how = How.ID, using = "password")
    private WebElement txtPassword;

    @FindBy(how = How.ID, using = "i0116")
    private WebElement txtEmailAdmin;               

    @FindBy(how = How.ID, using = "i0118")
    private WebElement txtPasswordAdmin;






    //esto es un constructor
    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        //instancia de clases
        this.buttonPages = new ButtonPages(driver);

    }

    public void llenarLogin(String userName, String password) {
        txtEmail.sendKeys(userName);
        txtPassword.sendKeys(password);
        buttonPages.btnLogin();
    }

       public void emailAdmin(String emailAdmin) {
        buttonPages.btnLoginAdmin();
        txtEmailAdmin.clear();
        txtEmailAdmin.sendKeys(emailAdmin);
        buttonPages.btnSiguiente();
        
    }

         public void passwordAdmin(String passwordAdmin) {
          buttonPages.lnkUsarPassword();
          txtPasswordAdmin.clear();
          txtPasswordAdmin.sendKeys(passwordAdmin);
          buttonPages.btnIniciarSesion();
          
     }




}
