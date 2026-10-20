
package com.example.gymlog_cloud.controlador;

import com.example.gymlog_cloud.servicio.UsuarioService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Controlador encargado de coordinar el inicio
 * de sesión de los usuarios.
 *
 * Responsabilidad:
 * Validar los datos de entrada, solicitar la
 * autenticación y comunicar el resultado a la vista.
 */
public class LoginController {

    // Servicio encargado de comunicarse con Flask.
    private final UsuarioService usuarioService;

    /**
     * Interfaz para informar los resultados a la vista,
     * sin depender directamente de una Activity.
     */
    public interface LoginCallback {

        void onExito(String mensaje);

        void onError(String mensaje);
    }

    /**
     * Inicializa el controlador con su servicio.
     */
    public LoginController() {
        this.usuarioService = new UsuarioService();
    }

    /**
     * Coordina el inicio de sesión del usuario.
     *
     * @param login Nombre de usuario.
     * @param password Contraseña.
     * @param callback Comunica el resultado a la vista.
     */
    public void iniciarSesion(
            String login,
            String password,
            LoginCallback callback) {

        // Comprobamos que los campos no estén vacíos.
        if (login == null || login.trim().isEmpty()
                || password == null || password.isEmpty()) {

            callback.onError("Completa todos los campos");
            return;
        }

        // Delegamos la comunicación HTTP al servicio.
        usuarioService.iniciarSesion(
                login.trim(),
                password,
                new Callback() {

                    @Override
                    public void onFailure(
                            Call call,
                            IOException e) {

                        callback.onError(
                                "Error de conexión con el servidor"
                        );
                    }

                    @Override
                    public void onResponse(
                            Call call,
                            Response response) {

                        // Cerramos los recursos HTTP automáticamente.
                        try (Response respuesta = response) {

                            if (respuesta.body() == null) {
                                callback.onError(
                                        "Respuesta vacía del servidor"
                                );
                                return;
                            }

                            // Interpretamos el JSON enviado por Flask.
                            String contenido = respuesta.body().string();
                            JSONObject datos = new JSONObject(contenido);

                            boolean valido = datos.optBoolean(
                                    "valido", false
                            );

                            String mensaje = datos.optString(
                                    "mensaje",
                                    "Respuesta desconocida"
                            );

                            // Solo aceptamos el login si Flask
                            // responde correctamente.
                            if (respuesta.isSuccessful() && valido) {
                                callback.onExito(mensaje);
                            } else {
                                callback.onError(mensaje);
                            }

                        } catch (IOException | JSONException e) {

                            callback.onError(
                                    "No se pudo procesar la respuesta"
                            );
                        }
                    }
                }
        );
    }
}
