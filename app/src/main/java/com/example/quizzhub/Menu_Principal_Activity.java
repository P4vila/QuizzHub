package com.example.quizzhub;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Handler;

public class Menu_Principal_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth; // Instancia de FirebaseAuth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Configurar BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // ... (navegación como antes)

        // Configurar los botones
        Button buttonJugar = findViewById(R.id.button_jugar);
        Button buttonCategorias = findViewById(R.id.button_categorias);
        Button buttonAjustes = findViewById(R.id.button_ajustes);
        Button buttonSalir = findViewById(R.id.button_salir);

        // Acción al hacer clic en "Jugar"
        buttonJugar.setOnClickListener(v -> {
            Intent intentPlay = new Intent(Menu_Principal_Activity.this, Modo_de_juego_Activity.class);
            startActivity(intentPlay);
        });

        // Acción al hacer clic en "Categorías"
        buttonCategorias.setOnClickListener(v -> {
            Intent intentCategories = new Intent(Menu_Principal_Activity.this, Categorias_Activity.class);
            startActivity(intentCategories);
        });

        // Acción al hacer clic en "Ajustes"
        buttonAjustes.setOnClickListener(v -> {
            Intent intentSettings = new Intent(Menu_Principal_Activity.this, Ajustes_Activity.class);
            startActivity(intentSettings);
        });

        // Acción al hacer clic en "Salir" (Logout)
        buttonSalir.setOnClickListener(v -> {
            // Crear un AlertDialog con mensaje
            new AlertDialog.Builder(Menu_Principal_Activity.this)
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
                                Toast.makeText(Menu_Principal_Activity.this, "Sesión cerrada. ¡Hasta pronto!", Toast.LENGTH_SHORT).show();
                                Intent intentLogout = new Intent(Menu_Principal_Activity.this, Login_Activity.class);
                                startActivity(intentLogout);
                                finish(); // Termina esta actividad para evitar que el usuario pueda regresar con el botón atrás
                            }, 2000); // 2 segundos de retraso
                        }
                    })
                    .setNegativeButton("No", null) // No hace nada si se cancela
                    .show();
        });

        // Configurar listener de selección para el BottomNavigationView
        navView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                return true;
            } else if (item.getItemId() == R.id.navigation_play) {
                Intent intent = new Intent(Menu_Principal_Activity.this, Modo_de_juego_Activity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.navigation_scores) {
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                Intent intent = new Intent(Menu_Principal_Activity.this, Perfil_Activity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
