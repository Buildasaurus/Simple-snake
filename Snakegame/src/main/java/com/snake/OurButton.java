package com.snake;

import com.snake.Model.Resources;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class OurButton extends Button

{
    private double scaler = 4.5;

    private double x = Settings.windowWidth/scaler;

    private double y = Settings.windowHeight/scaler;

    public OurButton(){
        super();
        setBackground();
    }

    public OurButton(String buttonName) 
    {
       super(buttonName);
       setBackground();
       
    }

    public OurButton(String buttonName, double scaler){
        super(buttonName);
        this.scaler = scaler;
        setBackground();
    }

    private void setBackground(){
        Image buttonbackgroundimgfile = Resources.getImageByNamesetWidth("smallplaybt", scaler);
        BackgroundImage buttonbackgroundimg = new BackgroundImage(buttonbackgroundimgfile, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
        Background buttonbackground = new Background(buttonbackgroundimg);
        setBackground(buttonbackground);
        setStyle("-fx-cursor: hand; -fx-font-size: 20");
        setPrefSize(Settings.windowWidth/scaler, Settings.windowHeight/scaler);
    }
    
}