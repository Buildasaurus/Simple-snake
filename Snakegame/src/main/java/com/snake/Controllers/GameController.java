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

// Made by Marinus Juhl

public class GameController implements IController
{
    private GameView view;
    private GameModel model;

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
        Vector direction = new Vector();
        switch (key.getCode())
        {
            case UP:
            case W:
                direction = new Vector(0, -1);
                break;
            case DOWN:
            case S:
                direction = new Vector(0, 1);
                break;
            case LEFT:
            case A:
                direction = new Vector(-1, 0);
                break;
            case RIGHT:
            case D:
                direction = new Vector(1, 0);
                break;
            default:
                break;
        }
        if (!model.getDirection().equals(direction.multiply(-1)))
        {
            model.setDirection(direction);
            executeNextStep();
        }
    }

    public GameModel getGameModel()
    {
        return model;
    }
}
