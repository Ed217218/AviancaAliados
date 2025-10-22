package Avianca.Steps;

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
}
