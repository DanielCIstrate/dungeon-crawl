package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Boss extends Enemy{
    public Boss(Cell cell) {
        super(cell);
        this.setDamage(6);
        this.setHealth(20);
        this.setSpeed(0.4);
    }

    @Override
    public String getTileName() {
        return "boss";
    }
}
