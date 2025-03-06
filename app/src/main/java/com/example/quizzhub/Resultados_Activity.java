package com.example.quizzhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Resultados_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        // Obtener los datos enviados desde la actividad de partida
        int puntos = getIntent().getIntExtra("PUNTOS", 0);
        int correctas = getIntent().getIntExtra("CORRECTAS", 0);
        int incorrectas = getIntent().getIntExtra("INCORRECTAS", 0);

        // Referencias a los TextViews para mostrar los resultados
        TextView textViewPuntos = findViewById(R.id.textView15);  // Asegúrate de que este ID esté en tu XML
        textViewPuntos.setText(puntos + " pts");

        TextView textViewCorrectas = findViewById(R.id.textView9);  // Asegúrate de que este ID esté en tu XML
        textViewCorrectas.setText("Correctas: " + correctas);

        TextView textViewIncorrectas = findViewById(R.id.textView10);  // Asegúrate de que este ID esté en tu XML
        textViewIncorrectas.setText("Incorrectas: " + incorrectas);

        // Configuración del botón "Ver Ranking de Sala"
        Button buttonVerRanking = findViewById(R.id.button_ver_ranking);
        buttonVerRanking.setOnClickListener(v -> {
            // Navegar a la actividad de ranking
            Intent intent = new Intent(Resultados_Activity.this, Ranking_Activity.class);
            startActivity(intent);
        });
    }
}
