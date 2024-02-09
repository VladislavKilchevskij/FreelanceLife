package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.PropertiesUtil;
import org.example.db.impl.ConnectionManagerImpl;
import org.example.model.FreelancerEntity;
import org.example.model.QualificationEntity;
import org.example.repository.FreelancerRepository;
import org.example.repository.impl.FreelancerRepositoryImpl;
import org.example.repository.impl.QualificationRelationRepositoryImpl;
import org.example.repository.mapper.impl.FreelancerResultSetMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@Testcontainers
class FreelancerRepositoryImplTest {
    private static final String TEST_DB_NAME = "freelance";
    private static final String TEST_DB_INIT_SCRIPT_FILE_NAME = "db-migration.SQL";
    private static final String IMAGE_NAME = "mysql:8.0";
    private static ConnectionManager connectionManager;
    private static FreelancerRepository repository;

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
        repository = new FreelancerRepositoryImpl(connectionManager, new FreelancerResultSetMapperImpl(), new QualificationRelationRepositoryImpl());
    }

    @AfterAll
    static void afterAll() {
        connectionManager.destroy();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void findByIdWhenPresent() {
        var expected = prepareExistedFreelancer();

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
    void containsFreelancerByEmailWhenPresentAndAbsent() {
        var existedEmail = "zaitseva@test.com";
        var absentEmail = "1111111@test.com";

        assertAll(
                () -> assertTrue(repository.containsFreelancerByEmail(existedEmail)),
                () -> assertFalse(repository.containsFreelancerByEmail(absentEmail))
        );
    }

    @Test
    void findAll() {
        int expectedSize = 20;

        var all = repository.findAll();

        assertEquals(expectedSize, all.size());
    }

    @Test
    void save() {
        var expected = prepareNewFreelancer();

        var saved = repository.save(expected);

        assertAll(
                () -> assertNotNull(expected.getId()),
                () -> assertEquals(expected, saved)
        );
    }


    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void updateWhenNewFieldsValue() {
        var expected = prepareExistedFreelancer();
        expected.setFreelancerName("тест");
        expected.setFreelancerSecondName("тест1");
        expected.setFreelancerSecondName("тест2");

        repository.update(expected);

        var updatedFromDB = repository.findById(expected.getId());

        assertAll(
                () -> assertTrue(updatedFromDB.isPresent()),
                () -> assertEquals(expected, updatedFromDB.get())
        );
    }

    @Test
    void deleteById() {
        var toDelete = prepareExistedFreelancer();

        assertAll(
                () -> assertTrue(repository.deleteById(toDelete.getId())),
                () -> assertTrue(repository.findById(toDelete.getId()).isEmpty())
        );
    }

    private static FreelancerEntity prepareExistedFreelancer() {
        FreelancerEntity expected = new FreelancerEntity();
        expected.setId(1L);
        expected.setFreelancerName("Иван");
        expected.setFreelancerSecondName("Иванов");
        expected.setFreelancerEmail("ivanov@test.com");
        return expected;
    }

    private static FreelancerEntity prepareNewFreelancer() {
        FreelancerEntity expected = new FreelancerEntity();
        expected.setFreelancerName("Марк");
        expected.setFreelancerSecondName("Кушин");
        expected.setFreelancerEmail("kushin@test.com");
        List<QualificationEntity> list = new ArrayList<>();
        var qual1 = new QualificationEntity();
        qual1.setId(1L);
        qual1.setQualificationName("Веб-разработка");
        var qual2 = new QualificationEntity();
        qual2.setId(2L);
        qual2.setQualificationName("Мобильная разработка");
        list.add(qual1);
        list.add(qual2);
        expected.setQualifications(list);
        return expected;
    }
}