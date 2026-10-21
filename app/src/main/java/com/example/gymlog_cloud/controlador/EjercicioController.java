

package com.example.gymlog_cloud.controlador;

import com.example.gymlog_cloud.modelo.Ejercicio;
import com.example.gymlog_cloud.modelo.Serie;
import com.example.gymlog_cloud.servicio.EjercicioService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Controlador encargado de coordinar las operaciones
 * relacionadas con los ejercicios de GymLog Cloud.
 *
 * Responsabilidades:
 * - Validar los datos de un ejercicio y sus series.
 * - Solicitar operaciones al servicio HTTP.
 * - Comunicar los resultados a las vistas.
 *
 * No realiza directamente solicitudes HTTP.
 */
public class EjercicioController {

    // Servicio que se comunica con Flask.
    private final EjercicioService ejercicioService;

    /**
     * Comunica los resultados del controlador
     * sin depender directamente de una Activity.
     */
    public interface EjercicioCallback {

        void onExito(String mensaje);

        void onError(String mensaje);
    }

    /**
     * Inicializa el servicio de ejercicios.
     */
    public EjercicioController() {
        this.ejercicioService = new EjercicioService();
    }

    /**
     * Valida los datos antes de solicitar el registro.
     *
     * @param ejercicio Ejercicio que se desea registrar.
     * @param callback Resultado de la operación.
     */
    public void registrarEjercicio(
            Ejercicio ejercicio,
            EjercicioCallback callback) {

        // Comprobamos que exista un ejercicio.
        if (ejercicio == null) {
            callback.onError("No se recibió ningún ejercicio");
            return;
        }

        // Validamos el nombre.
        if (ejercicio.getNombre() == null
                || ejercicio.getNombre().trim().isEmpty()) {

            callback.onError("Ingresa el nombre del ejercicio");
            return;
        }

        // Verificamos que exista al menos una serie.
        if (ejercicio.getCantidadSeries() <= 0) {

            callback.onError(
                    "El ejercicio debe tener al menos una serie"
            );
            return;
        }

        // Validamos cada serie de manera independiente.
        for (Serie serie : ejercicio.getSeries()) {

            if (serie == null || serie.getRepeticiones() <= 0) {

                callback.onError(
                        "Todas las series deben tener repeticiones mayores que cero"
                );
                return;
            }

            if (!Double.isFinite(serie.getPeso())
                    || serie.getPeso() < 0
                    || serie.getPeso() > 10000) {

                callback.onError(
                        "Todas las series deben tener un peso válido"
                );
                return;
            }
        }

        // Delegamos el registro al servicio.
        ejercicioService.registrarEjercicio(
                ejercicio,
                new Callback() {

                    @Override
                    public void onFailure(
                            Call call,
                            IOException e) {

                        callback.onError(
                                "No se pudo conectar con el servidor"
                        );
                    }

                    @Override
                    public void onResponse(
                            Call call,
                            Response response) {

                        try (Response respuesta = response) {

                            if (respuesta.body() == null) {
                                callback.onError(
                                        "El servidor devolvió una respuesta vacía"
                                );
                                return;
                            }

                            // Interpretamos el JSON recibido.
                            String contenido = respuesta.body().string();
                            JSONObject datos = new JSONObject(contenido);

                            if (respuesta.isSuccessful()) {

                                callback.onExito(
                                        datos.optString(
                                                "mensaje",
                                                "Ejercicio registrado correctamente"
                                        )
                                );

                            } else {

                                callback.onError(
                                        datos.optString(
                                                "error",
                                                "No se pudo registrar el ejercicio"
                                        )
                                );
                            }

                        } catch (IOException | JSONException e) {

                            callback.onError(
                                    "Error al procesar la respuesta del servidor"
                            );
                        }
                    }
                }
        );
    }

    /**
     * Solicita los ejercicios almacenados en Flask.
     *
     * @param callback Resultado de la consulta.
     */
    public void obtenerEjercicios(EjercicioCallback callback) {

        ejercicioService.obtenerEjercicios(
                new Callback() {

                    @Override
                    public void onFailure(
                            Call call,
                            IOException e) {

                        callback.onError(
                                "No se pudo obtener el historial"
                        );
                    }

                    @Override
                    public void onResponse(
                            Call call,
                            Response response) {

                        try (Response respuesta = response) {

                            if (respuesta.body() == null) {
                                callback.onError(
                                        "El servidor devolvió una respuesta vacía"
                                );
                                return;
                            }

                            String contenido = respuesta.body().string();
                            JSONObject datos = new JSONObject(contenido);

                            if (respuesta.isSuccessful()) {

                                // Entregamos los datos recibidos.
                                callback.onExito(datos.toString());

                            } else {

                                callback.onError(
                                        datos.optString(
                                                "error",
                                                "Error al consultar ejercicios"
                                        )
                                );
                            }

                        } catch (IOException | JSONException e) {

                            callback.onError(
                                    "Error al procesar el historial"
                            );
                        }
                    }
                }
        );
    }
}
