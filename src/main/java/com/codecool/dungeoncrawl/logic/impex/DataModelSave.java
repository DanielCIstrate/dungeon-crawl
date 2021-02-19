package com.codecool.dungeoncrawl.logic.impex;

import com.codecool.dungeoncrawl.logic.Common;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.ItemModel;
import com.codecool.dungeoncrawl.model.PlayerModel;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class DataModelSave {

    public static GameState saveGameState(Player player, List<Item> items, int maxLevel, int currLevel) {
        PlayerModel playerModel = new PlayerModel(player);
        String currentMapName = Common.levelShortNames[currLevel];
        Date absoluteDate = getCurrentTimeAsAbsoluteDate();
        GameState gameStateModel = new GameState(
                currentMapName,
                absoluteDate,  // timestamp without time zone
                playerModel
        );
        for (int i = 0; i <= maxLevel; i++) {
            gameStateModel.addDiscoveredMap(Common.levelShortNames[i]);
        }
        for (Item item : items) {
            ItemModel currentItemModel = new ItemModel(item);
            gameStateModel.addItemDelta(currentItemModel);
        }
        return gameStateModel;
    }

    private static Date getCurrentTimeAsAbsoluteDate() {
        ZoneId thisTZ = ZoneId.systemDefault();
        LocalDateTime nowInThisTZ = LocalDateTime.ofInstant(Instant.now(),thisTZ);
        java.sql.Date sqlDate = java.sql.Date.valueOf( nowInThisTZ.toLocalDate() );
        return sqlDate;
    }
}
