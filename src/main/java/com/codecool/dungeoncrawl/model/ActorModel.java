package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class ActorModel extends BaseModel {
    private String className;
    private int hp;
    private int x;
    private int y;

    public ActorModel(String className, int x, int y) {
        this.className = className;
        this.x = x;
        this.y = y;
    }

    public ActorModel(Actor actor) {
        this.className = actor.getClass().getName();
        this.x = actor.getX();
        this.y = actor.getY();

        this.hp = actor.getHealth();

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
