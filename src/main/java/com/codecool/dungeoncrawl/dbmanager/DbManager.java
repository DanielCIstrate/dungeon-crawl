package com.codecool.dungeoncrawl.dbmanager;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DbManager {
    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        String databaseName = System.getenv("PSQL_DB_NAME");
        String userName = System.getenv("PSQL_USER_NAME");
        String userPassword = System.getenv("PSQL_PASSWORD");

        dataSource.setDatabaseName(databaseName);
        dataSource.setUser(userName);
        dataSource.setPassword(userPassword);

        return dataSource;
    }


}
