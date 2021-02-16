package com.codecool.dungeoncrawl.ui;


import com.codecool.dungeoncrawl.logic.Drawable;
import com.codecool.dungeoncrawl.ui.LevelMap;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Tiles implements Serializable {
    public static int TILE_WIDTH = 32;

    private static Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static Map<String, Tile> tileMap = LevelMap.getLevelMap().getTileMap();
    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        tileMap.put("empty", new Tiles.Tile(0, 0));
        tileMap.put("wall", new Tiles.Tile(10, 17));
        tileMap.put("floor", new Tiles.Tile(2, 0));
        tileMap.put("player", new Tiles.Tile(27, 0));
        tileMap.put("skeleton", new Tiles.Tile(29, 6));
        tileMap.put("slime", new Tiles.Tile(23, 9));
        tileMap.put("sword", new Tiles.Tile(0,24));
        tileMap.put("key", new Tiles.Tile(16,23));
        tileMap.put("closedDoor",new Tiles.Tile(8,11));
        tileMap.put("openDoor",new Tiles.Tile(8,10));
        tileMap.put("levelGate", new Tiles.Tile(6,6));
        tileMap.put("treeWall", new Tiles.Tile(3,1));
        tileMap.put("lake", new Tiles.Tile(8,5));
        tileMap.put("candleStatue", new Tiles.Tile(4,15));
        tileMap.put("campFire", new Tiles.Tile(14,10));
        tileMap.put("footpath",new Tiles.Tile(1,0));
        tileMap.put("finalPortal",new Tiles.Tile(1,9));
        tileMap.put("levelGatePrev", new Tiles.Tile(6,6));
    }


    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }
}