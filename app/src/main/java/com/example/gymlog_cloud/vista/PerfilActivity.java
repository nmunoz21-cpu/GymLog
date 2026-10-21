package com.example.gymlog_cloud.vista;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymlog_cloud.R;
import com.example.gymlog_cloud.servicio.UsuarioService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Pantalla que muestra la información del usuario.
 *
 * Responsabilidad:
 * Gestionar la interfaz del perfil y presentar
 * los datos recibidos desde el servicio Flask.
 */
public class PerfilActivity extends AppCompatActivity {

    // Componentes de la interfaz.
    private TextView txtNombre;
    private TextView txtCorreo;
    private TextView txtRut;
    private TextView txtEstadoPerfil;
    private Button btnConsultarUsuario;

    // Servicio que realiza la solicitud GET.
    private UsuarioService usuarioService;

    /**
     * Inicializa la pantalla y configura sus eventos.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfil);

        usuarioService = new UsuarioService();

        inicializarComponentes();
        configurarEventos();
    }

    /**
     * Vincula los componentes Java con el XML.
     */
    private void inicializarComponentes() {

        txtNombre = findViewById(R.id.txtNombre);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtRut = findViewById(R.id.txtRut);
        txtEstadoPerfil = findViewById(R.id.txtEstadoPerfil);

        btnConsultarUsuario =
                findViewById(R.id.btnConsultarUsuario);
    }

    /**
     * Configura el botón para consultar los datos.
     */
    /**
     * Configura los botones disponibles en el perfil.
     */
    private void configurarEventos() {

        // Consulta los datos del usuario desde Flask.
        btnConsultarUsuario.setOnClickListener(
                v -> consultarUsuario()
        );

        // Abre la pantalla de ejercicios.
        Button btnMisEjercicios = findViewById(R.id.btnMisEjercicios);

        btnMisEjercicios.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PerfilActivity.this,
                    EjerciciosActivity.class
            );

            startActivity(intent);
        });
    }

    /**
     * Solicita los datos del usuario a Flask mediante
     * UsuarioService y muestra el resultado.
     */
    private void consultarUsuario() {

        btnConsultarUsuario.setEnabled(false);
        txtEstadoPerfil.setText("Consultando usuario...");

        usuarioService.obtenerUsuario(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(() -> {
                    btnConsultarUsuario.setEnabled(true);
                    txtEstadoPerfil.setText(
                            "Error de conexión con el servidor"
                    );
                });
            }

            @Override
            public void onResponse(Call call, Response response) {

                try (Response respuesta = response) {

                    if (!respuesta.isSuccessful()
                            || respuesta.body() == null) {

                        runOnUiThread(() -> {
                            btnConsultarUsuario.setEnabled(true);
                            txtEstadoPerfil.setText(
                                    "No se pudo consultar el usuario"
                            );
                        });
                        return;
                    }

                    // Interpretamos el JSON de Flask.
                    String contenido = respuesta.body().string();
                    JSONObject datos = new JSONObject(contenido);

                    String nombre = datos.getString("nombre");
                    String correo = datos.getString("correo");
                    String rut = datos.getString("rut");

                    // Actualizamos la interfaz en el hilo principal.
                    runOnUiThread(() -> {
                        btnConsultarUsuario.setEnabled(true);

                        txtNombre.setText("Nombre: " + nombre);
                        txtCorreo.setText("Correo: " + correo);
                        txtRut.setText("RUT: " + rut);

                        txtEstadoPerfil.setText(
                                "Datos obtenidos correctamente"
                        );
                    });

                } catch (IOException | JSONException e) {

                    runOnUiThread(() -> {
                        btnConsultarUsuario.setEnabled(true);
                        txtEstadoPerfil.setText(
                                "Error al procesar los datos"
                        );
                    });
                }
            }
        });
    }
}
