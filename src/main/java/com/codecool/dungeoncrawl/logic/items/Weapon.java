package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public abstract class Weapon extends Item{
    public Integer damage;

    public Weapon(Cell cell) {
        super(cell);
        this.damage = 1;
    }

    public Integer getDamage() {
        return this.damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }
}
