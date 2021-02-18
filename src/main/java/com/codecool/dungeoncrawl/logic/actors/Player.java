package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Inventory;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Weapon;


import static com.codecool.dungeoncrawl.ui.GameLog.getGameLog;

public class Player extends Actor {
    Inventory inventory;
    public Player(Cell cell) {
        super(cell);
        this.setDamage(5);
    }

    @Override
    public void onEncounterAsGuest(Actor host) {
        super.onEncounterAsGuest(host);
        if (host instanceof Enemy) {
            doCombat(host);
        }
    }

    @Override
    public void onEncounterAsHost(Actor guest) {
        super.onEncounterAsHost(guest);
        if (guest instanceof Enemy) {
            doCombat(guest);
        }
    }

    private void doCombat(Actor opponent) {

        Weapon equippedWeapon = Inventory.getInventory().getBestWeaponInInventory();
        Integer damageFromWeapon = 0;
        if (equippedWeapon != null) {
            damageFromWeapon = equippedWeapon.getDamage();
        }
        Integer dealtDamage = this.getDamage()+damageFromWeapon;
        opponent.setHealth( opponent.getHealth()-dealtDamage );
        getGameLog().pushInLog("You hit the " + opponent.getClass().getSimpleName() +
                " for " + dealtDamage + " damage.");
        if (opponent.getHealth() <= 0) {
            this.killAnother(opponent);
        }
    }

    public boolean hasKeyInInventory() {
        Class<?> keyClass = null;
        try {
             keyClass = Class.forName("com.codecool.dungeoncrawl.logic.items.Key");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();            
        }
        return Inventory.getInventory().isInInventoryItemOfType(keyClass);
    }


    public String getTileName() {
        return "player";
    }


    public String getCalculatedDamageString() {
        Weapon equippedWeapon = Inventory.getInventory().getBestWeaponInInventory();
        int damageFromWeapon = 0;
        if (equippedWeapon != null) {
            damageFromWeapon = equippedWeapon.getDamage();
        }
        int dealtDamage = this.getDamage()+damageFromWeapon;
        String result;
        result = "" + this.getDamage() + "+" + damageFromWeapon + " = " + dealtDamage;
        return result;
    }
}
