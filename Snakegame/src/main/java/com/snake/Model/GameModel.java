package com.snake.Model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import com.snake.App;
import com.snake.Settings;
import org.reflections.Reflections;

public class GameModel
{
    /**
     * The board of snake, of size rowcount and columncount stored in the {@link com.snake.Settings
     * Settings}.
     */
    public Tile[][] board;

    private int rowCount;
    private int columnCount;
    private Snake player;
    public int winner = -1;
    int freeSquares = 0;

    /**
     * Imports the current settings from {@link com.snake.Settings Settings} and initializes a board
     * with tiles, and initializes the players. Also spawns random {@link com.snake.Model.Fruit
     * fruits} on the level.
     */
    public GameModel()
    {
        this.rowCount = Settings.getGameSettings().getRowCount();
        this.columnCount = Settings.getGameSettings().getColumnCount();
        board = new Tile[rowCount][columnCount];
        Vector startSpawnPoint = new Vector(columnCount / 2, rowCount / 2 - (getPlayerCount() - 1));

        changedTiles = new ArrayList<>();
        player = new Snake(board, startSpawnPoint.add(0, 0), startSpawnPoint.add(1, 0),
                new Vector(-1, 0), 0);
        changedTiles.add(startSpawnPoint.add(0, 0));
        changedTiles.add(startSpawnPoint.add(-1, 0));


        Apple apple = new Apple();
        while (board[apple.getPosition().y][apple.getPosition().x] != null)
        {
            apple.setRandomPosition();
        }
        changedTiles.add(apple.getPosition());
        board[apple.getPosition().y][apple.getPosition().x] = apple;
        freeSquares = rowCount * columnCount - 1 - 2;

    }

    ArrayList<Vector> changedTiles = new ArrayList<Vector>();

    /**
     * Updates the board to the next state. Only updates the snakes whose id is in the
     * playersToUpdate
     *
     * @param playersToUpdate
     */
    public void nextState()
    {
        changedTiles.clear();

        // save which tiles have been modified
        changedTiles.add(player.getHeadPosition());
        changedTiles.add(player.getNextHeadPosition());
        changedTiles.add(player.getTailPosition());
        changedTiles.add(player.getNextTailPosition());

        // Now we check whether the next position will be clear, in the next frame.
        // This the snakes won't know, as there might be a snake tail that disappears, or that
        // doesn't
        // disappear, if the snake eats an apple, or dies, or is colliding.
        boolean willClear = true;
        Vector vec = player.getNextHeadPosition();
        // essentially looping through the playerss
        Tile tile = board[vec.y][vec.x];
        if (tile instanceof SnakeTile)
        {
            willClear = false;
            SnakeTile snakeTile = ((SnakeTile) tile);
            if (!player.willGrow(board) && snakeTile.tileType == TileType.Snaketail)
            {
                willClear = true;
            }
        }



        player.calculateNextFrame(willClear);
        if (player.Fruiteaten() != null)
        {
            Apple apple = new Apple();
            freeSquares--;

            if (freeSquares < 0)
            {
                return;
            }
            while (board[apple.getPosition().y][apple.getPosition().x] != null)
            {
                apple.setRandomPosition();
            }
            board[apple.getPosition().y][apple.getPosition().x] = apple;
            changedTiles.add(apple.getPosition());
        }


    }

    public ArrayList<Vector> getChangedPositions()
    {
        return changedTiles;
    }

    public void setDirection(Vector direction)
    {
        player.setDirection(direction);
    }

    boolean gameWon = false;

    public boolean gameOver()
    {
        if (freeSquares == -1)
        {
            gameWon = player.isAlive();
            return true;
        }
        return !player.isAlive();
    }


    public Tile[][] getBoard()
    {
        return board;
    }



    public double getSpeed(int playerIndex)
    {
        return player.getSpeed();
    }

    public int getAlivePlayerCount()
    {
        return player.isAlive() ? 1 : 0;
    }

    public int getSnakeLength()
    {
        return player.getSnakeLength();
    }

    public int getPlayerCount()
    {
        return 1;
    }



}
