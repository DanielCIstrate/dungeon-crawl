package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.LinkedList;
import java.util.List;

public final class Inventory {
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

}
