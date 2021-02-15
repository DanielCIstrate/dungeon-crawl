package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;

    private Player player;

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        if (areBoundedIndices(x, y)) {
            return cells[x][y];
        }
        else {
            return null;
        }
    }

    public boolean areBoundedIndices(int someX, int someY) {
        boolean assumption = false;
        if ((someX >= 0) && (someX < cells.length)) {
            if ((someY >= 0) && (someY < cells[someX].length)) {
                assumption = true;
            }
        }
        return assumption;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public Cell getRandomNeighborCell(int x, int y) {
        int dummyValue = -1000;
        int dx = dummyValue;
        int dy = dummyValue;
        List<Cell> neighborList = new ArrayList<>();
        char[] directions = new char[]{'E', 'N', 'W', 'S'};
        for (char direction : directions) {
            switch (direction) {
                case 'E':
                    dx = 1;
                    dy = 0;
                    break;
                case 'N':
                    dx = 0;
                    dy = -1;
                    break;
                case 'W':
                    dx = -1;
                    dy = 0;
                    break;
                case 'S':
                    dx = 0;
                    dy = 1;
                    break;
            }
            if (areBoundedIndices(x+dx, y+dy)) {
                neighborList.add(getCell(x+dx, y+dy));
            }

        }
        if (neighborList.size() > 0) {
            int randomIndex = new Random().nextInt(neighborList.size());
            return neighborList.get(randomIndex);
        } else {
            throw new NullPointerException("Could not get random neighbor list!");
        }
    }
}
