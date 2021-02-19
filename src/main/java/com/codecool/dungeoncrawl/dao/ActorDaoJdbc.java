package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.ActorModel;

import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class ActorDaoJdbc implements ActorDao {
    private DataSource dataSource;

    public ActorDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(ActorModel actorModel) {
        try (Connection conn = dataSource.getConnection()){
            String sql = "INSERT INTO public_actor (id, class_name, hp, x, y, default_id) VALUES (?, ?, ?, ?, ?, ? )";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, actorModel.getId());
            statement.setString(2, actorModel.getClassName());
            statement.setInt(3, actorModel.getHp());
            statement.setInt(4, actorModel.getX());
            statement.setInt(5, actorModel.getY());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            actorModel.setId(resultSet.getInt(1));
        }catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    @Override
    public void update(ActorModel actorModel) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE actor SET id = ?, class_name = ?, hp = ?, x = ?, y = ?, default_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, actorModel.getId());
            statement.setString(2, actorModel.getClassName());
            statement.setInt(3, actorModel.getHp());
            statement.setInt(4, actorModel.getX());
            statement.setInt(5,actorModel.getY());
//            statement.setInt(6, actorModel.ge);
            statement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ActorModel get(int id) {
        try (Connection conn = dataSource.getConnection()){
            String sql = "SELECT class_name, hp, x, y, default_id FROM actor WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                return null;
            }
            ActorModel actorModel = new ActorModel(result.getString(2), result.getInt(3),
                    result.getInt(4));
            actorModel.setId(id);
            return actorModel;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading actor id", e);
        }
    }

    @Override
    public List<ActorModel> getAll() {
        try (Connection conn = dataSource.getConnection()){
            String sql = "SELECT id, player_name, hp, x, y FROM actor";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            ActorModel actorModel;


            List<ActorModel> actorList = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt(1);
                String actor_name = rs.getString(2);
                int hp = rs.getInt(3);
                int x = rs.getInt(4);
                int y = rs.getInt(5);

                actorModel = new ActorModel (actor_name, x, y);
                actorModel.setId(id);
                actorModel.setHp(hp);
                actorList.add(actorModel);
            }
            return actorList;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
