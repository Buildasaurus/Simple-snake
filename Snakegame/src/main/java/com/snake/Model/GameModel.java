package com.snake.Model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import com.snake.Settings;
import com.snake.Utils.LevelGenerator;
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
    private Snake[] players;
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
        players = new Snake[Settings.getGameSettings().getPlayerCount()];
        Vector startSpawnPoint = new Vector(columnCount / 2, rowCount / 2 - (getPlayerCount() - 1));

        changedTiles = new ArrayList<>();
        for (int i = 0; i < Settings.getGameSettings().getPlayerCount(); i++)
        {
            players[i] = new Snake(board, startSpawnPoint.add(0, i * 2),
                    startSpawnPoint.add(-1, i * 2), new Vector(1, 0), i);
            changedTiles.add(startSpawnPoint.add(0, i * 2));
            changedTiles.add(startSpawnPoint.add(-1, i * 2));
        }


        LevelGenerator.generateLevel(board);
        int fruitCount = 0;
        // Nice StackOverflow code to dynamiccaly get all classes that extends fruit, and spawn them
        // https://stackoverflow.com/questions/205573/at-runtime-find-all-classes-in-a-java-application-that-extend-a-base-class
        System.out.println("Trying to find subclasses");
        Reflections reflections = new Reflections("com.snake");
        Set<Class<? extends Fruit>> classes =
                reflections.getSubTypesOf(com.snake.Model.Fruit.class);
        for (Class<? extends Fruit> aClass : classes)
        {
            System.out.println("subclass: " + aClass.getName());
            try
            { // cursed code from
              // https://stackoverflow.com/questions/5533702/instantiating-object-of-same-class-from-within-class-in-java
                Constructor constructor = aClass.getConstructor();
                Fruit piece = (Fruit) constructor.newInstance();
                while (board[piece.getPosition().y][piece.getPosition().x] != null)
                {
                    piece.setRandomPosition();
                }
                changedTiles.add(piece.getPosition());
                board[piece.getPosition().y][piece.getPosition().x] = piece;
                fruitCount++;
            }
            catch (Exception e)
            {
                System.out.println("Your constructor stuff in gamemodel doesn't work...");
                System.out.println(e);
            }
            freeSquares = rowCount * columnCount - LevelGenerator.wallCount - fruitCount
                    - players.length * 2;

        }


        /*
         * // level generation LevelGenerator.generateLevel(board); Apple apple = new Apple(); while
         * (board[apple.getPosition().y][apple.getPosition().x] != null) {
         * apple.setRandomPosition(); } changedTiles.add(apple.getPosition());
         * board[apple.getPosition().y][apple.getPosition().x] = apple;
         *
         * Cherry cherry = new Cherry(); while
         * (board[cherry.getPosition().y][cherry.getPosition().x] != null) {
         * cherry.setRandomPosition(); } board[cherry.getPosition().y][cherry.getPosition().x] =
         * cherry; changedTiles.add(cherry.getPosition());
         */
    }

    public GameModel(GameState gameState)
    {
        this.rowCount = Settings.getGameSettings().getRowCount();
        this.columnCount = Settings.getGameSettings().getColumnCount();

        board = new Tile[rowCount][columnCount];
        for (SnakeTile snakeTile : gameState.getSnakeTiles())
        {
            board[snakeTile.getPosition().y][snakeTile.getPosition().x] = snakeTile;
        }
        for (Apple apple : gameState.getApples())
        {
            board[apple.getPosition().y][apple.getPosition().x] = apple;
        }
        for (Banana banana : gameState.getBananas())
        {
            board[banana.getPosition().y][banana.getPosition().x] = banana;
        }
        for (Cherry cherry : gameState.getCherries())
        {
            board[cherry.getPosition().y][cherry.getPosition().x] = cherry;
        }
        for (Wall wall : gameState.getWalls())
        {
            board[wall.getPosition().y][wall.getPosition().x] = wall;
        }

        players = gameState.getPlayers();
        for (Snake player : players)
        {
            player.setBoard(board);
        }
    }

    ArrayList<Vector> changedTiles = new ArrayList<Vector>();

    /**
     * Updates the board to the next state. Only updates the snakes whose id is in the
     * playersToUpdate
     *
     * @param playersToUpdate
     */
    public void nextState(ArrayList<Integer> playersToUpdate)
    {
        changedTiles.clear();
        // for syncronization, find any snakes colliding head on, and tell them they are colliding.
        // These snakes won't update.
        Snake[] snakesToUpdate = new Snake[playersToUpdate.size()];
        int j = 0;
        // Gather the correct pointers for later use
        for (int playerID : playersToUpdate)
        {
            snakesToUpdate[j] = players[playerID];
            j++;
        }
        ArrayList<Vector> headPositions = new ArrayList<>();
        HashMap<String, Integer> nextHeadPositions = new HashMap<>();
        for (int i = 0; i < playersToUpdate.size(); i++)
        {
            Vector nextPosition = snakesToUpdate[i].getNextHeadPosition();
            headPositions.add(nextPosition);
            if (nextHeadPositions.containsKey(nextPosition.toString()))
            {
                snakesToUpdate[nextHeadPositions.get(nextPosition.toString())].isColliding = true;
                snakesToUpdate[i].isColliding = true;
            }
            else
            {
                nextHeadPositions.put(nextPosition.toString(), i);
            }
        }
        // save which tiles have been modified
        changedTiles.addAll(headPositions);
        for (Snake snake : snakesToUpdate)
        {
            changedTiles.add(snake.getHeadPosition());
            changedTiles.add(snake.getTailPosition());
            changedTiles.add(snake.getNextTailPosition());
        }

        // Now we check whether the next position will be clear, in the next frame.
        // This the snakes won't know, as there might be a snake tail that disappears, or that
        // doesn't
        // disappear, if the snake eats an apple, or dies, or is colliding.
        boolean[] willClear = new boolean[players.length];
        int i = 0;
        for (Vector vec : headPositions)
        {
            willClear[i] = true;
            // essentially looping through the playerss
            Tile tile = board[vec.y][vec.x];
            if (tile instanceof SnakeTile)
            {
                SnakeTile snakeTile = ((SnakeTile) tile);
                if (!(snakeTile.tileType == TileType.Snaketail)
                        || (players[snakeTile.assignedPlayer].isColliding
                                || players[snakeTile.assignedPlayer].willGrow(board))
                        || !playersToUpdate.contains(snakeTile.assignedPlayer))
                {
                    System.out.println(
                            snakeTile.tileType + " " + players[snakeTile.assignedPlayer].isColliding
                                    + "  " + players[snakeTile.assignedPlayer].willGrow(board));
                    willClear[i] = false;
                }
            }
            i++;
        }
        ArrayList<Fruit> fruitsToRespawn = new ArrayList<>();
        j = 0;
        for (Snake snake : snakesToUpdate)
        {
            if (snake.isAlive())
            {
                snake.calculateNextFrame(willClear[j]);
                if (snake.Fruiteaten() != null)
                {
                    fruitsToRespawn.add(snake.Fruiteaten());
                    if (snake.Fruiteaten().tileType == TileType.Banana)
                    {
                        for (Snake player : players)
                        {
                            if (Settings.getGameSettings().getSpeed() < player.getSpeed())
                            {
                                player.setSpeed(player.getSpeed() - 1);
                            }
                        }
                    }
                }
            }
            else
            {
                System.out.println(snake.playerNumber);
            }
            j++;
        }
        for (Fruit fruit : fruitsToRespawn)
        {
            // TODO - fix this, so that when someone wins the game, it isn't an infinite loop,
            // and when they are close, it doesn't take forever

            try
            { // cursed code from
              // https://stackoverflow.com/questions/5533702/instantiating-object-of-same-class-from-within-class-in-java
                Constructor constructor = fruit.getClass().getConstructor();
                Fruit piece = (Fruit) constructor.newInstance();
                if (fruit instanceof Apple)
                {
                    freeSquares--;

                }
                if (freeSquares < 0)
                {
                    break;
                }

                while (board[piece.getPosition().y][piece.getPosition().x] != null)
                {
                    piece.setRandomPosition();
                }
                changedTiles.add(piece.getPosition());
                board[piece.getPosition().y][piece.getPosition().x] = piece;

            }
            catch (Exception e)
            {
                System.out.println("Your constructor stuff in gamemodel doesn't work...");
                System.out.println(e);
            }
        }
    }

    public ArrayList<Vector> getChangedPositions()
    {
        return changedTiles;
    }

    public void setDirection(Vector direction, int player)
    {
        player = player % Settings.getGameSettings().getPlayerCount();
        players[player].setDirection(direction);
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
