package org.example.repository.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreelancerResultSetMapperImplTest {
    private FreelancerResultSetMapperImpl mapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        mapper = new FreelancerResultSetMapperImpl();
    }

    @Test
    void testMap() throws SQLException {
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("freelancerName")).thenReturn("test");
        when(resultSet.getString("freelancerSecondName")).thenReturn("test");
        when(resultSet.getString("freelancerEmail")).thenReturn("test@test.com");

        var entity = mapper.map(resultSet);

        assertAll(
                () -> assertEquals(1L, entity.getId()),
                () -> assertEquals("test", entity.getFreelancerName()),
                () -> assertEquals("test", entity.getFreelancerSecondName()),
                () -> assertEquals("test@test.com", entity.getFreelancerEmail())
        );
    }
}