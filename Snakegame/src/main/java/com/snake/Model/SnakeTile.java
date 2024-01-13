package com.snake.Model;

import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;

public class SnakeTile extends Tile
{
    Vector enterDirection;
    Vector targetDirection;
    int assignedPlayer;

    public SnakeTile(TileType type, Vector enterDirection, Vector targetDirection,
            int assignedPlayer)
    {
        super(type);
        tileType = type;
        this.enterDirection = enterDirection;
        this.targetDirection = targetDirection;
        this.assignedPlayer = assignedPlayer;
    }

    public ImageView getImage()
    {
        ImageView sprite = new ImageView(Resources.getImageByName("PixSnake"));
        return sprite;
    }


    public boolean equals()
    {
        return false;
    }


}
