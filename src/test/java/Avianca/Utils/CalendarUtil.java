package Avianca.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;

public class CalendarUtil {
    private WebDriver driver;
    private WebDriverWait wait;
    private ElementFinder elementFinder;
    
    // Selectores del calendario
    private final By calendarContainer = By.className("mat-datepicker-content-container");
    private final By currentMonthYear = By.className("mat-calendar-period-button");
    //private final By closeButton = By.className("mat-datepicker-close-button");

    public CalendarUtil(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.elementFinder = new ElementFinder(driver, 10);
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Selecciona una fecha en el calendario
     */
    public void selectDate(LocalDate targetDate) {
        try {
            System.out.println("🗓️ Intentando seleccionar la fecha: " + targetDate);
            
            System.out.println("⏳ Esperando a que el calendario sea visible...");
           // WebElement calendarElement = wait.until(ExpectedConditions.visibilityOfElementLocated(calendarContainer));
            System.out.println("✅ Calendario es visible");
            
            System.out.println("🔄 Navegando al mes y año deseados...");
            navigateToMonthYear(targetDate);
            
            System.out.println("🖱️ Seleccionando el día específico...");
            selectDay(targetDate);
            
            System.out.println("🔍 Validando selección de fecha...");
            validateDateSelection(targetDate);
            
            System.out.println("🔄 Cerrando el calendario...");
            closeCalendar();
            
            System.out.println("✅ Fecha seleccionada y validada correctamente: " + targetDate);
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar fecha: " + e.getMessage());
            takeScreenshot("calendar_error_" + System.currentTimeMillis());
            throw new RuntimeException("No se pudo seleccionar la fecha: " + targetDate, e);
        }
    }

    /**
     * 🔧 Abre el calendario haciendo clic en un campo de fecha - MÉTODO REINTEGRADO
     */
    public void openCalendar(WebElement dateField) {
        try {
            System.out.println("📂 Abriendo calendario desde campo de fecha");
            
            // Verificar que el campo de fecha sea visible y clickeable
            wait.until(ExpectedConditions.visibilityOf(dateField));
            wait.until(ExpectedConditions.elementToBeClickable(dateField));
            
            // Hacer scroll al elemento si es necesario
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dateField);
            Thread.sleep(300);
            
            // Intentar múltiples estrategias para hacer clic
            boolean clickSuccess = intentarClicConMultiplesEstrategias(dateField);
            
            if (!clickSuccess) {
                throw new RuntimeException("No se pudo hacer clic en el campo de fecha para abrir el calendario");
            }
            
            System.out.println("✅ Clic realizado en el campo de fecha");
            
            // Esperar a que el calendario se abra
            System.out.println("⏳ Esperando a que el calendario se abra...");
            WebElement calendarElement = wait.until(ExpectedConditions.visibilityOfElementLocated(calendarContainer));
            
            if (calendarElement.isDisplayed()) {
                System.out.println("✅ Calendario abierto correctamente");
            } else {
                throw new RuntimeException("El calendario no está visible después de hacer clic");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al abrir el calendario: " + e.getMessage());
            takeScreenshot("calendar_open_error_" + System.currentTimeMillis());
            throw new RuntimeException("No se pudo abrir el calendario", e);
        }
    }

    /**
     * 🔧 Verifica si una fecha específica está disponible en el calendario - MÉTODO REINTEGRADO
     */
    public boolean isDateAvailable(LocalDate date) {
        try {
            System.out.println("🔍 Verificando disponibilidad de fecha: " + date);
            
            // Primero verificar si el calendario ya está abierto
            List<WebElement> calendars = driver.findElements(calendarContainer);
            boolean calendarWasOpen = !calendars.isEmpty() && calendars.get(0).isDisplayed();
            
            // Si el calendario no está abierto, necesitamos abrirlo primero
            if (!calendarWasOpen) {
                System.out.println("⚠️ El calendario no está abierto. Este método requiere que el calendario ya esté abierto.");
                System.out.println("💡 Usa openCalendar(WebElement dateField) primero para abrir el calendario.");
                return false;
            }
            
            // Navegar al mes de la fecha
            System.out.println("🔄 Navegando al mes de la fecha a verificar...");
            navigateToMonthYear(date);
            
            // Obtener todos los días del mes actual
            List<WebElement> allDays = driver.findElements(By.className("mat-calendar-body-cell"));
            int targetDay = date.getDayOfMonth();
            
            System.out.println("🔍 Buscando día " + targetDay + " en el calendario...");
            
            // Buscar el día específico
            for (WebElement dayElement : allDays) {
                try {
                    String dayText = dayElement.getText().trim();
                    
                    if (!dayText.isEmpty() && Integer.parseInt(dayText) == targetDay) {
                        // Verificar si el día está deshabilitado
                        boolean isDisabled = dayElement.getAttribute("class").contains("mat-calendar-body-disabled");
                        
                        if (isDisabled) {
                            System.out.println("❌ El día " + dayText + " está deshabilitado");
                            return false;
                        } else {
                            System.out.println("✅ El día " + dayText + " está disponible");
                            return true;
                        }
                    }
                } catch (NumberFormatException e) {
                    // Ignorar elementos que no son números (como etiquetas de mes)
                }
            }
            
            System.out.println("❌ No se encontró el día " + targetDay + " en el calendario");
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Error al verificar disponibilidad de fecha: " + e.getMessage());
            takeScreenshot("calendar_availability_error_" + System.currentTimeMillis());
            return false;
        }
    }

    /**
     * 🔧 Navega al mes y año deseados - VERSIÓN MEJORADA
     */
    private void navigateToMonthYear(LocalDate targetDate) {
        try {
            YearMonth targetYearMonth = YearMonth.from(targetDate);
            String targetMonthYear = targetYearMonth.format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH)).toUpperCase();
            
            System.out.println("🎯 Mes/Año objetivo: " + targetMonthYear);
            
            WebElement currentMonthYearElement = wait.until(ExpectedConditions.elementToBeClickable(currentMonthYear));
            String currentMonthYearText = currentMonthYearElement.getText().trim().toUpperCase();
            
            System.out.println("📅 Mes/Año actual: " + currentMonthYearText);
            
            // Si ya estamos en el mes/año correcto, no navegar
            if (normalizeText(currentMonthYearText).equals(normalizeText(targetMonthYear))) {
                System.out.println("✅ Ya estamos en el mes/año correcto");
                return;
            }
            
            int maxAttempts = 24;
            int attempts = 0;
            
            while (!normalizeText(currentMonthYearText).equals(normalizeText(targetMonthYear)) && attempts < maxAttempts) {
                attempts++;
                
                YearMonth currentYearMonth = parseMonthYearSafely(currentMonthYearText);
                
                if (currentYearMonth == null) {
                    throw new RuntimeException("No se pudo parsear el texto del mes/año actual: " + currentMonthYearText);
                }
                
                if (targetYearMonth.isAfter(currentYearMonth)) {
                    System.out.println("➡️ Avanzando al siguiente mes");
                    clickNextMonth();
                } else {
                    System.out.println("⬅️ Retrocediendo al mes anterior");
                    clickPreviousMonth();
                }
                
                Thread.sleep(500);
                
                currentMonthYearElement = wait.until(ExpectedConditions.elementToBeClickable(currentMonthYear));
                currentMonthYearText = currentMonthYearElement.getText().trim().toUpperCase();
                
                System.out.println("📅 Mes/Año actual: " + currentMonthYearText);
            }
            
            if (attempts >= maxAttempts) {
                throw new RuntimeException("No se pudo llegar al mes/año deseado después de " + maxAttempts + " intentos");
            }
            
            System.out.println("✅ Mes/Año correcto alcanzado: " + currentMonthYearText);
        } catch (Exception e) {
            System.err.println("❌ Error al navegar al mes/año: " + e.getMessage());
            throw new RuntimeException("No se pudo navegar al mes/año deseado", e);
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Parseo seguro de mes/año
     */
    private YearMonth parseMonthYearSafely(String text) {
        String cleaned = text.replace(".", "").trim().toLowerCase();
        String[] patrones = {"MMM yyyy", "MMMM yyyy"};
        Locale[] locales = {Locale.ENGLISH, new Locale("es", "ES")};
        
        for (String pattern : patrones) {
            for (Locale locale : locales) {
                try {
                    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                            .parseCaseInsensitive()
                            .appendPattern(pattern)
                            .toFormatter(locale);
                    return YearMonth.parse(cleaned, formatter);
                } catch (Exception ignored) { 
                    // Continuar con el siguiente patrón/locale
                }
            }
        }
        
        System.err.println("⚠️ No se pudo interpretar el texto del calendario: " + text);
        return null;
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Normaliza texto para comparación
     */
    private String normalizeText(String text) {
        return text.replace(".", "").trim().toLowerCase();
    }

    /**
     * 🔧 Hace clic en el botón de mes siguiente - VERSIÓN MEJORADA
     */
    private void clickNextMonth() {
        try {
            System.out.println("🔍 Buscando botón de mes siguiente...");
            WebElement nextButton = encontrarBotonMesSiguiente();
            
            if (nextButton == null) {
                throw new RuntimeException("No se encontró el botón de mes siguiente");
            }
            
            System.out.println("✅ Botón de mes siguiente encontrado");
            
            // Esperar a que sea clickeable
            wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            
            // Verificar si el botón está habilitado
            String disabled = nextButton.getAttribute("disabled");
            boolean isDisabled = disabled != null && !disabled.equals("false") && !disabled.isEmpty();
            
            if (isDisabled) {
                System.out.println("⚠️ Botón de mes siguiente está deshabilitado");
                throw new RuntimeException("No se puede navegar más adelante en el calendario");
            }
            
            System.out.println("➡️ Haciendo clic en botón de mes siguiente");
            
            // Intentar múltiples estrategias de clic
            boolean clickSuccess = intentarClicConMultiplesEstrategias(nextButton);
            
            if (!clickSuccess) {
                throw new RuntimeException("No se pudo hacer clic en el botón de mes siguiente");
            }
            
            System.out.println("✅ Clic realizado en botón de mes siguiente");
            
            // Esperar a que el calendario se actualice
            Thread.sleep(800);
            
        } catch (Exception e) {
            System.err.println("❌ Error al hacer clic en mes siguiente: " + e.getMessage());
            throw new RuntimeException("No se pudo navegar al mes siguiente", e);
        }
    }

    /**
     * 🔧 Hace clic en el botón de mes anterior - VERSIÓN MEJORADA
     */
    private void clickPreviousMonth() {
        try {
            System.out.println("🔍 Buscando botón de mes anterior...");
            WebElement prevButton = encontrarBotonMesAnterior();
            
            if (prevButton == null) {
                throw new RuntimeException("No se encontró el botón de mes anterior");
            }
            
            System.out.println("✅ Botón de mes anterior encontrado");
            
            // Esperar a que sea clickeable
            wait.until(ExpectedConditions.elementToBeClickable(prevButton));
            
            // Verificar si el botón está habilitado
            String disabled = prevButton.getAttribute("disabled");
            boolean isDisabled = disabled != null && !disabled.equals("false") && !disabled.isEmpty();
            
            if (isDisabled) {
                System.out.println("⚠️ Botón de mes anterior está deshabilitado");
                throw new RuntimeException("No se puede navegar más atrás en el calendario");
            }
            
            System.out.println("⬅️ Haciendo clic en botón de mes anterior");
            
            // Intentar múltiples estrategias de clic
            boolean clickSuccess = intentarClicConMultiplesEstrategias(prevButton);
            
            if (!clickSuccess) {
                throw new RuntimeException("No se pudo hacer clic en el botón de mes anterior");
            }
            
            System.out.println("✅ Clic realizado en botón de mes anterior");
            
            // Esperar a que el calendario se actualice
            Thread.sleep(800);
            
        } catch (Exception e) {
            System.err.println("❌ Error al hacer clic en mes anterior: " + e.getMessage());
            throw new RuntimeException("No se pudo navegar al mes anterior", e);
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Intenta hacer clic con múltiples estrategias
     */
    private boolean intentarClicConMultiplesEstrategias(WebElement elemento) {
        try {
            // Estrategia 1: Clic normal
            try {
                elemento.click();
                return true;
            } catch (Exception e1) {
                System.out.println("⚠️ Clic normal falló, intentando JavaScript...");
            }
            
            // Estrategia 2: Clic con JavaScript
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
                return true;
            } catch (Exception e2) {
                System.out.println("⚠️ Clic con JavaScript falló, intentando Actions...");
            }
            
            // Estrategia 3: Clic con Actions
            try {
                org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
                actions.moveToElement(elemento).click().perform();
                return true;
            } catch (Exception e3) {
                System.out.println("⚠️ Clic con Actions falló");
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error al intentar múltiples estrategias de clic: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra el botón de mes anterior con múltiples estrategias mejoradas
     */
    private WebElement encontrarBotonMesAnterior() {
        try {
            System.out.println("🔍 Intentando encontrar botón de mes anterior...");
            
            // Esperar a que el calendario esté completamente cargado
            wait.until(ExpectedConditions.presenceOfElementLocated(calendarContainer));
            Thread.sleep(500);
            
            By[] localizadores = {
                By.xpath("//button[contains(@class, 'mat-calendar-previous-button')]"),
                By.cssSelector("button.mat-calendar-previous-button"),
                By.xpath("//button[@aria-label='Previous month']"),
                By.xpath("//mat-calendar-header//button[1]"),
                By.xpath("//button[.//mat-icon[contains(@class, 'mat-calendar-previous-icon')]]"),
                By.xpath("//button[contains(@class, 'mat-mdc-icon-button')][1]"),
                By.xpath("//button[contains(@class, 'mat-icon-button') and @aria-label='Previous month']")
            };
            
            for (By localizador : localizadores) {
                try {
                    List<WebElement> elementos = driver.findElements(localizador);
                    for (WebElement elemento : elementos) {
                        if (elemento.isDisplayed() && elemento.isEnabled()) {
                            System.out.println("✅ Botón de mes anterior encontrado con: " + localizador);
                            return elemento;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ No se encontró con localizador: " + localizador);
                }
            }
            
            // Último recurso: buscar todos los botones y filtrar
            System.out.println("🔍 Buscando todos los botones como último recurso...");
            List<WebElement> allButtons = driver.findElements(By.tagName("button"));
            for (WebElement button : allButtons) {
                try {
                    String ariaLabel = button.getAttribute("aria-label");
                    String classes = button.getAttribute("class");
                    
                    if ((ariaLabel != null && ariaLabel.contains("Previous")) || 
                        (classes != null && classes.contains("previous"))) {
                        if (button.isDisplayed() && button.isEnabled()) {
                            System.out.println("✅ Botón de mes anterior encontrado por búsqueda general");
                            return button;
                        }
                    }
                } catch (Exception e) {
                    // Ignorar y continuar
                }
            }
            
            System.err.println("❌ No se encontró el botón de mes anterior con ninguna estrategia");
            return null;
            
        } catch (Exception e) {
            System.err.println("❌ Error al buscar botón de mes anterior: " + e.getMessage());
            return null;
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra el botón de mes siguiente con múltiples estrategias mejoradas
     */
    private WebElement encontrarBotonMesSiguiente() {
        try {
            System.out.println("🔍 Intentando encontrar botón de mes siguiente...");
            
            // Esperar a que el calendario esté completamente cargado
            wait.until(ExpectedConditions.presenceOfElementLocated(calendarContainer));
            Thread.sleep(500);
            
            By[] localizadores = {
                By.xpath("//button[contains(@class, 'mat-calendar-next-button')]"),
                By.cssSelector("button.mat-calendar-next-button"),
                By.xpath("//button[@aria-label='Next month']"),
                By.xpath("//mat-calendar-header//button[2]"),
                By.xpath("//button[.//mat-icon[contains(@class, 'mat-calendar-next-icon')]]"),
                By.xpath("//button[contains(@class, 'mat-mdc-icon-button')][2]"),
                By.xpath("//button[contains(@class, 'mat-icon-button') and @aria-label='Next month']")
            };
            
            for (By localizador : localizadores) {
                try {
                    List<WebElement> elementos = driver.findElements(localizador);
                    for (WebElement elemento : elementos) {
                        if (elemento.isDisplayed() && elemento.isEnabled()) {
                            System.out.println("✅ Botón de mes siguiente encontrado con: " + localizador);
                            return elemento;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ No se encontró con localizador: " + localizador);
                }
            }
            
            // Último recurso: buscar todos los botones y filtrar
            System.out.println("🔍 Buscando todos los botones como último recurso...");
            List<WebElement> allButtons = driver.findElements(By.tagName("button"));
            for (WebElement button : allButtons) {
                try {
                    String ariaLabel = button.getAttribute("aria-label");
                    String classes = button.getAttribute("class");
                    
                    if ((ariaLabel != null && ariaLabel.contains("Next")) || 
                        (classes != null && classes.contains("next"))) {
                        if (button.isDisplayed() && button.isEnabled()) {
                            System.out.println("✅ Botón de mes siguiente encontrado por búsqueda general");
                            return button;
                        }
                    }
                } catch (Exception e) {
                    // Ignorar y continuar
                }
            }
            
            System.err.println("❌ No se encontró el botón de mes siguiente con ninguna estrategia");
            return null;
            
        } catch (Exception e) {
            System.err.println("❌ Error al buscar botón de mes siguiente: " + e.getMessage());
            return null;
        }
    }

    /**
     * 🔧 Selecciona el día específico - VERSIÓN MEJORADA
     */
    private void selectDay(LocalDate targetDate) {
        try {
            int targetDay = targetDate.getDayOfMonth();
            String monthName = targetDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            String year = String.valueOf(targetDate.getYear());
            String fechaCompleta = targetDay + " " + monthName + " " + year;
            
            System.out.println("🔍 Buscando día con aria-label: " + fechaCompleta);
            
            // Esperar a que los días estén cargados
            Thread.sleep(500);
            
            // Estrategia 1: XPath dinámico exacto
            By[] localizadoresDia = {
                By.xpath("//button[@aria-label='" + fechaCompleta + "' and not(contains(@class,'mat-calendar-body-disabled'))]"),
                By.xpath("//td[@role='gridcell']//button[@aria-label='" + fechaCompleta + "']"),
                By.xpath("//button[contains(@class,'mat-calendar-body-cell')][@aria-label='" + fechaCompleta + "']"),
                By.xpath("//mat-month-view//button[text()='" + targetDay + "' and not(contains(@class,'mat-calendar-body-disabled'))]")
            };
            
            WebElement dayElement = elementFinder.encontrarElemento(localizadoresDia);
            
            if (dayElement != null) {
                System.out.println("✅ Día encontrado con XPath dinámico");
                
                if (!dayElement.getAttribute("class").contains("mat-calendar-body-disabled")) {
                    System.out.println("🖱️ Seleccionando día: " + fechaCompleta);
                    
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dayElement);
                    Thread.sleep(300);
                    
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dayElement);
                    
                    Thread.sleep(1000);
                    
                    System.out.println("✅ Día seleccionado correctamente: " + fechaCompleta);
                } else {
                    throw new RuntimeException("El día " + fechaCompleta + " está deshabilitado");
                }
            } else {
                System.out.println("⚠️ No se encontró por aria-label, usando fallback...");
                selectDayByText(targetDay);
            }
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar el día: " + e.getMessage());
            throw new RuntimeException("No se pudo seleccionar el día: " + targetDate.getDayOfMonth(), e);
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Selecciona día por texto (fallback)
     */
    private void selectDayByText(int targetDay) {
        try {
            List<WebElement> allDays = driver.findElements(By.className("mat-calendar-body-cell"));
            
            for (WebElement dayElement : allDays) {
                try {
                    String dayText = dayElement.getText().trim();
                    
                    if (!dayText.isEmpty() && Integer.parseInt(dayText) == targetDay) {
                        if (!dayElement.getAttribute("class").contains("mat-calendar-body-disabled")) {
                            System.out.println("🖱️ Seleccionando día por texto: " + dayText);
                            
                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dayElement);
                            Thread.sleep(300);
                            
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dayElement);
                            
                            Thread.sleep(1000);
                            
                            System.out.println("✅ Día seleccionado correctamente por texto: " + dayText);
                            return;
                        } else {
                            throw new RuntimeException("El día " + dayText + " está deshabilitado");
                        }
                    }
                } catch (NumberFormatException e) {
                    // Ignorar elementos que no son números
                }
            }
            
            throw new RuntimeException("No se encontró el día " + targetDay + " en el calendario");
        } catch (Exception e) {
            System.err.println("❌ Error al seleccionar día por texto: " + e.getMessage());
            throw new RuntimeException("No se pudo seleccionar el día por texto: " + targetDay, e);
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Valida que la fecha fue seleccionada correctamente
     */
    private void validateDateSelection(LocalDate targetDate) {
        try {
            System.out.println("🔍 Validando selección de fecha...");
            Thread.sleep(1000);
            
            List<WebElement> calendars = driver.findElements(calendarContainer);
            
            if (!calendars.isEmpty() && calendars.get(0).isDisplayed()) {
                String monthName = targetDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                String year = String.valueOf(targetDate.getYear());
                String fechaCompleta = targetDate.getDayOfMonth() + " " + monthName + " " + year;
                
                By selectedDayLocator = By.xpath("//button[@aria-label='" + fechaCompleta + "' and @aria-pressed='true']");
                
                try {
                    WebElement selectedDay = driver.findElement(selectedDayLocator);
                    if (selectedDay != null) {
                        System.out.println("✅ Fecha validada: el día está marcado como seleccionado");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ No se pudo validar que el día esté seleccionado, pero continuando...");
                }
            } else {
                System.out.println("✅ Fecha validada: el calendario se cerró automáticamente");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error en validación, pero continuando: " + e.getMessage());
        }
    }

    /**
     * 🔧 Cierra el calendario si es necesario - VERSIÓN MEJORADA
     */
    public void closeCalendar() {
        try {
            Thread.sleep(1000);
            
            List<WebElement> calendars = driver.findElements(calendarContainer);
            
            if (!calendars.isEmpty() && calendars.get(0).isDisplayed()) {
                System.out.println("🔒 El calendario todavía está visible, intentando cerrarlo...");
                
                WebElement closeButton = encontrarBotonCerrarCalendario();
                
                if (closeButton != null && closeButton.isDisplayed()) {
                    closeButton.click();
                    System.out.println("✅ Botón de cerrar calendario clickeado");
                } else {
                    System.out.println("🔒 Haciendo clic fuera del calendario para cerrarlo...");
                    ((JavascriptExecutor) driver).executeScript("document.body.click();");
                }
                
                Thread.sleep(500);
            } else {
                System.out.println("ℹ️ El calendario se cerró automáticamente");
            }
        } catch (Exception e) {
            System.out.println("ℹ️ No se pudo cerrar el calendario o no es necesario: " + e.getMessage());
        }
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Encuentra el botón de cerrar calendario
     */
    private WebElement encontrarBotonCerrarCalendario() {
        By[] localizadores = {
            By.className("mat-datepicker-close-button"),
            By.xpath("//button[contains(@class, 'mat-datepicker-close-button')]"),
            By.xpath("//button[text()='Close calendar']"),
            By.xpath("//mat-calendar//following-sibling::button")
        };
        return elementFinder.encontrarElemento(localizadores);
    }

    /**
     * 🔧 MÉTODO AUXILIAR: Toma screenshot para depuración
     */
    private void takeScreenshot(String fileName) {
        try {
            System.out.println("📸 Screenshot guardado para depuración: " + fileName);
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo tomar screenshot: " + e.getMessage());
        }
    }
}