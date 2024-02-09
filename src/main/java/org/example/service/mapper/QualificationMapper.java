package org.example.service.mapper;

import org.example.model.QualificationEntity;
import org.example.service.dto.QualificationDto;
import org.example.service.dto.QualificationSimpleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface QualificationMapper {
    QualificationMapper INSTANCE = Mappers.getMapper(QualificationMapper.class);

    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "freelancers", ignore = true)
    QualificationEntity toEntity(QualificationSimpleDto dto);

    QualificationEntity toEntity(QualificationDto dto);

    QualificationDto toDto(QualificationEntity entity);
    QualificationSimpleDto toSimpleDto(QualificationEntity entity);

}
