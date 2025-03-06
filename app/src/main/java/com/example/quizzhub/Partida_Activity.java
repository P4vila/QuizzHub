package com.example.quizzhub;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Random;

public class Partida_Activity extends AppCompatActivity {

    private TextView textViewPregunta, textViewMonedas, textViewTimer;
    private Button btnOpcion1, btnOpcion2, btnOpcion3, btnOpcion4;
    private FirebaseFirestore db;
    private List<DocumentSnapshot> listaPreguntas;
    private int puntaje = 0;
    private CountDownTimer temporizador;
    private static final long TIEMPO_PREGUNTA = 30000; // 30 segundos por pregunta
    private long tiempoRestante; // Variable para almacenar el tiempo restante
    private String salaId;
    private List<String> jugadores;
    private int preguntasRespondidas = 0; // Contador de preguntas respondidas
    private static final int MAX_PREGUNTAS = 12; // Límite de 12 preguntas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        // Referencias a los elementos de la UI
        textViewPregunta = findViewById(R.id.textViewPregunta);
        textViewMonedas = findViewById(R.id.text_view_moneda);
        textViewTimer = findViewById(R.id.textViewTimer);
        btnOpcion1 = findViewById(R.id.btnOpcion1);
        btnOpcion2 = findViewById(R.id.btnOpcion2);
        btnOpcion3 = findViewById(R.id.btnOpcion3);
        btnOpcion4 = findViewById(R.id.btnOpcion4);
        db = FirebaseFirestore.getInstance();

        // Recibir los datos de la sala
        salaId = getIntent().getStringExtra("salaId");
        jugadores = getIntent().getStringArrayListExtra("jugadores");

        // Log para comprobar que los datos llegaron correctamente
        Log.d("Partida_Activity", "ID de la sala: " + salaId);
        Log.d("Partida_Activity", "Jugadores en la sala: " + jugadores);

        // Cargar preguntas desde Firebase
        cargarPreguntas();
    }

    private void cargarPreguntas() {
        db.collection("Preguntas").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listaPreguntas = task.getResult().getDocuments();
                mostrarNuevaPregunta();
            } else {
                Toast.makeText(this, "Error cargando preguntas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarNuevaPregunta() {
        if (listaPreguntas == null || listaPreguntas.isEmpty()) {
            Toast.makeText(this, "No hay preguntas disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        if (preguntasRespondidas >= MAX_PREGUNTAS) {
            // Terminar el juego si se alcanzan las 12 preguntas
            terminarJuego();
            return;
        }

        // Obtener el color definido en colors.xml
        int colorBoton = getResources().getColor(R.color.boton_geografia);

        // Restablecer colores de los botones
        btnOpcion1.setBackgroundColor(colorBoton);
        btnOpcion2.setBackgroundColor(colorBoton);
        btnOpcion3.setBackgroundColor(colorBoton);
        btnOpcion4.setBackgroundColor(colorBoton);

        // Seleccionar una pregunta aleatoria
        int index = new Random().nextInt(listaPreguntas.size());
        DocumentSnapshot preguntaActual = listaPreguntas.get(index);

        textViewPregunta.setText(preguntaActual.getString("Pregunta"));
        List<String> opciones = (List<String>) preguntaActual.get("Opciones");
        String respuestaCorrecta = preguntaActual.getString("Respuesta correcta");

        btnOpcion1.setText(opciones.get(0));
        btnOpcion2.setText(opciones.get(1));
        btnOpcion3.setText(opciones.get(2));
        btnOpcion4.setText(opciones.get(3));

        // Asignar eventos de clic para las opciones
        View.OnClickListener listener = v -> verificarRespuesta(((Button) v), respuestaCorrecta);
        btnOpcion1.setOnClickListener(listener);
        btnOpcion2.setOnClickListener(listener);
        btnOpcion3.setOnClickListener(listener);
        btnOpcion4.setOnClickListener(listener);

        // Iniciar el temporizador
        iniciarTemporizador();
    }

    private void iniciarTemporizador() {
        if (temporizador != null) {
            temporizador.cancel(); // Cancelar temporizador anterior si existe
        }

        temporizador = new CountDownTimer(TIEMPO_PREGUNTA, 1000) {
            public void onTick(long millisUntilFinished) {
                // Actualizar el TextView con la cuenta regresiva
                tiempoRestante = millisUntilFinished;
                long segundosRestantes = millisUntilFinished / 1000;
                textViewTimer.setText(String.valueOf(segundosRestantes));
            }

            public void onFinish() {
                Toast.makeText(Partida_Activity.this, "Tiempo agotado", Toast.LENGTH_SHORT).show();
                mostrarNuevaPregunta(); // Pasar a la siguiente pregunta
            }
        }.start();
    }

    private void verificarRespuesta(Button botonSeleccionado, String respuestaCorrecta) {
        String respuestaElegida = botonSeleccionado.getText().toString();

        // Cambiar el color del botón seleccionado a amarillo
        botonSeleccionado.setBackgroundColor(Color.YELLOW);

        // Retraso de 500 ms antes de mostrar la respuesta correcta/incorrecta
        new android.os.Handler().postDelayed(() -> {
            if (respuestaElegida.equals(respuestaCorrecta)) {
                // Respuesta correcta: cambiar el botón seleccionado a verde
                botonSeleccionado.setBackgroundColor(Color.GREEN);

                // Calcular los puntos basados en el tiempo restante
                long puntos = 100 + (tiempoRestante / 100); // Los puntos se suman con el tiempo restante

                // Asegurarse de que los puntos no sean negativos
                if (puntos < 0) puntos = 0;

                puntaje += puntos;
                textViewMonedas.setText(puntaje + " pts"); // Actualizar el TextView de puntos

                // Actualizar el puntaje en Firebase
                String jugadorId = jugadores.get(0); // Esto debería ser el jugador actual, usa el índice apropiado
                db.collection("salas").document(salaId)
                        .update("jugadores." + jugadorId + ".puntaje", puntaje)
                        .addOnSuccessListener(aVoid -> Log.d("Firebase", "Puntaje actualizado"))
                        .addOnFailureListener(e -> Log.w("Firebase", "Error al actualizar puntaje", e));

                Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            } else {
                // Respuesta incorrecta: cambiar el botón seleccionado a rojo
                botonSeleccionado.setBackgroundColor(Color.RED);
                Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();

                // Buscar la respuesta correcta y marcarla en verde
                if (btnOpcion1.getText().toString().equals(respuestaCorrecta)) {
                    btnOpcion1.setBackgroundColor(Color.GREEN);
                }
                if (btnOpcion2.getText().toString().equals(respuestaCorrecta)) {
                    btnOpcion2.setBackgroundColor(Color.GREEN);
                }
                if (btnOpcion3.getText().toString().equals(respuestaCorrecta)) {
                    btnOpcion3.setBackgroundColor(Color.GREEN);
                }
                if (btnOpcion4.getText().toString().equals(respuestaCorrecta)) {
                    btnOpcion4.setBackgroundColor(Color.GREEN);
                }
            }

            // Aumentar el contador de preguntas respondidas
            preguntasRespondidas++;

            // Retraso antes de pasar a la siguiente pregunta
            new android.os.Handler().postDelayed(this::mostrarNuevaPregunta, 700);

        }, 700);
    }

    private void terminarJuego() {
        Toast.makeText(this, "Fin del juego", Toast.LENGTH_SHORT).show();
        // Aquí puedes agregar lógica adicional para mostrar la puntuación final y finalizar la partida
    }

    @Override
    protected void onDestroy() {
        if (temporizador != null) {
            temporizador.cancel();
        }
        super.onDestroy();
    }
}
