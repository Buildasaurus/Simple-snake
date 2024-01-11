package com.snake.Views;

import java.util.Optional;

import com.snake.OurButton;
import com.snake.Controllers.GUIController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

public class SaveView extends ButtonOverlayView {
    Alert confirmationAlert;
    int saveIndex;
    GUIController controller;

    public SaveView(GUIController controller, String[] saveNames) {
        buttons = new Button[4];
        focusElementIndex = 0;
        this.controller = controller;

        initialize(saveNames);
    }

    public void initialize(String[] saveNames) {
        setBackground(new Color(0.0, 0.0, 0.0, 0.3));

        for (int i = 0; i < 3; i++) {
            OurButton saveButton = new OurButton();
            buttons[i] = saveButton;
            saveButton.setOnAction(this::handleSaveButtonPressed);
        }
        updateButtonNames(saveNames);

        OurButton backButton = new OurButton("Back to Pause Menu");
        buttons[3] = backButton;
        backButton.setOnAction(controller::handlePauseButtonPressed);

        confirmationAlert = new Alert(AlertType.CONFIRMATION, "You are going to overwrite a save, continue?");

        setBasicFormatting();

        Platform.runLater(() -> this.requestFocus());
    }

    public void updateButtonNames(String[] newNames) {
        for (int i = 0; i < 3; i++) {
            String buttonName = "" + (i + 1) + " ";
            buttonName += newNames[i] != null ? newNames[i] : "Empty Save";

            buttons[i].setText(buttonName);
        }
    }

    public void handleSaveButtonPressed(ActionEvent action) {
        OurButton actionOrigin = (OurButton) action.getSource();
        String saveName = actionOrigin.getText();
        saveIndex = Integer.parseInt(saveName.substring(0, 1));
        controller.handleSaveButtonPressed(saveIndex);
    }

    public void showAlert() {
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.handleSaving(saveIndex);
        }
    }
}
