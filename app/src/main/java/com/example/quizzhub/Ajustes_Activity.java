package com.example.quizzhub;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class Ajustes_Activity extends AppCompatActivity {

    private boolean isSoundOn = true;  // Para controlar el estado del sonido
    private boolean isVolumeOn = true; // Para controlar el estado del volumen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        // Configuración de los botones e imágenes
        ImageButton imageButton3 = findViewById(R.id.imageButton3);
        ImageView imageView5 = findViewById(R.id.imageView5);
        ImageView imageView6 = findViewById(R.id.imageView6);
        ImageButton imageButton2 = findViewById(R.id.imageButton2);
        ImageButton imageButton4 = findViewById(R.id.imageButton4);
        ImageView imageView3 = findViewById(R.id.imageView3); // ImageView que cambia de idioma
        TextView textViewIdiomaSeleccionado = findViewById(R.id.idioma_seleccionado);  // TextView para mostrar el idioma

        // Acción para volver atrás cuando se hace clic en imageButton3
        imageButton3.setOnClickListener(v -> {
            Intent intent = new Intent(Ajustes_Activity.this, Menu_Principal_Activity.class);
            startActivity(intent);
            finish();  // Finaliza la actividad actual para que no se quede en el back stack
        });

        // Acción para alternar el estado de los efectos de sonido
        imageView5.setOnClickListener(v -> {
            if (isSoundOn) {
                imageView5.setImageResource(R.drawable.music_mute_logo); // Cambiar a mute
                Toast.makeText(Ajustes_Activity.this, "Efectos de sonido desactivados (Off)", Toast.LENGTH_SHORT).show();
            } else {
                imageView5.setImageResource(R.drawable.music_logo); // Cambiar a sonido activado
                Toast.makeText(Ajustes_Activity.this, "Efectos de sonido activados (On)", Toast.LENGTH_SHORT).show();
            }
            isSoundOn = !isSoundOn; // Alternar el estado
        });

        // Acción para alternar el estado del volumen
        imageView6.setOnClickListener(v -> {
            if (isVolumeOn) {
                imageView6.setImageResource(R.drawable.volume_mute_logo); // Cambiar a mute
                Toast.makeText(Ajustes_Activity.this, "Volumen de música desactivado (Off)", Toast.LENGTH_SHORT).show();
            } else {
                imageView6.setImageResource(R.drawable.volume_logo); // Cambiar a volumen activado
                Toast.makeText(Ajustes_Activity.this, "Volumen de música activado (On)", Toast.LENGTH_SHORT).show();
            }
            isVolumeOn = !isVolumeOn; // Alternar el estado
        });

        // Acción para cambiar el idioma a inglés con un AlertDialog
        imageButton2.setOnClickListener(v -> {
            showLanguageChangeDialog("es", "en", imageView3, textViewIdiomaSeleccionado);  // Cambiar de español a inglés
        });

        // Acción para cambiar el idioma a español con un AlertDialog
        imageButton4.setOnClickListener(v -> {
            showLanguageChangeDialog("en", "es", imageView3, textViewIdiomaSeleccionado);  // Cambiar de inglés a español
        });

        // Verificar el idioma guardado en SharedPreferences y actualizar la imagen de imageView3
        updateLanguageImage(imageView3);
        updateLanguageText(textViewIdiomaSeleccionado);  // Actualizar el texto del idioma seleccionado
    }

    // Método para mostrar el AlertDialog de cambio de idioma
    private void showLanguageChangeDialog(String currentLang, String targetLang, ImageView imageView3, TextView textViewIdiomaSeleccionado) {
        String messageEs = "Se cambiará el idioma a inglés. ¿Estás seguro?";  // Español a inglés
        String messageEn = "The language will be changed to Spanish. Are you sure?"; // Inglés a español

        String messageToShow = currentLang.equals("es") ? messageEs : messageEn;
        String toastMessage = currentLang.equals("es") ? "The language has been changed to English" : "El idioma ha sido cambiado a español";

        // Crear el AlertDialog
        new AlertDialog.Builder(Ajustes_Activity.this)
                .setTitle("Confirmación")
                .setMessage(messageToShow)  // Mostrar el mensaje correspondiente
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Si el usuario confirma, cambiar el idioma
                    if (targetLang.equals("en")) {
                        changeLanguageToEnglish(imageView3, textViewIdiomaSeleccionado);
                    } else {
                        changeLanguageToSpanish(imageView3, textViewIdiomaSeleccionado);
                    }

                    // Mostrar mensaje de confirmación en el idioma opuesto
                    new Handler().postDelayed(() -> {
                        Toast.makeText(Ajustes_Activity.this, toastMessage, Toast.LENGTH_SHORT).show();
                    }, 1000); // Retraso de 1 segundo para mostrar el mensaje
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Si el usuario cancela, no hacer nada
                    dialog.dismiss();
                })
                .setCancelable(false) // No se puede cerrar el diálogo tocando fuera de él
                .show();
    }

    // Método para cambiar el idioma a inglés
    private void changeLanguageToEnglish(ImageView imageView3, TextView textViewIdiomaSeleccionado) {
        Locale locale = new Locale("en"); // Idioma inglés
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Cambiar la imagen de imageView3 a la bandera británica
        imageView3.setImageResource(R.drawable.britanic);

        // Cambiar el texto de textViewIdiomaSeleccionado a "English"
        textViewIdiomaSeleccionado.setText("English");

        // Guardar el idioma en SharedPreferences
        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", "en");
        editor.apply();

        // Reiniciar la actividad para que el cambio de idioma surta efecto
        Intent intent = new Intent(Ajustes_Activity.this, Ajustes_Activity.class);
        startActivity(intent);
        finish();
    }

    // Método para cambiar el idioma a español
    private void changeLanguageToSpanish(ImageView imageView3, TextView textViewIdiomaSeleccionado) {
        Locale locale = new Locale("es"); // Idioma español
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Restaurar la imagen original de imageView3
        imageView3.setImageResource(R.drawable.espana);  // Usa la imagen original que tengas

        // Cambiar el texto de textViewIdiomaSeleccionado a "Español"
        textViewIdiomaSeleccionado.setText("Español");

        // Guardar el idioma en SharedPreferences
        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", "es");
        editor.apply();

        // Reiniciar la actividad para que el cambio de idioma surta efecto
        Intent intent = new Intent(Ajustes_Activity.this, Ajustes_Activity.class);
        startActivity(intent);
        finish();
    }

    // Método para actualizar la imagen de imageView3 según el idioma
    private void updateLanguageImage(ImageView imageView3) {
        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String language = preferences.getString("language", "es");  // Valor por defecto es español

        if (language.equals("en")) {
            imageView3.setImageResource(R.drawable.britanic);  // Bandera británica si es inglés
        } else {
            imageView3.setImageResource(R.drawable.espana);  // Imagen original si es español
        }
    }

    // Método para actualizar el texto de textViewIdiomaSeleccionado según el idioma
    private void updateLanguageText(TextView textViewIdiomaSeleccionado) {
        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String language = preferences.getString("language", "es");  // Valor por defecto es español

        if (language.equals("en")) {
            textViewIdiomaSeleccionado.setText("English");
        } else {
            textViewIdiomaSeleccionado.setText("Español");
        }
    }
}
