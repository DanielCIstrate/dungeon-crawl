package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.items.*;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public final class Inventory implements Serializable {
    private List<Item> list;
    private static Inventory inventorySingleton;

    private Inventory() {
        this.list = new LinkedList<>();
    }

    public static Inventory getInventory() {
        if (inventorySingleton == null) {
            inventorySingleton = new Inventory();
        }
        return inventorySingleton;
    }

    public List<Item> getList() {
        return this.list;
    }

    public String toString() {
        StringBuilder contents = new StringBuilder("Items:\n");
        for (Item item: this.list) {
            contents.append(item.getClass().getSimpleName());
            contents.append("\n");
        }

        return contents.toString();
    }

    public void addToInventory(Item someItem) {
        this.list.add(someItem);
    }

    public boolean isInInventory(Item someItem) {
        return this.list.contains(someItem);
    }

    public boolean isInInventoryItemOfType(Class someItemSubClass) {
        boolean hasFound = false;
        for (Item item : this.list) {
            if (someItemSubClass.isAssignableFrom(item.getClass())) {
                hasFound = true;
                break;
            }
        }
        return hasFound;
    }

    public Weapon getBestWeaponInInventory() {
        Weapon foundWeapon = null;
        Double maxValue = 0.00;
        Double currentValue = 0.00;
        for (Item item : this.list ) {
            if (item instanceof Weapon) {
                Weapon itemAsWeapon = (Weapon) item;
                currentValue = itemAsWeapon.getAwesomenessValue();
                if (currentValue > maxValue) {
                    foundWeapon = itemAsWeapon;
                }
            }
        }
        return foundWeapon;
    }

}
