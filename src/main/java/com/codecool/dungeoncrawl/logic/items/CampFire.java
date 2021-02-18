package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class CampFire extends Decoration{
    public CampFire(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "campFire";
    }
}
