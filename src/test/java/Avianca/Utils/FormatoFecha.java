package Avianca.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class FormatoFecha {

    // Formatos de entrada que aceptaremos
    private static final List<DateTimeFormatter> FORMATOS_ENTRADA = Arrays.asList(
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),  // Formato ISO
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),  // Formato europeo
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),  // Formato americano
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),  // Formato con guiones
        DateTimeFormatter.ofPattern("MM-dd-yyyy")   // Formato americano con guiones
    );

    // Formato de salida deseado
    private static final DateTimeFormatter FORMATO_SALIDA = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Convierte una fecha en cualquier formato conocido a dd/MM/yyyy
     * @param fecha Fecha en cualquier formato aceptado
     * @return Fecha formateada como dd/MM/yyyy
     * @throws IllegalArgumentException si la fecha no puede ser parseada
     */
    public static String formatearFecha(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha no puede ser nula o vacía");
        }

        // Intentar parsear con cada formato conocido
        for (DateTimeFormatter formato : FORMATOS_ENTRADA) {
            try {
                LocalDate fechaLocal = LocalDate.parse(fecha, formato);
                return fechaLocal.format(FORMATO_SALIDA);
            } catch (DateTimeParseException e) {
                // Continuar con el siguiente formato
            }
        }

        // Si ningún formato funcionó, lanzar excepción
        throw new IllegalArgumentException(
            "Formato de fecha no reconocido: " + fecha + 
            ". Formatos aceptados: yyyy-MM-dd, dd/MM/yyyy, MM/dd/yyyy, dd-MM-yyyy, MM-dd-yyyy"
        );
    }

    /**
     * Verifica si una fecha ya está en el formato dd/MM/yyyy
     * @param fecha Fecha a verificar
     * @return true si la fecha está en el formato correcto
     */
    public static boolean estaEnFormatoCorrecto(String fecha) {
        try {
            LocalDate.parse(fecha, FORMATO_SALIDA);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Formatea la fecha solo si es necesario
     * @param fecha Fecha a formatear
     * @return Fecha en formato dd/MM/yyyy
     */
    public static String formatearSiEsNecesario(String fecha) {
        if (estaEnFormatoCorrecto(fecha)) {
            return fecha;
        }
        return formatearFecha(fecha);
    }


        // ...existing code...

        /**
         * 📅 Clase para extraer componentes de una fecha (día, mes, año)
         */
        public static class ComponentesFecha {
            private final int dia;
            private final int mes;
            private final int año;
            
            public ComponentesFecha(int dia, int mes, int año) {
                this.dia = dia;
                this.mes = mes;
                this.año = año;
            }
            
            public int getDia() {
                return dia;
            }
            
            public int getMes() {
                return mes;
            }
            
            public int getAño() {
                return año;
            }
            
            @Override
            public String toString() {
                return String.format("Día: %d, Mes: %d, Año: %d", dia, mes, año);
            }
        }

        /**
         * 🔧 Método para extraer componentes individuales de una fecha
         * 
         * @param fecha Fecha en formato String (ISO-8601: yyyy-MM-dd o dd/MM/yyyy)
         * @return Objeto ComponentesFecha con día, mes y año extraídos
         * @throws IllegalArgumentException Si la fecha es nula, vacía o tiene formato inválido
         * 
         * @example
         * ComponentesFecha componentes = FormatoFecha.extraerComponentesFecha("2025-11-12");
         * int dia = componentes.getDia();    // 12
         * int mes = componentes.getMes();    // 11
         * int año = componentes.getAño();    // 2025
         */
        public static ComponentesFecha extraerComponentesFecha(String fecha) {
            // Validar que la fecha no sea nula o vacía
            if (fecha == null || fecha.trim().isEmpty()) {
                throw new IllegalArgumentException("La fecha no puede ser nula o vacía");
            }
            
            try {
                // Limpiar la fecha de comillas y espacios
                String fechaLimpia = fecha.replace("\"", "").trim();
                
                // Parsear la fecha a LocalDate (acepta formato ISO-8601: yyyy-MM-dd)
                LocalDate fechaParseada = LocalDate.parse(fechaLimpia);
                
                // Extraer componentes individuales
                int dia = fechaParseada.getDayOfMonth();
                int mes = fechaParseada.getMonthValue();
                int año = fechaParseada.getYear();
                
                // Registrar en consola para debugging
                System.out.println("📅 Componentes extraídos de la fecha: " + fechaLimpia);
                System.out.println("   └─ Día: " + dia);
                System.out.println("   └─ Mes: " + mes);
                System.out.println("   └─ Año: " + año);
                
                // Retornar objeto con los componentes
                return new ComponentesFecha(dia, mes, año);
                
            } catch (java.time.format.DateTimeParseException e) {
                // Si falla el parseo ISO, intentar con el formateador personalizado
                try {
                    String fechaFormateada = formatearSiEsNecesario(fecha);
                    LocalDate fechaParseada = LocalDate.parse(fechaFormateada);
                    
                    int dia = fechaParseada.getDayOfMonth();
                    int mes = fechaParseada.getMonthValue();
                    int año = fechaParseada.getYear();
                    
                    System.out.println("📅 Componentes extraídos (con formato personalizado): " + fechaFormateada);
                    System.out.println("   └─ Día: " + dia);
                    System.out.println("   └─ Mes: " + mes);
                    System.out.println("   └─ Año: " + año);
                    
                    return new ComponentesFecha(dia, mes, año);
                    
                } catch (Exception ex) {
                    throw new IllegalArgumentException(
                        "Formato de fecha inválido: '" + fecha + "'. " +
                        "Use formato ISO-8601 (yyyy-MM-dd) o dd/MM/yyyy", ex
                    );
                }
            }
        }

        // ...existing code...











}
