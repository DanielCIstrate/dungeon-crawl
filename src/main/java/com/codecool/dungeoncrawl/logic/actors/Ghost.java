package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Ghost extends Enemy {
    public Ghost(Cell cell) {
        super(cell);
        this.setSpeed(0.3);
        this.setDamage(2);
        this.setHealth(9);
    }

    @Override
    public String getTileName() {
        return "ghost";
    }
}
