package com.snake.Controllers;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import com.snake.Model.GameModel;
import com.snake.Model.Vector;
import com.snake.Views.GameView;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;

public class GameController implements IController
{
    private GameView view;
    private GameModel model;


    private Timer gameTimer;
    private int[] playerProgress;
    private ArrayList<Integer> updateList;



    private boolean isGameOver = false;

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

        TimerTask timeLoop = new TimerTask()
        {
            public void run()
            {
                Platform.runLater(() -> {
                    if (!isGameOver)
                    {
                        isGameOver = executeNextStep();

                    }
                });
            }
        };

        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(timeLoop, 1000, 1000);
    }

    public boolean executeNextStep()
    {
        model.nextState();
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
    public int getCurrentScore()
    {
        return model.getSnakeLength();
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
                model.setDirection(new Vector(0, -1));
                break;
            case W:
                model.setDirection(new Vector(0, -1));
                break;
            case DOWN:
                model.setDirection(new Vector(0, 1));
                break;
            case S:
                model.setDirection(new Vector(0, 1));
                break;
            case LEFT:
                model.setDirection(new Vector(-1, 0));
                break;
            case A:
                model.setDirection(new Vector(-1, 0));
                break;
            case RIGHT:
                model.setDirection(new Vector(1, 0));
                break;
            case D:
                model.setDirection(new Vector(1, 0));
                break;
            case J:
                model.setDirection(new Vector(1, 0));
                break;
            case G:
                model.setDirection(new Vector(-1, 0));
                break;
            case Y:
                model.setDirection(new Vector(0, -1));
                break;
            case H:
                model.setDirection(new Vector(0, 1));
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
