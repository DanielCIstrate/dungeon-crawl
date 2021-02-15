package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;


public class Skeleton extends Enemy {
    public Skeleton(Cell cell) {
        super(cell);
        this.setDamage(2);
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
