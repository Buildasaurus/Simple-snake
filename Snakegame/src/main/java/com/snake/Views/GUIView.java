package com.snake.Views;

import com.snake.Settings;
import com.snake.Controllers.GUIController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class GUIView extends StackPane {
    private GameView gameView;
    private PauseView pauseView;
    private GameOverView gameOverView;
    private SaveView saveView;

    private Label frameRateCounter;
    private Label[] currentScores;
    private Label[] currentSpeeds;

    public GUIView(Parent gameView, int playerCount, double[] speedArray) {
        this.gameView = (GameView) gameView;
        frameRateCounter = new Label("fps: 0.0");
        currentScores = new Label[playerCount];
        currentSpeeds = new Label[playerCount];

        initialize(playerCount, speedArray);
    }

    public void initialize(int playerCount, double[] speedArray) {
        BorderPane gameGroup = new BorderPane();
        gameGroup.setCenter(this.gameView);
        gameGroup.setPadding(new Insets(5, 5, 5, 5));
        gameView.setAlignment(Pos.TOP_CENTER);

        HBox topBox = new HBox();
        double heightOfHBOX = (Settings.windowHeight - gameView.height) * 0.5;
        topBox.setSpacing(50.0);
        topBox.setPrefHeight(heightOfHBOX);
        topBox.setAlignment(Pos.CENTER);

        VBox leftBox = new VBox();
        double widthOfVBOX = (Settings.windowWidth - gameView.width) * 0.5;
        leftBox.setSpacing(50.0);
        leftBox.setPrefWidth(widthOfVBOX);
        leftBox.setAlignment(Pos.CENTER);

        frameRateCounter.setFont(new Font(15.0));
        topBox.getChildren().add(frameRateCounter);

        for (int i = 0; i < playerCount; i++) {
            Label currentScore = new Label();
            currentScore.setText("Player " + (i + 1) + "'s score: 0");
            currentScores[i] = currentScore;
            currentScore.setFont(new Font(15.0));
            topBox.getChildren().add(currentScore);
        }
        CheckBox toggleExtraVision = new CheckBox();
        toggleExtraVision.setSelected(Settings.getGameSettings().getExtraVision());
        toggleExtraVision.setOnAction((s) -> {
            Settings.getGameSettings().setExtraVision(toggleExtraVision.isSelected());
        });
        toggleExtraVision.setFocusTraversable(false);
        topBox.getChildren().add(toggleExtraVision);

        for (int i = 0; i < playerCount; i++) {
            Label currentSpeed = new Label();
            currentSpeed.setText("Player " + (i + 1) + "'s \n speed: " + speedArray[i]);
            currentSpeeds[i] = currentSpeed;
            currentSpeed.setFont(new Font(15.0));
            leftBox.getChildren().add(currentSpeed);
        }

        gameGroup.setLeft(leftBox);
        gameGroup.setTop(topBox);

        getChildren().add(gameGroup);
    }

    public void shiftFocus(KeyEvent key) {
        if (pauseView != null) {
            pauseView.shiftFocus(key);
        } else if (gameOverView != null) {
            gameOverView.shiftFocus(key);
        }
    }

    public void updateFrameRate(double frameRate) {
        frameRateCounter.setText(String.format("fps: %.1f", frameRate));
    }

    public void updateCurrentScore(int score, int player) {
        currentScores[player].setText("Player " + (player + 1) + "'s score: " + score);
    }

    public void updateCurrentSpeed(double speed, int player) {
        currentSpeeds[player].setText("Player " + (player + 1) + "'s \n speed: " + speed);
    }

    public void setPauseView(GUIController controller) {
        pauseView = new PauseView(controller);
        getChildren().add(this.pauseView);
    }

    public void removePauseView() {
        getChildren().remove(pauseView);
        pauseView = null;
    }

    public void setGameOverView(GUIController controller) {
        gameOverView = new GameOverView(controller);
        getChildren().add(this.gameOverView);
    }

    public void toggleGameOverButtonVisibility() {
        gameOverView.toggleGameOverButtonVisibility();
    }

    public void setSaveView(GUIController controller, String[] saveNames) {
        removePauseView();
        saveView = new SaveView(controller, saveNames);
        getChildren().add(this.saveView);
    }

    public void removeSaveView(GUIController controller) {
        getChildren().remove(saveView);
        saveView = null;
        setPauseView(controller);
    }

    public void updateSaveNames(String[] saveNames) {
        saveView.updateButtonNames(saveNames);
    }

    public void showAlert() {
        saveView.showAlert();
    }
}
