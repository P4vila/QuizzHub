package com.example.quizzhub;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private static final int SERVER_PORT = 12345;
    private static List<Socket> players = new ArrayList<>();  // Lista de jugadores conectados
    private static int playerCount = 0;  // Contador de jugadores

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Servidor iniciado. Esperando jugadores...");

            while (true) {
                // Acepta la conexión de un nuevo jugador
                Socket playerSocket = serverSocket.accept();
                players.add(playerSocket);  // Añadir el socket del jugador a la lista
                playerCount++;  // Incrementar el contador de jugadores

                // Mostrar el mensaje en la consola con el número de jugadores conectados
                System.out.println("Jugador conectado. Número de jugadores en la sala: " + playerCount);

                // Crea un nuevo hilo para manejar la comunicación con este jugador
                new Thread(new PlayerHandler(playerSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clase que maneja la comunicación con cada jugador
    private static class PlayerHandler implements Runnable {
        private Socket playerSocket;
        private PrintWriter out;
        private BufferedReader in;

        public PlayerHandler(Socket playerSocket) {
            this.playerSocket = playerSocket;
        }

        @Override
        public void run() {
            try {
                // Inicializa los streams de entrada y salida
                in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                out = new PrintWriter(playerSocket.getOutputStream(), true);

                // Muestra un mensaje de bienvenida al jugador
                out.println("¡Bienvenido al juego!");

                // Escuchar mensajes del jugador
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("LOGIN:")) {
                        String username = message.substring(6);  // Obtener el nombre de usuario
                        System.out.println("Jugador " + username + " ha iniciado sesión.");
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Cuando un jugador se desconecta, se elimina de la lista y se decrementa el contador
                try {
                    if (playerSocket != null) {
                        playerSocket.close();
                        synchronized (players) {
                            players.remove(playerSocket);
                            playerCount--;  // Decrementar el contador de jugadores
                            System.out.println("Jugador desconectado. Número de jugadores en la sala: " + playerCount);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }}
