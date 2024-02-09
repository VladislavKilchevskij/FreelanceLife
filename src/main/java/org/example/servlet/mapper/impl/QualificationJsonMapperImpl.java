package org.example.servlet.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.service.dto.QualificationDto;
import org.example.service.dto.QualificationSimpleDto;
import org.example.servlet.mapper.QualificationJsonMapper;

import java.text.SimpleDateFormat;
import java.util.List;

public final class QualificationJsonMapperImpl implements QualificationJsonMapper {
    private final ObjectMapper mapper;
    private static final String DATE_PATTERN = "yyyy-MM-dd";


    static {
        try {
            Class.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public QualificationJsonMapperImpl() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.setDateFormat(new SimpleDateFormat(DATE_PATTERN));
    }

    public QualificationJsonMapperImpl(ObjectMapper mapper) {
        this.mapper = mapper;
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.setDateFormat(new SimpleDateFormat(DATE_PATTERN));
    }

    @Override
    public String toJson(QualificationSimpleDto dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJson(QualificationDto dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJson(List<QualificationSimpleDto> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QualificationSimpleDto toSimpleDto(String json) {
        try {
            return mapper.readValue(json, QualificationSimpleDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QualificationDto toDto(String json) {
        try {
            return mapper.readValue(json, QualificationDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
