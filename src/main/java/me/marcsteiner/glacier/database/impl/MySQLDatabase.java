package me.marcsteiner.glacier.database.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import com.zaxxer.hikari.pool.HikariPool;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.marcsteiner.glacier.Glacier;
import me.marcsteiner.glacier.database.Database;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class MySQLDatabase implements Database {

    @Setter
    private HikariDataSource dataSource;

    private final String address;
    private final int port;
    private final String database;

    private final String username;
    private final String password;

    @Override
    public void connect() throws HikariPool.PoolInitializationException {
        if (!isConnected()) {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://" + address + ":" + port + "/" + database);
            config.setUsername(username);
            config.setPassword(password);
            config.setMinimumIdle(Glacier.getInstance().getConfig().getInt("database.pool.min"));
            config.setMaximumPoolSize(Glacier.getInstance().getConfig().getInt("database.pool.max"));
            config.setConnectionTimeout(Glacier.getInstance().getConfig().getInt("database.pool.timeout"));

            setDataSource(new HikariDataSource(config));
        } else {
            Glacier.getInstance().getLogger().warn("Tried to connect to database while already being connected!");
        }
    }

    @Override
    public void disconnect() {
        if (isConnected()) {
            dataSource.close();
        } else {
            Glacier.getInstance().getLogger().warn("Database has already been disconnected.");
        }
    }

    @Override
    public boolean isConnected() {
        return dataSource != null && !dataSource.isClosed();
    }

    @Override
    public void setup() throws SQLException {
        // Setup required tables etc.
    }

    @Override
    public void update(String query) {
        try {
            @Cleanup Connection connection = getConnection();
            @Cleanup PreparedStatement statement = null;

            statement = connection.prepareStatement(encode(query));
            statement.executeUpdate();
        } catch (SQLException ex) {
            Glacier.getInstance().getLogger().error("Error while executing SQL Update!", ex);
        }
    }

    @Override
    public ResultSet query(String query) {
        try {
            @Cleanup Connection connection = getConnection();
            @Cleanup PreparedStatement statement = null;
            @Cleanup ResultSet resultSet = null;

            statement = connection.prepareStatement(encode(query));
            resultSet = statement.executeQuery();

            return resultSet;
        } catch (SQLException ex) {
            Glacier.getInstance().getLogger().error("Error while executing SQL Query!", ex);
        }

        return null;
    }

    @Override
    public Connection getConnection() {
        if (isConnected()) {
            try {
                return dataSource.getConnection();
            } catch (SQLException ex) {
                Glacier.getInstance().getLogger().error("Could not get a idling connection from connection pool.", ex);
            }
        } else {
            Glacier.getInstance().getLogger().error("Connect first to the database before trying to get a connection.");
        }

        return null;
    }

    @Override
    public HikariDataSource getConnectionPool() {
        if (isConnected()) {
            return dataSource;
        } else {
            Glacier.getInstance().getLogger().error("Connect first to the database before trying to get a connection pool.");
        }

        return null;
    }

    @Override
    public int getAvailableConnections() {
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName poolName;
            poolName = new ObjectName("com.zaxxer.hikari:type=Pool (foo)");

            HikariPoolMXBean poolProxy = JMX.newMXBeanProxy(mBeanServer, poolName, HikariPoolMXBean.class);

            return poolProxy.getIdleConnections();
        } catch (MalformedObjectNameException ex) {
            Glacier.getInstance().getLogger().error("Could not get amount of available connections.", ex);
            return -1;
        }
    }
}
