
package com.example.gymlog_cloud.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa un ejercicio y las series que lo componen.
 *
 * Responsabilidad:
 * Almacenar el nombre del ejercicio y sus series.
 *
 * Cada serie contiene sus propias repeticiones y peso.
 */
public class Ejercicio {

    private final String nombre;

    // Conjunto de series registradas para el ejercicio.
    private final List<Serie> series;

    /**
     * Constructor para el nuevo sistema de series.
     *
     * @param nombre nombre del ejercicio.
     */
    public Ejercicio(String nombre) {
        this.nombre = nombre;
        this.series = new ArrayList<>();
    }

    /**
     * Constructor compatible con el sistema anterior.
     *
     * Permite que el código existente siga funcionando.
     * Crea las series con el mismo peso y repeticiones.
     */
    public Ejercicio(String nombre, int cantidadSeries,
                     int repeticiones, double peso) {

        this(nombre);

        for (int i = 0; i < cantidadSeries; i++) {
            agregarSerie(new Serie(repeticiones, peso));
        }
    }

    /**
     * Agrega una serie al ejercicio.
     *
     * @param serie serie que se desea registrar.
     */
    public void agregarSerie(Serie serie) {
        if (serie == null) {
            throw new IllegalArgumentException(
                    "La serie no puede ser nula"
            );
        }

        series.add(serie);
    }

    /**
     * Obtiene el nombre del ejercicio.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene una lista de las series registradas.
     *
     * Evita que otras clases modifiquen directamente
     * la lista interna del ejercicio.
     */
    public List<Serie> getSeries() {
        return Collections.unmodifiableList(series);
    }

    /**
     * Obtiene la cantidad total de series.
     */
    public int getCantidadSeries() {
        return series.size();
    }

    /**
     * Método de compatibilidad con el sistema anterior.
     *
     * Devuelve la cantidad de series.
     */
    public int getSeriesCantidad() {
        return getCantidadSeries();
    }

    /**
     * Método de compatibilidad temporal.
     *
     * Obtiene las repeticiones de la primera serie.
     */
    public int getRepeticiones() {
        return series.isEmpty()
                ? 0
                : series.get(0).getRepeticiones();
    }

    /**
     * Método de compatibilidad temporal.
     *
     * Obtiene el peso de la primera serie.
     */
    public double getPeso() {
        return series.isEmpty()
                ? 0
                : series.get(0).getPeso();
    }
}
