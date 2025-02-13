package com.example.quizzhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registro_Activity extends AppCompatActivity {

    private EditText hintName, hintTelephone, hintEmail, hintPassword;
    private FirebaseAuth mAuth;  // Instancia de FirebaseAuth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Inicializar los EditText
        hintName = findViewById(R.id.hint_name);
        hintTelephone = findViewById(R.id.hint_telephone);
        hintEmail = findViewById(R.id.hint_email);
        hintPassword = findViewById(R.id.hint_password);

        // Agregar OnClickListener al TextView de "Ya tienes cuenta"
        findViewById(R.id.already_account).setOnClickListener(v -> {
            // Ir a la actividad de login
            Intent intent = new Intent(Registro_Activity.this, Login_Activity.class);
            startActivity(intent);
        });

        // Agregar OnClickListener al botón de registro
        findViewById(R.id.btn_register).setOnClickListener(v -> {
            String name = hintName.getText().toString().trim();
            String telephone = hintTelephone.getText().toString().trim();
            String email = hintEmail.getText().toString().trim();
            String password = hintPassword.getText().toString().trim();

            // Validación de campos
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(Registro_Activity.this, "Por favor, ingresa tu nombre", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(telephone) || telephone.length() != 9 || !telephone.matches("[0-9]+")) {
                // Verificar que el teléfono tenga exactamente 9 números
                Toast.makeText(Registro_Activity.this, "El teléfono debe contener 9 dígitos numéricos", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Verificar que el email sea válido
                Toast.makeText(Registro_Activity.this, "Por favor, ingresa un email válido", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(Registro_Activity.this, "Por favor, ingresa una contraseña", Toast.LENGTH_SHORT).show();
            } else {
                // Registrar al usuario en Firebase
                registerUser(email, password);
            }
        });
    }

    private void registerUser(String email, String password) {
        // Mostrar el progreso de registro
        Toast.makeText(Registro_Activity.this, "Registrando, por favor espera...", Toast.LENGTH_SHORT).show();

        // Usar Firebase Authentication para registrar al usuario
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Registro_Activity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        // Aquí puedes redirigir al usuario al menú principal o a donde desees
                        Intent intent = new Intent(Registro_Activity.this, Login_Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Si el registro falla, mostrar el error
                        Toast.makeText(Registro_Activity.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
