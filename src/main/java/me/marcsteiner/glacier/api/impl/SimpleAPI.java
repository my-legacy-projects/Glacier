package me.marcsteiner.glacier.api.impl;

import me.marcsteiner.glacier.Glacier;
import me.marcsteiner.glacier.accounts.User;
import me.marcsteiner.glacier.api.GlacierAPI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleAPI implements GlacierAPI {

    @Override
    public void registerUser(User user) {
        checkNotNull(user);

        if (isRegistered(user)) {
            return;
        }

        String name = Glacier.getInstance().getDatabase().encode(user.getDisplayName());
        String mail = Glacier.getInstance().getDatabase().encode(user.getEMail());
        String salt = Glacier.getInstance().getDatabase().encode(user.getSalt());
        String password = Glacier.getInstance().getDatabase().encode(user.getPassword());

        Glacier.getInstance().getDatabase().update("INSERT INTO `users` (uuid, name, mail, salt, password) " +
                "VALUES ('" + user.getUuid() + "', '" + name + "', '" + mail + "', '" + salt + "', '" + password + "');");
    }

    @Override
    public void deleteUser(User user) {
        deleteUser(user.getUuid());
    }

    @Override
    public void deleteUser(UUID uuid) {
        checkNotNull(uuid);

        if (!isRegistered(uuid)) {
            return;
        }

        Glacier.getInstance().getDatabase().update("DELETE FROM `users` WHERE uuid = '" + uuid + "'");
    }

    @Override
    public User authUser(String username, String password) {
        checkNotNull(username);
        checkArgument(!username.isEmpty());
        checkNotNull(password);
        checkArgument(!password.isEmpty());

        User user = getUser(username);
        if (user == null) {
            return null;
        }

        return user.auth(password) ? user : null;
    }

    @Override
    public boolean isRegistered(User user) {
        return isRegistered(user.getUuid());
    }

    @Override
    @SuppressWarnings("LoopStatementThatDoesntLoop")
    public boolean isRegistered(String username) {
        checkNotNull(username);
        checkArgument(!username.isEmpty());

        String name = Glacier.getInstance().getDatabase().encode(username);

        ResultSet resultSet = Glacier.getInstance().getDatabase().query(
                "SELECT * FROM `users` WHERE name = '" + name + "'"
        );

        try {
            while (resultSet.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Glacier.getInstance().getLogger().error("Unable to check if user exists.", ex);
        }

        return false;
    }

    @Override
    @SuppressWarnings("LoopStatementThatDoesntLoop")
    public boolean isRegistered(UUID uuid) {
        checkNotNull(uuid);

        ResultSet resultSet = Glacier.getInstance().getDatabase().query(
                "SELECT * FROM `users` WHERE uuid = '" + uuid + "'"
        );

        try {
            while (resultSet.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Glacier.getInstance().getLogger().error("Unable to check if user exists.", ex);
        }

        return false;
    }

    @Override
    @SuppressWarnings("LoopStatementThatDoesntLoop")
    public User getUser(String username) {
        checkNotNull(username);
        checkArgument(!username.isEmpty());

        if (!isRegistered(username)) {
            return null;
        }

        String name = Glacier.getInstance().getDatabase().encode(username);

        ResultSet resultSet = Glacier.getInstance().getDatabase().query(
                "SELECT * FROM `users` WHERE name = '" + name + "'"
        );

        try {
            while (resultSet.next()) {
                return new User(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getString("mail"),
                        resultSet.getString("salt"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException ex) {
            Glacier.getInstance().getLogger().error("Unable to check if user exists.", ex);
        }

        return null;
    }

    @Override
    @SuppressWarnings("LoopStatementThatDoesntLoop")
    public User getUser(UUID uuid) {
        checkNotNull(uuid);

        if (!isRegistered(uuid)) {
            return null;
        }

        ResultSet resultSet = Glacier.getInstance().getDatabase().query(
                "SELECT * FROM `users` WHERE uuid = '" + uuid + "'"
        );

        try {
            while (resultSet.next()) {
                return new User(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getString("mail"),
                        resultSet.getString("salt"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException ex) {
            Glacier.getInstance().getLogger().error("Unable to check if user exists.", ex);
        }

        return null;
    }

}
