package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;

import java.util.stream.Stream;

public abstract class Actor implements Drawable {
    private Cell cell;
    private int health = 10;
    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (canMove(nextCell)) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;

        }
    }

    public int getHealth() { return health; }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    private boolean containsKey() {
        return Main.getInventory().stream().map(Item::getTileName).anyMatch("key"::equals);
    }

    public boolean canMove(Cell targetCell) {
        if (targetCell.getType().equals(CellType.WALL) ||
        targetCell.getType().equals(CellType.WALL2) ||
        targetCell.getType().equals(CellType.STATUE1) ||
        targetCell.getType().equals(CellType.STATUE2) ||
        targetCell.getType().equals(CellType.LAKE)) {
            return false;
        }
        if (targetCell.getType().equals(CellType.CLOSED_DOOR)) {
            if (containsKey()) {
                targetCell.setType(CellType.OPEN_DOOR);
            }
            return false;
        }

        // Encounter condition is true here and should call onEncounter methods
        // for the two actors when that is implemented
        return targetCell.getActor() == null;
    }
}
