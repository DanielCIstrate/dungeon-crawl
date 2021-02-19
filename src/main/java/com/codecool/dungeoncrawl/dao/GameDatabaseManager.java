package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.ItemModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.codecool.dungeoncrawl.ui.GameLog;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.security.InvalidKeyException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private ItemDao itemDao;
    private GameStateDao gameStateDao;
    private Connection commonConnection;

    public void setup() throws SQLException, InvalidKeyException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        itemDao = new ItemDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource);
        commonConnection = dataSource.getConnection();
    }

    public void savePlayer(Player player) {
        PlayerModel model = new PlayerModel(player);
        playerDao.add(model);
    }

    private DataSource connect() throws SQLException, InvalidKeyException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        String databaseName = System.getenv("PSQL_DB_NAME");
        String userName = System.getenv("PSQL_USER_NAME");
        String userPassword = System.getenv("PSQL_PASSWORD");

        if (databaseName == null || userName == null || userPassword == null) {
            throw new InvalidKeyException("Some PSQL_.. environment variables are missing!");
        }

        dataSource.setDatabaseName(databaseName);
        dataSource.setUser(userName);
        dataSource.setPassword(userPassword);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }

    public void registerDefaultItem(Item item) {
        ItemModel model = new ItemModel(item);
        itemDao.addInDefault(model);
    };

    public void saveItemDeltas(GameState gameStateModel) {
        List<ItemModel> itemModelList = gameStateModel.getItemDeltas();
        int gameStateId = gameStateModel.getId();
        int currentItemId;
        for (ItemModel itemModel : itemModelList) {
            itemDao.add(itemModel);
            currentItemId = itemModel.getId();
            gameStateDao.addInItemLinkTable(gameStateId, currentItemId);
        }


    }


    public void dumpGameState(GameState gameState) {
        boolean canRollback = false;
        boolean hasSavePoint = false;
        boolean previousAutoCommit = true;  // default behaviour
        boolean hasWrittenPlayer = false;
        boolean hasWrittenGameState = false;
        boolean hasWrittenItems = false;
        Savepoint savedState;
        try {
        previousAutoCommit = commonConnection.getAutoCommit();}
        catch (SQLException e) {
            System.out.println("Could not get autocommit flag!" + e.getMessage());
            System.out.println("Defaulting to reverting as 'autocommit = true' after transaction...");
        }
        try {
            commonConnection.setAutoCommit(false);
            canRollback = true;
        } catch (SQLException e) {
            throw new RuntimeException("Could not set autocommit flag", e);
        }
        try {
            savedState = commonConnection.setSavepoint();
            hasSavePoint = true;
        } catch (SQLException e) {
            throw new RuntimeException("Could not set a save point in DB", e);
        }
        if (canRollback && hasSavePoint) {
            // add player
            try {
                playerDao.add(gameState.getPlayer());
                hasWrittenPlayer = true;
            } catch (Exception e) {
                hasWrittenPlayer = false;
                e.printStackTrace();
            }
        }
        if (hasWrittenPlayer) {
            // add game_state
            try {
                gameStateDao.add(gameState);
                hasWrittenGameState = true;
            } catch (Exception e) {
                hasWrittenGameState = false;
                try {
                    commonConnection.rollback(savedState);
                } catch (Exception f) {
                    f.printStackTrace();
                }
                throw new RuntimeException("", e);
            }
        }
        // add items (gameState needs to be successful)
        if (hasWrittenGameState) {
            try {
                saveItemDeltas(gameState);
                hasWrittenItems = true;
            } catch (Exception e) {
                hasWrittenItems = false;
                try {
                    commonConnection.rollback(savedState);
                } catch (Exception f) {
                    f.printStackTrace();
                }
                throw new RuntimeException("Could not save all items!", e);
            }
        }

        if (hasWrittenPlayer && hasWrittenGameState && hasWrittenItems) {
            try {
                commonConnection.commit();
            } catch (SQLException e) {
                throw new RuntimeException("Could not commit!", e);
            }
            try {
                commonConnection.setAutoCommit(previousAutoCommit);
            } catch (Exception e) {
                System.out.println("Could not revert autocommit");
                e.printStackTrace();
            }
            GameLog.getGameLog().pushInLog("Save successful!");
        }
    }
}
