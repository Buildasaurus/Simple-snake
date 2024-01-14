package com.snake.Model;

import java.util.ArrayList;
import java.util.List;

public class GameSettings
{

    private int columnCount = 30;
    private int rowCount = 30;

    public GameSettings()
    {}


    public int getSpeed()
    {
        return 3;
    }


    public int getColumnCount()
    {
        return columnCount;
    }

    public void setColumnCount(int columnCount)
    {
        this.columnCount = columnCount;
    }

    public int getRowCount()
    {
        return rowCount;
    }

    public void setRowCount(int rowCount)
    {
        this.rowCount = rowCount;
    }

}
