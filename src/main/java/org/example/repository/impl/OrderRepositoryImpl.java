package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.impl.ConnectionManagerImpl;
import org.example.exception.RepositoryException;
import org.example.model.OrderEntity;
import org.example.repository.OrderRepository;
import org.example.repository.QualificationRelationRepository;
import org.example.repository.mapper.OrderResultSetMapper;
import org.example.repository.mapper.impl.OrderResultSetMapperImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {
    private final ConnectionManager manager;
    private final OrderResultSetMapper resultSetMapper;
    private final QualificationRelationRepository relationRepository;

    private enum OrderSQL {
        FIND_ALL("SELECT * FROM qualif_order"),
        FIND_BY_ID("SELECT * FROM qualif_order WHERE id=?"),
        SAVE("INSERT INTO qualif_order (orderTitle, orderDescription, orderPrice, orderTerm, qualificationId) VALUE (?, ?, ?, ?, ?)"),
        UPDATE("UPDATE qualif_order SET orderTitle=?, orderDescription=?, orderPrice=?, orderTerm=?, qualificationId=? " +
               "WHERE id=?"),
        DELETE("DELETE FROM qualif_order WHERE id=?");
        private final String value;

        OrderSQL(String value) {
            this.value = value;
        }

        private String getValue() {
            return value;
        }
    }

    public OrderRepositoryImpl() {
        this.manager = ConnectionManagerImpl.getInstance();
        this.resultSetMapper = new OrderResultSetMapperImpl();
        this.relationRepository = new QualificationRelationRepositoryImpl();
    }

    public OrderRepositoryImpl(ConnectionManager manager, OrderResultSetMapper mapper, QualificationRelationRepository relationRepository) {
        this.manager = manager;
        this.resultSetMapper = mapper;
        this.relationRepository = relationRepository;
    }

    @Override
    public Optional<OrderEntity> findById(Long id) {
        Connection connection = null;
        try {
            connection = manager.getConnection();
            connection.setAutoCommit(false);
            try (var preparedStatement = connection.prepareStatement(OrderSQL.FIND_BY_ID.getValue())) {
                preparedStatement.setLong(1, id);
                var resultSet = preparedStatement.executeQuery();
                OrderEntity order = null;
                if (resultSet.next()) {
                    order = resultSetMapper.map(resultSet);
                    var qualification = relationRepository.findByRelationId(order.getQualification().getId(), connection);
                    order.setQualification(qualification.orElse(null));
                }
                connection.commit();
                return Optional.ofNullable(order);
            }
        } catch (SQLException e) {
            RepositoryUtil.connectionRollback(connection);
            throw new RepositoryException(e);
        } finally {
            RepositoryUtil.connectionClose(connection);
        }
    }


    @Override
    public List<OrderEntity> findAll() {
        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(OrderSQL.FIND_ALL.getValue())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<OrderEntity> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(resultSetMapper.map(resultSet));
            }
            return orders;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public OrderEntity save(OrderEntity entity) {
        try (var connection = manager.getConnection();
             var preparedStatement = connection.prepareStatement(OrderSQL.SAVE.getValue(), Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getOrderTitle());
            preparedStatement.setString(2, entity.getOrderDescription());
            preparedStatement.setBigDecimal(3, entity.getOrderPrice());
            preparedStatement.setDate(4, Date.valueOf(entity.getOrderTerm()));
            preparedStatement.setLong(5, entity.getQualification().getId());
            preparedStatement.executeUpdate();
            var keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                entity.setId(keys.getObject(1, Long.class));
            }
            return entity;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(OrderEntity entity) {
        try (var connection = manager.getConnection();
             var preparedStatement = connection.prepareStatement(OrderSQL.UPDATE.getValue())) {
            preparedStatement.setString(1, entity.getOrderTitle());
            preparedStatement.setString(2, entity.getOrderDescription());
            preparedStatement.setBigDecimal(3, entity.getOrderPrice());
            preparedStatement.setDate(4, Date.valueOf(entity.getOrderTerm()));
            preparedStatement.setLong(5, entity.getQualification().getId());
            preparedStatement.setLong(6, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (var connection = manager.getConnection();
             var preparedStatement = connection.prepareStatement(OrderSQL.DELETE.getValue())) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}