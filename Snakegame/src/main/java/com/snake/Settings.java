package com.snake;

import com.snake.Model.GameSettings;

public class Settings
{
    public static int windowHeight = 600;
    public static int windowWidth = 600;

    public static void setGameSettings(GameSettings settings)
    {
        gameSettings = settings;
    }

    public static GameSettings getGameSettings()
    {
        return gameSettings;
    }

    private static GameSettings gameSettings = new GameSettings();
}
