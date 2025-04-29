package com.example.repository;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Actor;
import com.example.utils.DatabaseConnection;

public class ActorRepository implements Repository<Actor> {

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance();
    }

    @Override
    public List<Actor> findAll() throws SQLException {
        List<Actor> actors = new ArrayList<>();
        String sql = "SELECT * FROM sakila.actor";

        try (Statement myStatement = getConnection().createStatement()) {
            ResultSet myResultSet = myStatement.executeQuery(sql);
            while (myResultSet.next()) {
                Actor actor = createActor(myResultSet);
                actors.add(actor);
            }
        }
        return actors;
    }

    @Override
    public Actor getByID(Integer id) throws SQLException {
        Actor actor = null;
        String sql = "SELECT * FROM sakila.actor WHERE actor_id = ?";

        try (PreparedStatement myPreparedStatement = getConnection().prepareStatement(sql)) {
            myPreparedStatement.setInt(1, id);
            try (ResultSet myResultSet = myPreparedStatement.executeQuery()) {
                if (myResultSet.next()) {
                    actor = createActor(myResultSet);
                }
            }
        }
        return actor;

    }

    private Actor createActor(ResultSet myResultSet) throws SQLException {
        Actor a = new Actor();
        a.setActorID(myResultSet.getInt("actor_id"));
        a.setFirstName(myResultSet.getString("first_name"));
        a.setLastName(myResultSet.getString("last_name"));

        return a;
    }

    @Override
    public void save(Actor actor) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM sakila.actor WHERE actor_id = ?";
        String insertSql = "INSERT INTO sakila.actor (actor_id, first_name, last_name) VALUES (?, ?, ?)";
        String updateSql = "UPDATE sakila.actor SET first_name = ?, last_name = ? WHERE actor_id = ?";

        try (Connection connection = getConnection();
                PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {

            checkStatement.setInt(1, actor.getActorID());
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                // Update existing record
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                    updateStatement.setString(1, actor.getFirstName());
                    updateStatement.setString(2, actor.getLastName());
                    updateStatement.setInt(3, actor.getActorID());
                    updateStatement.executeUpdate();
                }
            } else {
                // Insert new record
                try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                    insertStatement.setInt(1, actor.getActorID());
                    insertStatement.setString(2, actor.getFirstName());
                    insertStatement.setString(3, actor.getLastName());
                    insertStatement.executeUpdate();
                }
            }
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM sakila.actor WHERE actor_id = ?";

        try (Connection connection = getConnection();
                PreparedStatement deleteStatement = connection.prepareStatement(sql)) {

            deleteStatement.setInt(1, id);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting actor with ID: " + id, e);
        }
    }

}
