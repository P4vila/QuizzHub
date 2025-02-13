package com.example.quizzhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;  // Instancia de FirebaseAuth
    private FirebaseAnalytics mFirebaseAnalytics;  // Instancia de FirebaseAnalytics

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializamos FirebaseAuth y FirebaseAnalytics
        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Enlazamos los elementos de la interfaz
        emailEditText = findViewById(R.id.hint_email);
        passwordEditText = findViewById(R.id.hint_password);
        loginButton = findViewById(R.id.btn_login);

        // Enlazamos el TextView para "¿Has olvidado tu contraseña?"
        TextView forgotPasswordTextView = findViewById(R.id.forgot_password);
        forgotPasswordTextView.setOnClickListener(v -> {
            // Registrar evento cuando el usuario haga clic en "Olvidé mi contraseña"
            logForgotPasswordEvent();
            // Iniciamos la actividad Restablecer_Activity cuando el texto es clickeado
            Intent intent = new Intent(Login_Activity.this, Restablecer_Activity.class);
            startActivity(intent);
        });

        // Enlazamos el TextView para "Registro"
        TextView registerTextView = findViewById(R.id.register_text);
        registerTextView.setOnClickListener(v -> {
            // Registrar evento cuando el usuario haga clic en "Registro"
            logRegisterEvent();
            // Iniciamos la actividad Registro_Activity cuando el texto es clickeado
            Intent intent = new Intent(Login_Activity.this, Registro_Activity.class);
            startActivity(intent);
        });

        // Configuramos el listener del botón de login
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validación de campos vacíos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login_Activity.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
                // Registrar evento cuando los campos están vacíos
                logLoginAttemptEvent("failed_empty_fields");
            } else {
                // Llamamos al método de inicio de sesión de Firebase
                logLoginAttemptEvent("attempt");
                loginUser(email, password);
            }
        });
    }

    // Método para iniciar sesión con Firebase Authentication
    private void loginUser(String email, String password) {
        // Mostrar un mensaje mientras se intenta iniciar sesión
        Toast.makeText(Login_Activity.this, "Iniciando sesión, por favor espera...", Toast.LENGTH_SHORT).show();

        // Usamos Firebase Authentication para autenticar al usuario
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Login_Activity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                        // Registrar el evento de inicio de sesión en Firebase Analytics
                        logSignInEvent(user);

                        // Redirigir al usuario a la actividad principal (Menu_Principal_Activity)
                        Intent intent = new Intent(Login_Activity.this, Menu_Principal_Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Si el inicio de sesión falla, mostrar un mensaje de error
                        Toast.makeText(Login_Activity.this, "Error en el inicio de sesión: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        // Registrar evento de intento fallido
                        logLoginAttemptEvent("failed");
                    }
                });
    }

    // Método para registrar el evento de inicio de sesión en Firebase Analytics
    private void logSignInEvent(FirebaseUser user) {
        if (user != null) {
            // Crear un bundle con la información del usuario
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.METHOD, "email_password");  // Método de autenticación
            bundle.putString(FirebaseAnalytics.Param.SUCCESS, "true");  // Éxito en el login
            bundle.putString(FirebaseAnalytics.Param.VALUE, user.getEmail());  // Correo electrónico del usuario

            // Registrar el evento "login" en Firebase Analytics
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
        }
    }

    // Método para registrar un intento de login (ya sea exitoso o fallido)
    private void logLoginAttemptEvent(String status) {
        Bundle bundle = new Bundle();
        bundle.putString("status", status);
        mFirebaseAnalytics.logEvent("login_attempt", bundle);
    }

    // Método para registrar el evento "Olvidé mi contraseña"
    private void logForgotPasswordEvent() {
        mFirebaseAnalytics.logEvent("forgot_password_clicked", null);
    }

    // Método para registrar el evento "Registro"
    private void logRegisterEvent() {
        mFirebaseAnalytics.logEvent("register_clicked", null);
    }
}
