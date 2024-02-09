package org.example.servlet.mapper;

import org.example.service.dto.OrderDto;
import org.example.service.dto.OrderSimpleDto;

import java.util.List;


public interface OrderJsonMapper {
    String toJson(OrderDto dto);

    String toJson(OrderSimpleDto dto);

    String toJson(List<OrderSimpleDto> list);

    OrderSimpleDto toSimpleDto(String json);

    OrderDto toDto(String json);
}
