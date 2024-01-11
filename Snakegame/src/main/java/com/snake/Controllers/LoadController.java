package com.snake.Controllers;

import com.snake.App;
import com.snake.Settings;
import com.snake.Model.Save;
import com.snake.Utils.SaveHandler;
import com.snake.Views.LoadView;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;

public class LoadController implements IController {
    LoadView view;

    public LoadController() {
        this.view = new LoadView(this, SaveHandler.getSaveNames());

        view.setOnKeyPressed(this::handleKeyPressed);
    }

    public Parent getView() {
        return view;
    }

    public void handleKeyPressed(KeyEvent key) {
        switch (key.getCode()) {
            case ESCAPE:
                handleBackButtonPressed(null);
                break;
        
            default:
                break;
        }
    }

    public void handleLoadSaveButtonPressed(ActionEvent action) {
        Button actionOrigin = (Button) action.getSource();
        String saveName = actionOrigin.getText();
        int index = Integer.parseInt(saveName.substring(0, 1));

        Save save = SaveHandler.readSave(index);
        
        if (save != null) {
            Settings.setGameSettings(save.getGameSettings());
            GUIController newController = new GUIController(save.getGameState());
            App.setRoot(newController);
        }
    }

    public void handleBackButtonPressed(ActionEvent action) {
        MenuController newController = new MenuController();
        App.setRoot(newController);
    }
}
