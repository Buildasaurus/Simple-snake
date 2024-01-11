package com.snake.Model;

public class Save {
    private String name;
    private GameSettings gameSettings;
    private GameState gameState;

    public Save() {
    }

    public Save(String name, GameSettings gameSettings, GameState gameState) {
        this.name = name;
        this.gameSettings = gameSettings;
        this.gameState = gameState;
    }

    public String getName() {
        return name;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public GameState getGameState() {
        return gameState;
    }
}
