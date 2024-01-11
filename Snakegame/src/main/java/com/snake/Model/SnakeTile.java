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
        String directionName = getDirectionName(enterDirection, targetDirection);
        if (tileType == TileType.Snaketail && directionName.contains("_"))
        {
            directionName = getDirectionName(targetDirection);
        }
        String imageName = tileType.toString().toLowerCase() + "_" + directionName;
        ImageView sprite = new ImageView(Resources.getImageByName(imageName));
        if (assignedPlayer == 1)
        {
            sprite.setBlendMode(BlendMode.DIFFERENCE);
        }
        if (assignedPlayer == 2)
        {
            sprite.setBlendMode(BlendMode.GREEN);
        }
        return sprite;
    }


    public boolean equals()
    {
        return false;
    }

    /**
     * Returns one of eight values, straightright, straightup, straightdown, straightleft,
     * turnright, turnup, turnleft, turndown Here the turns, eg turn up, is coming from the right,
     * then going upwards, always 90 degrees. Assuming that the two directions aren't opposite of
     * each other.
     *
     * @param firstDirection
     * @param secondDirection
     * @return
     */
    private String getDirectionName(Vector firstDirection, Vector secondDirection)
    {
        int crossProduct = firstDirection.crossProduct(secondDirection);
        switch (crossProduct)
        {
            case 0:
                // They are parallel, so now we can figure out in which direction
                if (firstDirection.x == 1 && firstDirection.y == 0)
                    return "right";
                else if (firstDirection.x == -1 && firstDirection.y == 0)
                    return "left";
                else if (firstDirection.x == 0 && firstDirection.y == -1)
                    return "up";
                else if (firstDirection.x == 0 && firstDirection.y == 1)
                    return "down";
                else
                {
                    System.out.println("vectors are weird, or something is wrong.");
                    return "error";
                }
            case 1:
                Vector temp = firstDirection.multiply(-1);
                firstDirection = secondDirection.multiply(-1);
                secondDirection = temp;
            case -1:
                // They are not parallel. So now depending on the firstdirection, the second should
                // change
                if (firstDirection.x == 1 && firstDirection.y == 0)
                    return "turn_left";
                else if (firstDirection.x == -1 && firstDirection.y == 0)
                    return "turn_right";
                else if (firstDirection.x == 0 && firstDirection.y == 1)
                    return "turn_up";
                else if (firstDirection.x == 0 && firstDirection.y == -1)
                    return "turn_down";
                else
                {
                    System.out.println("vectors are weird, or something is wrong.");
                    System.out.println(firstDirection.toString() + " " + secondDirection);
                    return "error";
                }

            default:
                System.out.println("vectors are weird, or something is wrong.");
                System.out.println(firstDirection.toString() + " " + secondDirection);

                return "";
        }
    }

    private String getDirectionName(Vector direction)
    {
        if (direction.x == 1 && direction.y == 0)
            return "right";
        else if (direction.x == -1 && direction.y == 0)
            return "left";
        else if (direction.x == 0 && direction.y == -1)
            return "up";
        else if (direction.x == 0 && direction.y == 1)
            return "down";
        else
        {
            System.out.println("vectors are weird, or something is wrong.");
            return "error";
        }

    }
}
