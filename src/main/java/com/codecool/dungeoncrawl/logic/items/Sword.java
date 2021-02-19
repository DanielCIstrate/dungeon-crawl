package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

import java.io.Serializable;

public class Sword extends Weapon implements Serializable {

    public Sword(Cell cell) {
        super(cell);
        this.damage = 20;
    }

    @Override
    public String getTileName() {
        return "sword";
    }


    public void modifyDurabilityBy(Integer amount ) {
        setDurability(super.getDurability() + amount);
    }

}
