package com.snake.Model;

// Made by Kajsa Berlstedt & Lucia Little


public class Apple extends Fruit
{
    public Apple()
    {
        super(TileType.Apple);
    }

    public void doEffect(Snake snake)
    {
        snake.grow();
    }

    public void playSound()
    {
        Resources.playSoundByName("EatAppleSound");
    }
}
