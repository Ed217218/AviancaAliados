
package Avianca.Steps;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import Avianca.Utils.WebDriverConfig;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Conexion {

    private WebDriver driver;

    public WebDriver abrirNavegador() {
        try {
            // WebDriverManager para Java 8 - más estable que Selenium Manager 4.10.0
            driver = WebDriverConfig.getDriver();
            WebDriverManager.chromedriver().setup();

            System.out.println("✅ ChromeDriver configurado con WebDriverManager");
            
            ChromeOptions option = new ChromeOptions();
            option.addArguments("--remote-allow-origins=*");
            option.addArguments("--no-sandbox");
            option.addArguments("--disable-dev-shm-usage");
            option.addArguments("--disable-extensions");
            option.addArguments("--disable-web-security");
            option.addArguments("--disable-blink-features=AutomationControlled");
            
            driver = new ChromeDriver(option);
            driver.manage().window().maximize();
            
            // Agregar validación de URL
            String url = "https://aliadosqa.aro.avtest.ink/";
            System.out.println("📍 Navegando a: " + url);
            driver.navigate().to(url);
            
            // Usar Duration en lugar del método deprecado
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            
            System.out.println("✅ Navegador abierto exitosamente");
            System.out.println("🌐 URL actual: " + driver.getCurrentUrl());
            return this.driver;
            
        } catch (Exception e) {
            System.err.println("❌ Error detallado al abrir el navegador: " + e.getMessage());
            e.printStackTrace();
            if (driver != null) {
                driver.quit();
            }
            throw new RuntimeException("No se pudo inicializar el navegador: " + e.getMessage(), e);
        }
    }
}
