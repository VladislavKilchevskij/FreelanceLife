package org.example.db.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.db.ConnectionManager;
import org.example.db.PropertiesUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManagerImpl implements ConnectionManager {

    private static ConnectionManagerImpl INSTANCE;
    private final HikariDataSource hikariCP;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        loadDriver();
    }

    {
        hikariCP = new HikariDataSource(preparedConfig());
    }

    private ConnectionManagerImpl() {
    }

    private static void loadDriver() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private HikariConfig preparedConfig() {
        var config = new HikariConfig(PropertiesUtil.getProperties());
        config.setMaximumPoolSize(20);
        return config;
    }

    public static ConnectionManagerImpl getInstance() {
        return INSTANCE == null ? new ConnectionManagerImpl() : INSTANCE;
    }

    @Override
    public Connection getConnection() {
        try {
            return hikariCP.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void destroy() {
        INSTANCE = null;
    }
}