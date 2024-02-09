package org.example.servlet.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.service.dto.FreelancerDto;
import org.example.service.dto.FreelancerSimpleDto;
import org.example.servlet.mapper.FreelancerJsonMapper;

import java.text.SimpleDateFormat;
import java.util.List;

public final class FreelancerJsonMapperImpl implements FreelancerJsonMapper {
    private final ObjectMapper mapper;
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    static {
        try {
            Class.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public FreelancerJsonMapperImpl() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.setDateFormat(new SimpleDateFormat(DATE_PATTERN));
    }

    public FreelancerJsonMapperImpl(ObjectMapper mapper) {
        this.mapper = mapper;
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.setDateFormat(new SimpleDateFormat(DATE_PATTERN));
    }

    @Override
    public String toJson(FreelancerDto dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJson(FreelancerSimpleDto dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJson(List<FreelancerSimpleDto> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FreelancerDto toDto(String json) {
        try {
            return mapper.readValue(json, FreelancerDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FreelancerSimpleDto toSimpleDto(String json) {
        try {
            return mapper.readValue(json, FreelancerSimpleDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
