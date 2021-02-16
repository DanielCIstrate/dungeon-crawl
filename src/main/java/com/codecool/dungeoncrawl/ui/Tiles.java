package com.codecool.dungeoncrawl.ui;

import com.codecool.dungeoncrawl.logic.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;

    private static Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();
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
        tileMap.put("empty", new Tile(0, 0));
        tileMap.put("wall", new Tile(10, 17));
        tileMap.put("floor", new Tile(2, 0));
        tileMap.put("player", new Tile(27, 0));
        tileMap.put("skeleton", new Tile(29, 6));
        tileMap.put("slime", new Tile(23, 9));
        tileMap.put("sword", new Tile(0,24));
        tileMap.put("key", new Tile(16,23));
        tileMap.put("closedDoor",new Tile(8,11));
        tileMap.put("openDoor",new Tile(8,10));
        tileMap.put("levelGate", new Tile(6,6));
        tileMap.put("treeWall", new Tile(3,1));
        tileMap.put("lake", new Tile(8,5));
        tileMap.put("candleStatue", new Tile(4,15));
        tileMap.put("campFire", new Tile(14,10));
        tileMap.put("footpath",new Tile(1,0));
        tileMap.put("finalPortal",new Tile(1,9));
        tileMap.put("zombie", new Tile(26, 2));
        tileMap.put("ghost", new Tile(27, 6));
        tileMap.put("boss", new Tile(31, 6));
    }

    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }
}