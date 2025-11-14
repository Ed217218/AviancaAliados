package Avianca.Steps;

//import org.checkerframework.checker.units.qual.s;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.function.Supplier;




//import Avianca.Utils.ApiErrorCapture;
import Avianca.Utils.ElementInteractions;
import Avianca.Utils.JavascriptResaltaHtml;
//import net.serenitybdd.screenplay.waits.Wait;
import Avianca.Utils.CalendarUtil;
import Avianca.Utils.ElementFinder;
//import Avianca.Utils.BrowserMobProxyHelper;




public class ButtonBloqueoPages {
    
    // ===== Objetos =====
    private WebDriver driver;
    private WebDriverWait wait;
    private ElementInteractions elementInteractions;
    //private ApiErrorCapture apiErrorCapture;
    private CalendarUtil calendarUtil;
    private ElementFinder elementFinder;
 //   private BrowserMobProxyHelper proxyHelper;
    private JavascriptResaltaHtml resaltador; // 




    
    // ===== Xpath / Localizadores (MENUS / SUB-MENUS) =====
/**
 * 🔧 MÉTODO AUXILIAR: Encuentra "Administración de Bloqueos" (menú principal)
 */
private WebElement encontrarAdministracionDeBloqueos() {
    By[] localizadores = {
        By.id("horizontal-menu-item-104"),
        By.xpath("//span[@class='horizontal-menu-title' and text()='Administracion de Bloqueos']"),
        By.xpath("//span[text()='Administracion de Bloqueos']"),
        By.xpath("//*[contains(text(), 'Administracion de Bloqueos')]"),
        By.xpath("//a[@id='horizontal-menu-item-104']//span[@class='horizontal-menu-title']"),
        By.xpath("//mat-icon[text()='dashboard']/following-sibling::span//span[text()='Administracion de Bloqueos']"),
        By.cssSelector("a#horizontal-menu-item-104"),
        By.xpath("//a[@id='horizontal-menu-item-104' and following-sibling::div[@id='horizontal-sub-menu-104']]"),
        By.xpath("//div[@class='horizontal-menu-item w-100 ng-star-inserted']//a[@id='horizontal-menu-item-104']")
    };
    return elementFinder.encontrarElemento(localizadores);
}

/**
 * 🔧 MÉTODO AUXILIAR: Encuentra submenú "Bandeja de Solicitudes"
 */
private WebElement encontrarBandejaDeSolicitudes() {
    By[] localizadores = {
        By.id("horizontal-menu-item-82"),
        By.xpath("//span[@class='horizontal-menu-title' and text()='Bandeja de Solicitudes']"),
        By.xpath("//span[text()='Bandeja de Solicitudes']"),
        By.xpath("//a[@id='horizontal-menu-item-82']//span[@class='horizontal-menu-title']"),
        By.xpath("//a[@href='/Inbox/Index']"),
        By.xpath("//a[@href='/Inbox/Index']//span[text()='Bandeja de Solicitudes']"),
        By.cssSelector("a#horizontal-menu-item-82"),
        By.xpath("//div[@id='horizontal-sub-menu-104']//a[@id='horizontal-menu-item-82']"),
        By.xpath("//a[@id='horizontal-menu-item-82' and @routerlinkactive='active-link']")
    };
    return elementFinder.encontrarElemento(localizadores);
}

/**
 * 🔧 MÉTODO AUXILIAR: Encuentra submenú "Creación de Bloqueos"
 */
private WebElement encontrarCreacionDeBloqueos() {
    By[] localizadores = {
        By.id("horizontal-menu-item-41"),
        By.xpath("//span[@class='horizontal-menu-title' and text()='Creacion de Bloqueos']"),
        By.xpath("//span[text()='Creacion de Bloqueos']"),
        By.xpath("//a[@id='horizontal-menu-item-41']//span[@class='horizontal-menu-title']"),
        By.xpath("//a[@href='/BlockFlights/Index']"),
        By.xpath("//a[@href='/BlockFlights/Index']//span[text()='Creacion de Bloqueos']"),
        By.cssSelector("a#horizontal-menu-item-41"),
        By.xpath("//div[@id='horizontal-sub-menu-104']//a[@id='horizontal-menu-item-41']"),
        By.xpath("//a[@id='horizontal-menu-item-41' and @routerlinkactive='active-link']")
    };
    return elementFinder.encontrarElemento(localizadores);
}

/**
 * 🔧 MÉTODO AUXILIAR: Encuentra submenú "Bloqueos Creados"
 */
private WebElement encontrarBloqueosCreados() {
    By[] localizadores = {
        By.id("horizontal-menu-item-10"),
        By.xpath("//span[@class='horizontal-menu-title' and text()='Bloqueos Creados']"),
        By.xpath("//span[text()='Bloqueos Creados']"),
        By.xpath("//a[@id='horizontal-menu-item-10']//span[@class='horizontal-menu-title']"),
        By.xpath("//a[@href='/AnEditBae/Index']"),
        By.xpath("//a[@href='/AnEditBae/Index']//span[text()='Bloqueos Creados']"),
        By.cssSelector("a#horizontal-menu-item-10"),
        By.xpath("//div[@id='horizontal-sub-menu-104']//a[@id='horizontal-menu-item-10']"),
        By.xpath("//a[@id='horizontal-menu-item-10' and @routerlinkactive='active-link']"),
        By.xpath("//a[@id='horizontal-menu-item-10' and contains(@class, 'active-link')]")
    };
    return elementFinder.encontrarElemento(localizadores);
}

/**
 * 🔧 MÉTODO AUXILIAR: Encuentra submenú "Consulta de Bloqueos"
 */
private WebElement encontrarConsultaDeBloqueos() {
    By[] localizadores = {
        By.id("horizontal-menu-item-61"),
        By.xpath("//span[@class='horizontal-menu-title' and text()='Consulta de Bloqueos']"),
        By.xpath("//span[text()='Consulta de Bloqueos']"),
        By.xpath("//a[@id='horizontal-menu-item-61']//span[@class='horizontal-menu-title']"),
        By.xpath("//a[@href='/QueryBlocks/Index']"),
        By.xpath("//a[@href='/QueryBlocks/Index']//span[text()='Consulta de Bloqueos']"),
        By.cssSelector("a#horizontal-menu-item-61"),
        By.xpath("//div[@id='horizontal-sub-menu-104']//a[@id='horizontal-menu-item-61']"),
        By.xpath("//a[@id='horizontal-menu-item-61' and @routerlinkactive='active-link']")
    };
    return elementFinder.encontrarElemento(localizadores);
}

/**
 * 🔧 MÉTODO AUXILIAR: Encuentra submenú "Bloqueos" (ya implementado)
 */
private WebElement encontrarBloqueos() {
    By[] localizadores = {
        By.id("horizontal-menu-item-105"),
        By.xpath("//span[@class='horizontal-menu-title' and text()='Bloqueos']"),
        By.xpath("//span[text()='Bloqueos']"),
        By.xpath("//*[contains(text(), 'Bloqueos')]"),
        By.xpath("//a[@id='horizontal-menu-item-105']//span[@class='horizontal-menu-title']"),
        By.xpath("//a[@href='/TopEditBae/Index']"),
        By.xpath("//a[@href='/TopEditBae/Index']//span[text()='Bloqueos']"),
        By.xpath("//div[@id='horizontal-sub-menu-104']//a[@id='horizontal-menu-item-105']"),
        By.cssSelector("a#horizontal-menu-item-105"),
        By.xpath("//a[@id='horizontal-menu-item-105' and contains(@class, 'active-link')]")
    };
    return elementFinder.encontrarElemento(localizadores);
}

/**
 * 🔧 MÉTODO AUXILIAR: Encuentra submenú "Migración de bloqueos"
 */
private WebElement encontrarMigracionDeBloqueos() {
    By[] localizadores = {
        By.id("horizontal-menu-item-117"),
        By.xpath("//span[@class='horizontal-menu-title' and text()='Migración de bloqueos']"),
        By.xpath("//span[text()='Migración de bloqueos']"),
        By.xpath("//a[@id='horizontal-menu-item-117']//span[@class='horizontal-menu-title']"),
        By.xpath("//a[@href='/Migration/Index']"),
        By.xpath("//a[@href='/Migration/Index']//span[text()='Migración de bloqueos']"),
        By.cssSelector("a#horizontal-menu-item-117"),
        By.xpath("//div[@id='horizontal-sub-menu-104']//a[@id='horizontal-menu-item-117']"),
        By.xpath("//a[@id='horizontal-menu-item-117' and @routerlinkactive='active-link']")
    };
    return elementFinder.encontrarElemento(localizadores);
}

/**
 * 🔧 MÉTODO AUXILIAR: Encuentra submenú "Modificación Masiva de Negos"
 */
private WebElement encontrarModificacionMasivaDeNegos() {
    By[] localizadores = {
        By.id("horizontal-menu-item-132"),
        By.xpath("//span[@class='horizontal-menu-title' and text()='Modificación Masiva de Negos']"),
        By.xpath("//span[text()='Modificación Masiva de Negos']"),
        By.xpath("//a[@id='horizontal-menu-item-132']//span[@class='horizontal-menu-title']"),
        By.xpath("//a[@href='/ModMultipleNegos/Index']"),
        By.xpath("//a[@href='/ModMultipleNegos/Index']//span[text()='Modificación Masiva de Negos']"),
        By.cssSelector("a#horizontal-menu-item-132"),
        By.xpath("//div[@id='horizontal-sub-menu-104']//a[@id='horizontal-menu-item-132']"),
        By.xpath("//a[@id='horizontal-menu-item-132' and @routerlinkactive='active-link']")
    };
    return elementFinder.encontrarElemento(localizadores);
}




// ===== BOTONES DE ACCIÓN =====


/**
 * 🔧 MÉTODO AUXILIAR: Encuentra el tab "Búsqueda" con múltiples estrategias
 */
        private WebElement encontrarTabBusqueda() {
            By[] localizadores = {
                By.id("mat-tab-label-0-4"),
                By.xpath("//div[@role='tab' and @id='mat-tab-label-0-4']"),
                By.xpath("//div[@role='tab' and contains(@class, 'mat-mdc-tab')]//span[contains(text(), 'Busqueda')]"),
                By.xpath("//span[@class='mdc-tab__text-label']//span[contains(text(), 'Busqueda')]"),
                By.xpath("//mat-icon[text()='search']/parent::span/parent::span/parent::div[@role='tab']"),
                By.xpath("//div[contains(@class, 'mat-mdc-tab') and @aria-posinset='5']"),
                By.cssSelector("div#mat-tab-label-0-4.mat-mdc-tab"),
                By.xpath("//div[@role='tab' and @aria-controls='mat-tab-content-0-4']"),
                By.xpath("//div[@role='tab']//mat-icon[text()='search']/ancestor::div[@role='tab']"),
                By.xpath("//span[contains(@class, 'mdc-tab__text-label') and contains(., 'Busqueda')]/ancestor::div[@role='tab']")
            };
            return elementFinder.encontrarElemento(localizadores);
        }

        
        /**
 * 🔧 MÉTODO AUXILIAR: Encuentra el botón "Buscar" del formulario con múltiples estrategias
 */
        private WebElement encontrarBotonBuscar() {
            By[] localizadores = {
                By.xpath("//button[@type='submit' and contains(@color, 'accent')]//span[text()=' Buscar ']"),
                By.xpath("//button[@type='submit']//span[contains(text(), 'Buscar')]"),
                By.xpath("//button[@color='accent' and @type='submit']"),
                By.xpath("//button[contains(@class, 'mat-accent') and @type='submit']//span[text()=' Buscar ']"),
                By.xpath("//button[@mat-raised-button and @color='accent']//span[text()=' Buscar ']"),
                By.cssSelector("button[type='submit'][color='accent']"),
                By.xpath("//button[@type='submit' and contains(@class, 'mdc-button--raised')]//span[text()=' Buscar ']"),
                By.xpath("//button[@type='submit' and contains(@class, 'mat-mdc-raised-button')]"),
                By.xpath("//mat-card-content//button[@type='submit']"),
                By.xpath("//form//button[@type='submit' and @color='accent']")
            };
            return elementFinder.encontrarElemento(localizadores);
        }







    // Campo de fecha 
    @FindBy(how = How.XPATH, using = "//input[@formcontrolname='fecha']")
    private WebElement dateField;



        // Selector para el contenedor del calendario
    private final By calendarContainer = By.className("mat-datepicker-content-container");

        // Constructor
        public ButtonBloqueoPages(WebDriver driver) {
            this.driver = driver;
            // ✅ OPTIMIZACIÓN: Agregar polling interval de 100ms
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(3), Duration.ofMillis(100));
            this.elementInteractions = new ElementInteractions(driver);
       //     this.apiErrorCapture = new ApiErrorCapture(driver);
            this.resaltador = new JavascriptResaltaHtml(driver);  
            this.calendarUtil = new CalendarUtil(driver);
            this.elementFinder = new ElementFinder(driver, 5); // 5 segundos de espera
            PageFactory.initElements(driver, this);
        }





            public void clickAdminBloqueos() {
                try {            
                    System.out.println("🔍 Buscando elemento 'Administración de Bloqueos'...");
                    WebElement elemento = encontrarAdministracionDeBloqueos();
                    wait.until(ExpectedConditions.visibilityOf(elemento));
                    wait.until(ExpectedConditions.elementToBeClickable(elemento));

                    if (elemento != null) {
                        System.out.println("🔍 Elemento encontrado: " + elemento.getText());
                        System.out.println("✅ Elemento encontrado. Realizando acción...");
                        
                        // ✅ PASO 1: RESALTAR EL ELEMENTO EN VERDE
                        resaltador.resaltarElemento(elemento, 2000); // 2 segundos en verde 
                        
                        // ✅ PASO 2: Hacer scroll hasta el elemento
                        elementInteractions.scrollToElement(elemento);
                        
                        // ✅ PASO 3: Esperar que sea clickeable
                        wait.until(ExpectedConditions.elementToBeClickable(elemento));
                        
                        // ✅ PASO 4: Realizar el clic
                        realizarClicConMultiplesEstrategias(elemento);
                        
                        System.out.println("✅ Clic realizado en 'Administración de Bloqueos'");
                
                        // ✅ PASO 5: Esperar a que el submenú se despliegue
                        Thread.sleep(3000);

                        // ✅ PASO 6: Verificar que TODOS los submenús estén visibles (HÍBRIDO)
                        verificarTodosLosSubMenusHibrida();

                    } else {
                        throw new RuntimeException("❌ No se encontró el elemento 'Administración de Bloqueos'");
                    }

                } catch (Exception e) {
                    System.err.println("❌ Error en clic sobre 'Administración de Bloqueos': " + e.getMessage());
                    throw new RuntimeException("Fallo al interactuar con 'Administración de Bloqueos'", e);
                }
            }


    
            


                /**
                 * 🔧 MÉTODO AUXILIAR: Verifica que todos los submenús estén visibles (HÍBRIDO)
                 * 
                 * VENTAJAS DE ESTE ENFOQUE:
                 * - ✅ No requiere reflexión (más seguro)
                 * - ✅ El IDE valida los métodos en tiempo de compilación
                 * - ✅ Escalable: solo agregar una línea al mapa
                 * - ✅ Legible y profesional
                 * - ✅ Usa programación funcional (Supplier<T>)
                 * 
                 * @throws RuntimeException si ningún submenú está visible
                 */
                private void verificarTodosLosSubMenusHibrida() {
                    System.out.println("🔍 ====== INICIANDO VERIFICACIÓN HÍBRIDA DE SUBMENÚS ======");
                    
                    // ⭐ MAPA de métodos usando referencias de método (method references)
                    // LinkedHashMap mantiene el orden de inserción
                    Map<String, Supplier<WebElement>> metodosSubMenu = new LinkedHashMap<>();
                    metodosSubMenu.put("Bandeja de Solicitudes", this::encontrarBandejaDeSolicitudes);
                    metodosSubMenu.put("Creación de Bloqueos", this::encontrarCreacionDeBloqueos);
                    metodosSubMenu.put("Bloqueos Creados", this::encontrarBloqueosCreados);
                    metodosSubMenu.put("Consulta de Bloqueos", this::encontrarConsultaDeBloqueos);
                    metodosSubMenu.put("Bloqueos", this::encontrarBloqueos);
                    metodosSubMenu.put("Migración de bloqueos", this::encontrarMigracionDeBloqueos);
                    metodosSubMenu.put("Modificación Masiva de Negos", this::encontrarModificacionMasivaDeNegos);
                    
                    int subMenusVisibles = 0;
                    int total = metodosSubMenu.size();
                    int contador = 0;
                    
                    // ✅ Iterar sobre cada submenú
                    for (Map.Entry<String, Supplier<WebElement>> entry : metodosSubMenu.entrySet()) {
                        contador++;
                        String nombreSubMenu = entry.getKey();
                        Supplier<WebElement> metodoProveedor = entry.getValue();
                        
                        try {
                            // ⭐ Ejecutar el método dinámicamente usando .get()
                            WebElement elemento = metodoProveedor.get();
                            
                            // Validar si el elemento está visible
                            if (elemento != null && elemento.isDisplayed()) {
                                System.out.println("✅ [" + contador + "/" + total + "] Submenú '" + nombreSubMenu + "' visible");
                                System.out.println("   📝 Texto: " + elemento.getText());
                                
                                // Resaltar elemento por 800ms
                                try {
                                    resaltador.resaltarElemento(elemento, 800);
                                } catch (Exception e) {
                                    System.err.println("   ⚠️ No se pudo resaltar: " + e.getMessage());
                                }
                                
                                subMenusVisibles++;
                            } else {
                                System.out.println("❌ [" + contador + "/" + total + "] Submenú '" + nombreSubMenu + "' NO visible");
                            }
                            
                        } catch (Exception e) {
                            System.err.println("❌ [" + contador + "/" + total + "] Error con '" + nombreSubMenu + "': " + e.getMessage());
                        }
                    }
                    
                    // ✅ Imprimir resumen final
                    imprimirResumenValidacion(subMenusVisibles, total);
                }

                /**
                 * 🔧 MÉTODO AUXILIAR: Imprime el resumen de validación de submenús
                 * 
                 * @param visibles Cantidad de submenús visibles
                 * @param total Cantidad total de submenús
                 * @throws RuntimeException si ningún submenú está visible
                 */
                private void imprimirResumenValidacion(int visibles, int total) {
                    System.out.println("========================================");
                    System.out.println("📊 RESUMEN DE VERIFICACIÓN:");
                    System.out.println("   Submenús visibles: " + visibles + "/" + total);
                    
                    // Calcular porcentaje
                    double porcentaje = (visibles * 100.0) / total;
                    System.out.println("   Porcentaje: " + String.format("%.2f", porcentaje) + "%");
                    
                    // Evaluación final
                    if (visibles == total) {
                        System.out.println("✅✅✅ TODOS LOS SUBMENÚS ESTÁN VISIBLES");
                        System.out.println("🎉 Validación 100% exitosa");
                    } else if (visibles >= (total * 0.7)) {
                        System.out.println("⚠️ MAYORÍA DE SUBMENÚS VISIBLES (" + visibles + "/" + total + ")");
                        System.out.println("⚠️ Continúa con precaución");
                    } else if (visibles > 0) {
                        System.out.println("⚠️ SOLO " + visibles + " DE " + total + " SUBMENÚS VISIBLES");
                        System.out.println("❌ Validación parcial - verifica la configuración");
                    } else {
                        System.out.println("❌ NINGÚN SUBMENÚ VISIBLE");
                        System.out.println("❌ El menú no se desplegó correctamente");
                        throw new RuntimeException("❌ No se desplegó el submenú correctamente");
                    }
                    
                    System.out.println("====== FIN VERIFICACIÓN DE SUBMENÚS ======\n");
                }



                public void clickAdminBloqueosTourOperador() {
                    try {            
                        System.out.println("🔍 Buscando elemento 'Administración de Bloqueos'...");
                        WebElement elemento = encontrarAdministracionDeBloqueos();
                        wait.until(ExpectedConditions.visibilityOf(elemento));
                        wait.until(ExpectedConditions.elementToBeClickable(elemento));

                        if (elemento != null) {
                            System.out.println("🔍 Elemento encontrado: " + elemento.getText());
                            System.out.println("✅ Elemento encontrado. Realizando acción...");
                            
                            // ✅ PASO 1: RESALTAR EL ELEMENTO EN VERDE
                            resaltador.resaltarElemento(elemento, 2000); // 2 segundos en verde 
                            
                            // ✅ PASO 2: Hacer scroll hasta el elemento
                            elementInteractions.scrollToElement(elemento);
                            
                            // ✅ PASO 3: Esperar que sea clickeable
                            wait.until(ExpectedConditions.elementToBeClickable(elemento));
                            
                            // ✅ PASO 4: Realizar el clic
                            realizarClicConMultiplesEstrategias(elemento);
                            
                            System.out.println("✅ Clic realizado en 'Administración de Bloqueos'");
                    
                            // ✅ PASO 5: Esperar a que el submenú se despliegue
                            Thread.sleep(3000);

                        } else {
                            throw new RuntimeException("❌ No se encontró el elemento 'Administración de Bloqueos'");
                        }

                    } catch (Exception e) {
                        System.err.println("❌ Error en clic sobre 'Administración de Bloqueos': " + e.getMessage());
                        throw new RuntimeException("Fallo al interactuar con 'Administración de Bloqueos'", e);
                    }

                }   




       public void clickBloqueos() {
            try {            
                System.out.println("🔍 Buscando elemento 'Bloqueos'...");
                WebElement elemento = encontrarBloqueos();
                wait.until(ExpectedConditions.visibilityOf(elemento));
                wait.until(ExpectedConditions.elementToBeClickable(elemento));

                    if (elemento != null) {
                        System.out.println("🔍 Elemento encontrado: " + elemento.getText());
                        System.out.println("✅ Elemento encontrado. Realizando acción...");
                        
                        // ✅ PASO 1: RESALTAR EL ELEMENTO EN VERDE
                        resaltador.resaltarElemento(elemento, 2000); // 2 segundos en verde 
                        
                        // ✅ PASO 2: Hacer scroll hasta el elemento
                        elementInteractions.scrollToElement(elemento);
                        //resaltador.resaltarElemento(elemento);
                        
                        // ✅ PASO 3: Esperar que sea clickeable
                        wait.until(ExpectedConditions.elementToBeClickable(elemento));
                        
                        // ✅ PASO 4: Realizar el clic
                        realizarClicConMultiplesEstrategias(elemento);
                        
                        System.out.println("✅ Clic realizado en 'Bloqueos'");
            
                    } else {
                           throw new RuntimeException("❌ No se encontró el elemento 'Bloqueos'");
                    }

                // Realizar acción con el elemento encontrado
            } catch (Exception e) {
                System.err.println("❌ Error en clic sobre 'Bloqueos': " + e.getMessage());
                throw new RuntimeException("Fallo al interactuar con 'Bloqueos'", e);
            }
       }






       public void clickBloqueosCreados() {
            try {            
                System.out.println("🔍 Buscando elemento 'Bloqueos Creados'...");
                WebElement elemento = encontrarBloqueosCreados();
                wait.until(ExpectedConditions.visibilityOf(elemento));
                wait.until(ExpectedConditions.elementToBeClickable(elemento));

                    if (elemento != null) {
                        System.out.println("🔍 Elemento encontrado: " + elemento.getText());
                        System.out.println("✅ Elemento encontrado. Realizando acción...");
                        
                        // ✅ PASO 1: RESALTAR EL ELEMENTO EN Azul
                        resaltador.resaltarElemento(elemento, 2000); // 2 segundos en azul 
                        
                        // ✅ PASO 2: Hacer scroll hasta el elemento
                        elementInteractions.scrollToElement(elemento);
                        //resaltador.resaltarElemento(elemento);
                        
                        // ✅ PASO 3: Esperar que sea clickeable
                        wait.until(ExpectedConditions.elementToBeClickable(elemento));
                        
                        // ✅ PASO 4: Realizar el clic
                        realizarClicConMultiplesEstrategias(elemento);
                        
                        System.out.println("✅ Clic realizado en 'Bloqueos Creados'");
            
                    } else {
                           throw new RuntimeException("❌ No se encontró el elemento 'Bloqueos'");
                    }

                // Realizar acción con el elemento encontrado
            } catch (Exception e) {
                System.err.println("❌ Error en clic sobre 'Bloqueos Creados': " + e.getMessage());
                throw new RuntimeException("Fallo al interactuar con 'Bloqueos Creados'", e);
            }
       }





/**
 * 🎯 MÉTODO PRINCIPAL: Hace clic en el tab "Búsqueda" (abre el popup)
 */
    public void clickTabBusqueda() {
        try {
            System.out.println("🔍 Buscando tab 'Búsqueda'...");
            WebElement elemento = encontrarTabBusqueda();
            wait.until(ExpectedConditions.visibilityOf(elemento));
            wait.until(ExpectedConditions.elementToBeClickable(elemento));

            if (elemento != null) {
                System.out.println("🔍 Elemento encontrado: " + elemento.getText());
                System.out.println("✅ Elemento encontrado. Realizando acción...");
                
                // ✅ PASO 1: RESALTAR EL ELEMENTO
                resaltador.resaltarElemento(elemento, 2000); // 2 segundos resaltado
                
                // ✅ PASO 2: Hacer scroll hasta el elemento
                elementInteractions.scrollToElement(elemento);
                
                // ✅ PASO 3: Esperar que sea clickeable
                wait.until(ExpectedConditions.elementToBeClickable(elemento));
                
                // ✅ PASO 4: Realizar el clic
                realizarClicConMultiplesEstrategias(elemento);
                
                System.out.println("✅ Clic realizado en tab 'Búsqueda'");
                
                // ✅ PASO 5: Esperar a que el popup/modal se abra
                Thread.sleep(2000);
                
                // ✅ PASO 6: Verificar que el popup esté visible
                try {
                    WebElement modal = driver.findElement(By.xpath("//mat-card//mat-card-title[contains(text(), 'Consultar bloqueos')]"));
                    if (modal.isDisplayed()) {
                        System.out.println("✅ Popup 'Consultar bloqueos' visible");
                        resaltador.resaltarElemento(modal, 1500);
                    } else {
                        System.out.println("⚠️ Popup 'Consultar bloqueos' no visible");
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ No se pudo verificar el popup: " + e.getMessage());
                }

            } else {
                throw new RuntimeException("❌ No se encontró el tab 'Búsqueda'");
            }

        } catch (Exception e) {
            System.err.println("❌ Error en clic sobre tab 'Búsqueda': " + e.getMessage());
            throw new RuntimeException("Fallo al interactuar con tab 'Búsqueda'", e);
        }
    }


/**
 * 🎯 MÉTODO PRINCIPAL: Hace clic en el botón "Buscar" del formulario
 */
        public void clickBotonBuscar() {
            try {
                System.out.println("🔍 Buscando botón 'Buscar' del formulario...");
                WebElement elemento = encontrarBotonBuscar();
                wait.until(ExpectedConditions.visibilityOf(elemento));
                wait.until(ExpectedConditions.elementToBeClickable(elemento));

                if (elemento != null) {
                    System.out.println("🔍 Elemento encontrado: " + elemento.getText());
                    System.out.println("✅ Elemento encontrado. Realizando acción...");
                    
                    // ✅ PASO 1: RESALTAR EL ELEMENTO
                    resaltador.resaltarElemento(elemento, 2000); // 2 segundos resaltado
                    
                    // ✅ PASO 2: Hacer scroll hasta el elemento
                    elementInteractions.scrollToElement(elemento);
                    
                    // ✅ PASO 3: Esperar que sea clickeable
                    wait.until(ExpectedConditions.elementToBeClickable(elemento));
                    
                    // ✅ PASO 4: Realizar el clic
                    realizarClicConMultiplesEstrategias(elemento);
                    
                    System.out.println("✅ Clic realizado en botón 'Buscar'");
                    
                    // ✅ PASO 5: Esperar a que se ejecute la búsqueda
                    Thread.sleep(3000);

                } else {
                    throw new RuntimeException("❌ No se encontró el botón 'Buscar'");
                }

            } catch (Exception e) {
                System.err.println("❌ Error en clic sobre botón 'Buscar': " + e.getMessage());
                throw new RuntimeException("Fallo al interactuar con botón 'Buscar'", e);
            }
        }










        
    /**
     * 🎯 MÉTODO PRINCIPAL: Selecciona una fecha usando el calendario
     */
    public void selectDateFromCalendar(int day, int month, int year) {
        try {
            System.out.println("🗓️ Seleccionando fecha: " + day + "/" + month + "/" + year);
            LocalDate targetDate = LocalDate.of(year, month, day);
            calendarUtil.openCalendar(dateField);
            calendarUtil.selectDate(targetDate);
            System.out.println("✅ Fecha seleccionada correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar fecha del calendario: " + e.getMessage());
            throw new RuntimeException("No se pudo seleccionar la fecha del calendario", e);
        }
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Verifica si una fecha está disponible en el calendario
     */
    public boolean isDateAvailable(int day, int month, int year) {
        try {
            System.out.println("🔍 Verificando disponibilidad de fecha: " + day + "/" + month + "/" + year);
            LocalDate targetDate = LocalDate.of(year, month, day);
            calendarUtil.openCalendar(dateField);
            boolean available = calendarUtil.isDateAvailable(targetDate);
            calendarUtil.closeCalendar();
            System.out.println("✅ Disponibilidad de fecha: " + available);
            return available;
        } catch (Exception e) {
            System.err.println("❌ Error al verificar disponibilidad de fecha: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Selecciona fecha inicial usando el calendario
     */
    public void seleccionarFechaInicial(int day, int month, int year) {
        try {
            System.out.println("🗓️ Seleccionando fecha inicial: " + day + "/" + month + "/" + year);
            System.out.println("🔍 Abriendo calendario de fecha inicial...");
            abrirCalendarioFechaInicial();
            LocalDate targetDate = LocalDate.of(year, month, day);
            System.out.println("📅 Fecha objetivo creada: " + targetDate);
            System.out.println("🖱️ Seleccionando fecha en el calendario...");
            calendarUtil.selectDate(targetDate);
            System.out.println("✅ Fecha inicial seleccionada correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar fecha inicial: " + e.getMessage());
            throw new RuntimeException("No se pudo seleccionar la fecha inicial", e);
        }
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Abre el calendario de fecha inicial
     */
    public void abrirCalendarioFechaInicial() {
        try {
            System.out.println("🔍 Buscando botón de calendario de fecha inicial...");
            WebElement botonCalendario = encontrarBotonCalendarioFechaInicial();
            
            if (botonCalendario != null) {
                System.out.println("📜 Haciendo scroll al botón de calendario...");
                elementInteractions.scrollToElement(botonCalendario);
                System.out.println("⏳ Esperando a que el botón sea clickeable...");
                wait.until(ExpectedConditions.elementToBeClickable(botonCalendario));
                System.out.println("🖱️ Intentando hacer clic en el botón...");
                if (!elementInteractions.intentarClicNormal(botonCalendario)) {
                    if (!elementInteractions.intentarClicConHoverYClick(botonCalendario)) {
                        if (!elementInteractions.intentarClickConJavaScript(botonCalendario)) {
                            throw new RuntimeException("❌ No se pudo hacer clic en el botón de calendario de fecha inicial");
                        }
                    }
                }
                System.out.println("✅ Clic realizado en el botón de calendario de fecha inicial");
                System.out.println("⏳ Esperando a que el calendario se abra...");
                Thread.sleep(1000);
                try {
                    WebElement calendarElement = driver.findElement(calendarContainer);
                    if (calendarElement.isDisplayed()) {
                        System.out.println("✅ Calendario de fecha inicial está visible");
                    } else {
                        throw new RuntimeException("❌ El calendario de fecha inicial no está visible");
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error al verificar visibilidad del calendario: " + e.getMessage());
                    throw e;
                }
            } else {
                throw new RuntimeException("❌ No se encontró el botón de calendario de fecha inicial");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al abrir calendario de fecha inicial: " + e.getMessage());
            throw new RuntimeException("Fallo al abrir calendario de fecha inicial", e);
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra el botón de calendario de fecha inicial con múltiples estrategias
     */
    private WebElement encontrarBotonCalendarioFechaInicial() {
        By[] localizadores = {
            By.cssSelector("mat-datepicker-toggle[data-mat-calendar='mat-datepicker-0'] button"),
            By.xpath("(//button[@aria-label='Open calendar'])[1]"),
            By.xpath("(//mat-datepicker-toggle/following-sibling::div//button)[1]"),
            By.xpath("//div[contains(@class, 'ng-tns-c1798928316-19')]//button[@aria-label='Open calendar']"),
            By.xpath("(//button[@aria-label='Open calendar']//svg)[1]/ancestor::button"),
            By.xpath("(//mat-datepicker-toggle)[1]//button"),
            By.xpath("(//div[contains(@class, 'mat-mdc-form-field-icon-suffix')])[1]//button"),
            By.xpath("(//button[contains(@class, 'mat-icon-button') and @aria-label='Open calendar'])[1]")
        };
        return elementFinder.encontrarElemento(localizadores);
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Selecciona fecha final usando el calendario
     */
    public void seleccionarFechaFinal(int day, int month, int year) {
        try {
            System.out.println("🗓️ Seleccionando fecha final: " + day + "/" + month + "/" + year);
            System.out.println("🔍 Abriendo calendario de fecha final...");
            abrirCalendarioFechaFinal();
            LocalDate targetDate = LocalDate.of(year, month, day);
            System.out.println("📅 Fecha objetivo creada: " + targetDate);
            System.out.println("🖱️ Seleccionando fecha en el calendario...");
            calendarUtil.selectDate(targetDate);
            System.out.println("✅ Fecha final seleccionada correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar fecha final: " + e.getMessage());
            throw new RuntimeException("No se pudo seleccionar la fecha final", e);
        }
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Abre el calendario de fecha final
     */
    public void abrirCalendarioFechaFinal() {
        try {
            System.out.println("🔍 Buscando botón de calendario de fecha final...");
            WebElement botonCalendario = encontrarBotonCalendarioFechaFinal();
            
            if (botonCalendario != null) {
                System.out.println("📜 Haciendo scroll al botón de calendario...");
                elementInteractions.scrollToElement(botonCalendario);
                System.out.println("⏳ Esperando a que el botón sea clickeable...");
                wait.until(ExpectedConditions.elementToBeClickable(botonCalendario));
                System.out.println("🖱️ Intentando hacer clic en el botón...");
                if (!elementInteractions.intentarClicNormal(botonCalendario)) {
                    if (!elementInteractions.intentarClicConHoverYClick(botonCalendario)) {
                        if (!elementInteractions.intentarClickConJavaScript(botonCalendario)) {
                            throw new RuntimeException("❌ No se pudo hacer clic en el botón de calendario de fecha final");
                        }
                    }
                }
                System.out.println("✅ Clic realizado en el botón de calendario de fecha final");
                System.out.println("⏳ Esperando a que el calendario se abra...");
                Thread.sleep(1000);
                try {
                    WebElement calendarElement = driver.findElement(calendarContainer);
                    if (calendarElement.isDisplayed()) {
                        System.out.println("✅ Calendario de fecha final está visible");
                    } else {
                        throw new RuntimeException("❌ El calendario de fecha final no está visible");
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error al verificar visibilidad del calendario: " + e.getMessage());
                    throw e;
                }
            } else {
                throw new RuntimeException("❌ No se encontró el botón de calendario de fecha final");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al abrir calendario de fecha final: " + e.getMessage());
            throw new RuntimeException("Fallo al abrir calendario de fecha final", e);
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra el botón de calendario de fecha final con múltiples estrategias
     */
    private WebElement encontrarBotonCalendarioFechaFinal() {
        By[] localizadores = {
            By.cssSelector("mat-datepicker-toggle[data-mat-calendar='mat-datepicker-1'] button"),
            By.xpath("(//button[@aria-label='Open calendar'])[2]"),
            By.xpath("(//mat-datepicker-toggle/following-sibling::div//button)[2]"),
            By.xpath("//div[contains(@class, 'ng-tns-c1798928316-20')]//button[@aria-label='Open calendar']"),
            By.xpath("(//button[@aria-label='Open calendar']//svg)[2]/ancestor::button"),
            By.xpath("(//mat-datepicker-toggle)[2]//button"),
            By.xpath("(//div[contains(@class, 'mat-mdc-form-field-icon-suffix')])[2]//button"),
            By.xpath("(//button[contains(@class, 'mat-icon-button') and @aria-label='Open calendar'])[2]")
        };
        return elementFinder.encontrarElemento(localizadores);
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Selecciona un rango de fechas
     */
    public void seleccionarRangoFechas(int startDay, int startMonth, int startYear, 
                                    int endDay, int endMonth, int endYear) {
        try {
            System.out.println("🗓️ Seleccionando rango de fechas:");
            System.out.println("   - Inicio: " + startDay + "/" + startMonth + "/" + startYear);
            System.out.println("   - Fin: " + endDay + "/" + endMonth + "/" + endYear);
            
            System.out.println("🔄 Iniciando selección de fecha inicial...");
            seleccionarFechaInicial(startDay, startMonth, startYear);
            
            System.out.println("⏳ Esperando a que el calendario inicial se cierre...");
            Thread.sleep(500); // Espera para asegurar que el calendario se cierre
            
            System.out.println("🔄 Iniciando selección de fecha final...");
            seleccionarFechaFinal(endDay, endMonth, endYear);
            
            System.out.println("✅ Rango de fechas seleccionado correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar rango de fechas: " + e.getMessage());
            throw new RuntimeException("No se pudo seleccionar el rango de fechas", e);
        }
    }





/**
 * 🔧 MÉTODO PÚBLICO: Busca filas con estado Aprobado (#6BCF00)
 * 
 * @return Lista de WebElement con las filas encontradas, o null si no hay
 */
        public List<WebElement> buscarFilasConEstadoAprobado() {
            By[] localizadores = {
                By.xpath("//mat-row[.//mat-card[@style='background-color: #6BCF00;']]"),
                By.xpath("//mat-row[.//mat-cell[contains(@class, 'mat-column-Estado')]//mat-card[contains(@style, '#6BCF00')]]"),
                By.xpath("//mat-row[.//mat-cell//mat-card[contains(@style, 'background-color: #6BCF00')]]"),
                By.xpath("//mat-row[descendant::mat-card[contains(@style, '6BCF00')]]"),
                By.cssSelector("mat-row:has(mat-card[style*='#6BCF00'])"),
                By.xpath("//mat-row[.//mat-card[contains(@style, '6BCF00') and contains(@style, 'background')]]"),
                By.xpath("//mat-table//mat-row[.//mat-cell[@class='mat-mdc-cell mdc-data-table__cell cdk-cell cdk-column-Estado mat-column-Estado ng-star-inserted']//mat-card[@style='background-color: #6BCF00;']]")
            };
            
            try {
                WebElement primeraFila = elementFinder.encontrarElemento(localizadores);
                if (primeraFila != null) {
                    List<WebElement> todasLasFilas = driver.findElements(localizadores[0]);
                    System.out.println("✅ Se encontraron " + todasLasFilas.size() + " filas con estado Aprobado (#6BCF00)");
                    return todasLasFilas;
                }
            } catch (Exception e) {
                System.err.println("⚠️ No se encontraron filas con estado Aprobado (#6BCF00)");
            }
            return null;
        }

/**
 * 🔧 MÉTODO PÚBLICO: Navega a la siguiente página usando el paginador
 * 
 * @return true si la navegación fue exitosa, false si no hay más páginas
 */
        public boolean navegarSiguientePagina() {
            By[] localizadores = {
                By.xpath("//button[contains(@class, 'mat-mdc-paginator-navigation-next') and not(@disabled)]"),
                By.xpath("//button[@aria-label='Siguiente página' and not(@disabled)]"),
                By.cssSelector("button.mat-mdc-paginator-navigation-next:not([disabled])"),
                By.xpath("//button[contains(@class, 'mat-mdc-paginator-navigation-next')]//svg[contains(@class, 'mat-mdc-paginator-icon')]"),
                By.xpath("//mat-paginator[@id='paginatorBusqueda']//button[contains(@class, 'navigation-next') and not(@disabled)]")
            };
            
            try {
                WebElement botonSiguiente = elementFinder.encontrarElemento(localizadores);
                if (botonSiguiente != null && botonSiguiente.isEnabled()) {
                    System.out.println("🔄 Haciendo clic en 'Siguiente página'...");
                    resaltador.resaltarElemento(botonSiguiente, 1000);
                    realizarClicConMultiplesEstrategias(botonSiguiente);
                    return true;
                }
            } catch (Exception e) {
                System.err.println("❌ No hay más páginas disponibles");
            }
            return false;
        }

/**
 * 🔧 MÉTODO PÚBLICO: Hace clic en el botón "edit" dentro de una fila
 * 
 * @param fila WebElement de la fila que contiene el botón edit
 */
        public void hacerClicEnEditarFila(WebElement fila) {
            try {
                System.out.println("🎯 Seleccionando fila para editar...");
                resaltador.resaltarElemento(fila, 2000);
                
                By[] localizadores = {
                    By.xpath(".//mat-cell[contains(@class, 'mat-column-Editar')]//mat-icon[text()='edit']"),
                    By.xpath(".//mat-icon[text()='edit']"),
                    By.xpath(".//mat-cell[contains(@class, 'cdk-column-Editar')]//a//mat-icon"),
                    By.xpath(".//a[@class='draggable p-3']//mat-icon[text()='edit']"),
                    By.cssSelector("mat-cell.mat-column-Editar a mat-icon")
                };
                
                WebElement botonEditar = null;
                for (By locator : localizadores) {
                    try {
                        botonEditar = fila.findElement(locator);
                        if (botonEditar != null) break;
                    } catch (Exception ignored) {}
                }
                
                if (botonEditar != null) {
                    System.out.println("🖱️ Haciendo clic en botón 'edit'...");
                    elementInteractions.scrollToElement(botonEditar);
                    resaltador.resaltarElemento(botonEditar, 1500);
                    realizarClicConMultiplesEstrategias(botonEditar);
                    System.out.println("✅ Clic realizado en 'edit'");
                } else {
                    throw new RuntimeException("❌ No se encontró el botón 'edit' en la fila");
                }
            } catch (Exception e) {
                System.err.println("❌ Error al hacer clic en editar fila: " + e.getMessage());
                throw new RuntimeException("Fallo al hacer clic en editar fila", e);
            }
        }

/**
 * 🔧 MÉTODO PÚBLICO: Verifica si el popup de edición está visible
 * 
 * @return true si el popup está visible, false en caso contrario
 */
        public boolean verificarPopupEditarVisible() {
            By[] localizadores = {
                By.xpath("//mat-card-subtitle[contains(text(), 'Bloqueo')]//p[contains(text(), 'S')]"),
                By.xpath("//mat-card-subtitle//p[@style='color: #C52727;']"),
                By.xpath("//mat-tab-group//mat-tab-body[contains(@class, 'mat-mdc-tab-body-active')]"),
                By.xpath("//div[@role='tab' and contains(text(), 'Editar Nego')]"),
                By.xpath("//form[@name='form']//mat-form-field//input[@name='asiAdicionales']")
            };
            
            try {
                WebElement popup = elementFinder.encontrarElemento(localizadores);
                boolean visible = popup != null && popup.isDisplayed();
                if (visible) {
                    System.out.println("✅ Popup de edición está visible");
                }
                return visible;
            } catch (Exception e) {
                System.err.println("❌ Popup de edición no está visible");
                return false;
            }
        }

/**
 * 🔧 MÉTODO PÚBLICO: Extrae N° Solicitud y RecLoc del popup
 * 
 * @return Array con [N° Solicitud, RecLoc], ej: ["S80812", "AVVVTZ"]
 */
        public String[] extraerDatosBloqueoDelPopup() {
            By[] localizadores = {
                By.xpath("//mat-card-subtitle[contains(text(), 'Bloqueo')]//p[@style='color: #C52727;']"),
                By.xpath("//mat-card-subtitle//p[contains(@style, 'C52727')]"),
                By.xpath("//mat-card-header//mat-card-subtitle//p"),
                By.cssSelector("mat-card-subtitle p[style*='#C52727']")
            };
            
            try {
                WebElement elemento = elementFinder.encontrarElemento(localizadores);
                if (elemento != null) {
                    String textoCompleto = elemento.getText().trim();
                    System.out.println("📋 Texto completo del bloqueo: " + textoCompleto);
                    
                    if (textoCompleto.contains("-")) {
                        String[] partes = textoCompleto.split("-");
                        String nSolicitud = partes[0].trim();
                        String recLoc = partes[1].trim();
                        
                        System.out.println("✅ Datos extraídos → N° Solicitud: " + nSolicitud + " | RecLoc: " + recLoc);
                        return new String[]{nSolicitud, recLoc};
                    }
                }
            } catch (Exception e) {
                System.err.println("❌ Error al extraer datos del popup: " + e.getMessage());
            }
            return null;
        }

/**
 * 🔧 MÉTODO PÚBLICO: Ingresa valor en el campo "Asientos adicionales"
 * 
 * @param asientos Valor a ingresar en el campo
 */
        public void ingresarAsientosAdicionales(String asientos) {
            By[] localizadores = {
                By.xpath("//input[@name='asiAdicionales']"),
                By.xpath("//input[@formcontrolname='asiAdicionales']"),
                By.xpath("//input[@placeholder='Asientos adicionales']"),
                By.id("mat-input-7"),
                By.xpath("//mat-label[text()='Asientos adicionales']/ancestor::mat-form-field//input")
            };
            
            try {
                WebElement campo = elementFinder.encontrarElemento(localizadores);
                if (campo != null) {
                    System.out.println("📝 Ingresando valor en 'Asientos adicionales': " + asientos);
                    elementInteractions.scrollToElement(campo);
                    resaltador.resaltarElemento(campo, 1500);
                    campo.clear();
                    campo.sendKeys(asientos);
                    System.out.println("✅ Valor ingresado correctamente");
                } else {
                    throw new RuntimeException("❌ No se encontró el campo 'Asientos adicionales'");
                }
            } catch (Exception e) {
                System.err.println("❌ Error al ingresar asientos adicionales: " + e.getMessage());
                throw new RuntimeException("Fallo al ingresar asientos adicionales", e);
            }
        }




/**
 * 🔧 MÉTODO PÚBLICO: Ingresa valor en el campo "Liberar asientos"
 * 
 * @param asientos Valor a ingresar en el campo
 */
public void ingresarLiberarAsientos(String asientos) {
    By[] localizadores = {
        By.xpath("//input[@name='libAsientos']"),
        By.xpath("//input[@formcontrolname='libAsientos']"),
        By.xpath("//input[@placeholder='Liberar asientos']"),
        By.id("mat-input-40"),
        By.xpath("//mat-label[text()='Liberar asientos']/ancestor::mat-form-field//input"),
        By.xpath("//input[@maxlength='2' and @name='libAsientos']"),
        By.cssSelector("input[name='libAsientos']"),
        By.xpath("//mat-form-field//input[@formcontrolname='libAsientos' and @required]")
    };
    
    try {
        WebElement campo = elementFinder.encontrarElemento(localizadores);
        if (campo != null) {
            System.out.println("📝 Ingresando valor en 'Liberar asientos': " + asientos);
            elementInteractions.scrollToElement(campo);
            resaltador.resaltarElemento(campo, 1500);
            campo.clear();
            campo.sendKeys(asientos);
            System.out.println("✅ Valor ingresado correctamente en 'Liberar asientos'");
        } else {
            throw new RuntimeException("❌ No se encontró el campo 'Liberar asientos'");
        }
    } catch (Exception e) {
        System.err.println("❌ Error al ingresar liberar asientos: " + e.getMessage());
        throw new RuntimeException("Fallo al ingresar liberar asientos", e);
    }
}

















/**
 * 🔧 MÉTODO PÚBLICO: Guarda los cambios en el popup
 */
        public void guardarCambiosBloqueo() {
            By[] localizadores = {
                By.xpath("//button[@type='submit' and contains(@color, 'accent')]//span[text()=' Guardar cambios ']"),
                By.xpath("//button[@type='submit']//span[contains(text(), 'Guardar cambios')]"),
                By.xpath("//button[@color='accent' and @type='submit']"),
                By.cssSelector("button[type='submit'][color='accent']")
            };
            
            try {
                WebElement boton = elementFinder.encontrarElemento(localizadores);
                if (boton != null) {
                    System.out.println("💾 Haciendo clic en 'Guardar cambios'...");
                    elementInteractions.scrollToElement(boton);
                    resaltador.resaltarElemento(boton, 1500);
                    realizarClicConMultiplesEstrategias(boton);
                    Thread.sleep(5000); // Esperar procesamiento
                    System.out.println("✅ Cambios guardados");
                } else {
                    throw new RuntimeException("❌ No se encontró el botón 'Guardar cambios'");
                }
            } catch (Exception e) {
                System.err.println("❌ Error al guardar cambios: " + e.getMessage());
                throw new RuntimeException("Fallo al guardar cambios", e);
            }
        }

/**
 * 🔧 MÉTODO PÚBLICO: Cierra el popup de edición
 */
        public void cerrarPopupEdicion() {
            By[] localizadores = {
                By.xpath("//button[@mat-dialog-close and @color='primary']//span[text()=' Cancelar ']"),
                By.xpath("//button[@type='button' and @color='primary']//span[contains(text(), 'Cancelar')]"),
                By.xpath("//button[@mat-dialog-close]//span[text()=' Cancelar ']"),
                By.cssSelector("button[mat-dialog-close][color='primary']")
            };
            
            try {
                WebElement boton = elementFinder.encontrarElemento(localizadores);
                if (boton != null) {
                    System.out.println("🚪 Cerrando popup...");
                    resaltador.resaltarElemento(boton, 1000);
                    realizarClicConMultiplesEstrategias(boton);
                    Thread.sleep(2000);
                    System.out.println("✅ Popup cerrado");
                }
            } catch (Exception e) {
                System.err.println("⚠️ No se pudo cerrar el popup: " + e.getMessage());
            }
        }











/**
 * 🔧 MÉTODO PÚBLICO REFACTORIZADO: Busca una fila por N° Solicitud y RecLoc
 * Valida el color de forma OPCIONAL y reporta el estado encontrado
 * 
 * @param nSolicitud N° de Solicitud a buscar (OBLIGATORIO)
 * @param recLoc RecLoc a buscar (OBLIGATORIO)
 * @param colorEsperado Color esperado en formato hex (OPCIONAL, puede ser null)
 * @return true si encuentra la fila, false en caso contrario
 */
public boolean validarFilaConDeteccionDeEstado(String nSolicitud, String recLoc, String colorEsperado) {
    System.out.println("🔍 ====== INICIANDO BUSQUEDA DE FILA ======");
    System.out.println("   📋 Parametros de bUsqueda:");
    System.out.println("      • N° Solicitud: " + nSolicitud);
    System.out.println("      • RecLoc: " + recLoc);
    System.out.println("      • Color esperado: " + (colorEsperado != null ? colorEsperado : "N/A (deteccion automatica)"));
    
    // ✅ PASO 1: Localizadores SIN filtro de color (búsqueda por nSolicitud y recLoc únicamente)
    By[] localizadores = {
        // Localizador exacto
        By.xpath("//mat-row[.//mat-cell[text()='" + nSolicitud + "'] and .//mat-cell//p[contains(text(), '" + recLoc + "')]]"),
        // Localizador flexible con contains
        By.xpath("//mat-row[.//mat-cell[contains(text(), '" + nSolicitud + "')] and .//mat-cell[contains(., '" + recLoc + "')]]"),
        // Localizador muy flexible
        By.xpath("//mat-row[contains(., '" + nSolicitud + "') and contains(., '" + recLoc + "')]"),
        // Localizador con estructura Material Design específica
        By.xpath("//mat-row[.//mat-cell[contains(@class, 'mat-column-nRequest') and contains(text(), '" + nSolicitud + "')] and .//mat-cell[contains(@class, 'mat-column-RecLoc') and contains(., '" + recLoc + "')]]")
    };
    
    try {
        // ✅ PASO 2: Buscar la fila SIN importar el color
        WebElement fila = elementFinder.encontrarElemento(localizadores);
        
        if (fila != null) {
            System.out.println("✅ Fila encontrada con N° Solicitud: " + nSolicitud + " y RecLoc: " + recLoc);
            
            // ✅ PASO 3: Resaltar la fila completa
            elementInteractions.scrollToElement(fila);
            resaltador.resaltarConParpadeo(fila, 3);
            
            // ✅ PASO 4: Detectar el color del estado (OPCIONAL)
            String colorDetectado = detectarColorEstado(fila);
            String estadoDetectado = mapearColorAEstado(colorDetectado);
            
            // ✅ PASO 5: Reportar el color y estado detectados
            if (colorDetectado != null) {
                System.out.println("🎨 Color detectado: " + colorDetectado);
                System.out.println("📊 Estado detectado: " + estadoDetectado);
                
                // Si se especificó un color esperado, validar coincidencia
                if (colorEsperado != null) {
                    if (colorDetectado.equalsIgnoreCase(colorEsperado)) {
                        System.out.println("✅ El color detectado COINCIDE con el esperado");
                    } else {
                        System.out.println("⚠️ ADVERTENCIA: Color esperado era '" + colorEsperado + "' pero se encontró '" + colorDetectado + "'");
                    }
                }
            } else {
                System.out.println("⚠️ No se pudo detectar el color del estado");
            }
            
            // ✅ PASO 6: Resaltar celdas específicas
            try {
                WebElement celdaSolicitud = fila.findElement(By.xpath(".//mat-cell[contains(@class, 'mat-column-nRequest')]"));
                WebElement celdaRecLoc = fila.findElement(By.xpath(".//mat-cell[contains(@class, 'mat-column-RecLoc')]"));
                
                resaltador.resaltarElemento(celdaSolicitud, 3000);
                resaltador.resaltarElemento(celdaRecLoc, 3000);
                
                System.out.println("🎯 N° Solicitud y RecLoc resaltados con éxito");
            } catch (Exception e) {
                System.err.println("⚠️ No se pudieron resaltar las celdas individuales: " + e.getMessage());
            }
            
            System.out.println("====== FIN BÚSQUEDA DE FILA ======\n");
            return true;
            
        } else {
            System.out.println("❌ No se encontró ninguna fila con N° Solicitud: " + nSolicitud + " y RecLoc: " + recLoc);
            return false;
        }
        
    } catch (Exception e) {
        System.err.println("❌ Error al buscar la fila: " + e.getMessage());
        return false;
    }
}

/**
 * 🔧 MÉTODO AUXILIAR: Detecta el color del estado en una fila
 * 
 * @param fila WebElement de la fila a analizar
 * @return String con el código hexadecimal del color, o null si no se detecta
 */
private String detectarColorEstado(WebElement fila) {
    try {
        // Buscar el mat-card con background-color dentro de la fila
        WebElement matCard = fila.findElement(By.xpath(".//mat-card[@style]"));
        
        if (matCard != null) {
            String styleAttribute = matCard.getAttribute("style");
            
            // Extraer el color del atributo style
            if (styleAttribute != null && styleAttribute.contains("background-color")) {
                // Ejemplo: "background-color: #6BCF00;" -> "#6BCF00"
                String[] parts = styleAttribute.split("background-color:");
                if (parts.length > 1) {
                    String colorPart = parts[1].trim();
                    // Extraer solo el código hexadecimal
                    if (colorPart.contains(";")) {
                        colorPart = colorPart.substring(0, colorPart.indexOf(";"));
                    }
                    return colorPart.trim(); // Ej: "#6BCF00"
                }
            }
        }
    } catch (Exception e) {
        System.err.println("⚠️ Error al detectar color: " + e.getMessage());
    }
    return null;
}

/**
 * 🔧 MÉTODO AUXILIAR: Mapea un código de color hexadecimal a su estado correspondiente
 * 
 * @param colorHex Código hexadecimal del color (ej: "#6BCF00")
 * @return String con el nombre del estado
 */
private String mapearColorAEstado(String colorHex) {
    if (colorHex == null) {
        return "Desconocido";
    }
    
    // Normalizar el color (convertir a mayúsculas y quitar espacios)
    String colorNormalizado = colorHex.toUpperCase().trim();
    
    // Mapa de colores a estados
    switch (colorNormalizado) {
        case "#6BCF00":
            return "Aprobado";
        case "#FFD414":
            return "Revision";   
        case "#14D1FF":
            return "Modificado";            
        case "#F63913":
            return "Cancelado";            
        case "#A600CF":
            return "Fallido";
        default:
            return "Estado Desconocido (" + colorHex + ")";
    }
}

/**
 * 🔧 MÉTODO PÚBLICO: Wrapper para validar color AMARILLO específicamente
 * (Mantiene compatibilidad con código existente)
 * 
 * @param nSolicitud N° de Solicitud a buscar
 * @param recLoc RecLoc a buscar
 * @return true si encuentra la fila, false en caso contrario
 */
public boolean validarFilaConColorAmarillo(String nSolicitud, String recLoc) {
    System.out.println("🟡 Validación específica: Esperando color AMARILLO");
    return validarFilaConDeteccionDeEstado(nSolicitud, recLoc, "#FFD414");
}

/**
 * 🔧 MÉTODO PÚBLICO: Wrapper para validar color AZUL específicamente
 * (Mantiene compatibilidad con código existente)
 * 
 * @param nSolicitud N° de Solicitud a buscar
 * @param recLoc RecLoc a buscar
 * @return true si encuentra la fila, false en caso contrario
 */
public boolean validarFilaConColorAzul(String nSolicitud, String recLoc) {
    System.out.println("🔵 Validación específica: Esperando color AZUL");
    return validarFilaConDeteccionDeEstado(nSolicitud, recLoc, "#14D1FF");
}

/**
 * 🔧 MÉTODO PÚBLICO: Busca fila SIN validar color (detección automática)
 * 
 * @param nSolicitud N° de Solicitud a buscar
 * @param recLoc RecLoc a buscar
 * @return true si encuentra la fila, false en caso contrario
 */
public boolean buscarFilaPorSolicitudYRecLoc(String nSolicitud, String recLoc) {
    System.out.println("🔍 Busqueda generica: Sin validar color especifico");
    return validarFilaConDeteccionDeEstado(nSolicitud, recLoc, null);
}



























/**
 * 🔧 MÉTODO PÚBLICO: Valida que la fila tiene color amarillo (#F6B113 o #FFD414)
 * 
 * @param nSolicitud N° de Solicitud a buscar
 * @param recLoc RecLoc a buscar
 * @return true si encuentra la fila con color amarillo, false en caso contrario
 
        public boolean validarFilaConColorAmarillo(String nSolicitud, String recLoc) {
            By[] localizadores = {
                By.xpath("//mat-row[.//mat-cell[text()='" + nSolicitud + "'] and .//mat-cell//p[contains(text(), '" + recLoc + "')] and (.//mat-card[@style='background-color: #F6B113;'] or .//mat-card[@style='background-color: #FFD414;'])]"),
                By.xpath("//mat-row[.//mat-cell[contains(text(), '" + nSolicitud + "')] and .//mat-cell[contains(., '" + recLoc + "')] and .//mat-card[contains(@style, 'F6B113') or contains(@style, 'FFD414')]]"),
                By.xpath("//mat-row[contains(., '" + nSolicitud + "') and contains(., '" + recLoc + "') and (.//mat-card[contains(@style, '#F6B113')] or .//mat-card[contains(@style, '#FFD414')])]")
            };
            
            try {
                WebElement fila = elementFinder.encontrarElemento(localizadores);
                if (fila != null) {
                    System.out.println("✅✅✅ ¡ÉXITO! Fila encontrada con estado Amarillo (#F6B113 o #FFD414)");
                    elementInteractions.scrollToElement(fila);
                    resaltador.resaltarConParpadeo(fila, 3);
                    
                    // Resaltar N° Solicitud y RecLoc específicamente
                    try {
                        WebElement celdaSolicitud = fila.findElement(By.xpath(".//mat-cell[contains(@class, 'mat-column-nRequest')]"));
                        WebElement celdaRecLoc = fila.findElement(By.xpath(".//mat-cell[contains(@class, 'mat-column-RecLoc')]"));
                        
                        resaltador.resaltarElemento(celdaSolicitud, 3000);
                        resaltador.resaltarElemento(celdaRecLoc, 3000);
                        
                        System.out.println("🎉 N° Solicitud y RecLoc resaltados con éxito");
                    } catch (Exception e) {
                        System.err.println("⚠️ No se pudieron resaltar las celdas individuales");
                    }
                    
                    return true;
                }
            } catch (Exception e) {
                System.err.println("❌ No se encontró la fila con color amarillo");
            }
            return false;
        }

*/




/**
 * 🔧 MÉTODO PÚBLICO: Valida que la fila tiene color azul (#14D1FF)
 * 
 * @param nSolicitud N° de Solicitud a buscar
 * @param recLoc RecLoc a buscar
 * @return true si encuentra la fila con color azul, false en caso contrario
 
        public boolean validarFilaConColorAzul(String nSolicitud, String recLoc) {
            By[] localizadores = {
                By.xpath("//mat-row[.//mat-cell[text()='" + nSolicitud + "'] and .//mat-cell//p[contains(text(), '" + recLoc + "')] and .//mat-card[@style='background-color: #14D1FF;']]"),
                By.xpath("//mat-row[.//mat-cell[contains(text(), '" + nSolicitud + "')] and .//mat-cell[contains(., '" + recLoc + "')] and .//mat-card[contains(@style, '14D1FF')]]"),
                By.xpath("//mat-row[contains(., '" + nSolicitud + "') and contains(., '" + recLoc + "') and .//mat-card[contains(@style, '#14D1FF')]]"),
                By.xpath("//mat-row[.//mat-cell[text()='" + nSolicitud + "'] and .//mat-cell//p[contains(text(), '" + recLoc + "')] and .//mat-card[contains(@style, 'background-color: #14D1FF')]]")
            };
            
            try {
                WebElement fila = elementFinder.encontrarElemento(localizadores);
                if (fila != null) {
                    System.out.println("✅✅✅ ¡ÉXITO! Fila encontrada con estado Azul (#14D1FF)");
                    elementInteractions.scrollToElement(fila);
                    resaltador.resaltarConParpadeo(fila, 3);
                    
                    // Resaltar N° Solicitud y RecLoc específicamente
                    try {
                        WebElement celdaSolicitud = fila.findElement(By.xpath(".//mat-cell[contains(@class, 'mat-column-nRequest')]"));
                        WebElement celdaRecLoc = fila.findElement(By.xpath(".//mat-cell[contains(@class, 'mat-column-RecLoc')]"));
                        
                        resaltador.resaltarElemento(celdaSolicitud, 3000);
                        resaltador.resaltarElemento(celdaRecLoc, 3000);
                        
                        System.out.println("🎉 N° Solicitud y RecLoc resaltados con éxito");
                    } catch (Exception e) {
                        System.err.println("⚠️ No se pudieron resaltar las celdas individuales");
                    }
                    
                    return true;
                }
            } catch (Exception e) {
                System.err.println("❌ No se encontró la fila con color azul");
            }
            return false;
        }


*/






    /**
     * 🔧 MÉTODO AUXILIAR: Realiza el clic usando múltiples estrategias
     */
    private void realizarClicConMultiplesEstrategias(WebElement elemento ) {
        if (!elementInteractions.intentarClicNormal(elemento)) {
            System.out.println("❌ No se pudo hacer clic con clic normal");
            
            if (!elementInteractions.intentarClicConHoverYClick(elemento)) {
                System.out.println("❌ No se pudo hacer clic con hover tradicional");
                
                if (!elementInteractions.intentarClicConHoverYClickAngular(elemento)) {
                    System.out.println("❌ No se pudo hacer clic con hover para Angular");
                    
                    if (!elementInteractions.intentarClickConJavaScript(elemento)) {
                        System.out.println("❌ No se pudo hacer clic con JavaScript");
                        throw new RuntimeException("❌ No se pudo hacer clic");
                    }
                }
            }
        }
    }







}
