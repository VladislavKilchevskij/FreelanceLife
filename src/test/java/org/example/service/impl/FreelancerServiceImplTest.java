package org.example.service.impl;

import org.example.model.FreelancerEntity;
import org.example.repository.FreelancerRepository;
import org.example.service.FreelancerService;
import org.example.service.dto.FreelancerDto;
import org.example.service.dto.FreelancerSimpleDto;
import org.example.service.mapper.FreelancerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreelancerServiceImplTest {
    @Mock
    private static FreelancerRepository repository;

    @Mock
    private static FreelancerMapper mapper;
    private static FreelancerService service;

    @BeforeEach
    void beforeEach() {
        service = new FreelancerServiceImpl(repository, mapper);
    }

    @Test
    void saveWhenArgumentNotNullAndAbsentInDB() {
        var dto = prepareNewDtoWithNullId();
        var entityMock = mock(FreelancerEntity.class);

        when(repository.containsFreelancerByEmail(dto.getFreelancerEmail())).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(entityMock);
        when(repository.save(entityMock)).thenReturn(entityMock);
        when(mapper.toDto(entityMock)).thenReturn(dto);

        service.save(dto);

        verify(repository, times(1)).containsFreelancerByEmail(anyString());
        verify(mapper, times(1)).toEntity(any(FreelancerDto.class));
        verify(repository, times(1)).save(any());
        verify(mapper, times(1)).toDto(any(FreelancerEntity.class));
    }

    @Test
    void saveWhenArgumentNotNullAndExistInDB() {
        var dto = prepareNewDtoWithNullId();

        when(repository.containsFreelancerByEmail(dto.getFreelancerEmail())).thenReturn(true);

        var saved = service.save(dto);

        verify(repository, times(1)).containsFreelancerByEmail(anyString());
        assertNull(saved.getId());
    }

    @SuppressWarnings({"unchecked", "OptionalGetWithoutIsPresent"})
    @Test
    void findByIdWhenIdValid() {
        Long id = 1L;
        var entity = new FreelancerEntity();
        var entityOptionalMock = mock(Optional.class);

        when(repository.findById(id)).thenReturn(entityOptionalMock);
        when(entityOptionalMock.isPresent()).thenReturn(true);
        when(entityOptionalMock.get()).thenReturn(entity);

        service.findById(id);

        verify(repository, times(1)).findById(id);
        verify(entityOptionalMock, times(1)).isPresent();
        verify(entityOptionalMock, times(1)).get();
        verify(mapper, times(1)).toDto(any(FreelancerEntity.class));
    }

    @Test
    void findAll() {
        var entity1 = spy(FreelancerEntity.class);
        var entity2 = spy(FreelancerEntity.class);
        var dto1 = spy(FreelancerSimpleDto.class);
        var dto2 = spy(FreelancerSimpleDto.class);
        List<FreelancerEntity> entities = List.of(entity1, entity2);

        when(repository.findAll()).thenReturn(entities);
        when(mapper.toSimpleDto(entity1)).thenReturn(dto1);
        when(mapper.toSimpleDto(entity2)).thenReturn(dto2);

        List<FreelancerSimpleDto> all = service.findAll();

        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).toSimpleDto(any(FreelancerEntity.class));

        assertEquals(2, all.size());
    }

    @Test
    void updateWhenDtoAndIdNotNullThenTrue() {
        var dto = new FreelancerDto();
        dto.setId(1L);
        var entity = new FreelancerEntity();
        entity.setId(1L);

        when(mapper.toEntity(dto)).thenReturn(entity);

        boolean update = service.update(dto);

        verify(mapper, times(1)).toEntity(dto);
        verify(repository, times(1)).update(entity);

        assertTrue(update);
    }

    @Test
    void updateWhenDtoNotNullAndIdNullThenFalse() {
        assertFalse(service.update(spy(FreelancerDto.class)));
    }

    @Test
    void deleteWhenExistAndIdNotNullThenTrue() {
        Long id = 1L;
        when(repository.deleteById(id)).thenReturn(true);

        boolean delete = service.delete(id);

        verify(repository, times(1)).deleteById(id);
        assertTrue(delete);
    }

    // When method argument is null
    @Test
    void saveWhenDtoNullThenNull() {
        assertNull(service.save(null));
    }

    @Test
    void findByIdWhenIdNullThenNull() {
       assertNull(service.findById(null));
    }

    @Test
    void updateWhenDtoNullThenFalse() {
        assertFalse(service.update(null));
    }

    @Test
    void deleteWhenExistAndIdNullThenFalse() {
        assertFalse(service.delete(null));
    }

    private static FreelancerDto prepareNewDtoWithNullId() {
        var dto = new FreelancerDto();
        dto.setFreelancerName("test");
        dto.setFreelancerSecondName("test");
        dto.setFreelancerEmail("test");
        return dto;
    }
}