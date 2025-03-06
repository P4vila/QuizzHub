package com.example.quizzhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.net.*;

public class GameClientActivity extends AppCompatActivity {
    private static final String SERVER_IP = "10.0.2.2";  // Dirección del servidor (Emulador)
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private TextView responseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_client);

        responseTextView = findViewById(R.id.responseTextView);

        // Conectar al servidor en un hilo separado
        new Thread(this::connectToServer).start();
    }

    private void sendLoginMessage(String username) {
        if (out != null) {
            out.println("LOGIN:" + username);  // Enviar un mensaje de login al servidor
        }
    }


    private void connectToServer() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Enviar el mensaje de bienvenida al cliente
            String serverMessage = in.readLine(); // "Bienvenido al juego!" (mensaje del servidor)

            // Mostrar el mensaje en la UI
            runOnUiThread(() -> responseTextView.setText(serverMessage));

            // Después de mostrar el mensaje, navegar al LoginActivity
            runOnUiThread(this::navigateToLogin);

        } catch (IOException e) {
            runOnUiThread(() -> Toast.makeText(GameClientActivity.this, "Error al conectar", Toast.LENGTH_SHORT).show());
            e.printStackTrace();
        }
    }

    private void navigateToLogin() {
        // Iniciar LoginActivity después de la conexión exitosa
        Intent intent = new Intent(GameClientActivity.this, Login_Activity.class);
        startActivity(intent);
        finish();  // Finaliza GameClientActivity para que no quede en el stack
    }
}