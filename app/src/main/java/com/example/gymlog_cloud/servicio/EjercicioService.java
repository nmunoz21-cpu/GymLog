
package com.example.gymlog_cloud.servicio;

import com.example.gymlog_cloud.modelo.Ejercicio;
import com.example.gymlog_cloud.modelo.Serie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Servicio encargado de comunicarse con Flask.
 *
 * Responsabilidades:
 * - Consultar los ejercicios mediante GET.
 * - Registrar ejercicios mediante POST.
 * - Transformar las series a formato JSON.
 *
 * La autorización Basic se utiliza únicamente
 * con la cuenta ficticia del prototipo.
 */
public class EjercicioService {

    // Dirección HTTPS de nuestra API alojada en AWS.
    private static final String URL_BASE =
            "https://54-164-92-46.sslip.io";

    // Credenciales exclusivamente de demostración.
    // No utilizar credenciales reales en el código.
    private static final String USUARIO_DEMO = "nure";
    private static final String PASSWORD_DEMO = "1234";

    private static final MediaType JSON =
            MediaType.get("application/json; charset=utf-8");

    /**
     * Consulta los ejercicios del servidor mediante GET.
     *
     * @param callback Recibe el resultado de OkHttp.
     */
    public void obtenerEjercicios(Callback callback) {

        Request solicitud = new Request.Builder()
                .url(URL_BASE + "/ejercicios")
                .get()
                .build();

        ApiClient.obtenerCliente()
                .newCall(solicitud)
                .enqueue(callback);
    }

    /**
     * Envía un ejercicio con series independientes.
     *
     * Cada serie contiene repeticiones y peso propios.
     *
     * @param ejercicio Ejercicio que se registrará.
     * @param callback Recibe el resultado de la petición.
     */
    public void registrarEjercicio(
            Ejercicio ejercicio,
            Callback callback) {

        try {

            // Creamos el objeto principal.
            JSONObject datos = new JSONObject();

            datos.put("nombre", ejercicio.getNombre());

            // Arreglo que contiene las series.
            JSONArray listaSeries = new JSONArray();

            int numeroSerie = 1;

            for (Serie serie : ejercicio.getSeries()) {

                JSONObject datosSerie = new JSONObject();

                datosSerie.put("numero", numeroSerie);
                datosSerie.put(
                        "repeticiones",
                        serie.getRepeticiones()
                );
                datosSerie.put(
                        "peso",
                        serie.getPeso()
                );

                listaSeries.put(datosSerie);

                numeroSerie++;
            }

            datos.put("series", listaSeries);

            // Convertimos los datos a cuerpo HTTP.
            RequestBody cuerpo = RequestBody.create(
                    datos.toString(),
                    JSON
            );

            // Preparamos la autorización Basic del prototipo.
            String autorizacion = Credentials.basic(
                    USUARIO_DEMO,
                    PASSWORD_DEMO
            );

            // Construimos la petición POST.
            Request solicitud = new Request.Builder()
                    .url(URL_BASE + "/ejercicios")
                    .header("Authorization", autorizacion)
                    .post(cuerpo)
                    .build();

            // Ejecutamos la solicitud en segundo plano.
            ApiClient.obtenerCliente()
                    .newCall(solicitud)
                    .enqueue(callback);

        } catch (JSONException e) {

            callback.onFailure(
                    null,
                    new IOException(
                            "Error al construir el JSON",
                            e
                    )
            );
        }
    }
}
