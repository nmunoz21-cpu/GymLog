
package com.example.gymlog_cloud.modelo;

/**
 * Representa un ejercicio realizado durante
 * una sesión de entrenamiento.
 *
 * Responsabilidad:
 * Almacenar los datos de un ejercicio.
 */
public class Ejercicio {

    // Atributos privados del ejercicio.
    private final String nombre;
    private final int series;
    private final int repeticiones;
    private final double peso;

    /**
     * Inicializa un ejercicio con sus datos.
     *
     * @param nombre Nombre del ejercicio.
     * @param series Cantidad de series realizadas.
     * @param repeticiones Repeticiones por serie.
     * @param peso Peso utilizado en kilogramos.
     */
    public Ejercicio(String nombre, int series,
                     int repeticiones, double peso) {

        this.nombre = nombre;
        this.series = series;
        this.repeticiones = repeticiones;
        this.peso = peso;
    }

    /** Obtiene el nombre del ejercicio. */
    public String getNombre() {
        return nombre;
    }

    /** Obtiene la cantidad de series. */
    public int getSeries() {
        return series;
    }

    /** Obtiene las repeticiones por serie. */
    public int getRepeticiones() {
        return repeticiones;
    }

    /** Obtiene el peso utilizado en kilogramos. */
    public double getPeso() {
        return peso;
    }
}
