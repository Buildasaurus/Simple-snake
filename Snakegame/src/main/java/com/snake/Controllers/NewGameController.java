package com.snake.Controllers;

import com.snake.App;
import com.snake.SelectionButton;
import com.snake.Settings;
import com.snake.Model.GameSettings;
import com.snake.Model.GameSettings.GameMode;
import com.snake.Model.GameSettings.Level;
import com.snake.Views.NewGameView;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;


public class NewGameController implements IController
{
    NewGameView view;
    GameSettings gameSettings;

    public NewGameController()
    {
        gameSettings = Settings.getGameSettings();
        view = new NewGameView(this, gameSettings.getAllPossiblePlayerCounts());
        view.setOnKeyPressed(this::handleKeyPressed);
    }

    public Parent getView()
    {
        return view;
    }

    public void setLevel(Level level)
    {
        gameSettings.setLevel(level);
    }

    public Level getLevel()
    {
        return gameSettings.getLevel();
    }

    public void setGameMode(GameMode gameMode)
    {
        gameSettings.setGameMode(gameMode);
    }

    public GameMode getGameMode()
    {
        return gameSettings.getGameMode();
    }

    public boolean setPlayerCount(int playerCount)
    {
        return gameSettings.setPlayerCount(playerCount);
    }

    public int getPlayerCount()
    {
        return gameSettings.getPlayerCount();
    }

    public void handleKeyPressed(KeyEvent key) {
        switch (key.getCode()) {
            case ESCAPE:
                MenuController newController = new MenuController();
                App.setRoot(newController);
                break;
        
            default:
                break;
        }
    }

    /**
     * Sets the next active window to the {@link com.snake.Controllers.GUIController GUI Controller}
     * and updates the {@link #gameSettings game settings} stored.
     *
     * @param action
     */
    public void handlePlayButtonPressed(ActionEvent action)
    {
        Settings.setGameSettings(gameSettings);
        GUIController newController = new GUIController();
        App.setRoot(newController);
    }
    public void handlePlayerCounts(ActionEvent action){
        SelectionButton actionOrigin = (SelectionButton) action.getSource();
        gameSettings.setPlayerCount(Integer.valueOf(actionOrigin.getText()));
        view.playerselection.buttonPressed(actionOrigin);
    }

    public void handlegameMode(ActionEvent action){
        SelectionButton actionorigin = (SelectionButton) action.getSource();
        gameSettings.setGameMode(GameMode.valueOf(actionorigin.getText()));
        view.gamemode.buttonPressed(actionorigin);
    }

    public void handlelevel(ActionEvent action){
        SelectionButton actionorigin = (SelectionButton) action.getSource();
        gameSettings.setLevel(Level.valueOf(actionorigin.getText()));
        view.maptype.buttonPressed(actionorigin);
    }

}
