package com.snake.Views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class ButtonOverlayView extends StackPane
{
    protected Button[] buttons;
    protected int focusElementIndex;

    public void setBackground(Color overlayColor)
    {
        BackgroundFill bgFill = new BackgroundFill(overlayColor, null, getInsets());
        Background bg = new Background(bgFill);
        setBackground(bg);
    }

    public void setBasicFormatting()
    {
        VBox buttonGroup = new VBox(20.0, buttons);
        buttonGroup.setAlignment(Pos.CENTER);
        getChildren().add(buttonGroup);

        for (Button button : buttons)
        {
            button.setFont(new Font(20));
        }
    }

    public void shiftFocus(KeyEvent key)
    {
        switch (key.getCode())
        {
            case W:
            case A:
                focusElementIndex =
                        focusElementIndex == 0 ? buttons.length - 1 : focusElementIndex - 1;
                buttons[focusElementIndex].requestFocus();
                break;

            case S:
            case D:
                focusElementIndex =
                        focusElementIndex == buttons.length - 1 ? 0 : focusElementIndex + 1;
                buttons[focusElementIndex].requestFocus();
                break;

            default:
                break;
        }
    }
}
