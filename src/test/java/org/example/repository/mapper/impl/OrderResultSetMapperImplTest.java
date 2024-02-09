package org.example.repository.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderResultSetMapperImplTest {
    private OrderResultSetMapperImpl mapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        mapper = new OrderResultSetMapperImpl();
    }

    @Test
    void testMap() throws SQLException {
        var orderPrice = new BigDecimal("100.00");
        var orderTerm = LocalDate.of(2024, 12, 31);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("orderTitle")).thenReturn("test");
        when(resultSet.getString("orderDescription")).thenReturn("test");
        when(resultSet.getBigDecimal("orderPrice")).thenReturn(orderPrice);
        when(resultSet.getObject("orderTerm", LocalDate.class)).thenReturn(orderTerm);

        var entity = mapper.map(resultSet);

        assertAll(
                () -> assertEquals(1L, entity.getId()),
                () -> assertEquals("test", entity.getOrderTitle()),
                () -> assertEquals("test", entity.getOrderDescription()),
                () -> assertEquals(orderPrice, entity.getOrderPrice()),
                () -> assertEquals(orderTerm, entity.getOrderTerm())
        );
    }
}