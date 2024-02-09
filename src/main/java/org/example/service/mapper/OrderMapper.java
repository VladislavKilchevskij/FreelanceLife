package org.example.service.mapper;

import org.example.model.OrderEntity;
import org.example.service.dto.OrderDto;
import org.example.service.dto.OrderSimpleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "qualification", ignore = true)
    OrderEntity toEntity(OrderSimpleDto dto);

    OrderEntity toEntity(OrderDto dto);

    OrderSimpleDto toSimpleDto(OrderEntity entity);

    OrderDto toDto(OrderEntity entity);
}
