package org.example.repository.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QualificationResultSetMapperImplTest {
    private QualificationResultSetMapperImpl mapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        mapper = new QualificationResultSetMapperImpl();
    }

    @Test
    void testMap() throws SQLException {
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("qualificationName")).thenReturn("test");

        var entity = mapper.map(resultSet);

        assertAll(
                () -> assertEquals(1L, entity.getId()),
                () -> assertEquals("test", entity.getQualificationName())
        );
    }
}