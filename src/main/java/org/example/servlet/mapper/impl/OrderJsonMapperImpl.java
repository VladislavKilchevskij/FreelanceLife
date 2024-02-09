package org.example.servlet.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.service.dto.OrderDto;
import org.example.service.dto.OrderSimpleDto;
import org.example.servlet.mapper.OrderJsonMapper;

import java.text.SimpleDateFormat;
import java.util.List;

public final class OrderJsonMapperImpl implements OrderJsonMapper {
    private final ObjectMapper mapper;

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    static {
        try {
            Class.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public OrderJsonMapperImpl() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.setDateFormat(new SimpleDateFormat(DATE_PATTERN));
    }

    public OrderJsonMapperImpl(ObjectMapper mapper) {
        this.mapper = mapper;
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.setDateFormat(new SimpleDateFormat(DATE_PATTERN));
    }

    @Override
    public String toJson(OrderDto dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJson(OrderSimpleDto dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJson(List<OrderSimpleDto> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrderSimpleDto toSimpleDto(String json) {
        try {
            return mapper.readValue(json, OrderSimpleDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrderDto toDto(String json) {
        try {
            return mapper.readValue(json, OrderDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
