package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Ghost extends Enemy {
    public Ghost(Cell cell) {
        super(cell);
        this.setSpeed(0.3);
        this.setDamage(2);
        this.setHealth(9);
    }

    @Override
    public void move() {
        Cell nextCell = this.getCell().getRandomNeighbor();

//         Will do combat, but it will also not move after attempting
//         to enter an occupied square (will move slower than once every 'speed' seconds)
        if (canMove(nextCell)) {
            this.getCell().setActor(null);
            nextCell.setActor(this);
            this.setCell(nextCell);

        }
    }

    @Override
    public boolean canMove(Cell targetCell) {
        if (targetCell == null) { return false; }
        if (targetCell.getActor() == null) {
            return true;
        } else {
            // Encounter condition is true here and should call onEncounter methods
            Actor targetActor = targetCell.getActor();
            this.onEncounterAsGuest(targetActor);
            targetActor.onEncounterAsHost(this);
            // finally..
            return false;   // can not actually do a move (replace actor of that cell)
        }
    }

    @Override
    public String getTileName() {
        return "ghost";
    }
}
