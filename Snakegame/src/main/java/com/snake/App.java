package com.snake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import com.snake.Controllers.GameController;
import com.snake.Controllers.IController;


/**
 * Simple snake - Made by Kajsa Berlstedt & Lucia Little & Marinus Juhl & Jonathan Sommerlund
 *
 */
public class App extends Application
{
    private static Scene scene;
    static int maxDimension = 700;
    private static IController controller;

    @Override
    public void start(Stage stage) throws IOException
    {
        controller = new GameController(Settings.windowWidth, Settings.windowHeight);
        scene = new Scene(controller.getView(), Settings.windowWidth, Settings.windowHeight);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public static void setRoot(IController newController)
    {
        controller = newController;
        scene.setRoot(controller.getView());
    }

    public static void main(String[] args)
    {
        try
        {
            int width = Integer.parseInt(args[0]);
            int height = Integer.parseInt(args[1]);
            int max = Math.max(width, height);
            int boxSize = maxDimension / max;
            Settings.windowHeight = boxSize * height;
            System.out.println(Settings.windowHeight);
            System.out.println(height);

            Settings.windowWidth = boxSize * width;
            Settings.getGameSettings().setColumnCount(width);
            Settings.getGameSettings().setRowCount(height);
        }
        catch (Exception e)
        {
            // TODO: handle exception
            System.out.println(e);
            System.out.println(
                    "Input arguments weren't correct. Expecting two values width, height, that  are between 5 and 100");
        }
        launch();
    }

    public void stop()
    {
        System.out.println(controller);
        System.out.println("closing app");
    }
}
