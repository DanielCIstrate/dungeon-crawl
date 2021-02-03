package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public abstract class Enemy extends Actor {

    public Enemy(Cell cell) {
        super(cell);
    }

    @Override
    public void onEncounterAsHost(Actor guest) {
        super.onEncounterAsHost(guest);
        if (guest.getTileName().equals("player")) {
            guest.setHealth(guest.getHealth() - this.getDamage());
            System.out.println("The " + this.getClass().getSimpleName() + " hits " +
                    guest.getClass().getSimpleName() + " for " +
                    this.getDamage() + " damage.");
        }
    }
}
