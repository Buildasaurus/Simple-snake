package com.snake.Model;

import java.util.ArrayList;
import java.util.List;

public class GameSettings
{
    private transient List<Runnable> listeners = new ArrayList<>();
    public final static int maxPlayerCount = 1;
    private GameMode gameMode;
    private Level level;
    private int playerCount;
    private boolean extraVision;
    private int extraVisionDepth = 2;
    private int columnCount = 30;
    private int rowCount = 30;

    public GameSettings()
    {
        extraVision = true;
        playerCount = 1;
        gameMode = GameMode.Normal;
        level = Level.Empty;
    }

    /**
     * GameMode can be a variant of different things. What changes is e.g. the acceleration of the
     * snake.
     */
    public enum GameMode
    {
        Easy, Normal, Hard, Insane
    }

    /**
     * This can be extended with eg "block in the middle", "racetrack" or "maze"
     */
    public enum Level
    {
        Empty, Random
    }

    /**
     * Sets the {@link #playerCount player count} if the given player count is valid, and can be
     * handled.
     *
     * @return Returns if the update was succesful
     */
    public boolean setPlayerCount(int newPlayerCount)
    {
        if (newPlayerCount > 0 && newPlayerCount <= maxPlayerCount)
        {
            playerCount = newPlayerCount;
            return true;
        }
        return false;
    }

    public int getPlayerCount()
    {
        return playerCount;
    }

    public int[] getAllPossiblePlayerCounts()
    {
        int[] playerCounts = new int[maxPlayerCount];
        for (int i = 1; i <= maxPlayerCount; i++)
        {
            playerCounts[i - 1] = i;
        }
        return playerCounts;
    }

    public Level getLevel()
    {
        return level;
    }

    public void setLevel(Level level)
    {
        this.level = level;
    }

    public GameMode getGameMode()
    {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode)
    {
        this.gameMode = gameMode;
    }

    public int getSpeed()
    {
        switch (gameMode)
        {
            case Easy:
                return 2;
            case Normal:
                return 3;
            case Hard:
                return 4;
            case Insane:
                return 6;
            default: // shouldn't be hit
                System.out.println(
                        "something is wrong in gamesettings, getspeed() method. A gameMode isn't taken care of");
                return -1;
        }
    }

    public double getLevelFill()
    {
        if (level == Level.Empty)
        {
            return 0;
        }
        else
        {
            switch (gameMode)
            {
                case Easy:
                    return 0.35;
                case Normal:
                    return 0.41;
                case Hard:
                    return 0.45;
                case Insane:
                    return 0.52;
                default:
                    return 0;
            }
        }
    }

    public boolean getExtraVision()
    {
        return extraVision;
    }

    public int getExtraVisionDepth()
    {
        return extraVisionDepth;
    }

    public void setExtraVision(boolean value)
    {
        extraVision = value;
        for (Runnable listener : listeners)
        {
            listener.run();
        }
    }

    public void addExtraVisionListener(Runnable listener)
    {
        listeners.add(listener);
    }

    public int getColumnCount()
    {
        return columnCount;
    }

    public void setColumnCount(int columnCount)
    {
        this.columnCount = columnCount;
    }

    public int getRowCount()
    {
        return rowCount;
    }

    public void setRowCount(int rowCount)
    {
        this.rowCount = rowCount;
    }

    /**
     * Calculates the actual rowcount, after extravision depth has been added.
     *
     * @return
     */
    public int getExtendedRowCount()
    {
        return rowCount + extraVisionDepth*2;
    }


    /**
     * Calculates the actual columncount, after extravision depth has been added.
     * @return
     */
    public int getExtendedColumnCount()
    {
        return columnCount + extraVisionDepth*2;

    }
}
