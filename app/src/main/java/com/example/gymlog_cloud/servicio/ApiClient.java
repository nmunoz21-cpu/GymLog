
package com.example.gymlog_cloud.servicio;

import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

/**
 * Proporciona un cliente HTTP compartido
 * para la comunicación con la API Flask.
 *
 * Responsabilidad:
 * Configurar y entregar el cliente HTTP.
 */
public class ApiClient {

    // Cliente reutilizable para las solicitudes HTTP.
    private static final OkHttpClient CLIENTE =
            new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();

    // Evita crear instancias de esta clase.
    private ApiClient() {
    }

    /**
     * Devuelve el cliente HTTP configurado.
     *
     * @return Instancia compartida de OkHttpClient.
     */
    public static OkHttpClient obtenerCliente() {
        return CLIENTE;
    }
}
