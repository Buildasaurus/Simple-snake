package com.snake.Views;

import java.util.ArrayList;
import com.snake.Settings;
import com.snake.Model.Tile;
import com.snake.Model.Vector;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

public class GameView extends GridPane
{
    int rowCount;
    int columnCount;
    int height;
    int width;
    Node[][] nodes;

    /**
     * Initialises the board with a height and width.
     *
     * @param rowCount The amount of rows (height) that will be drawn.
     * @param columnCount The amount of columns (width) that will be drawn.
     * @param height The height of the board in pixels
     * @param width The width of the board in pixels
     */
    public GameView(int width, int height, Tile[][] board)
    {
        this.height = height;
        this.width = width;
        this.rowCount = Settings.getGameSettings().getRowCount();
        this.columnCount = Settings.getGameSettings().getColumnCount();
        initialize(board);
    }

    boolean firstUdpdate = true;

    /**
     * Initializes the board with the height and width stored.
     */
    public void initialize(Tile[][] board)
    {
        nodes = new Node[rowCount][columnCount];
        this.getColumnConstraints().clear();
        this.getRowConstraints().clear();

        for (int i = 0; i < columnCount; i++)
        {
            ColumnConstraints column = new ColumnConstraints(width / columnCount);
            this.getColumnConstraints().add(column);
        }
        for (int i = 0; i < rowCount; i++)
        {
            RowConstraints row = new RowConstraints(height / rowCount);
            this.getRowConstraints().add(row);
        }

        BackgroundFill bgFillDark =
                new BackgroundFill(new Color(0, 0.6, 0.1, 1), null, getInsets());
        BackgroundFill bgFillLight =
                new BackgroundFill(new Color(0, 0.5, 0.1, 1), null, getInsets());
        Background bgDark = new Background(bgFillDark);
        Background bgLight = new Background(bgFillLight);
        boolean isLastDark = false;
        for (int row = 0; row < rowCount; row++)
        {
            for (int column = 0; column < columnCount; column++)
            {
                Pane bgCell = new Pane();
                if (row != 0 && column == 0)
                {
                    if (columnCount % 2 == 0)
                    {
                        isLastDark = !isLastDark;
                    }
                }
                if (isLastDark)
                {
                    bgCell.setBackground(bgLight);
                }
                else
                {
                    bgCell.setBackground(bgDark);
                }
                isLastDark = !isLastDark;
                this.add(bgCell, column, row);

            }
        }
        // Save all nodes for later easy access.
        for (Node node : this.getChildren())
        {
            nodes[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] = node;
            refreshPoint(new Vector(GridPane.getColumnIndex(node), GridPane.getRowIndex(node)),
                    board);

        }
    }

    public void update(Tile[][] board, ArrayList<Vector> relevantPositions)
    {
        // This variable might change from frame to frame, so
        if (board != null)
        {
            for (Vector position : relevantPositions)
            {
                refreshPoint(position, board);
            }
        }
        else
        {
            System.out.println("board isnull");

        }
    }


    public void refreshPoint(Vector position, Tile[][] board)
    {
        Pane pane = (Pane) nodes[position.y][position.x];
        int row = position.y;
        int column = position.x;



        Tile tile = board[row][column];

        pane.getChildren().clear();
        // Label text = new Label(
        // getColumnIndex(pane) + " " + getRowIndex(pane) + " : " + column + " " + row);
        // pane.getChildren().add(text);
        if (tile != null)
        {
            ImageView imageView = tile.getImage();
            imageView.setFitWidth(height / rowCount);
            imageView.setPreserveRatio(true);

            pane.getChildren().add(imageView);
        }
    }
}
