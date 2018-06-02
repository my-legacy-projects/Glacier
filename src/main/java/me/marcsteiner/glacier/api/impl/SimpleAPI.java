package me.marcsteiner.glacier.api.impl;

import me.marcsteiner.glacier.Glacier;
import me.marcsteiner.glacier.accounts.User;
import me.marcsteiner.glacier.api.GlacierAPI;
import org.apache.commons.lang3.NotImplementedException;

import java.util.UUID;

public class SimpleAPI implements GlacierAPI {

    // TODO: https://stackoverflow.com/questions/9964923/java-class-to-database-table-structure

    @Override
    public void registerUser(User user) {
        if (isRegistered(user)) {
            return;
        }

        throw new NotImplementedException("TODO");
    }

    @Override
    public void deleteUser(User user) {
        deleteUser(user.getUuid());
    }

    @Override
    public void deleteUser(UUID uuid) {
        if (!isRegistered(uuid)) {
            return;
        }

        throw new NotImplementedException("TODO");
    }

    @Override
    public User authUser(String username, String password) {
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
    public boolean isRegistered(UUID uuid) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public User getUser(String username) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public User getUser(UUID uuid) {
        throw new NotImplementedException("TODO");
    }

}
