package org.example.service;

import org.example.service.dto.FreelancerDto;
import org.example.service.dto.FreelancerSimpleDto;

import java.util.List;

public interface FreelancerService {
    FreelancerDto save(FreelancerDto dto);

    FreelancerDto findById(Long id);

    List<FreelancerSimpleDto> findAll();

    boolean update(FreelancerDto dto);

    boolean delete(Long id);
}
