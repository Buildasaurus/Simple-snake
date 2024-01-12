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
        player = new Snake(board, startSpawnPoint.add(0, 0), startSpawnPoint.add(-1, 0),
                new Vector(1, 0), 0);
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
        changedTiles.add(player.getHeadPosition());
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
            SnakeTile snakeTile = ((SnakeTile) tile);
            if (!player.willGrow(board))
            {
                willClear = false;
            }
        }

        ArrayList<Fruit> fruitsToRespawn = new ArrayList<>();


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
        System.out.println("free squares at gameover Called: " + freeSquares);

        // Single player
        if (players.length == 1)
        {
            System.out.println(players[0].isAlive());
            if (freeSquares == -1)
            {
                gameWon = players[0].isAlive();
                return true;
            }
            return !players[0].isAlive();
        }
        // multiplayer
        else if (players.length > 1)
        {
            if (getAlivePlayerCount() < 2 || freeSquares == -1)
            {
                gameWon = getAlivePlayerCount() > 0 && freeSquares == -1;
                return true;
            }
            return false;
        }

        return true; // Neither multiplayer or singleplayer?? Should not happen
    }

    /**
     * Returns the text describing what player won.
     *
     * @return
     */
    public String getGameOverText()
    {
        if (gameOver())
        {
            if (gameWon)
            {
                return "You defeated snake!\nThere is no more space \nleft to spawn fruits.";
            }
            int playerID = getAlivePlayerID();
            if (playerID == -1 && players.length > 1)
            {
                return "All the remaining players\ndied at the same time!";
            }
            else
            {
                return players.length == 1 ? "You lost" : "Player " + (playerID + 1) + " Won!";
            }

        }
        return "Game is not over yet";
    }

    public Tile[][] getBoard()
    {
        return board;
    }

    /**
     * Figures out what the ID of the player that is alive is. If there is more or less than one
     * player alive, it returns -1
     *
     * @return The ID of the snake alive, from 0 to the playercount - 1.
     */
    public int getAlivePlayerID()
    {
        if (getAlivePlayerCount() == 1)
        {
            int playerID = -1;
            for (Snake snake : players)
            {
                if (snake.isAlive())
                {
                    playerID = snake.playerNumber;
                }
            }
            return playerID;
        }
        return -1;

    }

    public double getSpeed(int playerIndex)
    {
        return players[playerIndex].getSpeed();
    }

    public int getAlivePlayerCount()
    {
        int alivePlayerCount = players.length;
        for (Snake player : players)
        {
            if (!player.isAlive())
            {
                alivePlayerCount -= 1;
            }
        }

        return alivePlayerCount;
    }

    public int getSnakeLength(int player)
    {
        player = player % Settings.getGameSettings().getPlayerCount();
        return players[player].getSnakeLength();
    }

    public int getPlayerCount()
    {
        return players.length;
    }

    public GameState getGameState()
    {
        ArrayList<SnakeTile> snakeTiles = new ArrayList<>();
        ArrayList<Apple> apples = new ArrayList<>();
        ArrayList<Banana> bananas = new ArrayList<>();
        ArrayList<Cherry> cherries = new ArrayList<>();
        ArrayList<Wall> walls = new ArrayList<>();

        for (int i = 0; i < board.length; i++)
        {
            for (int n = 0; n < board[i].length; n++)
            {
                if (board[i][n] != null)
                {
                    switch (board[i][n].tileType)
                    {
                        case Snakehead:
                        case Snakebody:
                        case Snaketail:
                            board[i][n].position = new Vector(n, i);
                            snakeTiles.add((SnakeTile) board[i][n]);
                            break;

                        case Apple:
                            apples.add((Apple) board[i][n]);
                            break;

                        case Banana:
                            bananas.add((Banana) board[i][n]);
                            break;

                        case Cherry:
                            cherries.add((Cherry) board[i][n]);
                            break;

                        case Wall:
                            board[i][n].position = new Vector(n, i);
                            walls.add((Wall) board[i][n]);
                            break;

                        default:
                            break;
                    }
                }
            }
        }

        return new GameState(snakeTiles, apples, bananas, cherries, walls, players);
    }
}
