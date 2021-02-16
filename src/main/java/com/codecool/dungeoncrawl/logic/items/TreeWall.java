package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class TreeWall extends Decoration{

    public TreeWall(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "treeWall";
    }
}
