package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.ItemModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.security.InvalidKeyException;
import java.sql.SQLException;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private ItemDao itemDao;

    public void setup() throws SQLException, InvalidKeyException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        itemDao = new ItemDaoJdbc(dataSource);
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
}
