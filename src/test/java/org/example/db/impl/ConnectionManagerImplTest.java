package org.example.db.impl;

import org.example.db.ConnectionManager;
import org.example.db.PropertiesUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Testcontainers
class ConnectionManagerImplTest {

    private static ConnectionManager connectionManager;
    @Container
    static final MySQLContainer<?> CONTAINER = new MySQLContainer<>(("mysql:8.0"));

    @BeforeAll
    static void beforeAll() {
        var testProperties = new Properties();
        testProperties.put("jdbcUrl", CONTAINER.getJdbcUrl());
        testProperties.put("username", CONTAINER.getUsername());
        testProperties.put("password", CONTAINER.getPassword());

        try (MockedStatic<PropertiesUtil> staticMock = mockStatic(PropertiesUtil.class)) {
            staticMock.when(PropertiesUtil::getProperties).thenReturn(testProperties);
            connectionManager = ConnectionManagerImpl.getInstance();
        }
    }

    @AfterAll
    static void afterAll() {
        connectionManager.destroy();
    }

    @Test
    void getConnectionSuccess() {
        assertDoesNotThrow(() -> {
            try (var conn = connectionManager.getConnection()) {
            }
        });
    }
}