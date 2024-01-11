package com.snake.Views;

import com.snake.OurButton;
import com.snake.Settings;
import com.snake.Controllers.MenuController;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MenuView extends StandardBackground
{
    MenuController controller;

    public MenuView(MenuController controller)
    {
        this.controller = controller;

        initialize();
    }

    private void initialize()
    {

        //title
        Text title = new Text("Snake!");
        title.setFill(Color.BLACK);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        setAlignment(title,Pos.TOP_CENTER);
        getChildren().add(title);
        title.setTranslateY(Settings.windowHeight*0.1);

        //buttons
        OurButton playButton = new OurButton("play");
        OurButton continueButton = new OurButton("continue");
        OurButton loadButton = new OurButton("load");
        OurButton quitButton = new OurButton("quit", 6.5);

        playButton.setOnAction(controller::handlePlayButtonPressed);
        continueButton.setOnAction(controller::handleContinueButtonPressed);
        loadButton.setOnAction(controller::handleLoadButtonPressed);
        quitButton.setOnAction(controller::handleExitButtonPressed);

        /*//TODO fix this in another way, ideally make button and background scale the same
        //playButton.setTranslateY(Settings.windowHeight*0.1);
        getChildren().add(playButton);

        continueButton.setTranslateY(Settings.windowHeight*0.2);
        getChildren().add(continueButton);

        loadButton.setTranslateY(Settings.windowHeight*0.4);
        getChildren().add(loadButton);*/
        VBox vbox = new VBox(0.05);
        vbox.getChildren().addAll(playButton,continueButton,loadButton);
        vbox.setStyle("-fx-alignment: center");
        vbox.setTranslateY(Settings.windowWidth*0.05);
        getChildren().add(vbox);


        quitButton.setTranslateX(Settings.windowWidth*0.5 - (quitButton.getPrefWidth()*0.6));
        quitButton.setTranslateY(Settings.windowHeight*0.5 - (quitButton.getPrefHeight()*0.5));
        getChildren().add(quitButton);
    }

}
