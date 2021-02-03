package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Sword extends Weapon{

    public Sword(Cell cell) {
        super(cell);
        this.damage = 160;
    }

    @Override
    public String getTileName() {
        return "sword";
    }


    public void modifyDurabilityBy(Integer amount ) {
        setDurabilty(super.getDurability() + amount);
    }

}
