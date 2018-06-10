package me.marcsteiner.glacier.api;

import me.marcsteiner.glacier.accounts.User;

import java.util.UUID;

/**
 * Glacier Main API face used for interacting with various components of
 * Glacier without needing to hassle with them directly. Used internally
 * by Glacier and by outside plugins.
 *
 * @author Marc Steiner
 */
public interface GlacierAPI {

    /**
     * Registers a new User in Glaciers ecosystem and
     * adds them to the persistent database.
     *
     * @param user User to be registered
     */
    void registerUser(User user);

    /**
     * Deletes a user from GLaciers ecosystem
     * and removes them from the persistent databse,
     * if existent. This calls {@link me.marcsteiner.glacier.api.GlacierAPI#deleteUser(UUID)}
     * with the {@link me.marcsteiner.glacier.accounts.User#uuid}.
     *
     * @param user User to be deleted
     */
    void deleteUser(User user);

    /**
     * Deletes a user from Glaciers ecosystem
     * and removes them from the persistent database,
     * if existent.
     *
     * @param uuid UUID to be deleted
     */
    void deleteUser(UUID uuid);

    /**
     * Authenticates a user with the provided username
     * (which can be either username or eMail) and password.
     *
     * @param username Username or eMail to be checked for
     * @param password Password to be checked for
     * @return User object if authentication was successful, else <code>null</code>
     */
    User authUser(String username, String password);

    /**
     * Returns <code>true</code> if user has been
     * registered in Glaciers ecosystem and in the
     * persistent database. This calls {@link me.marcsteiner.glacier.api.GlacierAPI#isRegistered(UUID)}
     * with the {@link me.marcsteiner.glacier.accounts.User#uuid}.
     *
     * @param user User to be checked for
     * @return <code>true</code> if registered, otherwise <code>false</code>
     */
    boolean isRegistered(User user);

    /**
     * Returns <code>true</code> if user has been
     * registered in Glaciers ecosystem and in the
     * persistent database.
     *
     * @param username Username to be checked for
     * @return <code>true</code> if registered, otherwise <code>false</code>
     */
    boolean isRegistered(String username);

    /**
     * Returns <code>true</code> if user has been
     * registered in Glaciers ecosystem and in the
     * persistent database.
     *
     * @param uuid UUID to be checked for
     * @return <code>true</code> if registered, otherwise <code>false</code>
     */
    boolean isRegistered(UUID uuid);

    /**
     * Constructs a user object based
     * on the provided username from values found in
     * persistent database, if existent.
     *
     * @param username Username to be searched for in Database to construct user
     * @return Constructed user
     */
    User getUser(String username);

    /**
     * Constructs a user object based
     * on provided UUID from values found in
     * persistent database, if existent.
     *
     * @param uuid UUID to be searched for in Database to construct user
     * @return Constructed user
     */
    User getUser(UUID uuid);

}
