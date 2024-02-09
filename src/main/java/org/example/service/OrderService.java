package org.example.service;

import org.example.service.dto.OrderDto;
import org.example.service.dto.OrderSimpleDto;

import java.util.List;

public interface OrderService {
    OrderDto save(OrderDto dto);

    OrderDto findById(Long id);

    List<OrderSimpleDto> findAll();

    boolean update(OrderDto dto);

    boolean delete(Long id);
}
