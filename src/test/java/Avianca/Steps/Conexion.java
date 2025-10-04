package Avianca.Steps;

import java.time.Duration;
import java.io.File;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Conexion {

    private WebDriver driver;

    public WebDriver abrirNavegador() {
        try {
            ChromeOptions option = new ChromeOptions();
            option.addArguments("--remote-allow-origins=*");
            option.addArguments("--no-sandbox");
            option.addArguments("--disable-dev-shm-usage");
            option.addArguments("--disable-extensions");
            option.addArguments("--disable-web-security"); // Agregar para evitar problemas CORS
            
            // Verificar si existe el chromedriver en la carpeta Drivers
            String projectPath = System.getProperty("user.dir");
            File chromeDriverFile = new File(projectPath + "\\Drivers\\chromedriver.exe");
            
            if (chromeDriverFile.exists()) {
                System.setProperty("webdriver.chrome.driver", chromeDriverFile.getAbsolutePath());
                System.out.println("Usando ChromeDriver desde: " + chromeDriverFile.getAbsolutePath());
            } else {
                System.out.println("ChromeDriver no encontrado en Drivers/, usando WebDriverManager automático");
                WebDriverManager.chromedriver().setup();
            }
            
            driver = new ChromeDriver(option);
            driver.manage().window().maximize();
            
            // Agregar validación de URL
            String url = "https://aliadosqa.aro.avtest.ink/";
            System.out.println("Navegando a: " + url);
            driver.navigate().to(url);
            
            // Usar Duration en lugar del método deprecado
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            
            System.out.println("Navegador abierto exitosamente");
            System.out.println("URL actual: " + driver.getCurrentUrl());
            return this.driver;
            
        } catch (Exception e) {
            System.err.println("Error detallado al abrir el navegador: " + e.getMessage());
            e.printStackTrace();
            if (driver != null) {
                driver.quit();
            }
            throw new RuntimeException("No se pudo inicializar el navegador: " + e.getMessage(), e);
        }
    }
}
