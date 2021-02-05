package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;

import java.lang.reflect.Array;

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
//        char[] directions = new Array<Character>;
//                {'E', 'N', 'W', 'S'};
//        for
        return null;
    }
}
