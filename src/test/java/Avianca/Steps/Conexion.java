
package Avianca.Steps;

import java.time.Duration;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;

import Avianca.Utils.BrowserMobProxyHelper;
import Avianca.Utils.WebDriverConfig;

public class Conexion {

    private WebDriver driver;
    private BrowserMobProxyHelper proxyHelper;
    private static final boolean ENABLE_PROXY = Boolean.parseBoolean(System.getProperty("enableProxy", "false"));

    public WebDriver abrirNavegador() {
        try {
            // Inicializar BrowserMob Proxy si está habilitado
            if (ENABLE_PROXY) {
                System.out.println("🔧 Iniciando BrowserMob Proxy...");
                proxyHelper = new BrowserMobProxyHelper();
                proxyHelper.iniciarProxy();
                
                Proxy seleniumProxy = proxyHelper.obtenerConfiguracionSelenium();
                WebDriverConfig.setSeleniumProxy(seleniumProxy);
                System.out.println("✅ BrowserMob Proxy iniciado y configurado");
            } else {
                System.out.println("ℹ️ BrowserMob Proxy deshabilitado (enableProxy=false)");
            }
            
            // Obtener driver con configuración de proxy si está activa
            driver = WebDriverConfig.getDriver();
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
            
            // Limpiar recursos en caso de error
            if (driver != null) {
                driver.quit();
            }
            if (proxyHelper != null) {
                proxyHelper.detenerProxy();
            }
            
            throw new RuntimeException("No se pudo inicializar el navegador: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene el helper del proxy para usarlo en las páginas
     * @return BrowserMobProxyHelper o null si el proxy está deshabilitado
     */
    public BrowserMobProxyHelper getProxyHelper() {
        return proxyHelper;
    }
    
    /**
     * Cierra el navegador y detiene el proxy si está activo
     */
    public void cerrarNavegador() {
        try {
            if (driver != null) {
                driver.quit();
                System.out.println("✅ Navegador cerrado");
            }
            
            if (proxyHelper != null) {
                proxyHelper.detenerProxy();
                System.out.println("✅ BrowserMob Proxy detenido");
            }
            
            WebDriverConfig.clearProxy();
            
        } catch (Exception e) {
            System.err.println("⚠️ Error al cerrar navegador/proxy: " + e.getMessage());
        }
    }
}
