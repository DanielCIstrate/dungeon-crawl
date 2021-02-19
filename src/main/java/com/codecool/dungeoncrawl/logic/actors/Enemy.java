package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import static com.codecool.dungeoncrawl.ui.GameLog.getGameLog;

public abstract class Enemy extends Actor {

    private Double speed;    // speed in seconds (can be fractional, hence the double)

    public Enemy(Cell cell) {
        super(cell);
        this.speed = 0.000;
    }

    public Double getSpeed() {
        return this.speed;
    }

    public void setSpeed(Double newValue) {
        this.speed = newValue;
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

    @Override
    public void onEncounterAsGuest(Actor host) {
        super.onEncounterAsGuest(host);
        if (host.getTileName().equals("player")) {
            host.setHealth(host.getHealth() - this.getDamage());
            getGameLog().pushInLog("The " + this.getClass().getSimpleName() + " hits " +
                    host.getClass().getSimpleName() + " for " +
                    this.getDamage() + " damage.");
        }
    }

    public void move() {

    }

}
