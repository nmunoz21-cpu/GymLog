
package com.example.gymlog_cloud.vista;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymlog_cloud.R;
import com.example.gymlog_cloud.controlador.LoginController;

/**
 * Pantalla encargada de presentar el inicio de sesión.
 *
 * Responsabilidad:
 * Gestionar los componentes visuales y mostrar
 * los resultados entregados por LoginController.
 */
public class LoginActivity extends AppCompatActivity {

    // Componentes visuales de la pantalla.
    private EditText edtUsuario;
    private EditText edtPassword;
    private TextView txtMensaje;
    private Button btnIniciarSesion;

    // Controlador que coordina la autenticación.
    private LoginController loginController;

    /**
     * Inicializa la pantalla y sus componentes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        loginController = new LoginController();

        inicializarComponentes();
        configurarEventos();
    }

    /**
     * Vincula los elementos Java con sus IDs del XML.
     */
    private void inicializarComponentes() {

        edtUsuario = findViewById(R.id.edtUsuario);
        edtPassword = findViewById(R.id.edtPassword);
        txtMensaje = findViewById(R.id.txtMensaje);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
    }

    /**
     * Configura la acción del botón de inicio de sesión.
     */
    private void configurarEventos() {

        btnIniciarSesion.setOnClickListener(v -> {

            String usuario = edtUsuario.getText().toString();
            String password = edtPassword.getText().toString();

            btnIniciarSesion.setEnabled(false);
            txtMensaje.setText("Verificando credenciales...");

            // Delegamos el inicio de sesión al controlador.
            loginController.iniciarSesion(
                    usuario,
                    password,
                    new LoginController.LoginCallback() {

                        @Override
                        public void onExito(String mensaje) {

                            // Las actualizaciones visuales deben
                            // realizarse en el hilo principal.
                            runOnUiThread(() -> {
                                btnIniciarSesion.setEnabled(true);
                                txtMensaje.setText(mensaje);
                            });
                        }

                        @Override
                        public void onError(String mensaje) {

                            runOnUiThread(() -> {
                                btnIniciarSesion.setEnabled(true);
                                txtMensaje.setText(mensaje);
                            });
                        }
                    }
            );
        });
    }
}
