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
import Avianca.Pages.SolicitudBloqueoPage;
import Avianca.Utils.BrowserMobProxyHelper;
import Avianca.Utils.FormatoFecha;
import lombok.var;

public class AdminBloqueoPage {


    private ButtonPages buttonPages;
    private WebDriver driver;
    private WebDriverWait wait;
    private SolicitudBloqueoPage solicitudBloqueoPage;

    public AdminBloqueoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.buttonPages = new ButtonPages(driver);
        this.solicitudBloqueoPage = new SolicitudBloqueoPage(driver);   
        PageFactory.initElements(driver, this);
    }

    public void bandejaDeSolicitudesDeBloqueo() {
        this.buttonPages.btnAdministracionDeBloqueos();
    }

    public void hacerClicBandejaDeSolicitudes() {
        this.buttonPages.btnBandejaDeSolicitudes();
    }



/**
 * 🎯 MÉTODO MEJORADO: Gestiona la solicitud de bloqueo creada
 * Obtiene el número de solicitud GUARDADO EN MEMORIA desde ButtonPages
 */
public void gestionarLaSolicitudDeBloqueoCreada() {
    try {
        System.out.println("🚀 Iniciando gestión de solicitud de bloqueo...");
        
        // ✅ PASO 1: Obtener el número de solicitud DESDE LA MEMORIA de ButtonPages
        System.out.println("📋 Obteniendo número de solicitud guardado en memoria...");
        
        String numeroSolicitud = this.buttonPages.obtenerNumeroSolicitud();
        
        if (numeroSolicitud == null || numeroSolicitud.isEmpty()) {
            throw new RuntimeException("❌ No se pudo obtener el número de solicitud de la memoria");
        }
        
        System.out.println("✅ Número de solicitud recuperado: " + numeroSolicitud);
        System.out.println("📋 ========================================");
        System.out.println("🔍 BUSCANDO SOLICITUD EN BANDEJA");
        System.out.println("🆔 Número a buscar: " + numeroSolicitud);
        System.out.println("📋 ========================================");
        
        // PASO 2: Navegar a la bandeja de solicitudes (si no estamos ahí)
        System.out.println("🔄 Verificando navegación a Bandeja de Solicitudes...");
        String urlActual = driver.getCurrentUrl();
        if (!urlActual.contains("/Inbox/Index")) {
            System.out.println("⚠️ No estamos en Bandeja de Solicitudes, navegando...");
            navegarABandejaDeSolicitudes();
        } else {
            System.out.println("✅ Ya estamos en Bandeja de Solicitudes");
        }
        
        // PASO 3: Esperar a que la tabla se cargue completamente
        System.out.println("⏳ Esperando carga de la tabla...");
        esperarCargaTabla();
        
        // PASO 4: Buscar y resaltar la fila con el número de solicitud
        System.out.println("🔍 Buscando solicitud " + numeroSolicitud + " en la tabla...");
        WebElement filaEncontrada = buscarYResaltarSolicitudEnTabla(numeroSolicitud);
        
        if (filaEncontrada == null) {
            throw new RuntimeException("❌ No se encontró la solicitud " + numeroSolicitud + " en la tabla");
        }
        
        System.out.println("✅ Solicitud encontrada en la tabla");
        
        // PASO 5: Hacer clic en el botón arrow_forward de esa fila
        System.out.println("🖱️ Haciendo clic en el botón de acción de la fila...");
        boolean clickExitoso = clickBotonArrowForward(filaEncontrada);
        
        if (clickExitoso) {
            System.out.println("✅ Gestión de solicitud completada exitosamente");
            Thread.sleep(2000);
        } else {
            throw new RuntimeException("❌ No se pudo hacer clic en el botón arrow_forward");
        }
        
    } catch (Exception e) {
        System.err.println("❌ Error en la gestión de solicitud: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Fallo en la gestión de solicitud de bloqueo", e);
    }
}

/**
 * 🔧 MÉTODO AUXILIAR: Espera a que la tabla Material Angular se cargue
 */
private void esperarCargaTabla() {
    try {
        System.out.println("⏳ Esperando a que la tabla se cargue...");
        
        // Esperar a que la tabla esté presente
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("mat-table.mat-mdc-table")
        ));
        
        // Esperar a que al menos una fila esté visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("mat-row.mat-mdc-row")
        ));
        
        // Pequeña pausa para asegurar que todos los elementos se renderizaron
        Thread.sleep(1500);
        
        System.out.println("✅ Tabla cargada correctamente");
        
    } catch (Exception e) {
        System.err.println("⚠️ Error esperando la tabla: " + e.getMessage());
    }
}

/**
 * 🎯 MÉTODO MEJORADO: Busca el número de solicitud en la tabla Material Angular y resalta la fila
 * @param numeroSolicitud Número de solicitud a buscar (ej: "S80799")
 * @return WebElement de la fila encontrada, o null si no existe
 */
public WebElement buscarYResaltarSolicitudEnTabla(String numeroSolicitud) {
    try {
        System.out.println("🔍 Buscando solicitud en la tabla Material: " + numeroSolicitud);
        
        // Múltiples estrategias para encontrar la fila en Material Angular
        By[] localizadoresFila = {
            // Buscar por mat-cell que contenga el ID exacto
            By.xpath("//mat-cell[contains(@class, 'mat-column-id') and contains(normalize-space(.), '" + numeroSolicitud + "')]/parent::mat-row"),
            
            // Buscar por mat-row que contenga el número
            By.xpath("//mat-row[contains(., '" + numeroSolicitud + "')]"),
            
            // Buscar por mat-cell con clase específica
            By.xpath("//mat-cell[contains(@class, 'cdk-column-id') and normalize-space()='" + numeroSolicitud + "']/ancestor::mat-row"),
            
            // Búsqueda más flexible
            By.xpath("//mat-row[.//mat-cell[contains(text(), '" + numeroSolicitud + "')]]"),
            
            // Búsqueda por clase completa
            By.xpath("//mat-cell[@class='mat-mdc-cell mdc-data-table__cell cdk-cell cdk-column-id mat-column-id ng-star-inserted' and contains(., '" + numeroSolicitud + "')]/parent::mat-row")
        };
        
        WebElement filaEncontrada = null;
        
        // Intentar cada localizador
        for (int i = 0; i < localizadoresFila.length; i++) {
            try {
                List<WebElement> elementos = driver.findElements(localizadoresFila[i]);
                if (!elementos.isEmpty()) {
                    filaEncontrada = elementos.get(0);
                    System.out.println("✅ Fila encontrada con localizador #" + (i + 1));
                    break;
                }
            } catch (Exception e) {
                // Continuar con el siguiente localizador
            }
        }
        
        if (filaEncontrada != null) {
            System.out.println("🎯 ¡Solicitud encontrada en la tabla!");
            
            // 1. Hacer scroll hasta el elemento y centrarlo
            scrollYCentrarElemento(filaEncontrada);
            
            // 2. Resaltar con parpadeo (3 veces) para llamar la atención
            resaltarConParpadeo(filaEncontrada, 3);
            
            // 3. Dejar resaltado por 2 segundos
            resaltarElemento(filaEncontrada, 2000);
            
            return filaEncontrada;
            
        } else {
            System.out.println("❌ No se encontró la solicitud " + numeroSolicitud + " en la tabla");
            
            // Debug: Imprimir todas las solicitudes visibles
            imprimirSolicitudesVisibles();
            
            return null;
        }
        
    } catch (Exception e) {
        System.err.println("❌ Error al buscar solicitud en tabla: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}

/**
 * 🔧 MÉTODO AUXILIAR DEBUG: Imprime todas las solicitudes visibles en la tabla
 */
private void imprimirSolicitudesVisibles() {
    try {
        System.out.println("📋 Solicitudes visibles en la tabla:");
        
        List<WebElement> celdasId = driver.findElements(
            By.cssSelector("mat-cell.mat-column-id")
        );
        
        for (int i = 0; i < celdasId.size(); i++) {
            String id = celdasId.get(i).getText().trim();
            System.out.println("   " + (i + 1) + ". " + id);
        }
        
    } catch (Exception e) {
        System.out.println("⚠️ No se pudieron listar las solicitudes");
    }
}

/**
 * 🎯 MÉTODO ESPECÍFICO: Hace clic en el botón arrow_forward de la fila
 * @param filaEncontrada Fila que contiene la solicitud
 * @return true si se hizo clic exitosamente, false en caso contrario
 */
public boolean clickBotonArrowForward(WebElement filaEncontrada) {
    try {
        System.out.println("🔍 Buscando botón 'arrow_forward' en la fila...");
        
        // Múltiples estrategias para encontrar el botón arrow_forward
        By[] localizadoresBoton = {
            // Buscar el primer botón <a> con icono arrow_forward
            By.xpath(".//a[@mat-stroked-button]//mat-icon[text()='arrow_forward']/ancestor::a"),
            
            // Buscar por clase y contenido
            By.xpath(".//a[contains(@class, 'mat-mdc-outlined-button')]//mat-icon[normalize-space()='arrow_forward']/parent::*/parent::a"),
            
            // Buscar directamente el mat-icon arrow_forward y subir al botón
            By.xpath(".//mat-icon[contains(text(), 'arrow_forward')]/ancestor::a[@mat-stroked-button]"),
            
            // Buscar por mat-cell de acción
            By.xpath(".//mat-cell[contains(@class, 'mat-column-accion')]//a[.//mat-icon[text()='arrow_forward']]"),
            
            // Búsqueda más genérica
            By.xpath(".//a[contains(@class, 'mdc-button--outlined')][1]")
        };
        
        WebElement boton = null;
        
        // Buscar el botón dentro de la fila
        for (int i = 0; i < localizadoresBoton.length; i++) {
            try {
                List<WebElement> botones = filaEncontrada.findElements(localizadoresBoton[i]);
                if (!botones.isEmpty()) {
                    boton = botones.get(0);
                    System.out.println("✅ Botón 'arrow_forward' encontrado con localizador #" + (i + 1));
                    break;
                }
            } catch (Exception e) {
                // Continuar con el siguiente localizador
            }
        }
        
        if (boton != null) {
            // Resaltar el botón antes de hacer clic
            resaltarElemento(boton, 1000);
            
            // Hacer clic usando múltiples estrategias
            try {
                // Estrategia 1: Clic normal con wait
                wait.until(ExpectedConditions.elementToBeClickable(boton));
                boton.click();
                System.out.println("✅ Clic realizado en el botón 'arrow_forward' (método normal)");
                
            } catch (Exception e1) {
                try {
                    // Estrategia 2: Clic con JavaScript
                    System.out.println("⚠️ Clic normal falló, intentando con JavaScript...");
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].click();", boton);
                    System.out.println("✅ Clic realizado con JavaScript");
                    
                } catch (Exception e2) {
                    // Estrategia 3: Actions
                    System.out.println("⚠️ JavaScript falló, intentando con Actions...");
                    Actions actions = new Actions(driver);
                    actions.moveToElement(boton).click().perform();
                    System.out.println("✅ Clic realizado con Actions");
                }
            }
            
            // Esperar a que se procese la acción
            Thread.sleep(1500);
            
            return true;
            
        } else {
            System.out.println("❌ No se encontró el botón 'arrow_forward' en la fila");
            return false;
        }
        
    } catch (Exception e) {
        System.err.println("❌ Error al hacer clic en el botón: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

/**
 * 🔧 MÉTODO AUXILIAR: Resalta un elemento cambiando su estilo
 * @param elemento Elemento a resaltar
 * @param duracionMs Duración del resaltado en milisegundos
 */
private void resaltarElemento(WebElement elemento, int duracionMs) {
    try {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // Guardar el estilo original
        String estiloOriginal = elemento.getAttribute("style");
        
        // Aplicar nuevo estilo (fondo amarillo y borde rojo grueso)
        js.executeScript(
            "arguments[0].setAttribute('style', arguments[1]);",
            elemento,
            "background-color: #FFFF00 !important; border: 4px solid #FF0000 !important; box-shadow: 0 0 20px #FF0000 !important;"
        );
        
        System.out.println("✨ Elemento resaltado visualmente");
        
        // Esperar para que sea visible
        Thread.sleep(duracionMs);
        
        // Restaurar el estilo original
        if (estiloOriginal != null && !estiloOriginal.isEmpty()) {
            js.executeScript(
                "arguments[0].setAttribute('style', arguments[1]);",
                elemento,
                estiloOriginal
            );
        } else {
            js.executeScript(
                "arguments[0].removeAttribute('style');",
                elemento
            );
        }
        
        System.out.println("✅ Estilo original restaurado");
        
    } catch (Exception e) {
        System.err.println("⚠️ No se pudo resaltar el elemento: " + e.getMessage());
    }
}

/**
 * 🔧 MÉTODO AUXILIAR: Resalta con efecto de parpadeo
 * @param elemento Elemento a resaltar
 * @param veces Número de parpadeos
 */
private void resaltarConParpadeo(WebElement elemento, int veces) {
    try {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String estiloOriginal = elemento.getAttribute("style");
        
        for (int i = 0; i < veces; i++) {
            // Resaltar (verde brillante con borde rojo)
            js.executeScript(
                "arguments[0].setAttribute('style', 'background-color: #00FF00 !important; border: 4px solid #FF0000 !important;');",
                elemento
            );
            Thread.sleep(400);
            
            // Restaurar
            if (estiloOriginal != null && !estiloOriginal.isEmpty()) {
                js.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    elemento,
                    estiloOriginal
                );
            } else {
                js.executeScript("arguments[0].removeAttribute('style');", elemento);
            }
            Thread.sleep(400);
        }
        
        System.out.println("✨ Efecto de parpadeo aplicado " + veces + " veces");
        
    } catch (Exception e) {
        System.err.println("⚠️ Error en el parpadeo: " + e.getMessage());
    }
}

/**
 * 🔧 MÉTODO AUXILIAR: Hace scroll hasta el elemento y lo centra en la pantalla
 * @param elemento Elemento a centrar
 */
private void scrollYCentrarElemento(WebElement elemento) {
    try {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // Scroll suave hasta el elemento y centrarlo
        js.executeScript(
            "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'});",
            elemento
        );
        
        Thread.sleep(1000); // Esperar a que termine el scroll suave
        
        System.out.println("📍 Elemento centrado en la pantalla");
        
    } catch (Exception e) {
        System.err.println("⚠️ Error al centrar elemento: " + e.getMessage());
    }
}

/**
 * 🎯 MÉTODO: Navega a la Bandeja de Solicitudes desde el menú
 */
public void navegarABandejaDeSolicitudes() {
    try {
        System.out.println("🔍 Iniciando navegación a Bandeja de Solicitudes...");
        
        // Paso 1: Abrir el menú principal
        this.buttonPages.btnAdministracionDeBloqueos();
        
        // Paso 2: Hacer clic en el submenú
        this.buttonPages.btnBandejaDeSolicitudes();
        
        // Paso 3: Esperar a que cargue la página
        wait.until(ExpectedConditions.urlContains("/Inbox/Index"));
        
        System.out.println("✅ Navegación exitosa a Bandeja de Solicitudes");
        
    } catch (Exception e) {
        System.err.println("❌ Error al navegar a Bandeja de Solicitudes: " + e.getMessage());
        throw new RuntimeException("Fallo en la navegación", e);
    }
}


















    
}
