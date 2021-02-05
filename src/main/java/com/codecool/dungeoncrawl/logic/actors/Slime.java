package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Slime extends Enemy {
    public Slime(Cell cell) {
        super(cell);
        this.setDamage(3);
        this.setSpeed(1.0);
    }

    public void move() {
//        Cell nextCell = this.getCell().getRandomNeighbor();
        // Will do combat, but it will also not move after attempting
        // to enter an occupied square (will move slower than once every 'speed' seconds)
//        if (canMove(nextCell)) {
//            this.getCell().setActor(null);
//            nextCell.setActor(this);
//            this.setCell(nextCell);

//        }

    }

    @Override
    public String getTileName() {
        return "slime";
    }
}
