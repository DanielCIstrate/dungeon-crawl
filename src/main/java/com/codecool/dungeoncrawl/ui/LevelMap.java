package com.codecool.dungeoncrawl.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class LevelMap implements Serializable {
        private Map<String, Tiles.Tile> tileMap;
        private static LevelMap singleton;

        private LevelMap() {
            this.tileMap = new HashMap<>();
        }

        public static LevelMap getLevelMap() {
            if (singleton == null) {
                singleton = new LevelMap();
            }
            return singleton;
        }

        public Map<String, Tiles.Tile> getTileMap() {
            return tileMap;
        }

        // tileIndex and keys have the same length
        public void updateTileMap(int[][] tileIndex, ArrayList<String> keys) {
            int i = 0;
            for (String key : keys) {
                tileMap.put(key, new Tiles.Tile(tileIndex[i][0], tileIndex[i][1]));
                i++;
            }
        }

        public void resetTileMap() {
                tileMap.put("empty", new Tiles.Tile(0, 0));
                tileMap.put("wall", new Tiles.Tile(10, 17));
                tileMap.put("floor", new Tiles.Tile(2, 0));
                tileMap.put("player", new Tiles.Tile(27, 0));
                tileMap.put("skeleton", new Tiles.Tile(29, 6));
                tileMap.put("slime", new Tiles.Tile(23, 9));
                tileMap.put("sword", new Tiles.Tile(0,24));
                tileMap.put("key", new Tiles.Tile(16,23));
                tileMap.put("closedDoor", new Tiles.Tile(8,11));
                tileMap.put("openDoor", new Tiles.Tile(8,10));
                tileMap.put("levelGate", new Tiles.Tile(6,6));
                tileMap.put("treeWall", new Tiles.Tile(3,1));
                tileMap.put("lake", new Tiles.Tile(8,5));
                tileMap.put("candleStatue", new Tiles.Tile(4,15));
                tileMap.put("campFire", new Tiles.Tile(14,10));
                tileMap.put("footpath", new Tiles.Tile(1,0));
                tileMap.put("finalPortal", new Tiles.Tile(1,9));
            }

}




