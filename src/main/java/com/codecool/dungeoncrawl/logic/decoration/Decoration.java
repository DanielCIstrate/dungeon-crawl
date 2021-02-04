package com.codecool.dungeoncrawl.logic.decoration;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

public class Decoration implements Drawable {
    private Cell cell;
    public final String decorationName;


    public Decoration(Cell cell, String decorationName) {
        this.cell = cell;
        this.decorationName = decorationName;
    }

    public Cell getCell() {
        return cell;
    }

    @Override
    public String getTileName() {
        return decorationName;
    }
}
