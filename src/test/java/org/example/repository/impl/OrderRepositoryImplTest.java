package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.PropertiesUtil;
import org.example.db.impl.ConnectionManagerImpl;
import org.example.model.OrderEntity;
import org.example.model.QualificationEntity;
import org.example.repository.OrderRepository;
import org.example.repository.impl.OrderRepositoryImpl;
import org.example.repository.impl.QualificationRelationRepositoryImpl;
import org.example.repository.mapper.impl.OrderResultSetMapperImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@Testcontainers
class OrderRepositoryImplTest {
    private static final String TEST_DB_NAME = "freelance";
    private static final String TEST_DB_INIT_SCRIPT_FILE_NAME = "db-migration.SQL";
    private static final String IMAGE_NAME = "mysql:8.0";
    private static ConnectionManager connectionManager;
    private static OrderRepository repository;

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
        repository = new OrderRepositoryImpl(connectionManager, new OrderResultSetMapperImpl(), new QualificationRelationRepositoryImpl());
    }

    @AfterAll
    static void afterAll() {
        connectionManager.destroy();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void findByIdWhenPresent() {
        var expected = prepareExistedOrder();

        var actual = repository.findById(expected.getId());

        assertAll(
                () -> assertTrue(actual.isPresent()),
                () -> assertEquals(expected, actual.get())
        );
    }

    @Test
    void findByIdWhenAbsent() {
        Long givenId = 700L;

        var actual = repository.findById(givenId);

        assertTrue(actual.isEmpty());
    }

    @Test
    void findAll() {
        int expectedSize = 35;

        var all = repository.findAll();

        assertEquals(expectedSize, all.size());
    }

    @Test
    void save() {
        var expected = prepareNewOrder();

        var saved = repository.save(expected);

        assertAll(
                () -> assertNotNull(expected.getId()),
                () -> assertEquals(expected, saved)
        );
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void updateWhenNewFieldsValue() {
        var expected = prepareExistedOrder();
        expected.setOrderTitle("тест");
        expected.setOrderDescription("тест");
        expected.setOrderPrice(new BigDecimal("10.00"));
        expected.setOrderTerm(LocalDate.of(2024, 12, 31));

        repository.update(expected);

        var updatedFromDB = repository.findById(expected.getId());

        assertAll(
                () -> assertTrue(updatedFromDB.isPresent()),
                () -> assertEquals(expected, updatedFromDB.get())
        );
    }

    @Test
    void deleteById() {
        var toDelete = prepareExistedOrder();

        assertAll(
                () -> assertTrue(repository.deleteById(toDelete.getId())),
                () -> assertTrue(repository.findById(toDelete.getId()).isEmpty())
        );
    }

    private static OrderEntity prepareExistedOrder() {
        OrderEntity expected = new OrderEntity();
        expected.setId(1L);
        expected.setOrderTitle("Разработка корпоративного сайта");
        expected.setOrderDescription("Требуется создание корпоративного сайта для крупной компании");
        expected.setOrderPrice(new BigDecimal("1500.00"));
        expected.setOrderTerm(LocalDate.of(2024, 3, 15));
        var qualification = new QualificationEntity();
        qualification.setId(1L);
        expected.setQualification(qualification);
        return expected;
    }

    private static OrderEntity prepareNewOrder() {
        OrderEntity expected = new OrderEntity();
        expected.setOrderTitle("Разработка Rest API");
        expected.setOrderDescription("Требуется создать Rst API сервиса по подготовке к собесам");
        expected.setOrderPrice(new BigDecimal("1600.00"));
        expected.setOrderTerm(LocalDate.of(2024, 4, 17));
        var qualification = new QualificationEntity();
        qualification.setId(1L);
        expected.setQualification(qualification);
        return expected;
    }
}