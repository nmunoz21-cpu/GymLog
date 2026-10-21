
package com.example.gymlog_cloud.modelo;

/**
 * Representa una serie realizada dentro de un ejercicio.
 *
 * Responsabilidad:
 * Almacenar el número de repeticiones y el peso
 * utilizado en una serie individual.
 *
 * Ejemplo:
 * Serie 1: 12 repeticiones con 60 kg.
 * Serie 2: 10 repeticiones con 70 kg.
 */
public class Serie {

    // Número de repeticiones realizadas.
    private final int repeticiones;

    // Peso utilizado durante la serie, en kilogramos.
    private final double peso;

    /**
     * Construye una serie con sus propios valores.
     *
     * @param repeticiones repeticiones de la serie.
     * @param peso peso utilizado en kilogramos.
     */
    public Serie(int repeticiones, double peso) {
        this.repeticiones = repeticiones;
        this.peso = peso;
    }

    /**
     * Obtiene las repeticiones de esta serie.
     */
    public int getRepeticiones() {
        return repeticiones;
    }

    /**
     * Obtiene el peso utilizado en esta serie.
     */
    public double getPeso() {
        return peso;
    }
}
