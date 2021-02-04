package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    CLOSED_DOOR("closedDoor"),
    OPEN_DOOR("openDoor"),
    GATE("levelGate"),
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
