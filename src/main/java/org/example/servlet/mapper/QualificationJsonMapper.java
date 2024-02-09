package org.example.servlet.mapper;

import org.example.service.dto.QualificationDto;
import org.example.service.dto.QualificationSimpleDto;

import java.util.List;

public interface QualificationJsonMapper {
    String toJson(QualificationSimpleDto dto);

    String toJson(QualificationDto dto);

    String toJson(List<QualificationSimpleDto> list);

    QualificationSimpleDto toSimpleDto(String json);

    QualificationDto toDto(String json);
}
