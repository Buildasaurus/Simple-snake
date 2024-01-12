package com.snake.Controllers;

import java.util.ArrayList;

import com.snake.Model.GameModel;
import com.snake.Model.Vector;
import com.snake.Views.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;

public class GameController implements IController
{
    private GameView view;
    private GameModel model;


    private AnimationTimer gameTimer;
    private int[] playerProgress;
    private ArrayList<Integer> updateList;

    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    private boolean isGameOver = false;
    private boolean isPaused = false;
    private boolean isSaving = false;

    private int playerCount;


    public Parent getView()
    {
        return view;
    }

    public GameController(int width, int height)
    {
        this.model = new GameModel();
        this.view = new GameView(width, height, model.getBoard());

        view.update(model.getBoard(), model.getChangedPositions());

        view.setOnKeyPressed(this::handleKeyPressed);
        Platform.runLater(() -> view.requestFocus());

        playerProgress = new int[playerCount];
        updateList = new ArrayList<Integer>();

        gameTimer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                if (!isGameOver)
                {
                    for (int i = 0; i < playerProgress.length; i++)
                    {
                        playerProgress[i] += getSpeed(i);
                        if (playerProgress[i] > 100)
                        {
                            updateList.add(i);
                            playerProgress[i] = 0;
                        }
                    }
                    if (!updateList.isEmpty())
                    {
                        isGameOver = executeNextStep(updateList);
                        if (isGameOver)
                        {
                            stop();
                        }
                        updateList.clear();
                    }
                }
            }
        };
        gameTimer.start();
    }

    public boolean executeNextStep(ArrayList<Integer> updateList)
    {
        model.nextState(updateList);
        if (model.gameOver())
        {
            System.out.printf("There are %d players alive.\n", model.getAlivePlayerCount());
            return true;
        }
        else
        {
            view.update(model.getBoard(), model.getChangedPositions());
            return false;
        }
    }

    public double getSpeed(int playerIndex)
    {
        return model.getSpeed(playerIndex);
    }

    /**
     * Returns the current score for the given player. Players are indexed from 0. You can use
     * {@link #getPlayerCount()} to get the playerocunt
     *
     * @param player
     * @return
     */
    public int getCurrentScore(int player)
    {
        return model.getSnakeLength(player);
    }

    public int getPlayerCount()
    {
        return model.getPlayerCount();
    }

    public void handleKeyPressed(KeyEvent key)
    {
        switch (key.getCode())
        {
            case UP:
                model.setDirection(new Vector(0, -1), 0);
                break;
            case W:
                model.setDirection(new Vector(0, -1), 1);
                break;
            case DOWN:
                model.setDirection(new Vector(0, 1), 0);
                break;
            case S:
                model.setDirection(new Vector(0, 1), 1);
                break;
            case LEFT:
                model.setDirection(new Vector(-1, 0), 0);
                break;
            case A:
                model.setDirection(new Vector(-1, 0), 1);
                break;
            case RIGHT:
                model.setDirection(new Vector(1, 0), 0);
                break;
            case D:
                model.setDirection(new Vector(1, 0), 1);
                break;
            case J:
                model.setDirection(new Vector(1, 0), 2);
                break;
            case G:
                model.setDirection(new Vector(-1, 0), 2);
                break;
            case Y:
                model.setDirection(new Vector(0, -1), 2);
                break;
            case H:
                model.setDirection(new Vector(0, 1), 2);
                break;
            default:
                break;
        }
    }
    public GameModel getGameModel()
    {
        return model;
    }
}
