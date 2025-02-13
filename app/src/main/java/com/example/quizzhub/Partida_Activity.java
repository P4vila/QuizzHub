package com.example.quizzhub;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;

public class Partida_Activity extends AppCompatActivity {

    private Button btnOpcion1, btnOpcion2, btnOpcion3, btnOpcion4;
    private TextView textViewPregunta;
    private String respuestaCorrecta; // Respuesta correcta
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean respuestaDada = false; // Para controlar si ya se respondió la pregunta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        // Inicializa los componentes
        btnOpcion1 = findViewById(R.id.btnOpcion1);
        btnOpcion2 = findViewById(R.id.btnOpcion2);
        btnOpcion3 = findViewById(R.id.btnOpcion3);
        btnOpcion4 = findViewById(R.id.btnOpcion4);
        textViewPregunta = findViewById(R.id.textViewPregunta);

        // Cargar una pregunta aleatoria desde Firestore
        cargarPregunta();

        // Configurar listeners para los botones
        btnOpcion1.setOnClickListener(v -> verificarRespuesta(btnOpcion1, btnOpcion1.getText().toString()));
        btnOpcion2.setOnClickListener(v -> verificarRespuesta(btnOpcion2, btnOpcion2.getText().toString()));
        btnOpcion3.setOnClickListener(v -> verificarRespuesta(btnOpcion3, btnOpcion3.getText().toString()));
        btnOpcion4.setOnClickListener(v -> verificarRespuesta(btnOpcion4, btnOpcion4.getText().toString()));
    }

    // Método para cargar una pregunta aleatoria desde Firestore
    private void cargarPregunta() {
        if (respuestaDada) return;  // Si ya se respondió una pregunta, no cargamos otra.

        // Reseteamos los botones y colores
        resetearColores();

        // Cargamos la pregunta desde Firestore
        db.collection("Preguntas")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Elegir una pregunta aleatoria
                        int randomIndex = (int) (Math.random() * queryDocumentSnapshots.size());
                        QueryDocumentSnapshot documentSnapshot = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(randomIndex);

                        // Obtener los valores de la pregunta y las opciones
                        String pregunta = documentSnapshot.getString("Pregunta");
                        respuestaCorrecta = documentSnapshot.getString("Respuesta correcta");
                        // Las opciones están dentro de un array, por lo que las obtenemos de esa manera
                        List<String> opciones = (List<String>) documentSnapshot.get("Opciones");

                        // Verificamos si las opciones se cargaron correctamente
                        if (opciones != null && opciones.size() == 4) {
                            // Asignar la pregunta y las opciones a la UI
                            textViewPregunta.setText(pregunta);
                            btnOpcion1.setText(opciones.get(0));
                            btnOpcion2.setText(opciones.get(1));
                            btnOpcion3.setText(opciones.get(2));
                            btnOpcion4.setText(opciones.get(3));
                        }

                        // Imprimir los datos para verificar que todo está llegando correctamente
                        Log.d("Firestore", "Pregunta: " + pregunta);
                        Log.d("Firestore", "Opciones: " + opciones);
                        Log.d("Firestore", "Respuesta correcta: " + respuestaCorrecta);
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error obteniendo la pregunta", e));
    }

    // Método para verificar la respuesta seleccionada
    private void verificarRespuesta(Button botonSeleccionado, String respuestaSeleccionada) {
        // Evitar que se pueda responder más de una vez
        if (respuestaDada) return;

        respuestaDada = true;  // Marcar que la respuesta ya ha sido dada

        // Cambiar color de los botones
        if (respuestaSeleccionada.equals(respuestaCorrecta)) {
            botonSeleccionado.setBackgroundColor(getResources().getColor(R.color.correcta_color));  // Correcta
        } else {
            botonSeleccionado.setBackgroundColor(getResources().getColor(R.color.seleccionada_color));  // Incorrecta
            // Cambiar el color de la respuesta correcta a verde
            cambiarColorCorrecto();
        }

        // Esperar 1 segundo (1000 ms) para mostrar el color de la respuesta correcta
        new Handler().postDelayed(() -> {
            // Si la respuesta es incorrecta, la ponemos roja
            if (!respuestaSeleccionada.equals(respuestaCorrecta)) {
                botonSeleccionado.setBackgroundColor(getResources().getColor(R.color.incorrecta_color));  // Incorrecta
            }
            // Esperamos otro segundo antes de cargar una nueva pregunta
            new Handler().postDelayed(() -> {
                // Volver a cargar una nueva pregunta
                respuestaDada = false;
                cargarPregunta();
            }, 1500);
        }, 1500);
    }

    // Método para cambiar el color de la respuesta correcta a verde
    private void cambiarColorCorrecto() {
        // Revisamos todos los botones y cambiamos el color del correcto
        if (btnOpcion1.getText().toString().equals(respuestaCorrecta)) {
            btnOpcion1.setBackgroundColor(getResources().getColor(R.color.correcta_color));
        } else if (btnOpcion2.getText().toString().equals(respuestaCorrecta)) {
            btnOpcion2.setBackgroundColor(getResources().getColor(R.color.correcta_color));
        } else if (btnOpcion3.getText().toString().equals(respuestaCorrecta)) {
            btnOpcion3.setBackgroundColor(getResources().getColor(R.color.correcta_color));
        } else if (btnOpcion4.getText().toString().equals(respuestaCorrecta)) {
            btnOpcion4.setBackgroundColor(getResources().getColor(R.color.correcta_color));
        }
    }

    // Método para resetear los colores y el estado de los botones
    private void resetearColores() {
        // Reseteamos el color de los botones a su color original
        btnOpcion1.setBackgroundColor(getResources().getColor(R.color.preguntas_azul));
        btnOpcion2.setBackgroundColor(getResources().getColor(R.color.preguntas_azul));
        btnOpcion3.setBackgroundColor(getResources().getColor(R.color.preguntas_azul));
        btnOpcion4.setBackgroundColor(getResources().getColor(R.color.preguntas_azul));
    }
}
