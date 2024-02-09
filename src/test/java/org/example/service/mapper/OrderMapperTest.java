package org.example.service.mapper;

import org.example.model.OrderEntity;
import org.example.model.QualificationEntity;
import org.example.service.dto.OrderDto;
import org.example.service.dto.OrderSimpleDto;
import org.example.service.dto.QualificationSimpleDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {
    @Test
    void toEntityFromSimpleDto() {
        var simpleDto = new OrderSimpleDto();
        var orderPrice = new BigDecimal("100.00");
        var orderTerm = LocalDate.of(2024, 12, 31);
        simpleDto.setId(1L);
        simpleDto.setOrderTitle("test");
        simpleDto.setOrderDescription("test1");
        simpleDto.setOrderPrice(orderPrice);
        simpleDto.setOrderTerm(orderTerm);

        var entity = OrderMapper.INSTANCE.toEntity(simpleDto);
        var nullEntity = OrderMapper.INSTANCE.toEntity((OrderSimpleDto) null);

        assertAll(
                () -> assertNull(nullEntity),
                () -> assertEquals(1L, entity.getId()),
                () -> assertEquals("test", entity.getOrderTitle()),
                () -> assertEquals("test1", entity.getOrderDescription()),
                () -> assertEquals(orderPrice, entity.getOrderPrice()),
                () -> assertEquals(orderTerm, entity.getOrderTerm())
        );
    }

    @Test
    void toSimpleDtoFromEntity() {
        var entity = new OrderEntity();
        var orderPrice = new BigDecimal("100.00");
        var orderTerm = LocalDate.of(2024, 12, 31);
        entity.setId(1L);
        entity.setOrderTitle("test");
        entity.setOrderDescription("test1");
        entity.setOrderPrice(orderPrice);
        entity.setOrderTerm(orderTerm);

        var dro = OrderMapper.INSTANCE.toSimpleDto(entity);
        var nullDto = OrderMapper.INSTANCE.toSimpleDto(null);

        assertAll(
                () -> assertNull(nullDto),
                () -> assertEquals(1L, dro.getId()),
                () -> assertEquals("test", dro.getOrderTitle()),
                () -> assertEquals("test1", dro.getOrderDescription()),
                () -> assertEquals(orderPrice, dro.getOrderPrice()),
                () -> assertEquals(orderTerm, dro.getOrderTerm())
        );
    }

    @Test
    void toEntityFromDtoWhenDtoNullAndNotNull() {
        var dto = new OrderDto();
        var orderPrice = new BigDecimal("100.00");
        var orderTerm = LocalDate.of(2024, 12, 31);
        dto.setId(1L);
        dto.setOrderTitle("test");
        dto.setOrderDescription("test1");
        dto.setOrderPrice(orderPrice);
        dto.setOrderTerm(orderTerm);
        var simpleDto = new QualificationSimpleDto();
        simpleDto.setId(1L);
        simpleDto.setQualificationName("test22");
        dto.setQualification(simpleDto);

        var entity = OrderMapper.INSTANCE.toEntity(dto);
        var nullEntity = OrderMapper.INSTANCE.toEntity((OrderDto) null);

        assertAll(
                () -> assertNull(nullEntity),
                () -> assertEquals(1L, entity.getId()),
                () -> assertEquals("test", entity.getOrderTitle()),
                () -> assertEquals("test1", entity.getOrderDescription()),
                () -> assertEquals(orderPrice, entity.getOrderPrice()),
                () -> assertEquals(orderTerm, entity.getOrderTerm()),
                () -> assertAll(
                        () -> assertEquals(simpleDto.getId(), entity.getQualification().getId()),
                        () -> assertEquals(simpleDto.getQualificationName(), entity.getQualification().getQualificationName())
                )
        );
    }

    @Test
    void toDtoFromEntity() {
        var entity = new OrderEntity();
        var orderPrice = new BigDecimal("100.00");
        var orderTerm = LocalDate.of(2024, 12, 31);
        entity.setId(1L);
        entity.setOrderTitle("test");
        entity.setOrderDescription("test1");
        entity.setOrderPrice(orderPrice);
        entity.setOrderTerm(orderTerm);
        var qualif1 = new QualificationEntity();
        qualif1.setId(1L);
        qualif1.setQualificationName("test22");
        entity.setQualification(qualif1);

        var dto = OrderMapper.INSTANCE.toDto(entity);
        var nullDto = OrderMapper.INSTANCE.toDto(null);

        assertAll(
                () -> assertNull(nullDto),
                () -> assertEquals(1L, dto.getId()),
                () -> assertEquals("test", dto.getOrderTitle()),
                () -> assertEquals("test1", dto.getOrderDescription()),
                () -> assertEquals(orderPrice, dto.getOrderPrice()),
                () -> assertEquals(orderTerm, dto.getOrderTerm()),
                () -> assertAll(
                        () -> assertEquals(qualif1.getId(), dto.getQualification().getId()),
                        () -> assertEquals(qualif1.getQualificationName(), dto.getQualification().getQualificationName())
                )
        );
    }
}