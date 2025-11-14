package Avianca.Test;

import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

/**
 * 🚀 RUNNER PRINCIPAL
 * 
 * EJECUCIÓN:
 * - Desde IDE: Click derecho → Run
 * - Desde Maven: mvn clean verify
 * 
 * TAGS:
 * - @HU001: Eliminación de bloqueos
 * - @HU002: Creación de solicitud
 * - @HU003: Aprobación de solicitud
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    // 📁 Ubicación de los features
    features = {"src/test/resources/features"},
    
    // 🔗 Paquete donde están los step definitions
    glue = {"Avianca.Definitions"},
    
    // 🏷️ Tags a ejecutar (MODIFICAR SEGÚN NECESIDAD)
/* forma de ejecutar mas de una HU */
    // tags = "@HU001 or @HU002 or @HU003", 
    tags = "@HU007",
    
    // 📊 Reportes adicionales
    plugin = {
        "pretty",  // Salida colorida en consola
        "html:target/cucumber-reports/cucumber.html",  // Reporte HTML
        "json:target/cucumber-reports/cucumber.json",  // Reporte JSON
        "junit:target/cucumber-reports/cucumber.xml"   // Reporte JUnit
    },
    
    // 📝 Snippets en formato Camel Case
    snippets = CucumberOptions.SnippetType.CAMELCASE,
      
    // 🎨 Embellecer salida JSON
    monochrome = false
)
public class RunnersFeature {
    
    /*
     * OPCIONES DE TAGS:
     * 
     * 1. Ejecutar UNA HU:
     *    tags = "@HU001"
     * 
     * 2. Ejecutar MÚLTIPLES HU (OR):
     *    tags = "@HU001 or @HU002 or @HU003"
     * 
     * 3. Ejecutar HU con REGRESIÓN (AND):
     *    tags = "@Regresion and (@HU001 or @HU002)"
     * 
     * 4. Ejecutar TODO EXCEPTO una HU (NOT):
     *    tags = "@Regresion and not @HU003"
     * 
     * 5. Ejecutar SOLO SMOKE:
     *    tags = "@Smoke"
     * 
     * 6. Ejecutar CRÍTICOS:
     *    tags = "@Critico"
     * 
     * 7. Ejecutar ESTABLES (no WIP):
     *    tags = "@Regresion and not @WIP"
     * 
     * 8. Ejecutar MÓDULO ESPECÍFICO:
     *    tags = "@Bloqueos"
     * 
     * 9. Ejecutar COMBINACIÓN:
     *    tags = "(@Smoke or @Critico) and not @Skip"
     * 
     * 10. Ejecutar TODO:
     *     tags = ""  // Deja vacío
     */
    
}