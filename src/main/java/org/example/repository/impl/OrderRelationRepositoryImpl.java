package org.example.repository.impl;

import org.example.exception.RepositoryException;
import org.example.model.OrderEntity;
import org.example.repository.OrderRelationRepository;
import org.example.repository.mapper.OrderResultSetMapper;
import org.example.repository.mapper.impl.OrderResultSetMapperImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRelationRepositoryImpl implements OrderRelationRepository {
    private final OrderResultSetMapper resultSetMapper;

    private static final String FIND_ALL_WITH_FOREIGN_KEY = "SELECT * FROM qualif_order WHERE qualificationId=?";

    public OrderRelationRepositoryImpl() {
        this.resultSetMapper = new OrderResultSetMapperImpl();
    }

    public OrderRelationRepositoryImpl(OrderResultSetMapper resultSetMapper) {
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public List<OrderEntity> findAllByRelationId(Long relId, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_WITH_FOREIGN_KEY)) {
            preparedStatement.setLong(1, relId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<OrderEntity> orders = new ArrayList<>();
            while (resultSet.next()) {
                var order = resultSetMapper.map(resultSet);
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}
