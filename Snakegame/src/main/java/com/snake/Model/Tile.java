package com.snake.Model;

import javafx.scene.image.ImageView;

// Made by Kajsa Berlstedt & Lucia Little


public abstract class Tile
{
    protected TileType tileType;
    protected Vector position;

    public Tile(TileType type)
    {
        tileType = type;
    }

    public abstract ImageView getImage();

    public Vector getPosition()
    {
        return position;
    }

    public boolean equals()
    {
        return false;
    }


}
