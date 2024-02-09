package org.example.repository.mapper.impl;

import org.example.model.OrderEntity;
import org.example.model.QualificationEntity;
import org.example.repository.mapper.OrderResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrderResultSetMapperImpl implements OrderResultSetMapper {
    @Override
    public OrderEntity map(ResultSet resultSet) throws SQLException {
        OrderEntity entity = new OrderEntity();
        entity.setId(resultSet.getLong("id"));
        entity.setOrderTitle(resultSet.getString("orderTitle"));
        entity.setOrderDescription(resultSet.getString("orderDescription"));
        entity.setOrderPrice(resultSet.getBigDecimal("orderPrice"));
        entity.setOrderTerm(resultSet.getObject("orderTerm", LocalDate.class));
        var qualif = new QualificationEntity();
        qualif.setId(resultSet.getLong("qualificationId"));
        entity.setQualification(qualif);
        return entity;
    }
}