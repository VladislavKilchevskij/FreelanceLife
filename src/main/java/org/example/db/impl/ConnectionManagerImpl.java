package org.example.db.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.db.ConnectionManager;
import org.example.db.PropertiesUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManagerImpl implements ConnectionManager {

    private static ConnectionManagerImpl instance;
    private final HikariDataSource hikariCP;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        loadDriver();
    }

    {
        hikariCP = new HikariDataSource(new HikariConfig(PropertiesUtil.getProperties()));
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

    public static ConnectionManagerImpl getInstance() {
        return instance == null ? new ConnectionManagerImpl() : instance;
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
        hikariCP.close();
        instance = null;
    }
}