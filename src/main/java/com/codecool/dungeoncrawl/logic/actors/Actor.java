package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Common;
import com.codecool.dungeoncrawl.logic.Drawable;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;

import java.util.stream.Stream;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.codecool.dungeoncrawl.ui.GameLog.getGameLog;

public abstract class Actor implements Drawable {
    private Cell cell;
    private static Map<String, Integer> attributeModifierMap = new HashMap<>();
    private int health;
    private int damage;
    private int defaultId;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
        health = 10;
        damage = 0;
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell != null) {
            if (canMove(nextCell)) {
                cell.setActor(null);
                nextCell.setActor(this);
                cell = nextCell;
            }
        }
    }

    public int getHealth() { return health; }

    public void setHealth(int newValue) { this.health = newValue; }


    public int getDamage() { return damage; }

    public void setDamage(int newValue) { this.damage = newValue; }

//    public void modifyAttribute(String attribute, Integer modifierValue) {
//        if (attributeModifierMap.containsKey(attribute)) {
//            Integer oldValue = attributeModifierMap.get(attribute);
//            attributeModifierMap.put(attribute, oldValue+modifierValue);
//        } else {
//            attributeModifierMap.put(attribute, modifierValue);
//        }
//    }
//
//    public void recalculateAttributes() throws NoSuchFieldException, IllegalAccessException {
//        Field[] allFields = this.getClass().getFields();
//        boolean hasModified = false;
//        for (Field attribute : allFields) {
//            if (attribute.getType().isAssignableFrom(Integer.class) ) {
//
//                if (attributeModifierMap.containsKey(attribute.getName())) {
//                    hasModified = true;
//                    Field attributeAsIntField = null;
//                    try {
//                         attributeAsIntField = this.getClass().getField(attribute.getName());
//                        attributeAsIntField.setAccessible(true);
//                    } catch (NoSuchFieldException e1) {
//                        e1.printStackTrace();
//                        throw new NoSuchFieldException("Can not get field [" + attribute.getName() +
//                                "] from " +
//                                this.getClass().getName());
//                    }
//                    Integer oldValue = null;
//                    try {
//                        oldValue = (Integer) attribute.get(this);
//                    } catch (IllegalAccessException e2) {
//                        e2.printStackTrace();
//                        throw new IllegalAccessException("Can not cast [" + attribute.getName() +
//                                "] gotten from " + this.getClass().getName() + " as Integer");
//                    }
//
//                    Integer newValue = oldValue + attributeModifierMap.get(attribute.getName());
//                    try {
//                        attributeAsIntField.setInt(this, newValue);
//                    } catch (Exception e3) {
//                        e3.printStackTrace();
//                        throw new IllegalAccessException("Can not set an int value to [" +
//                                attribute.getName() + "] gotten from " + this.getClass().getName());
//                    }
//
//
//                }
//            }
//        }
//        attributeModifierMap.clear();
//        if (!hasModified) {System.out.println("No attributes were modified!");}
//        else {System.out.println("Recalculation modified something. Proceeding as normal...");}
//    }


    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell newCell) {
        this.cell = newCell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }



    public boolean canMove(Cell targetCell) {
        CellType targetCellType = targetCell.getType();
        if (targetCellType.equals(CellType.WALL) ||
                targetCellType.equals(CellType.WALL2) ||
                targetCellType.equals(CellType.STATUE1) ||
                targetCellType.equals(CellType.STATUE2) ||
                targetCellType.equals(CellType.LAKE)) {
            return false;
        }
        if (targetCell.getType().equals(CellType.CLOSED_DOOR)) {
            if (this instanceof Player) {
                Player thisAsPlayer = (Player) this;
                if (thisAsPlayer.hasKeyInInventory()) {
                    targetCell.setType(CellType.OPEN_DOOR);
                }
            }
            return false;
        }


        if (targetCell.getActor() == null) {
            return true;
        } else {
            // Encounter condition is true here and should call onEncounter methods
            Actor targetActor = targetCell.getActor();
            this.onEncounterAsGuest(targetActor);
            targetActor.onEncounterAsHost(this);
            // finally..
            return false;   // can not actually do a move (replace actor of that cell)
        }
    }

    public void onEncounterAsHost(Actor guest) {
        // What happens when 'guest' tries to enter this.cell (specifically, this Actor's cell)
        //
        // empty for now... After some common patterns will be established, can implement here
    }

    public void onEncounterAsGuest(Actor host) {
        // What happens when this Actor "encounters" (tries to move) into a cell that contains 'host'
        //
        // empty for now... After some common patterns will be established, can implement here
    }

    public void killAnother(Actor victim) {
        Cell hostCell = victim.getCell();
        hostCell.setActor(null);
        hostCell.getGameMap().removeNullsInActorList();
        getGameLog().pushInLog(victim.getClass().getSimpleName()+" has died!");
        victim = null;    // sets pointer to 'victim' as null, and it will be deleted by the garbage collector

    }

    public void doMoveLogic() {
        if (this instanceof Enemy) {
            Enemy thisActorAsEnemy = (Enemy) this;
            if (thisActorAsEnemy.getSpeed() > 0.0) {
                double approxMovesToMake = 2*thisActorAsEnemy.getSpeed();
                if (approxMovesToMake > 0.5) {
                    int movesToMake = (int) Math.round(approxMovesToMake);
                    for (int i = 0; i < movesToMake; i++) {
                        thisActorAsEnemy.move();
                    }
                } else {
                    int periodOfMovement = (int) Math.floor(1.0 / approxMovesToMake);
//                    assert (periodOfMovement >= 2);
                    if (Common.turnCounter % periodOfMovement == 0) {
                        thisActorAsEnemy.move();
                    }
                }

            }
        }
    }


}
