package Avianca.Steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * 🔧 Hooks de Cucumber con Serenity BDD
 * Maneja el ciclo de vida de los escenarios (inicio y cierre)
 */
public class Hooks {
    
    @Before
    public void setUp(Scenario scenario) {
        // Configurar encoding UTF-8 para mostrar emojis en consola Windows
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
            System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));
        } catch (Exception e) {
            // Si falla, continuar sin UTF-8
        }
        
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("🚀 INICIANDO ESCENARIO: " + scenario.getName());
        System.out.println("════════════════════════════════════════════════════");
    }
    
    @After
    public void tearDown(Scenario scenario) {
        try {
            // Obtener el WebDriver actual de Serenity
            WebDriver driver = null;
            try {
                driver = ThucydidesWebDriverSupport.getDriver();
            } catch (Exception e) {
                System.out.println("⚠️ No se pudo obtener el driver de Serenity");
            }
            
            if (scenario.isFailed()) {
                System.out.println("════════════════════════════════════════════════════");
                System.out.println("❌ ESCENARIO FALLIDO: " + scenario.getName());
                System.out.println("════════════════════════════════════════════════════");
                
                // Captura de pantalla en caso de error (Serenity lo hace automáticamente también)
                if (driver != null) {
                    try {
                        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                        scenario.attach(screenshot, "image/png", "screenshot_error");
                        System.out.println("📸 Captura de pantalla guardada");
                    } catch (Exception e) {
                        System.out.println("⚠️ No se pudo tomar captura de pantalla: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("════════════════════════════════════════════════════");
                System.out.println("✅ ESCENARIO EXITOSO: " + scenario.getName());
                System.out.println("════════════════════════════════════════════════════");
            }
            
            // Cerrar el navegador (Serenity maneja esto automáticamente, pero lo forzamos por seguridad)
            if (driver != null) {
                System.out.println("🔒 Cerrando el navegador...");
                try {
                    driver.quit();
                    System.out.println("✅ Navegador cerrado exitosamente");
                } catch (Exception e) {
                    System.out.println("⚠️ El navegador ya estaba cerrado o error al cerrar: " + e.getMessage());
                }
            }
            
            // Limpiar sesión de Serenity
            Serenity.done();
            
        } catch (Exception e) {
            System.err.println("⚠️ Error en tearDown: " + e.getMessage());
        }
    }
}
