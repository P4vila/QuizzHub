package com.example.quizzhub;

public class Jugador {
    private String name;
    private int score;

    // Constructor vacío necesario para Firebase
    public Jugador() {}

    public Jugador(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
