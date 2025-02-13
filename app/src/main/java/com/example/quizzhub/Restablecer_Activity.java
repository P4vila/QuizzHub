package com.example.quizzhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Restablecer_Activity extends AppCompatActivity {

    private EditText textoRestablecerContrasena;
    private Button btnRestablecer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer);

        // Inicializar las vistas
        textoRestablecerContrasena = findViewById(R.id.texto_restablecer_contrasena);
        btnRestablecer = findViewById(R.id.btn_restablecer);

        // Configurar el botón "Restablecer"
        btnRestablecer.setOnClickListener(v -> {
            String input = textoRestablecerContrasena.getText().toString().trim();

            if (TextUtils.isEmpty(input)) {
                // Si el campo está vacío, mostrar un mensaje de error
                Toast.makeText(Restablecer_Activity.this, "Por favor ingresa un correo electrónico o teléfono", Toast.LENGTH_SHORT).show();
            } else if (!isValidEmail(input) && !isValidPhoneNumber(input)) {
                // Si el correo electrónico o teléfono no es válido, mostrar un mensaje de error
                Toast.makeText(Restablecer_Activity.this, "Por favor ingresa un correo electrónico o teléfono válido", Toast.LENGTH_SHORT).show();
            } else {
                // Si todo es válido, mostrar un mensaje de confirmación
                Toast.makeText(Restablecer_Activity.this, "Te hemos enviado un código al correo o teléfono. Por favor revisa tu bandeja de entrada o mensajes.", Toast.LENGTH_LONG).show();

                // Agregar un retraso antes de redirigir a la actividad de introducir el código
                new android.os.Handler().postDelayed(() -> {
                    // Redirigir a la actividad para introducir el código
                    Intent intent = new Intent(Restablecer_Activity.this, Restablecer_Codigo_Activity.class);
                    startActivity(intent);
                }, 2000); // 2 segundos de retraso
            }
        });
    }

    // Método para verificar si el texto es un correo electrónico válido
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Método para verificar si el texto es un número de teléfono válido (con al menos 9 dígitos)
    private boolean isValidPhoneNumber(String phone) {
        // Verifica si el teléfono tiene 9 dígitos numéricos
        return phone.matches("\\d{9}");
    }
}
