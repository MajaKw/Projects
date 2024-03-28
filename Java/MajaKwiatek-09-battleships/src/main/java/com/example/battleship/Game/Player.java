package com.example.battleship.Game;

public enum Player {
    SERVER("Server"), CLIENT("Client");

    private String name;

    Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
