package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import static com.codecool.dungeoncrawl.ui.GameLog.getGameLog;

public abstract class Enemy extends Actor {

    public Enemy(Cell cell) {
        super(cell);
    }

    @Override
    public void onEncounterAsHost(Actor guest) {
        super.onEncounterAsHost(guest);
        if (guest.getTileName().equals("player")) {
            guest.setHealth(guest.getHealth() - this.getDamage());
            getGameLog().pushInLog("The " + this.getClass().getSimpleName() + " hits " +
                    guest.getClass().getSimpleName() + " for " +
                    this.getDamage() + " damage.");
        }
    }
}
