package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.items.Item;

public class ItemModel extends BaseModel {
    private String className;
    private boolean isInInventory;
    private Integer x;
    private Integer y;
    private int defaultId;

    public ItemModel(String className, boolean shouldBeFalse, Integer x, Integer y, int idFromDefaultTable) {
        this.className = className;
        this.x = x;
        this.y = y;
        this.isInInventory = shouldBeFalse;
        this.defaultId = idFromDefaultTable;
    }

    public ItemModel(Item item) {
        this.className = item.getClass().getName();
        this.isInInventory = item.isInInventory;
        this.x = item.getX();
        this.y = item.getY();
        this.defaultId = item.getDefaultId();
    };



    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean getIsInInventory() {
        return isInInventory;
    }

    public void setIsInInventory(boolean newValue) {
        this.isInInventory = newValue;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDefaultId() {
        return defaultId;
    }

    public void setDefaultId(int defaultId) {
        this.defaultId = defaultId;
    }
}
