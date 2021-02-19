package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.ItemModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class ItemDaoJdbc implements ItemDao {
    private DataSource dataSource;

    public ItemDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(ItemModel item) {
        try(Connection connectionObject = dataSource.getConnection()) {
        String sqlQuery = "INSERT INTO item (class_name, is_in_inventory, x, y, default_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement precompiledQuery = connectionObject.prepareStatement(
                sqlQuery,
                Statement.RETURN_GENERATED_KEYS
        );


        precompiledQuery.setString(1, item.getClassName());  // sets the value of the first parameter

        boolean statusInInventory = item.getIsInInventory();
        precompiledQuery.setBoolean(2, statusInInventory);
        if (statusInInventory) {
            precompiledQuery.setNull(3, Types.INTEGER);
            precompiledQuery.setNull(4, Types.INTEGER);
        } else {
            precompiledQuery.setInt(3, item.getX());
            precompiledQuery.setInt(4, item.getY());
        }
        precompiledQuery.setInt(5,item.getDefaultId());
        precompiledQuery.executeUpdate();
        ResultSet resultCursor = precompiledQuery.getGeneratedKeys();   // get results Map/dictionary (using the

        resultCursor.next();
        item.setId(resultCursor.getInt(1));

        } catch(
        SQLException throwables)

        {
            throw new RuntimeException("Error while adding new Item.", throwables);
        }

    }

    @Override
    public void update(ItemModel item) {

    }

    @Override
    public ItemModel get(int id) {
        return null;
    }

    @Override
    public List<ItemModel> getAll() {
        return null;
    }

    @Override
    public void addInDefault(ItemModel item) {
        try(Connection connectionObject = dataSource.getConnection()) {
            String sqlQuery = "INSERT INTO default_item (id, class_name, is_in_inventory, x, y) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(
                    sqlQuery,
                    Statement.RETURN_GENERATED_KEYS
            );


            precompiledQuery.setString(2, item.getClassName());  // sets the value of the first parameter

            boolean statusInInventory = item.getIsInInventory();
            precompiledQuery.setBoolean(3, statusInInventory);
            if (statusInInventory) {
                precompiledQuery.setNull(4, Types.INTEGER);
                precompiledQuery.setNull(5, Types.INTEGER);
            } else {
                precompiledQuery.setInt(4, item.getX());
                precompiledQuery.setInt(5, item.getY());
            }
            precompiledQuery.setInt(1,item.getDefaultId());
            precompiledQuery.executeUpdate();
            ResultSet resultCursor = precompiledQuery.getGeneratedKeys();   // get results Map/dictionary (using the

            resultCursor.next();
            item.setId(resultCursor.getInt(1));

        } catch(
                SQLException throwables)

        {
            throw new RuntimeException("Error while adding new Item.", throwables);
        }
    }

    @Override
    public void updateInDefault(ItemModel item) {

    }

    @Override
    public ItemModel getFromDefault(int id) {
        return null;
    }

    @Override
    public List<ItemModel> getAllFromDefault() {
        return null;
    }
}
