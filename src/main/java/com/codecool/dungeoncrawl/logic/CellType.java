package com.codecool.dungeoncrawl.logic;

import java.io.Serializable;

public enum CellType implements Serializable {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    CLOSED_DOOR("closedDoor"),
    OPEN_DOOR("openDoor"),
    GATE("levelGate"),
    GATE_UP("levelGatePrev"),
    WALL2("treeWall"),
    STATUE1("candleStatue"),
    STATUE2("campFire"),
    LAKE("lake"),
    FLOOR2("footpath"),
    GATE_FINAL("finalPortal");

    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
