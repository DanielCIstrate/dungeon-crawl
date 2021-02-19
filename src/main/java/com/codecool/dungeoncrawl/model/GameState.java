package com.codecool.dungeoncrawl.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class GameState extends BaseModel {
    private Date savedAt;   // java.sql.Date is just the Year-Month-Day (no hour:min)
    private String currentMap;
    private List<String> discoveredMaps = new ArrayList<>();
    private List<ItemModel> itemDeltas = new ArrayList<>();
    private PlayerModel player;
    private final SimpleStringProperty currentMapForTable;
    private final SimpleStringProperty saveDateForTable;
    private final SimpleStringProperty playerNameForTable;
    private final SimpleIntegerProperty playerHpForTable;

    public GameState(String currentMap, Date savedAt, PlayerModel player) {
        this.currentMap = currentMap;
        this.savedAt = savedAt;
        this.player = player;
        currentMapForTable = new SimpleStringProperty(currentMap);
        saveDateForTable = new SimpleStringProperty(savedAt.toString());
        playerNameForTable = new SimpleStringProperty(player.getPlayerName());
        playerHpForTable = new SimpleIntegerProperty(player.getHp());
    }

    public Date getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    public String getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(String currentMap) {
        this.currentMap = currentMap;
    }

    public List<String> getDiscoveredMaps() {
        return discoveredMaps;
    }

    public void addDiscoveredMap(String map) {
        this.discoveredMaps.add(map);
    }

    public PlayerModel getPlayer() {
        return player;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
    }

    public List<ItemModel> getItemDeltas() {
        return itemDeltas;
    }

    public void addItemDelta(ItemModel item) {
        this.itemDeltas.add(item);
    }

    public String getCurrentMapForTable() {
        return currentMapForTable.get();
    }

    public SimpleStringProperty currentMapForTableProperty() {
        return currentMapForTable;
    }

    public String getSaveDateForTable() {
        return saveDateForTable.get();
    }

    public SimpleStringProperty saveDateForTableProperty() {
        return saveDateForTable;
    }

    public String getPlayerNameForTable() {
        return playerNameForTable.get();
    }

    public SimpleStringProperty playerNameForTableProperty() {
        return playerNameForTable;
    }

    public int getPlayerHpForTable() {
        return playerHpForTable.get();
    }

    public SimpleIntegerProperty playerHpForTableProperty() {
        return playerHpForTable;
    }
}
