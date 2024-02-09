package org.example.service.impl;

import org.example.model.QualificationEntity;
import org.example.repository.QualificationRepository;
import org.example.repository.impl.QualificationRepositoryImpl;
import org.example.service.QualificationService;
import org.example.service.dto.QualificationDto;
import org.example.service.dto.QualificationSimpleDto;
import org.example.service.mapper.QualificationMapper;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class QualificationServiceImpl implements QualificationService {
    private final QualificationRepository repository;
    private final QualificationMapper mapper;

    public QualificationServiceImpl() {
        this(new QualificationRepositoryImpl(), QualificationMapper.INSTANCE);
    }

    public QualificationServiceImpl(QualificationRepository repository, QualificationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public QualificationSimpleDto save(QualificationSimpleDto dto) {
        return dto != null ? mapper.toSimpleDto(repository.save(mapper.toEntity(dto))) : null;
    }

    @Override
    public QualificationDto findById(Long id) {
        if (id == null) return null;
        Optional<QualificationEntity> maybeEntity = repository.findById(id);
        QualificationDto dto = null;
        if (maybeEntity.isPresent()) {
            dto = mapper.toDto(maybeEntity.get());
        }
        return dto;
    }

    @Override
    public List<QualificationSimpleDto> findAll() {
        return repository.findAll().stream().map(mapper::toSimpleDto).collect(toList());
    }

    @Override
    public boolean update(QualificationSimpleDto dto) {
        if (dto != null && dto.getId() != null) {
            repository.update(mapper.toEntity(dto));
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return repository.deleteById(id);
    }
}