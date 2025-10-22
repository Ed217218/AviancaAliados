package Avianca.Utils;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 🔍 Helper para capturar tráfico HTTP usando BrowserMob Proxy
 * Permite interceptar peticiones y respuestas sin usar CDP
 */
public class BrowserMobProxyHelper {
    
    private BrowserMobProxy proxy;
    
    /**
     * 🚀 Inicia el proxy en un puerto disponible
     */
    public void iniciarProxy() {
        try {
            System.out.println("🚀 Iniciando BrowserMob Proxy...");
            proxy = new BrowserMobProxyServer();
            proxy.start(0); // Puerto automático
            
            // Configurar captura de contenido completo
            proxy.enableHarCaptureTypes(
                CaptureType.REQUEST_CONTENT, 
                CaptureType.RESPONSE_CONTENT,
                CaptureType.REQUEST_HEADERS,
                CaptureType.RESPONSE_HEADERS
            );
            
            // Crear nuevo HAR (HTTP Archive)
            proxy.newHar("AviancaAliados");
            
            System.out.println("✅ Proxy iniciado en puerto: " + proxy.getPort());
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar proxy: " + e.getMessage());
            throw new RuntimeException("No se pudo iniciar el proxy", e);
        }
    }
    
    /**
     * 🔧 Obtiene la configuración de proxy para Selenium
     */
    public Proxy obtenerConfiguracionSelenium() {
        if (proxy == null) {
            throw new RuntimeException("El proxy no ha sido iniciado");
        }
        
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        System.out.println("🔗 Proxy configurado para Selenium");
        return seleniumProxy;
    }
    
    /**
     * 📡 Captura todas las peticiones HTTP registradas
     */
    public List<HarEntry> capturarTodasLasPeticiones() {
        if (proxy == null) {
            System.err.println("⚠️ El proxy no está iniciado");
            return Collections.emptyList();
        }
        
        Har har = proxy.getHar();
        return har.getLog().getEntries();
    }
    
    /**
     * 🔍 Busca una petición específica por URL parcial
     */
    public HarEntry buscarPeticionPorUrl(String urlParcial) {
        List<HarEntry> peticiones = capturarTodasLasPeticiones();
        
        return peticiones.stream()
            .filter(entry -> entry.getRequest().getUrl().contains(urlParcial))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * 🔍 Busca todas las peticiones que coincidan con una URL parcial
     */
    public List<HarEntry> buscarTodasLasPeticionesPorUrl(String urlParcial) {
        List<HarEntry> peticiones = capturarTodasLasPeticiones();
        
        return peticiones.stream()
            .filter(entry -> entry.getRequest().getUrl().contains(urlParcial))
            .collect(Collectors.toList());
    }
    
    /**
     * 📊 Obtiene el código de respuesta HTTP de una petición
     */
    public int obtenerCodigoRespuesta(String urlParcial) {
        HarEntry entry = buscarPeticionPorUrl(urlParcial);
        
        if (entry != null) {
            return entry.getResponse().getStatus();
        }
        
        System.out.println("⚠️ No se encontró petición para: " + urlParcial);
        return -1;
    }
    
    /**
     * 📄 Obtiene el cuerpo (body) de la respuesta
     */
    public String obtenerBodyRespuesta(String urlParcial) {
        HarEntry entry = buscarPeticionPorUrl(urlParcial);
        
        if (entry != null && entry.getResponse().getContent() != null) {
            return entry.getResponse().getContent().getText();
        }
        
        System.out.println("⚠️ No se encontró respuesta para: " + urlParcial);
        return null;
    }
    
    /**
     * 📄 Obtiene el cuerpo (body) de la petición
     */
    public String obtenerBodyPeticion(String urlParcial) {
        HarEntry entry = buscarPeticionPorUrl(urlParcial);
        
        if (entry != null && entry.getRequest().getPostData() != null) {
            return entry.getRequest().getPostData().getText();
        }
        
        System.out.println("⚠️ No se encontró petición para: " + urlParcial);
        return null;
    }
    
    /**
     * ⏱️ Obtiene el tiempo de respuesta en milisegundos
     */
    public long obtenerTiempoRespuesta(String urlParcial) {
        HarEntry entry = buscarPeticionPorUrl(urlParcial);
        
        if (entry != null) {
            return entry.getTime();
        }
        
        return -1;
    }
    
    /**
     * 🔄 Reinicia el HAR para comenzar una nueva captura
     */
    public void reiniciarCaptura() {
        if (proxy != null) {
            proxy.newHar("AviancaAliados");
            System.out.println("🔄 Captura de HAR reiniciada");
        }
    }
    
    /**
     * 📊 Imprime un resumen de todas las peticiones capturadas
     */
    public void imprimirResumenPeticiones() {
        List<HarEntry> peticiones = capturarTodasLasPeticiones();
        
        System.out.println("\n📊 RESUMEN DE PETICIONES HTTP:");
        System.out.println("=====================================");
        System.out.println("Total de peticiones: " + peticiones.size());
        System.out.println("-------------------------------------");
        
        for (int i = 0; i < peticiones.size(); i++) {
            HarEntry entry = peticiones.get(i);
            System.out.println((i + 1) + ". " + entry.getRequest().getMethod() + " " + 
                             entry.getRequest().getUrl());
            System.out.println("   Estado: " + entry.getResponse().getStatus() + 
                             " | Tiempo: " + entry.getTime() + "ms");
        }
        System.out.println("=====================================\n");
    }
    
    /**
     * 🛑 Detiene el proxy
     */
    public void detenerProxy() {
        if (proxy != null) {
            try {
                proxy.stop();
                System.out.println("🛑 Proxy detenido");
            } catch (Exception e) {
                System.err.println("⚠️ Error al detener proxy: " + e.getMessage());
            }
        }
    }
    
    /**
     * 🔍 Verifica si el proxy está activo
     */
    public boolean estaActivo() {
        return proxy != null && proxy.isStarted();
    }
    
    /**
     * 📍 Obtiene el puerto en el que está corriendo el proxy
     */
    public int obtenerPuerto() {
        if (proxy != null) {
            return proxy.getPort();
        }
        return -1;
    }
    
    /**
     * ✅ Valida que una petición se haya completado exitosamente (200-299)
     */
    public boolean peticionExitosa(String urlParcial) {
        int statusCode = obtenerCodigoRespuesta(urlParcial);
        return statusCode >= 200 && statusCode < 300;
    }
    
    /**
     * ❌ Valida si una petición tuvo error (400+)
     */
    public boolean peticionConError(String urlParcial) {
        int statusCode = obtenerCodigoRespuesta(urlParcial);
        return statusCode >= 400;
    }
}
