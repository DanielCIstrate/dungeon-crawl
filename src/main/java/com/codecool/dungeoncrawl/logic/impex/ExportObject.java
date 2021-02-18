package com.codecool.dungeoncrawl.logic.impex;

import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.Inventory;

import java.io.Serializable;

public class ExportObject implements Serializable {
    private GameMap exportedMapState;
    private Inventory exportedInventory;

    public ExportObject(GameMap currentMapState, Inventory currentInventory) {
        this.exportedInventory = currentInventory;
        this.exportedMapState = currentMapState;
    }

    public GameMap getExportedMapState() {
        return exportedMapState;
    }

    public void setExportedMapState(GameMap exportedMapState) {
        this.exportedMapState = exportedMapState;
    }

    public Inventory getExportedInventory() {
        return exportedInventory;
    }

    public void setExportedInventory(Inventory exportedInventory) {
        this.exportedInventory = exportedInventory;
    }
}
