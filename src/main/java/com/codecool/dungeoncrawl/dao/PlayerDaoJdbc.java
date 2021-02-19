package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;
    private PlayerModel playerModel;

    public PlayerDaoJdbc(DataSource dataSource, PlayerModel playerModel) {
        this.dataSource = dataSource;
        this.playerModel = playerModel;
    }

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player (player_name, hp, x, y) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getHp());
            statement.setInt(3, player.getX());
            statement.setInt(4, player.getY());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PlayerModel player) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE player SET id = ?, player_name = ?, hp = ?, x = ?, y= ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, player.getId());
            statement.setString(2, player.getPlayerName());
            statement.setInt(3, player.getHp());
            statement.setInt(4, player.getX());
            statement.setInt(5, player.getY());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerModel get(int id) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT player_name, hp, x, y FROM public.player WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                return null;
            }
            PlayerModel playerModel = new PlayerModel(
                    result.getString(2),
                    result.getInt(4),
                    result.getInt(5));
            playerModel.setId(id);
            return playerModel;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading player with id", e);
        }
    }

    @Override
    public List<PlayerModel> getAll() {
        try (Connection conn = dataSource.getConnection()){
            String sql = "SELECT id, player_name, hp, x, y FROM player";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            PlayerModel playerModel;


            List<PlayerModel> playerList = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt(1);
                String player_name = rs.getString(2);
                int hp = rs.getInt(3);
                int x = rs.getInt(4);
                int y = rs.getInt(5);

                playerModel = new PlayerModel (player_name, x, y);
                playerModel.setId(id);
                playerModel.setHp(hp);
                playerList.add(playerModel);
            }
            return playerList;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
