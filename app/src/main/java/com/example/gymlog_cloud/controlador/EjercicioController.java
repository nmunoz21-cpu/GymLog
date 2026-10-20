
package com.example.gymlog_cloud.controlador;

import com.example.gymlog_cloud.modelo.Ejercicio;
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
 * Responsabilidad:
 * Validar los datos, coordinar el servicio de ejercicios
 * y comunicar los resultados a las vistas.
 */
public class EjercicioController {

    // Servicio responsable de la comunicación con Flask.
    private final EjercicioService ejercicioService;

    /**
     * Interfaz que permite comunicar resultados a las
     * vistas sin depender directamente de una Activity.
     */
    public interface EjercicioCallback {

        void onExito(String mensaje);

        void onError(String mensaje);
    }

    /**
     * Inicializa el controlador y su servicio.
     */
    public EjercicioController() {
        this.ejercicioService = new EjercicioService();
    }

    /**
     * Valida un ejercicio y solicita su registro en Flask.
     *
     * @param ejercicio Objeto con los datos del ejercicio.
     * @param callback Comunica el resultado del registro.
     */
    public void registrarEjercicio(
            Ejercicio ejercicio,
            EjercicioCallback callback) {

        // Comprobamos que exista un ejercicio.
        if (ejercicio == null) {
            callback.onError("No se recibió ningún ejercicio");
            return;
        }

        // Validamos los datos antes de enviarlos.
        if (ejercicio.getNombre() == null
                || ejercicio.getNombre().trim().isEmpty()) {

            callback.onError("Ingresa el nombre del ejercicio");
            return;
        }

        if (ejercicio.getSeries() <= 0
                || ejercicio.getRepeticiones() <= 0) {

            callback.onError(
                    "Las series y repeticiones deben ser mayores que cero"
            );
            return;
        }

        if (!Double.isFinite(ejercicio.getPeso())
                || ejercicio.getPeso() < 0
                || ejercicio.getPeso() > 10000) {

            callback.onError("Ingresa un peso válido");
            return;
        }

        // Delegamos el registro al servicio HTTP.
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

                            // Leemos el JSON de respuesta.
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
     * Solicita el historial de ejercicios al servidor.
     *
     * @param callback Comunica el resultado de la consulta.
     */
    public void obtenerEjercicios(EjercicioCallback callback) {

        // Solicitamos el historial mediante EjercicioService.
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

                            // Convertimos la respuesta a JSON.
                            String contenido = respuesta.body().string();
                            JSONObject datos = new JSONObject(contenido);

                            if (respuesta.isSuccessful()) {

                                // Entregamos el JSON a la vista.
                                // Más adelante lo convertiremos
                                // en una lista de objetos Ejercicio.
                                callback.onExito(
                                        datos.toString()
                                );

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
