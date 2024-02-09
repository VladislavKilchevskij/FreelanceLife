package org.example.servlet.mapper;

import org.example.service.dto.FreelancerDto;
import org.example.service.dto.FreelancerSimpleDto;

import java.util.List;

public interface FreelancerJsonMapper {
    String toJson(FreelancerDto dto);

    String toJson(FreelancerSimpleDto dto);

    String toJson(List<FreelancerSimpleDto> list);

    FreelancerDto toDto(String json);

    FreelancerSimpleDto toSimpleDto(String json);
}
