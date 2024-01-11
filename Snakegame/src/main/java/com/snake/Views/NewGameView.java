package com.snake.Views;


import com.snake.CustomHBox;
import com.snake.OurButton;
import com.snake.SelectionButton;
import com.snake.Settings;
import com.snake.Controllers.NewGameController;
import com.snake.Model.GameSettings.GameMode;
import com.snake.Model.GameSettings.Level;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class NewGameView extends StandardBackground
{
    NewGameController controller;
    public CustomHBox playerselection;
    public CustomHBox gamemode;
    public CustomHBox maptype;

    public NewGameView(NewGameController controller, int[] playerCounts)
    {
        this.controller = controller;
        initialize(playerCounts);
    }

    private void initialize(int[] playerCounts)
    {

        playerselection = new CustomHBox("Players");


        for (int i : playerCounts)
        {
            SelectionButton button = new SelectionButton(Integer.toString(i));
            if (Settings.getGameSettings().getPlayerCount() == i){
                button.pressed();
                playerselection.defaultButton = button;
            }
            button.setOnAction(controller::handlePlayerCounts);
            playerselection.getChildren().add(button);
        }

        // gamemode hbox

        gamemode = new CustomHBox("Game Mode");

        for (GameMode mode : GameMode.values())
        {
            SelectionButton button = new SelectionButton(mode.toString());
            if (Settings.getGameSettings().getGameMode() == mode){
                button.pressed();
                gamemode.defaultButton = button;
            }
            button.setOnAction(controller::handlegameMode);
            gamemode.getChildren().add(button);
        }

        // maptype hbox

        maptype = new CustomHBox("Map Type");

        for (Level level : Level.values())
        {
            SelectionButton button = new SelectionButton(level.toString());
            if (Settings.getGameSettings().getLevel() == level){
                button.pressed();
                maptype.defaultButton = button;
            }
            button.setOnAction(controller::handlelevel);
            maptype.getChildren().add(button);
        }

        VBox vbox = new VBox(10);

        setMargin(vbox, new Insets(500, -300, 200, 100));

        vbox.getChildren().addAll(playerselection, gamemode, maptype);

        getChildren().add(vbox);




        // Start game
        OurButton startGame = new OurButton("Start game");
        startGame.setOnAction(controller::handlePlayButtonPressed);

        startGame.setTranslateY(165);
        getChildren().add(startGame);


        // vbox adjust
        vbox.setTranslateY(-200);
        vbox.setTranslateX(-200);

        setPadding(new Insets(0,120,0,120));

    
    }

}
