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

public class AdminBloqueoPage {


   private ButtonPages buttonPages;
    private WebDriver driver;
    private WebDriverWait wait;

    public AdminBloqueoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.buttonPages = new ButtonPages(driver);
        PageFactory.initElements(driver, this);
    }

















    
}
