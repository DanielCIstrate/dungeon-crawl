package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class CandleStatue extends Decoration{
    public CandleStatue(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "candleStatue";
    }
}
