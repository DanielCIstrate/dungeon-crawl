package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Decoration extends Item {
    private Cell cell;


    public Decoration(Cell cell) {
        super(cell);
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell newCell) {
        this.cell = newCell;
    }

}
