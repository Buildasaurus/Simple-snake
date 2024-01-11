package com.snake.Controllers;

import com.snake.App;
import com.snake.Settings;
import com.snake.Model.Save;
import com.snake.Utils.SaveHandler;
import com.snake.Views.MenuView;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;

public class MenuController implements IController {
    private MenuView view;
    public MenuController() {
        this.view = new MenuView(this);

        view.setOnKeyPressed(this::handleKeyPressed);
    }

    public Parent getView() {
        return view;
    }

    public void handlePlayButtonPressed(ActionEvent action) {
        NewGameController newController = new NewGameController();
        App.setRoot(newController);
    }

    public void handleContinueButtonPressed(ActionEvent action) {
        Save save = SaveHandler.readSave(0);
        
        if (save != null) {
            Settings.setGameSettings(save.getGameSettings());
            GUIController newController = new GUIController(save.getGameState());
            App.setRoot(newController);
        }
    }

    public void handleLoadButtonPressed(ActionEvent action) {
        LoadController newController = new LoadController();
        App.setRoot(newController);
    }

    public void handleExitButtonPressed(ActionEvent action) {
        Platform.exit();
    }

    private void handleKeyPressed(KeyEvent key) {
        switch (key.getCode()) {
            case ESCAPE:
                Platform.exit();
                break;

            default:
                break;
        }
    }
}
