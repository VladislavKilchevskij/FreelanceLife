package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.PropertiesUtil;
import org.example.db.impl.ConnectionManagerImpl;
import org.example.model.QualificationEntity;
import org.example.repository.QualificationRepository;
import org.example.repository.impl.*;
import org.example.repository.mapper.impl.QualificationResultSetMapperImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Testcontainers
class QualificationRepositoryImplTest {
    private static final String TEST_DB_NAME = "freelance";
    private static final String TEST_DB_INIT_SCRIPT_FILE_NAME = "db-migration.SQL";
    private static final String IMAGE_NAME = "mysql:8.0";
    private static ConnectionManager connectionManager;
    private static QualificationRepository repository;

    @Container
    static final MySQLContainer<?> CONTAINER =
            new MySQLContainer<>(IMAGE_NAME)
                    .withDatabaseName(TEST_DB_NAME)
                    .withInitScript(TEST_DB_INIT_SCRIPT_FILE_NAME);

    @BeforeAll
    static void beforeAll() {
        Properties testDbProps = PropertiesUtil.getProperties();
        testDbProps.setProperty("jdbcUrl", CONTAINER.getJdbcUrl());
        testDbProps.setProperty("username", CONTAINER.getUsername());
        testDbProps.setProperty("password", CONTAINER.getPassword());
        try (MockedStatic<PropertiesUtil> mockedProps = mockStatic(PropertiesUtil.class)) {
            mockedProps.when(PropertiesUtil::getProperties).thenReturn(testDbProps);
            connectionManager = ConnectionManagerImpl.getInstance();
        }
        repository = new QualificationRepositoryImpl(connectionManager,
                new QualificationResultSetMapperImpl(),
                new OrderRelationRepositoryImpl(),
                new FreelancerRelationRepositoryImpl());
    }

    @AfterAll
    static void afterAll() {
        connectionManager.destroy();
    }

    @Test
    void findByIdWhenPresent() {
        var expected = prepareExistedQualification();

        var actual = repository.findById(expected.getId());

        assertAll(
                () -> assertTrue(actual.isPresent()),
                () -> assertEquals(expected, actual.get())
        );
    }

    @Test
    void findByIdWhenAbsent() {
        Long givenId = 25L;

        var actual = repository.findById(givenId);

        assertTrue(actual.isEmpty());
    }

    @Test
    void findAll() {
        int expectedSize = 7;

        var all = repository.findAll();

        assertEquals(expectedSize, all.size());
    }

    @Test
    void save() {
        var expected = prepareNewQualification();

        var saved = repository.save(expected);

        assertAll(
                () -> assertNotNull(expected.getId()),
                () -> assertEquals(expected, saved)
        );
    }


    @Test
    void updateWhenNewFieldsValue() {
        var expected = prepareExistedQualification();
        expected.setQualificationName("тест");

        repository.update(expected);

        var updatedFromDB = repository.findById(expected.getId());

        assertAll(
                () -> assertTrue(updatedFromDB.isPresent()),
                () -> assertEquals(expected, updatedFromDB.get())
        );
    }

    @Test
    void deleteById() {
        var toDelete = prepareExistedQualification();

        assertAll(
                () -> assertTrue(repository.deleteById(toDelete.getId())),
                () -> assertTrue(repository.findById(toDelete.getId()).isEmpty())
        );
    }

    private static QualificationEntity prepareExistedQualification() {
        QualificationEntity expected = new QualificationEntity();
        expected.setId(1L);
        expected.setQualificationName("Веб-разработка");
        return expected;
    }

    private static QualificationEntity prepareNewQualification() {
        QualificationEntity expected = new QualificationEntity();
        expected.setQualificationName("тест");
        return expected;
    }
}