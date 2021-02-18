package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.io.Serializable;

public abstract class Item implements Drawable, Serializable {
    private Cell cell;
    public Integer durability;
    public boolean isInInventory;


    public Item(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
        this.durability = 100;
        this.isInInventory = false;
    }

    public Cell getCell() {
        return cell;
    }

    public abstract String getTileName();

    public boolean getUnlockProperty() {
        return false;
    };

    public Integer getDurability() {
        return this.durability;
    };

    public void setDurability(Integer durability) {
        this.durability = durability;
    }

}
