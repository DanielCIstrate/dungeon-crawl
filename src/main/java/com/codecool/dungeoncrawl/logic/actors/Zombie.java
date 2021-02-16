package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Zombie extends Enemy{

    public Zombie(Cell cell) {
        super(cell);
        this.setSpeed(0.5);
        this.setDamage(5);
        this.setHealth(11);

    }

    @Override
    public String getTileName() {
        return "zombie";
    }
}
