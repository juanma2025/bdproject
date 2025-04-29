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
    public void save(Actor t) {
        return;
    }

    @Override
    public void delete(Integer id) {
        return;
    }

}
