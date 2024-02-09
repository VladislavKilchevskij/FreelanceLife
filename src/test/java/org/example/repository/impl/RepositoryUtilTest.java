package org.example.repository.impl;

import org.example.exception.RepositoryException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepositoryUtilTest {
    @Test
    void connectionRollbackWhenConnectionNotNull() throws SQLException {
        Connection connectionMock = mock(Connection.class);

        RepositoryUtil.connectionRollback(connectionMock);

        verify(connectionMock).rollback();
    }

    @Test
    void connectionCloseWhenConnectionNotNull() throws SQLException {
        Connection connectionMock = mock(Connection.class);

        RepositoryUtil.connectionClose(connectionMock);

        verify(connectionMock).close();
    }

    @Test
    void connectionRollbackWithException() throws SQLException {
        Connection connectionMock = mock(Connection.class);

        doThrow(new SQLException("Fake SQLException")).when(connectionMock).rollback();

        assertThrows(RepositoryException.class, () -> RepositoryUtil.connectionRollback(connectionMock));
    }

    @Test
    void connectionCloseWithException() throws SQLException {
        Connection connectionMock = mock(Connection.class);

        doThrow(new SQLException("Fake SQLException")).when(connectionMock).close();

        assertThrows(RepositoryException.class, () -> RepositoryUtil.connectionClose(connectionMock));
    }
}