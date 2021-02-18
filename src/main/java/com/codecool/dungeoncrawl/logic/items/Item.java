package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Common;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Item implements Drawable {
    private Cell cell;
    public Integer durability;
    public boolean isInInventory;
    private int defaultId;


    public Item(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
        this.durability = 100;
        this.isInInventory = false;
        this.defaultId = Common.dummyIntValue;
    }

    public Item(Cell cell, int defaultId) {
        this.cell = cell;
        this.cell.setItem(this);
        this.durability = 100;
        this.isInInventory = false;
        this.defaultId = defaultId;
    }

    public Cell getCell() {
        return cell;
    }
    
    public void setCell(Cell newCell) {
        if (newCell != null) {
            this.cell = newCell;
            newCell.setItem(this);
        } else {
            if (this.cell != null) {
                this.cell.setItem(null);
                this.cell = null;
            }
        }
    }
    
    public Integer getX() {
        if (this.cell != null) {
            return this.cell.getX();
        } else {
            return null;
        }
    }
    
    public Integer getY() {
        if (this.cell != null) {
            return this.cell.getY();
        } else {
            return null;
        }
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

    public int getDefaultId() {
        return defaultId;
    }

    public void setDefaultId(int defaultId) {
        this.defaultId = defaultId;
    }
}
