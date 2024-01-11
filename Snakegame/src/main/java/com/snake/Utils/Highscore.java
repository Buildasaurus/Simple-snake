package com.snake.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*
 * Static class for accesing high scores.
 */
public class Highscore
{
    private static String fileName = "Highscores.txt";
    private static int highscore = -1;

    /**
     * Finds the saved highscore and returns it. If there is not saved highscore, it returns 0.
     *
     * @return
     */
    public static int getHighscore()
    {
        if (highscore != -1)
        {
            return highscore;
        }
        int result = 0;
        try
        {
            File myObj = new File(fileName);
            if (myObj.createNewFile())
            {
                System.out.println("File created: " + myObj.getName());
            }
            else
            {
                System.out.println("File already exists.");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine())
                {
                    String data = myReader.nextLine();
                    result = Integer.parseInt(data);
                    System.out.println(data);
                }
                myReader.close();
            }
        }
        catch (IOException e)
        {
            System.out.println("An error occurred in Highscore.");
            e.printStackTrace();
        }
        highscore = result;
        return result;
    }

    /**
     * Attempts to set the highscore to the given highscore
     *
     * @param highscore The highscore to try and save. If number is smaller than saved highscore,
     *        nothing happens.
     */
    public static void setHighscore(int newHighscore)
    {
        if (newHighscore > highscore)
        {
            highscore = newHighscore;
            // save the highscore
            try
            {
                FileWriter myWriter = new FileWriter(fileName);
                myWriter.write("" + highscore);
                myWriter.close();
                System.out.println("Successfully wrote " + highscore + " to the file.");
            }
            catch (IOException e)
            {
                System.out.println("An error occurred. in Highscore");
                e.printStackTrace();
            }
        }
    }
}
