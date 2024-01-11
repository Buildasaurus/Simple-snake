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
    int extraVisionDepth;
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
        this.extraVisionDepth = Settings.getGameSettings().getExtraVisionDepth();
        this.height = height;
        this.width = width;
        this.rowCount = Settings.getGameSettings().getExtendedRowCount();
        this.columnCount = Settings.getGameSettings().getExtendedColumnCount();
        initialize(board);
    }

    boolean firstUdpdate = true;

    /**
     * Initializes the board with the height and width stored.
     */
    public void initialize(Tile[][] board)
    {
        nodes = new Node[rowCount][columnCount];
        Settings.getGameSettings().addExtraVisionListener(() -> {
            toggleExtraVisionSquares(Settings.getGameSettings().getExtraVision());
        });
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
                    board, Settings.getGameSettings().getExtraVision());

        }
    }

    public void update(Tile[][] board, ArrayList<Vector> relevantPositions)
    {
        // This variable might change from frame to frame, so
        boolean extraVision = Settings.getGameSettings().getExtraVision();
        if (board != null)
        {
            for (Vector position : relevantPositions)
            {
                updatePoint(position, board, extraVision);
            }
        }
        else
        {
            System.out.println("board isnull");

        }
    }


    public void refreshPoint(Vector position, Tile[][] board, boolean extraVision)
    {
        Pane pane = (Pane) nodes[position.y][position.x];
        int row = getRowIndex(pane) - extraVisionDepth;
        int column = getColumnIndex(pane) - extraVisionDepth;


        // if is on edge, set visible to whether extravision is true. Also these squares are
        // special
        if (row < 0 || row > rowCount - 1 - extraVisionDepth * 2 || column < 0
                || column > columnCount - 1 - extraVisionDepth * 2)
        {
            column = column < 0 ? columnCount - extraVisionDepth * 2 + column : column;
            column = column > columnCount - 1 - extraVisionDepth * 2
                    ? column - (columnCount - extraVisionDepth * 2)
                    : column;

            row = row < 0 ? rowCount - extraVisionDepth * 2 + row : row;
            row = row > rowCount - 1 - extraVisionDepth * 2
                    ? row - (rowCount - extraVisionDepth * 2)
                    : row;
            pane.setVisible(extraVision);
            pane.setOpacity(0.6);
        }
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

    public void toggleExtraVisionSquares(boolean visible)
    {
        for (int row = 0; row < extraVisionDepth; row++)
        {
            for (int column = 0; column < columnCount; column++)
            {
                nodes[row][column].setVisible(visible);
            }
        }
        for (int column = 0; column < extraVisionDepth; column++)
        {
            for (int row = 0; row < rowCount; row++)
            {
                nodes[row][column].setVisible(visible);
            }
        }
        for (int column = columnCount - 1; column >= Settings.getGameSettings().getColumnCount()
                + extraVisionDepth; column--)
        {
            for (int row = 0; row < rowCount; row++)
            {
                nodes[row][column].setVisible(visible);
            }
        }
        for (int row = rowCount - 1; row >= Settings.getGameSettings().getRowCount()
                + extraVisionDepth; row--)
        {
            for (int column = 0; column < columnCount; column++)
            {
                nodes[row][column].setVisible(visible);
            }
        }
    }

    public void updatePoint(Vector position, Tile[][] board, boolean extraVision)
    {
        Vector gridCoordinate = position.add(extraVisionDepth);
        ArrayList<Vector> gridPositions = new ArrayList<Vector>();
        gridPositions.add(gridCoordinate);

        // if is on edge, Several panes should be refreshed, if extravision is true
        if (position.x < extraVisionDepth)
        {
            gridPositions
                    .add(new Vector(Settings.getGameSettings().getColumnCount() + gridCoordinate.x,
                            gridCoordinate.y));
        }
        if (position.y < extraVisionDepth)
        {
            gridPositions.add(new Vector(gridCoordinate.x,
                    Settings.getGameSettings().getRowCount() + gridCoordinate.y));
            if (position.x < extraVisionDepth) // topleft corner.
            {
                gridPositions.add(
                        new Vector(Settings.getGameSettings().getColumnCount() + gridCoordinate.x,
                                gridCoordinate.y + Settings.getGameSettings().getRowCount()));
            }
            if (position.x >= Settings.getGameSettings().getColumnCount() - extraVisionDepth)// topright
                                                                                             // corner
            {
                gridPositions.add(
                        new Vector(gridCoordinate.x - Settings.getGameSettings().getColumnCount(),
                                Settings.getGameSettings().getRowCount() + gridCoordinate.y));
            }
        }
        if (position.y >= Settings.getGameSettings().getRowCount() - extraVisionDepth)
        {
            int convertedY = gridCoordinate.y - Settings.getGameSettings().getRowCount();
            gridPositions.add(new Vector(gridCoordinate.x, convertedY));

            if (position.x < extraVisionDepth) // buttomLeft corner.
            {
                gridPositions.add(
                        new Vector(Settings.getGameSettings().getColumnCount() + gridCoordinate.x,
                                convertedY));
            }
            if (position.x >= Settings.getGameSettings().getColumnCount() - extraVisionDepth)// buttomright
                                                                                             // corner
            {
                gridPositions.add(
                        new Vector(gridCoordinate.x - Settings.getGameSettings().getColumnCount(),
                                convertedY));
            }
        }
        if (position.x >= Settings.getGameSettings().getColumnCount() - extraVisionDepth)
        {
            gridPositions
                    .add(new Vector(gridCoordinate.x - Settings.getGameSettings().getColumnCount(),
                            gridCoordinate.y));

        }


        for (Vector vector : gridPositions)
        {
            refreshPoint(vector, board, extraVision);
        }
    }
}
