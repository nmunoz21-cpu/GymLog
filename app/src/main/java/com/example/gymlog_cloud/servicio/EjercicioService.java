
package com.example.gymlog_cloud.servicio;

import com.example.gymlog_cloud.modelo.Ejercicio;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Servicio encargado de comunicarse con los
 * endpoints de ejercicios de nuestra API Flask.
 *
 * Responsabilidad:
 * Enviar y consultar ejercicios mediante HTTP.
 */
public class EjercicioService {

    // Dirección temporal para las pruebas locales.
    // Se reemplazará por la URL HTTPS de AWS.
    private static final String URL_BASE =
            "https://54-164-92-46.sslip.io";

    // Tipo de contenido utilizado para enviar JSON.
    private static final MediaType JSON =
            MediaType.get("application/json; charset=utf-8");

    /**
     * Consulta el historial de ejercicios mediante GET.
     *
     * @param callback Recibe la respuesta o error de OkHttp.
     */
    public void obtenerEjercicios(Callback callback) {

        // Construimos la solicitud HTTP GET.
        Request solicitud = new Request.Builder()
                .url(URL_BASE + "/ejercicios")
                .get()
                .build();

        // Ejecutamos la solicitud en segundo plano.
        ApiClient.obtenerCliente()
                .newCall(solicitud)
                .enqueue(callback);
    }

    /**
     * Envía un ejercicio a Flask mediante POST.
     *
     * @param ejercicio Ejercicio que se registrará.
     * @param callback Recibe la respuesta o error.
     */
    public void registrarEjercicio(
            Ejercicio ejercicio,
            Callback callback) {

        try {

            // Convertimos los atributos del ejercicio a JSON.
            JSONObject datos = new JSONObject();

            datos.put("nombre", ejercicio.getNombre());
            datos.put("series", ejercicio.getSeries());
            datos.put("repeticiones", ejercicio.getRepeticiones());
            datos.put("peso", ejercicio.getPeso());

            // Preparamos el cuerpo de la petición.
            RequestBody cuerpo = RequestBody.create(
                    datos.toString(),
                    JSON
            );

            // Construimos la solicitud HTTP POST.
            Request solicitud = new Request.Builder()
                    .url(URL_BASE + "/ejercicios")
                    .post(cuerpo)
                    .build();

            // Enviamos la solicitud de forma asíncrona.
            ApiClient.obtenerCliente()
                    .newCall(solicitud)
                    .enqueue(callback);

        } catch (JSONException e) {

            // Informamos el error de construcción del JSON.
            callback.onFailure(
                    null,
                    new IOException(
                            "Error al construir el JSON del ejercicio",
                            e
                    )
            );
        }
    }
}
