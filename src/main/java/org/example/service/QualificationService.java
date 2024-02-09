package org.example.service;

import org.example.service.dto.QualificationDto;
import org.example.service.dto.QualificationSimpleDto;

import java.util.List;


public interface QualificationService {
    QualificationSimpleDto save(QualificationSimpleDto dto);

    QualificationDto findById(Long id);

    List<QualificationSimpleDto> findAll();

    boolean update(QualificationSimpleDto dto);

    boolean delete(Long id);
}
