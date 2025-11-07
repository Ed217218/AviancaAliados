package Avianca.Utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * 🎨 CLASE UTILITARIA: Métodos JavaScript para resaltar elementos HTML
 * 
 * Esta clase proporciona métodos para resaltar visualmente elementos en la página web
 * durante la ejecución de pruebas automatizadas, facilitando el debugging y la visualización.
 * 
 * Métodos disponibles:
 * - resaltarElemento(): Resalta un elemento con fondo amarillo y borde rojo
 * - resaltarConParpadeo(): Resalta un elemento con efecto de parpadeo verde
 * - scrollYCentrarElemento(): Hace scroll hasta un elemento y lo centra en la pantalla
 * 
 * @author AviancaAliados QA Team
 * @version 1.0
 */
public class JavascriptResaltaHtml {
    
    private WebDriver driver;
    private JavascriptExecutor js;
    
    /**
     * Constructor que inicializa el WebDriver y JavascriptExecutor
     * @param driver Instancia del WebDriver
     */
    public JavascriptResaltaHtml(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
    }
    
    /**
     * 🔧 MÉTODO: Resalta un elemento cambiando su estilo visual
     * 
     * Aplica un fondo amarillo (#FFFF00), borde rojo grueso (4px) y sombra roja
     * al elemento especificado durante un tiempo determinado.
     * 
     * @param elemento Elemento web a resaltar
     * @param duracionMs Duración del resaltado en milisegundos
     * 
     * Ejemplo de uso:
     * <pre>
     * JavascriptResaltaHtml resaltador = new JavascriptResaltaHtml(driver);
     * resaltador.resaltarElemento(botonSubmit, 2000); // Resalta por 2 segundos
     * </pre>
     */
    public void resaltarElemento(WebElement elemento, int duracionMs) {
        try {
            // Guardar el estilo original
            String estiloOriginal = elemento.getAttribute("style");
            
            // Aplicar nuevo estilo (fondo amarillo y borde rojo grueso)
            js.executeScript(
                "arguments[0].setAttribute('style', arguments[1]);",
                elemento,
                "background-color: #81C2FF !important; border: 2px solid #000000ff !important; box-shadow: 0 0 8px #ffffffff !important;"
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
     * 🔧 MÉTODO: Resalta un elemento con efecto de parpadeo
     * 
     * Aplica un efecto visual de parpadeo alternando entre fondo verde brillante
     * y el estilo original del elemento. Útil para llamar la atención sobre
     * elementos críticos durante las pruebas.
     * 
     * @param elemento Elemento web a resaltar con parpadeo
     * @param veces Número de veces que parpadeará (cada parpadeo = on + off)
     * 
     * Ejemplo de uso:
     * <pre>
     * JavascriptResaltaHtml resaltador = new JavascriptResaltaHtml(driver);
     * resaltador.resaltarConParpadeo(checkbox, 3); // Parpadea 3 veces
     * </pre>
     */
    public void resaltarConParpadeo(WebElement elemento, int veces) {
        try {
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
     * 🔧 MÉTODO: Hace scroll hasta el elemento y lo centra en la pantalla
     * 
     * Utiliza scrollIntoView con comportamiento suave (smooth) para desplazar
     * la vista hasta el elemento especificado, centrándolo tanto vertical como
     * horizontalmente en la ventana del navegador.
     * 
     * @param elemento Elemento web a centrar en la pantalla
     * 
     * Ejemplo de uso:
     * <pre>
     * JavascriptResaltaHtml resaltador = new JavascriptResaltaHtml(driver);
     * resaltador.scrollYCentrarElemento(botonInferior); // Centra el botón
     * </pre>
     */
    public void scrollYCentrarElemento(WebElement elemento) {
        try {
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
     * 🔧 MÉTODO ADICIONAL: Resalta múltiples elementos simultáneamente
     * 
     * Resalta varios elementos al mismo tiempo con el mismo estilo.
     * Útil para resaltar grupos relacionados de elementos.
     * 
     * @param elementos Array de elementos web a resaltar
     * @param duracionMs Duración del resaltado en milisegundos
     */
    public void resaltarMultiplesElementos(WebElement[] elementos, int duracionMs) {
        try {
            // Guardar estilos originales
            String[] estilosOriginales = new String[elementos.length];
            
            // Aplicar resaltado a todos
            for (int i = 0; i < elementos.length; i++) {
                estilosOriginales[i] = elementos[i].getAttribute("style");
                js.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    elementos[i],
                    "background-color: #FFFF00 !important; border: 4px solid #FF0000 !important; box-shadow: 0 0 20px #FF0000 !important;"
                );
            }
            
            System.out.println("✨ " + elementos.length + " elementos resaltados simultáneamente");
            
            // Esperar
            Thread.sleep(duracionMs);
            
            // Restaurar estilos originales
            for (int i = 0; i < elementos.length; i++) {
                if (estilosOriginales[i] != null && !estilosOriginales[i].isEmpty()) {
                    js.executeScript(
                        "arguments[0].setAttribute('style', arguments[1]);",
                        elementos[i],
                        estilosOriginales[i]
                    );
                } else {
                    js.executeScript("arguments[0].removeAttribute('style');", elementos[i]);
                }
            }
            
            System.out.println("✅ Estilos originales restaurados en todos los elementos");
            
        } catch (Exception e) {
            System.err.println("⚠️ Error al resaltar múltiples elementos: " + e.getMessage());
        }
    }
    
    /**
     * 🔧 MÉTODO ADICIONAL: Aplica un borde personalizado al elemento
     * 
     * Permite personalizar el color y grosor del borde para resaltar elementos
     * con diferentes esquemas de color según la necesidad.
     * 
     * @param elemento Elemento web a resaltar
     * @param colorBorde Color del borde en formato hexadecimal (ej: "#FF0000")
     * @param grosorPx Grosor del borde en píxeles
     * @param duracionMs Duración del resaltado en milisegundos
     */
    public void resaltarConBordePersonalizado(WebElement elemento, String colorBorde, int grosorPx, int duracionMs) {
        try {
            String estiloOriginal = elemento.getAttribute("style");
            
            // Aplicar borde personalizado
            String estiloNuevo = "border: " + grosorPx + "px solid " + colorBorde + " !important; " +
                               "box-shadow: 0 0 15px " + colorBorde + " !important;";
            
            js.executeScript(
                "arguments[0].setAttribute('style', arguments[1]);",
                elemento,
                estiloNuevo
            );
            
            System.out.println("✨ Borde personalizado aplicado: " + colorBorde + " - " + grosorPx + "px");
            
            Thread.sleep(duracionMs);
            
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
            
            System.out.println("✅ Estilo original restaurado");
            
        } catch (Exception e) {
            System.err.println("⚠️ Error al aplicar borde personalizado: " + e.getMessage());
        }
    }
}
