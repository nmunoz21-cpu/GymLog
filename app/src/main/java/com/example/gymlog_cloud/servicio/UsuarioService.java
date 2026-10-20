
package com.example.gymlog_cloud.servicio;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Gestiona las solicitudes HTTP relacionadas
 * con los usuarios de GymLog Cloud.
 *
 * Responsabilidad:
 * Construir y enviar las peticiones de usuario
 * al servicio REST desarrollado con Flask.
 */
public class UsuarioService {

    // Dirección temporal para pruebas con emulador.
    // Se reemplazará por HTTPS cuando configuremos AWS.
    private static final String URL_BASE =
            "http://10.0.2.2:5000";

    // Tipo de contenido que enviamos al servidor.
    private static final MediaType JSON =
            MediaType.get("application/json; charset=utf-8");

    /**
     * Consulta los datos del usuario mediante GET.
     *
     * @param callback Recibe la respuesta de manera asíncrona.
     */
    public void obtenerUsuario(Callback callback) {

        Request solicitud = new Request.Builder()
                .url(URL_BASE + "/usuario")
                .get()
                .build();

        ApiClient.obtenerCliente()
                .newCall(solicitud)
                .enqueue(callback);
    }

    /**
     * Envía las credenciales al servidor mediante POST.
     *
     * @param login Nombre de usuario.
     * @param password Contraseña.
     * @param callback Recibe la respuesta del servidor.
     */
    public void iniciarSesion(
            String login,
            String password,
            Callback callback) {

        try {

            // Construimos el objeto JSON.
            JSONObject datos = new JSONObject();

            datos.put("login", login);
            datos.put("password", password);

            // Preparamos el cuerpo de la petición.
            RequestBody cuerpo = RequestBody.create(
                    datos.toString(),
                    JSON
            );

            // Construimos la solicitud POST.
            Request solicitud = new Request.Builder()
                    .url(URL_BASE + "/login")
                    .post(cuerpo)
                    .build();

            // Ejecutamos la solicitud en segundo plano.
            ApiClient.obtenerCliente()
                    .newCall(solicitud)
                    .enqueue(callback);

        } catch (JSONException e) {

            // Comunicamos el error al controlador.
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
