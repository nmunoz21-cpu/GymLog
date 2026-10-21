
package com.example.gymlog_cloud.vista;

import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymlog_cloud.R;
import com.example.gymlog_cloud.controlador.EjercicioController;
import com.example.gymlog_cloud.modelo.Ejercicio;
import com.example.gymlog_cloud.modelo.Serie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla para registrar y consultar ejercicios.
 *
 * Responsabilidad:
 * Administrar los componentes visuales, recoger
 * los datos ingresados y mostrar los resultados.
 *
 * Las validaciones de negocio se realizan en
 * EjercicioController y las solicitudes HTTP
 * en EjercicioService.
 */
public class EjerciciosActivity extends AppCompatActivity {

    // Componentes principales de la pantalla.
    private EditText edtNombreEjercicio;

    private LinearLayout contenedorSeries;

    private Button btnAgregarSerie;
    private Button btnGuardarEjercicio;
    private Button btnConsultarEjercicios;

    private TextView txtEstadoEjercicio;
    private TextView txtListaEjercicios;

    // Controlador del módulo de ejercicios.
    private EjercicioController ejercicioController;

    // Lista de filas visuales de las series.
    private final List<FilaSerie> filasSeries = new ArrayList<>();

    /**
     * Representa los componentes visuales de una serie.
     */
    private static class FilaSerie {

        final LinearLayout contenedor;
        final TextView titulo;
        final EditText edtRepeticiones;
        final EditText edtPeso;

        FilaSerie(
                LinearLayout contenedor,
                TextView titulo,
                EditText edtRepeticiones,
                EditText edtPeso) {

            this.contenedor = contenedor;
            this.titulo = titulo;
            this.edtRepeticiones = edtRepeticiones;
            this.edtPeso = edtPeso;
        }
    }

    /**
     * Inicializa la pantalla y sus componentes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ejercicios);

        ejercicioController = new EjercicioController();

        inicializarComponentes();
        configurarEventos();

        // Comenzamos con una serie disponible.
        agregarFilaSerie();
    }

    /**
     * Conecta las variables Java con los elementos XML.
     */
    private void inicializarComponentes() {

        edtNombreEjercicio =
                findViewById(R.id.edtNombreEjercicio);

        contenedorSeries =
                findViewById(R.id.contenedorSeries);

        btnAgregarSerie =
                findViewById(R.id.btnAgregarSerie);

        btnGuardarEjercicio =
                findViewById(R.id.btnGuardarEjercicio);

        btnConsultarEjercicios =
                findViewById(R.id.btnConsultarEjercicios);

        txtEstadoEjercicio =
                findViewById(R.id.txtEstadoEjercicio);

        txtListaEjercicios =
                findViewById(R.id.txtListaEjercicios);
    }

    /**
     * Configura las acciones de cada botón.
     */
    private void configurarEventos() {

        btnAgregarSerie.setOnClickListener(
                v -> agregarFilaSerie()
        );

        btnGuardarEjercicio.setOnClickListener(
                v -> guardarEjercicio()
        );

        btnConsultarEjercicios.setOnClickListener(
                v -> consultarEjercicios()
        );
    }

    /**
     * Convierte valores dp a píxeles según la pantalla.
     */
    private int dp(int valor) {

        return (int) (
                valor * getResources()
                        .getDisplayMetrics().density + 0.5f
        );
    }

    /**
     * Crea una nueva fila para registrar una serie.
     *
     * Cada fila contiene:
     * - Número de serie.
     * - Campo de repeticiones.
     * - Campo de peso.
     * - Botón para quitar la serie.
     */
    private void agregarFilaSerie() {

        // Contenedor principal de la nueva fila.
        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.VERTICAL);
        fila.setPadding(dp(12), dp(12), dp(12), dp(12));
        fila.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams parametrosFila =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametrosFila.bottomMargin = dp(12);

        contenedorSeries.addView(fila, parametrosFila);

        // Encabezado de la serie.
        LinearLayout encabezado = new LinearLayout(this);
        encabezado.setOrientation(LinearLayout.HORIZONTAL);
        encabezado.setGravity(Gravity.CENTER_VERTICAL);

        TextView titulo = new TextView(this);
        titulo.setTextColor(Color.rgb(29, 55, 97));
        titulo.setTextSize(17);
        titulo.setTypeface(null, Typeface.BOLD);

        encabezado.addView(
                titulo,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        // Botón para quitar una serie del formulario.
        Button btnEliminar = new Button(this);
        btnEliminar.setText("Quitar");

        encabezado.addView(btnEliminar);

        fila.addView(encabezado);

        // Contenedor horizontal de repeticiones y peso.
        LinearLayout campos = new LinearLayout(this);
        campos.setOrientation(LinearLayout.HORIZONTAL);

        // Campo de repeticiones.
        EditText edtRepeticiones = new EditText(this);
        edtRepeticiones.setHint("Repeticiones");

        edtRepeticiones.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
        );

        edtRepeticiones.setTextColor(
                Color.rgb(37, 56, 88)
        );

        edtRepeticiones.setHintTextColor(
                Color.rgb(100, 116, 139)
        );

        LinearLayout.LayoutParams parametrosReps =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        parametrosReps.rightMargin = dp(8);

        campos.addView(
                edtRepeticiones,
                parametrosReps
        );

        // Campo de peso individual de la serie.
        EditText edtPeso = new EditText(this);
        edtPeso.setHint("Peso (kg)");

        edtPeso.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
                        | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        edtPeso.setTextColor(
                Color.rgb(37, 56, 88)
        );

        edtPeso.setHintTextColor(
                Color.rgb(100, 116, 139)
        );

        campos.addView(
                edtPeso,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        fila.addView(campos);

        // Almacenamos las referencias de la fila.
        FilaSerie nuevaFila = new FilaSerie(
                fila,
                titulo,
                edtRepeticiones,
                edtPeso
        );

        filasSeries.add(nuevaFila);

        // Eliminamos una fila sin afectar las demás.
        btnEliminar.setOnClickListener(v -> {

            if (filasSeries.size() <= 1) {

                txtEstadoEjercicio.setText(
                        "Debe existir al menos una serie"
                );
                return;
            }

            contenedorSeries.removeView(nuevaFila.contenedor);
            filasSeries.remove(nuevaFila);

            actualizarNumeracion();
        });

        actualizarNumeracion();
    }

    /**
     * Actualiza la numeración visual de las series.
     */
    private void actualizarNumeracion() {

        for (int i = 0; i < filasSeries.size(); i++) {

            filasSeries.get(i).titulo.setText(
                    "Serie " + (i + 1)
            );
        }
    }

    /**
     * Recoge los datos del formulario y construye
     * un ejercicio con sus series independientes.
     */
    private void guardarEjercicio() {

        String nombre = edtNombreEjercicio
                .getText()
                .toString()
                .trim();

        if (nombre.isEmpty()) {

            txtEstadoEjercicio.setText(
                    "Ingresa el nombre del ejercicio"
            );
            return;
        }

        // Creamos el ejercicio sin series inicialmente.
        Ejercicio ejercicio = new Ejercicio(nombre);

        // Procesamos los campos de cada serie.
        for (int i = 0; i < filasSeries.size(); i++) {

            FilaSerie fila = filasSeries.get(i);

            String repsTexto = fila.edtRepeticiones
                    .getText()
                    .toString()
                    .trim();

            String pesoTexto = fila.edtPeso
                    .getText()
                    .toString()
                    .trim();

            if (repsTexto.isEmpty() || pesoTexto.isEmpty()) {

                txtEstadoEjercicio.setText(
                        "Completa la serie " + (i + 1)
                );
                return;
            }

            try {

                int repeticiones =
                        Integer.parseInt(repsTexto);

                double peso = Double.parseDouble(
                        pesoTexto.replace(',', '.')
                );

                if (repeticiones <= 0
                        || !Double.isFinite(peso)
                        || peso < 0
                        || peso > 10000) {

                    txtEstadoEjercicio.setText(
                            "Valores inválidos en la serie " + (i + 1)
                    );
                    return;
                }

                // Agregamos la serie al ejercicio.
                ejercicio.agregarSerie(
                        new Serie(repeticiones, peso)
                );

            } catch (NumberFormatException e) {

                txtEstadoEjercicio.setText(
                        "Revisa los números de la serie " + (i + 1)
                );
                return;
            }
        }

        // Evitamos registrar dos veces por pulsaciones.
        btnGuardarEjercicio.setEnabled(false);

        txtEstadoEjercicio.setText(
                "Guardando ejercicio..."
        );

        // El controlador valida y delega al servicio HTTP.
        ejercicioController.registrarEjercicio(
                ejercicio,
                new EjercicioController.EjercicioCallback() {

                    @Override
                    public void onExito(String mensaje) {

                        runOnUiThread(() -> {

                            btnGuardarEjercicio.setEnabled(true);

                            txtEstadoEjercicio.setText(mensaje);

                            // Consultamos el historial actualizado.
                            consultarEjercicios();
                        });
                    }

                    @Override
                    public void onError(String mensaje) {

                        runOnUiThread(() -> {

                            btnGuardarEjercicio.setEnabled(true);
                            txtEstadoEjercicio.setText(mensaje);
                        });
                    }
                }
        );
    }

    /**
     * Consulta el historial almacenado en AWS.
     *
     * Reconoce dos formatos:
     * - Antiguo: cantidad de series como entero.
     * - Nuevo: lista de series independientes.
     */
    private void consultarEjercicios() {

        btnConsultarEjercicios.setEnabled(false);

        txtEstadoEjercicio.setText(
                "Consultando ejercicios..."
        );

        ejercicioController.obtenerEjercicios(
                new EjercicioController.EjercicioCallback() {

                    @Override
                    public void onExito(String respuesta) {

                        try {

                            JSONObject json =
                                    new JSONObject(respuesta);

                            JSONArray ejercicios =
                                    json.getJSONArray("ejercicios");

                            StringBuilder lista =
                                    new StringBuilder();

                            for (int i = 0; i < ejercicios.length(); i++) {

                                JSONObject ejercicio =
                                        ejercicios.getJSONObject(i);

                                String nombre =
                                        ejercicio.getString("nombre");

                                lista.append("Ejercicio: ")
                                        .append(nombre)
                                        .append("\n");

                                Object series =
                                        ejercicio.get("series");

                                if (series instanceof JSONArray) {

                                    // Formato nuevo.
                                    JSONArray listaSeries =
                                            (JSONArray) series;

                                    for (int j = 0;
                                         j < listaSeries.length();
                                         j++) {

                                        JSONObject serie =
                                                listaSeries.getJSONObject(j);

                                        lista.append("  Serie ")
                                                .append(j + 1)
                                                .append(": ")
                                                .append(
                                                        serie.getInt(
                                                                "repeticiones"
                                                        )
                                                )
                                                .append(" reps, ")
                                                .append(
                                                        serie.getDouble("peso")
                                                )
                                                .append(" kg\n");
                                    }

                                } else {

                                    // Formato antiguo.
                                    lista.append("  Series: ")
                                            .append(ejercicio.getInt("series"))
                                            .append("\n  Repeticiones: ")
                                            .append(
                                                    ejercicio.getInt(
                                                            "repeticiones"
                                                    )
                                            )
                                            .append("\n  Peso: ")
                                            .append(
                                                    ejercicio.getDouble("peso")
                                            )
                                            .append(" kg\n");
                                }

                                lista.append("\n");
                            }

                            String resultado =
                                    lista.length() == 0
                                            ? "No hay ejercicios registrados"
                                            : lista.toString();

                            runOnUiThread(() -> {

                                btnConsultarEjercicios.setEnabled(true);

                                txtListaEjercicios.setText(resultado);

                                txtEstadoEjercicio.setText(
                                        "Consulta realizada correctamente"
                                );
                            });

                        } catch (JSONException e) {

                            mostrarErrorConsulta(
                                    "Error al procesar los ejercicios"
                            );
                        }
                    }

                    @Override
                    public void onError(String mensaje) {

                        mostrarErrorConsulta(mensaje);
                    }
                }
        );
    }

    /**
     * Muestra los errores de consulta en el hilo principal.
     */
    private void mostrarErrorConsulta(String mensaje) {

        runOnUiThread(() -> {

            btnConsultarEjercicios.setEnabled(true);

            txtEstadoEjercicio.setText(mensaje);
        });
    }
}
