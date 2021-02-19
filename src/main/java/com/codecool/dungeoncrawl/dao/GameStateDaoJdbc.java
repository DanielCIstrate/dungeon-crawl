package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO game_state (current_map, saved_at, player_id) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, state.getCurrentMap());
            statement.setDate(2, state.getSavedAt());
            statement.setInt(3, state.getPlayer().getId());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();

            state.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding new GameState", e);
        }
    }

    @Override
    public void update(GameState state) {
        try (Connection conn = dataSource.getConnection()){
            String sql = "UPDATE game_state SET current_map = ?, saved_at = ?, player_id = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, state.getCurrentMap());
            statement.setDate(2, state.getSavedAt());
            statement.setInt(3, state.getPlayer().getId());
            statement.setInt(4, state.getId());
            statement.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(int id) {
        try (Connection conn = dataSource.getConnection()){
            String sql = "SELECT " +
                    "g.current_map, g.saved_at, p.player_name, p.hp, p.x, p.y " +
                    "FROM " +
                    "game_state AS g INNER JOIN player AS p " +
                    "ON (p.id = g.player_id) " +
                    "WHERE g.id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                return null;
            } else {
                PlayerModel playerModel = new PlayerModel(
                        result.getString(3),
                        result.getInt(5),
                        result.getInt(6)
                );
                playerModel.setHp(result.getInt(4));
                GameState gameState = new GameState(
                        result.getString(1),
                        result.getDate(2),
                        playerModel
                );
                return gameState;
            }



        } catch (SQLException e) {
            throw new RuntimeException("Error while reading game state with id" + id, e);
        }
    }

    @Override
    public List<GameState> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT " +
                    "g.current_map, g.saved_at, p.player_name, p.hp, p.x, p.y " +
                    "FROM " +
                    "game_state AS g INNER JOIN player AS p " +
                    "ON (p.id = g.player_id)";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            List<GameState> gameStateList = new ArrayList<>();
            while (result.next()) {
                PlayerModel playerModel = new PlayerModel(
                        result.getString(3),
                        result.getInt(5),
                        result.getInt(6)
                );
                playerModel.setHp(result.getInt(4));
                GameState gameState = new GameState(
                        result.getString(1),
                        result.getDate(2),
                        playerModel
                );
                gameStateList.add(gameState);
            }
            return gameStateList;
        } catch (SQLException e) {
            throw new RuntimeException("Error while trying to read all game states", e);
        }
    }


    @Override
    public void addInItemLinkTable(int gameStateId, int itemId) {
        try(Connection connectionObject = dataSource.getConnection()) {
            String sqlQuery = "INSERT INTO gamestate_item (gamestate_id, item_id) VALUES (?, ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(
                    sqlQuery,
                    Statement.RETURN_GENERATED_KEYS
            );


            precompiledQuery.setInt(1, gameStateId);
            precompiledQuery.setInt(2, itemId);
            precompiledQuery.executeUpdate();
            ResultSet resultCursor = precompiledQuery.getGeneratedKeys();   // get results Map/dictionary (using the

            resultCursor.next();

        } catch(
                SQLException throwables)

        {
            throw new RuntimeException("Error while linking item "+itemId+" to gamestate "+gameStateId, throwables);
        }
    }
}
