package org.example.repository.impl;

import org.example.exception.RepositoryException;

import java.sql.Connection;
import java.sql.SQLException;

class RepositoryUtil {

    private RepositoryUtil() {
    }

    static void connectionRollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RepositoryException(e);
            }
        }
    }

    static void connectionClose(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RepositoryException(e);
            }
        }
    }
}
