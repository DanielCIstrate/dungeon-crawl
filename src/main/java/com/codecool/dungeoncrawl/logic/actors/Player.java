package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Inventory;
import com.codecool.dungeoncrawl.logic.items.Weapon;


import static com.codecool.dungeoncrawl.ui.GameLog.getGameLog;

public class Player extends Actor {
    public Player(Cell cell) {
        super(cell);
        this.setDamage(5);
    }

    @Override
    public void onEncounterAsGuest(Actor host) {
        super.onEncounterAsGuest(host);
        if (host instanceof Enemy) {
            Weapon equippedWeapon = Inventory.getInventory().getBestWeaponInInventory();
            Integer damageFromWeapon = 0;
            if (equippedWeapon != null) {
                damageFromWeapon = equippedWeapon.getDamage();
            }
            Integer dealtDamage = this.getDamage()+damageFromWeapon;
            host.setHealth( host.getHealth()-dealtDamage );
            getGameLog().pushInLog("You hit the " + host.getClass().getSimpleName() +
                    " for " + dealtDamage + " damage.");
            if (host.getHealth() <= 0) {
                this.killAnother(host);
            }
        }
    }

    public String getTileName() {
        return "player";
    }


}
