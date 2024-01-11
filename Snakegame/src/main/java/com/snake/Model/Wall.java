package com.snake.Model;

import javafx.scene.image.ImageView;

public class Wall extends Tile
{
    public Wall()
    {
        super(TileType.Wall);
    }

    public ImageView getImage()
    {
        return new ImageView(Resources.getImageByName("wall"));
    }
}
