package com.snake.Model;

public class Cherry extends Fruit
{
    public Cherry()
    {
        super(TileType.Cherry);
    }

    public void doEffect(Snake snake)
    {
        snake.setSpeed(snake.getSpeed() + 3);
        snake.updateSnakePosition();
    }

    public void playSound()
    {
        //TODO fox this.
        Resources.playSoundByName("EatAppleSound");
    }
}
