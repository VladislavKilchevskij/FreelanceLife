package org.example.service.impl;

import org.example.model.OrderEntity;
import org.example.repository.OrderRepository;
import org.example.service.OrderService;
import org.example.service.dto.OrderDto;
import org.example.service.dto.OrderSimpleDto;
import org.example.service.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private static OrderRepository repository;

    @Mock
    private static OrderMapper mapper;
    private static OrderService service;

    @BeforeEach
    void beforeEach() {
        service = new OrderServiceImpl(repository, mapper);
    }

    @Test
    void saveWhenArgumentNotNull() {
        var dto = spy(OrderDto.class);
        var entityMock = spy(OrderEntity.class);

        when(mapper.toEntity(dto)).thenReturn(entityMock);
        when(repository.save(entityMock)).thenReturn(entityMock);
        when(mapper.toDto(entityMock)).thenReturn(dto);

        service.save(dto);

        verify(mapper, times(1)).toEntity(any(OrderDto.class));
        verify(repository, times(1)).save(any());
        verify(mapper, times(1)).toDto(any(OrderEntity.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void findByIdWhenIdValid() {
        Long id = 1L;
        var entity = new OrderEntity();
        var entityOptionalMock = mock(Optional.class);

        when(repository.findById(id)).thenReturn(entityOptionalMock);
        when(entityOptionalMock.isPresent()).thenReturn(true);
        when(entityOptionalMock.get()).thenReturn(entity);

        service.findById(id);

        verify(repository, times(1)).findById(id);
        verify(entityOptionalMock, times(1)).isPresent();
        verify(entityOptionalMock, times(1)).get();
        verify(mapper, times(1)).toDto(any(OrderEntity.class));
    }

    @Test
    void findAll() {
        var entity1 = spy(OrderEntity.class);
        var entity2 = spy(OrderEntity.class);
        var dto1 = spy(OrderSimpleDto.class);
        var dto2 = spy(OrderSimpleDto.class);
        List<OrderEntity> entities = List.of(entity1, entity2);

        when(repository.findAll()).thenReturn(entities);
        when(mapper.toSimpleDto(entity1)).thenReturn(dto1);
        when(mapper.toSimpleDto(entity2)).thenReturn(dto2);

        List<OrderSimpleDto> all = service.findAll();

        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).toSimpleDto(any(OrderEntity.class));

        assertEquals(2, all.size());
    }

    @Test
    void updateWhenDtoAndIdNotNullThenTrue() {
        var dto = new OrderDto();
        dto.setId(1L);
        var entity = new OrderEntity();
        entity.setId(1L);

        when(mapper.toEntity(dto)).thenReturn(entity);

        boolean update = service.update(dto);

        verify(mapper, times(1)).toEntity(dto);
        verify(repository, times(1)).update(entity);

        assertTrue(update);
    }

    @Test
    void updateWhenDtoNotNullAndIdNullThenFalse() {
        assertFalse(service.update(new OrderDto()));
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
}