package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.*;
import com.codecool.dungeoncrawl.ui.LevelMap;
import com.codecool.dungeoncrawl.ui.Tiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Scanner;

public class MapLoader implements Serializable {

    public static GameMap loadMap(String fileName) {
        InputStream is = MapLoader.class.getResourceAsStream(fileName);
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();
        List<Actor> actorsOnMap = new LinkedList<>();
        List<Item> itemsOnMap = new LinkedList<>();
        Item currentItem;

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
//                    Map<String, Tiles.Tile> tileMap =  LevelMap.getLevelMap().getTileMap();
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            actorsOnMap.add(new Skeleton(cell));
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case '$':
                            cell.setType(CellType.FLOOR);
                            currentItem = new Sword(cell);
                            updateWithDefaultId(currentItem, itemsOnMap);
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            currentItem = new Key(cell);
                            updateWithDefaultId(currentItem, itemsOnMap);
                            break;
                        case 'c':
                            cell.setType(CellType.CLOSED_DOOR);
                            break;
                        case 'g':
                            cell.setType(CellType.GATE);
                            break;
                        case '~':
                            cell.setType(CellType.WALL);
                            new Lake(cell);
                            break;
                        case 'a':
                            cell.setType(CellType.WALL);
                            new CandleStatue(cell);
                            break;
                        case 'w':
                            cell.setType(CellType.WALL);
                            new CampFire(cell);
//                            new Decoration(cell,"campFire");
                            break;
                        case '^':
                            cell.setType(CellType.WALL);
                            new TreeWall(cell);

//                            new Decoration(cell, "treeWall");
                            break;
                        case ',':
                            cell.setType(CellType.FLOOR2);
                            break;
                        case 'f':
                            cell.setType(CellType.GATE_FINAL);
                            break;
                        case 'z':
                            cell.setType(CellType.FLOOR);
                            actorsOnMap.add(new Zombie(cell));
                            break;
                        case 'O':
                            cell.setType(CellType.FLOOR2);
                            actorsOnMap.add(new Ghost(cell));
                            break;
                        case 'B':
                            cell.setType(CellType.FLOOR2);
                            actorsOnMap.add(new Boss(cell));
                            break;
                        case 'o':
                            cell.setType(CellType.FLOOR);
                            actorsOnMap.add(new Slime(cell));
                            break;
                        case 'G':
                            cell.setType(CellType.GATE_UP);
                            map.setPlayer(new Player(cell));
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        map.setActorList(actorsOnMap);
        map.setItemList(itemsOnMap);
        return map;
    }

    private static void updateWithDefaultId(Item currentItem, List<Item> itemsOnMap) {
        Common.itemIdCounter++;
        currentItem.setDefaultId(Common.itemIdCounter);
        itemsOnMap.add(currentItem);
    }

}
