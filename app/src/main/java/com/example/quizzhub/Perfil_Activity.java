package com.example.quizzhub;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Perfil_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth; // Instancia de FirebaseAuth
    private TextView textUsername; // Para mostrar el nombre de usuario
    private Button buttonSalir; // El botón para salir

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializamos FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Referenciamos los elementos de la UI
        textUsername = findViewById(R.id.textusername);
        buttonSalir = findViewById(R.id.button_salir);

        // Actualizamos el nombre de usuario al iniciar sesión
        String userEmail = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : "Usuario no logueado";
        String userName = userEmail != null ? userEmail.split("@")[0] : "Usuario no logueado"; // Extraemos la parte antes de "@" como nombre de usuario
        textUsername.setText(userName);

        // Acción al hacer clic en "Salir" (Logout)
        buttonSalir.setOnClickListener(v -> {
            // Crear un AlertDialog con mensaje
            new AlertDialog.Builder(Perfil_Activity.this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                    .setCancelable(false) // Evita que se cierre tocando fuera de la ventana
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Realizar el logout en Firebase
                            mAuth.signOut(); // Esto cierra la sesión de Firebase

                            // Retraso de 2 segundos antes de redirigir a la pantalla de Login
                            new Handler().postDelayed(() -> {
                                Toast.makeText(Perfil_Activity.this, "Sesión cerrada. ¡Hasta pronto!", Toast.LENGTH_SHORT).show();
                                Intent intentLogout = new Intent(Perfil_Activity.this, Login_Activity.class);
                                startActivity(intentLogout);
                                finish(); // Termina esta actividad para evitar que el usuario pueda regresar con el botón atrás
                            }, 2000); // 2 segundos de retraso
                        }
                    })
                    .setNegativeButton("No", null) // No hace nada si se cancela
                    .show(); // Muestra el dialogo
        });
    }
}
