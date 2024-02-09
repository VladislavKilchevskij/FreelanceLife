package org.example.repository;


import org.example.model.OrderEntity;

import java.sql.Connection;
import java.util.List;

public interface OrderRelationRepository {
    List<OrderEntity> findAllByRelationId(Long relId, Connection connection);
}
