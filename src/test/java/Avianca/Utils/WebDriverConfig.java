package Avianca.Utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

import java.util.logging.Level;

public class WebDriverConfig {
    private static final String BROWSER = System.getProperty("browser", "chrome");
    private static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "false"));
    private static final boolean ENABLE_LOGS = Boolean.parseBoolean(System.getProperty("enableLogs", "true"));
    private static Proxy seleniumProxy = null;

    /**
     * Configura el proxy de Selenium para BrowserMob
     * @param proxy Configuración del proxy de Selenium
     */
    public static void setSeleniumProxy(Proxy proxy) {
        seleniumProxy = proxy;
        System.out.println("🔧 Proxy configurado en WebDriverConfig");
    }
    
    /**
     * Limpia la configuración del proxy
     */
    public static void clearProxy() {
        seleniumProxy = null;
        System.out.println("🧹 Proxy limpiado de WebDriverConfig");
    }
    
    public static WebDriver getDriver() {
        switch (BROWSER.toLowerCase()) {
            case "chrome":
                return getChromeDriver();
            case "firefox":
                return getFirefoxDriver();
            case "edge":
                return getEdgeDriver();
            default:
                throw new IllegalArgumentException("Browser no soportado: " + BROWSER);
        }
    }

    private static WebDriver getChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (HEADLESS) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        
        // Configurar proxy si está disponible
        if (seleniumProxy != null) {
            options.setProxy(seleniumProxy);
            options.setAcceptInsecureCerts(true);
            System.out.println("🌐 ChromeDriver configurado con BrowserMob Proxy");
        }
        
        // Logs de rendimiento - solo si está habilitado y no causa conflictos
        if (ENABLE_LOGS) {
            try {
                LoggingPreferences logPrefs = new LoggingPreferences();
                logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
                logPrefs.enable(LogType.BROWSER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logPrefs);
            } catch (Exception e) {
                System.out.println("⚠️ No se pudieron habilitar logs de rendimiento: " + e.getMessage());
            }
        }
        
        return new ChromeDriver(options);
    }

    private static WebDriver getFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (HEADLESS) {
            options.addArguments("-headless");
        }
        
        // Configurar proxy si está disponible
        if (seleniumProxy != null) {
            options.setProxy(seleniumProxy);
            options.setAcceptInsecureCerts(true);
            System.out.println("🌐 FirefoxDriver configurado con BrowserMob Proxy");
        }
        
        if (ENABLE_LOGS) {
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
            logPrefs.enable(LogType.BROWSER, Level.ALL);
            options.setCapability("moz:loggingPrefs", logPrefs);
        }
        
        return new FirefoxDriver(options);
    }

    private static WebDriver getEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (HEADLESS) {
            options.addArguments("--headless=new");
        }
        
        // Configurar proxy si está disponible
        if (seleniumProxy != null) {
            options.setProxy(seleniumProxy);
            options.setAcceptInsecureCerts(true);
            System.out.println("🌐 EdgeDriver configurado con BrowserMob Proxy");
        }
        
        if (ENABLE_LOGS) {
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
            logPrefs.enable(LogType.BROWSER, Level.ALL);
            options.setCapability("goog:loggingPrefs", logPrefs);
        }
        
        return new EdgeDriver(options);
    }
}