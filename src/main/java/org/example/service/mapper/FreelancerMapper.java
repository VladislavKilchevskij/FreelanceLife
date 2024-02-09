package org.example.service.mapper;

import org.example.model.FreelancerEntity;
import org.example.service.dto.FreelancerDto;
import org.example.service.dto.FreelancerSimpleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FreelancerMapper {

    FreelancerMapper INSTANCE = Mappers.getMapper(FreelancerMapper.class);

    @Mapping(target = "qualifications", ignore = true)
    FreelancerEntity toEntity(FreelancerSimpleDto dto);
    FreelancerEntity toEntity(FreelancerDto dto);

    FreelancerDto toDto(FreelancerEntity entity);

    FreelancerSimpleDto toSimpleDto(FreelancerEntity entity);
}
