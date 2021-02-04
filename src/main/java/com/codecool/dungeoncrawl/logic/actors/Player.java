package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Player extends Actor {
    public Player(Cell cell) {
        super(cell);
        this.setDamage(5);
    }

    @Override
    public void onEncounterAsGuest(Actor host) {
        super.onEncounterAsGuest(host);
        if (host instanceof Enemy) {
            host.setHealth(host.getHealth()-this.getDamage());
            System.out.println("You hit the " + host.getClass().getSimpleName() +
                    " for " + this.getDamage() + " damage.");
            if (host.getHealth() <= 0) {
                this.killAnother(host);
            }
        }
    }

    public String getTileName() {
        return "player";
    }


}
