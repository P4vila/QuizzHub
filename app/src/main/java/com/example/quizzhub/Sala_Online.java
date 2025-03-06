package com.example.quizzhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Sala_Online extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText hintName;
    private Button buttonCrearSala, buttonUnirSala;
    private TextView textSalaCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_online);

        db = FirebaseFirestore.getInstance();

        // Inicialización de vistas
        hintName = findViewById(R.id.hint_name);
        buttonCrearSala = findViewById(R.id.button_crear_sala);
        buttonUnirSala = findViewById(R.id.button_unir_sala);
        textSalaCodigo = findViewById(R.id.textView17);

        // Crear Sala
        buttonCrearSala.setOnClickListener(v -> {
            String nombreSala = "Sala 1"; // Nombre estático de la sala
            String jugador = "Jugador1";  // Nombre del jugador que crea la sala

            Map<String, Object> sala = Map.of(
                    "nombre", nombreSala,
                    "jugadores", Arrays.asList(jugador) // Lista de jugadores inicial
            );

            db.collection("salas")
                    .add(sala)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("Firebase", "Sala creada con ID: " + documentReference.getId());
                        // Mostrar el código de la sala al usuario
                        textSalaCodigo.setText("Código de sala: " + documentReference.getId());
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Firebase", "Error al crear sala", e);
                    });
        });

        // Unirse a una Sala
        buttonUnirSala.setOnClickListener(v -> {
            String salaId = hintName.getText().toString().trim(); // Obtener el código de la sala

            if (!salaId.isEmpty()) {
                db.collection("salas").document(salaId)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Log.d("Firebase", "Sala encontrada: " + documentSnapshot.getData());

                                // Recuperar la lista actual de jugadores
                                Object jugadoresObj = documentSnapshot.get("jugadores");
                                List<String> jugadores;
                                if (jugadoresObj instanceof List) {
                                    jugadores = (List<String>) jugadoresObj;
                                } else {
                                    jugadores = new ArrayList<>();
                                }

                                // Añadir el jugador actual a la lista
                                jugadores.add("Jugador2");

                                // Actualizar la lista de jugadores en Firebase
                                db.collection("salas").document(salaId)
                                        .update("jugadores", jugadores)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("Firebase", "Jugador unido a la sala");

                                            // Verificar si la sala tiene al menos 2 jugadores para iniciar el juego
                                            if (jugadores.size() >= 2) {
                                                // Pasar a la siguiente actividad del juego
                                                Intent intent = new Intent(Sala_Online.this, Partida_Activity.class);
                                                intent.putExtra("salaId", salaId);
                                                intent.putExtra("jugadores", new ArrayList<>(jugadores));
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(Sala_Online.this, "Se necesitan al menos 2 jugadores para iniciar el juego.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w("Firebase", "Error al unir jugador a la sala", e);
                                        });
                            } else {
                                Log.d("Firebase", "Sala no encontrada");
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.w("Firebase", "Error al acceder a la sala", e);
                        });
            } else {
                Log.d("Firebase", "Código de sala vacío");
            }
        });
    }
}
