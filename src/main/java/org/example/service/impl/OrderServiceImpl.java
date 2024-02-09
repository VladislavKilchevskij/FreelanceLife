package org.example.service.impl;

import org.example.model.OrderEntity;
import org.example.repository.OrderRepository;
import org.example.repository.impl.OrderRepositoryImpl;
import org.example.service.OrderService;
import org.example.service.dto.OrderDto;
import org.example.service.dto.OrderSimpleDto;
import org.example.service.mapper.OrderMapper;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;

    public OrderServiceImpl() {
        this.repository = new OrderRepositoryImpl();
        this.mapper = OrderMapper.INSTANCE;
    }

    public OrderServiceImpl(OrderRepository repository, OrderMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public OrderDto save(OrderDto dto) {
        return dto != null ? mapper.toDto(repository.save(mapper.toEntity(dto))) : null;
    }

    @Override
    public OrderDto findById(Long id) {
        if (id == null) return null;
        Optional<OrderEntity> maybeEntity = repository.findById(id);
        OrderDto dto = null;
        if (maybeEntity.isPresent()) {
            dto = mapper.toDto(maybeEntity.get());
        }
        return dto;
    }

    @Override
    public List<OrderSimpleDto> findAll() {
        return repository.findAll().stream().map(mapper::toSimpleDto).collect(toList());
    }

    @Override
    public boolean update(OrderDto dto) {
        if (dto != null && dto.getId() != null) {
            repository.update(mapper.toEntity(dto));
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return id != null && repository.deleteById(id);
    }
}