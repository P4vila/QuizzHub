package com.example.quizzhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class modo_de_juego extends AppCompatActivity {

    private ImageButton imageButton;
    private Button buttonSalaPrivada;
    private Button buttonContraReloj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_de_juego); // Asegúrate de que el layout sea correcto

        // Inicializar los botones y el ImageButton
        imageButton = findViewById(R.id.imageButton);
        buttonSalaPrivada = findViewById(R.id.button_salapriv);
        buttonContraReloj = findViewById(R.id.button_contrarreloj);

        // Acción para cuando se hace clic en el ImageButton (ir a Menu_Principal_Activity)
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(modo_de_juego.this, Menu_Principal_Activity.class);
                startActivity(intent);
                finish(); // Opcional, si no quieres que la actividad actual quede en el stack
            }
        });

        // Acción para cuando se hace clic en el botón de "Sala Online"
        buttonSalaPrivada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(modo_de_juego.this, com.example.quizzhub.Sala_Online.class);
                startActivity(intent);
            }
        });

        // Acción para cuando se hace clic en el botón "Contra Reloj" (puedes agregar la lógica necesaria aquí)
        buttonContraReloj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar la lógica de la acción del botón de Contra Reloj.
                // Por ejemplo, podrías iniciar un nuevo juego o actividad.
            }
        });
    }
}
