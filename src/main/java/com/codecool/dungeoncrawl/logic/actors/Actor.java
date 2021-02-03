package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class Actor implements Drawable {
    private Cell cell;
    private static Map<String, Integer> attributeModifierMap = new HashMap<>();
    private int health = 10;
    private int damage = 0;

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

    public void setHealth(int newValue) { this.health = newValue; }


    public int getDamage() { return damage; }

    public void setDamage(int newValue) { this.damage = newValue; }

    public void modifyAttribute(String attribute, Integer modifierValue) {
        if (attributeModifierMap.containsKey(attribute)) {
            Integer oldValue = attributeModifierMap.get(attribute);
            attributeModifierMap.put(attribute, oldValue+modifierValue);
        } else {
            attributeModifierMap.put(attribute, modifierValue);
        }
    }

    public void recalculateAttributes() throws NoSuchFieldException, IllegalAccessException {
        Field[] allFields = this.getClass().getFields();
        boolean hasModified = false;
        for (Field attribute : allFields) {
            if (attribute.getType().isAssignableFrom(Integer.class) ) {

                if (attributeModifierMap.containsKey(attribute.getName())) {
                    hasModified = true;
                    Field attributeAsIntField = null;
                    try {
                         attributeAsIntField = this.getClass().getField(attribute.getName());
                        attributeAsIntField.setAccessible(true);
                    } catch (NoSuchFieldException e1) {
                        e1.printStackTrace();
                        throw new NoSuchFieldException("Can not get field [" + attribute.getName() +
                                "] from " +
                                this.getClass().getName());
                    }
                    Integer oldValue = null;
                    try {
                        oldValue = (Integer) attribute.get(this);
                    } catch (IllegalAccessException e2) {
                        e2.printStackTrace();
                        throw new IllegalAccessException("Can not cast [" + attribute.getName() +
                                "] gotten from " + this.getClass().getName() + " as Integer");
                    }

                    Integer newValue = oldValue + attributeModifierMap.get(attribute.getName());
                    try {
                        attributeAsIntField.setInt(this, newValue);
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        throw new IllegalAccessException("Can not set an int value to [" +
                                attribute.getName() + "] gotten from " + this.getClass().getName());
                    }


                }
            }
        }
        attributeModifierMap.clear();
        if (!hasModified) {System.out.println("No attributes were modified!");}
        else {System.out.println("Recalculation modified something. Proceeding as normal...");}
    }


    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public boolean canMove(Cell targetCell) {
        if (targetCell.getType().equals(CellType.WALL)) {return false;}


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
        System.out.println(victim.getClass().getSimpleName()+" has died!");
        victim = null;    // sets pointer to 'host' as null, and it will be deleted by the garbage collector

    }


}
