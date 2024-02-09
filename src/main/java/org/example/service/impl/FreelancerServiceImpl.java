package org.example.service.impl;

import org.example.model.FreelancerEntity;
import org.example.repository.FreelancerRepository;
import org.example.repository.impl.FreelancerRepositoryImpl;
import org.example.service.FreelancerService;
import org.example.service.dto.FreelancerDto;
import org.example.service.dto.FreelancerSimpleDto;
import org.example.service.mapper.FreelancerMapper;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;

public class FreelancerServiceImpl implements FreelancerService {
    private final FreelancerRepository repository;
    private final FreelancerMapper mapper;

    public FreelancerServiceImpl() {
        this.repository = new FreelancerRepositoryImpl();
        this.mapper = FreelancerMapper.INSTANCE;
    }

    public FreelancerServiceImpl(FreelancerRepository repository, FreelancerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public FreelancerDto save(FreelancerDto dto) {
        return dto != null && !repository.containsFreelancerByEmail(dto.getFreelancerEmail())
                ? mapper.toDto(repository.save(mapper.toEntity(dto)))
                : dto;
    }

    @Override
    public FreelancerDto findById(Long id) {
        if (id == null) return null;
        Optional<FreelancerEntity> maybeEntity = repository.findById(id);
        FreelancerDto dto = null;
        if (maybeEntity.isPresent()) {
            dto = mapper.toDto(maybeEntity.get());
        }
        return dto;
    }

    @Override
    public List<FreelancerSimpleDto> findAll() {
        return repository.findAll().stream().map(mapper::toSimpleDto).collect(toList());
    }

    @Override
    public boolean update(FreelancerDto dto) {
        if (dto != null && dto.getId() != null) {
            repository.update(mapper.toEntity(dto));
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return id != null && repository.deleteById(id);
    }
}