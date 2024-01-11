package com.snake.Model;

import java.util.ArrayList;

public class GameState {
    private ArrayList<SnakeTile> snakeTiles;
    private ArrayList<Apple> apples;
    private ArrayList<Banana> bananas;
    private ArrayList<Cherry> cherries;
    private ArrayList<Wall> walls;
    private Snake[] players;

    public GameState(ArrayList<SnakeTile> snakeTiles, ArrayList<Apple> apples, ArrayList<Banana> bananas, ArrayList<Cherry> cherries, ArrayList<Wall> walls, Snake[] players) {
        this.snakeTiles = snakeTiles;
        this.apples = apples;
        this.bananas = bananas;
        this.cherries = cherries;
        this.walls = walls;
        this.players = players;
    }

    public ArrayList<SnakeTile> getSnakeTiles() {
        return snakeTiles;
    }

    public ArrayList<Apple> getApples() {
        return apples;
    }

    public ArrayList<Banana> getBananas() {
        return bananas;
    }

    public ArrayList<Cherry> getCherries() {
        return cherries;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public Snake[] getPlayers() {
        return players;
    }
}
