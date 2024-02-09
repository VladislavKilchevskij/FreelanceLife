package org.example.service.mapper;

import org.example.model.FreelancerEntity;
import org.example.model.OrderEntity;
import org.example.model.QualificationEntity;
import org.example.service.dto.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QualificationMapperTest {
    @Test
    void toEntityFromSimpleDto() {
        var simpleDto = new QualificationSimpleDto();
        simpleDto.setId(1L);
        simpleDto.setQualificationName("test");

        var entity = QualificationMapper.INSTANCE.toEntity(simpleDto);

        assertAll(
                () -> assertEquals(1L, entity.getId()),
                () -> assertEquals("test", entity.getQualificationName())
        );
    }

    @Test
    void toSimpleDtoFromEntity() {
        var entity = new QualificationEntity();
        entity.setId(1L);
        entity.setQualificationName("test");

        var simpleDto = QualificationMapper.INSTANCE.toSimpleDto(entity);

        assertAll(
                () -> assertEquals(1L, simpleDto.getId()),
                () -> assertEquals("test", simpleDto.getQualificationName())
        );
    }

    @Test
    void toEntityFromDtoWhenDtoNullAndNotNull() {
        var dto = new QualificationDto();
        dto.setId(1L);
        dto.setQualificationName("test");

        var simpleDto = new FreelancerSimpleDto();
        simpleDto.setId(1L);
        simpleDto.setFreelancerName("test23");
        simpleDto.setFreelancerSecondName("test24");
        simpleDto.setFreelancerEmail("test25");

        var simpleDto1 = new FreelancerSimpleDto();
        simpleDto1.setId(2L);
        simpleDto1.setFreelancerName("test33");
        simpleDto1.setFreelancerSecondName("test34");
        simpleDto1.setFreelancerEmail("test35");

        List<FreelancerSimpleDto> freelancers = List.of(simpleDto, simpleDto1);

        var simpleDto2 = new OrderSimpleDto();
        var orderPrice = new BigDecimal("100.00");
        var orderTerm = LocalDate.of(2024, 12, 31);
        simpleDto2.setId(1L);
        simpleDto2.setOrderTitle("test23");
        simpleDto2.setOrderDescription("test24");
        simpleDto2.setOrderPrice(orderPrice);
        simpleDto2.setOrderTerm(orderTerm);

        var simpleDto3 = new OrderSimpleDto();
        var orderPrice1 = new BigDecimal("100.00");
        var orderTerm1 = LocalDate.of(2024, 12, 31);
        simpleDto3.setId(2L);
        simpleDto3.setOrderTitle("test23");
        simpleDto3.setOrderDescription("test24");
        simpleDto3.setOrderPrice(orderPrice1);
        simpleDto3.setOrderTerm(orderTerm1);

        List<OrderSimpleDto> orders = List.of(simpleDto2, simpleDto3);

        dto.setFreelancers(freelancers);
        dto.setOrders(orders);

        var entity = QualificationMapper.INSTANCE.toEntity(dto);

        assertAll(
                () -> assertEquals(1L, entity.getId()),
                () -> assertEquals("test", entity.getQualificationName()),

                () -> assertEquals(simpleDto.getId(), entity.getFreelancers().get(0).getId()),
                () -> assertEquals(simpleDto.getFreelancerName(), entity.getFreelancers().get(0).getFreelancerName()),
                () -> assertEquals(simpleDto.getFreelancerSecondName(), entity.getFreelancers().get(0).getFreelancerSecondName()),
                () -> assertEquals(simpleDto.getFreelancerEmail(), entity.getFreelancers().get(0).getFreelancerEmail()),

                () -> assertEquals(simpleDto1.getId(), entity.getFreelancers().get(1).getId()),
                () -> assertEquals(simpleDto1.getFreelancerName(), entity.getFreelancers().get(1).getFreelancerName()),
                () -> assertEquals(simpleDto1.getFreelancerSecondName(), entity.getFreelancers().get(1).getFreelancerSecondName()),
                () -> assertEquals(simpleDto1.getFreelancerEmail(), entity.getFreelancers().get(1).getFreelancerEmail()),

                () -> assertEquals(simpleDto2.getId(), entity.getOrders().get(0).getId()),
                () -> assertEquals(simpleDto2.getOrderTitle(), entity.getOrders().get(0).getOrderTitle()),
                () -> assertEquals(simpleDto2.getOrderDescription(), entity.getOrders().get(0).getOrderDescription()),
                () -> assertEquals(simpleDto2.getOrderPrice(), entity.getOrders().get(0).getOrderPrice()),
                () -> assertEquals(simpleDto2.getOrderTerm(), entity.getOrders().get(0).getOrderTerm()),

                () -> assertEquals(simpleDto3.getId(), entity.getOrders().get(1).getId()),
                () -> assertEquals(simpleDto3.getOrderTitle(), entity.getOrders().get(1).getOrderTitle()),
                () -> assertEquals(simpleDto3.getOrderDescription(), entity.getOrders().get(1).getOrderDescription()),
                () -> assertEquals(simpleDto3.getOrderPrice(), entity.getOrders().get(1).getOrderPrice()),
                () -> assertEquals(simpleDto3.getOrderTerm(), entity.getOrders().get(1).getOrderTerm())
        );
    }

    @Test
    void toDtoFromEntity() {
        var entity = new QualificationEntity();
        entity.setId(1L);
        entity.setQualificationName("test");

        var entityRel = new FreelancerEntity();
        entityRel.setId(1L);
        entityRel.setFreelancerName("test23");
        entityRel.setFreelancerSecondName("test24");
        entityRel.setFreelancerEmail("test25");

        var entityRel1 = new FreelancerEntity();
        entityRel1.setId(2L);
        entityRel1.setFreelancerName("test33");
        entityRel1.setFreelancerSecondName("test34");
        entityRel1.setFreelancerEmail("test35");

        List<FreelancerEntity> freelancers = List.of(entityRel, entityRel1);

        var entityRel2 = new OrderEntity();
        var orderPrice = new BigDecimal("100.00");
        var orderTerm = LocalDate.of(2024, 12, 31);
        entityRel2.setId(1L);
        entityRel2.setOrderTitle("test23");
        entityRel2.setOrderDescription("test24");
        entityRel2.setOrderPrice(orderPrice);
        entityRel2.setOrderTerm(orderTerm);

        var entityRel3 = new OrderEntity();
        var orderPrice1 = new BigDecimal("100.00");
        var orderTerm1 = LocalDate.of(2024, 12, 31);
        entityRel3.setId(2L);
        entityRel3.setOrderTitle("test23");
        entityRel3.setOrderDescription("test24");
        entityRel3.setOrderPrice(orderPrice1);
        entityRel3.setOrderTerm(orderTerm1);

        List<OrderEntity> orders = List.of(entityRel2, entityRel3);

        entity.setFreelancers(freelancers);
        entity.setOrders(orders);

        var dto = QualificationMapper.INSTANCE.toDto(entity);

        assertAll(
                () -> assertEquals(1L, dto.getId()),
                () -> assertEquals("test", dto.getQualificationName()),

                () -> assertEquals(entityRel.getId(), dto.getFreelancers().get(0).getId()),
                () -> assertEquals(entityRel.getFreelancerName(), dto.getFreelancers().get(0).getFreelancerName()),
                () -> assertEquals(entityRel.getFreelancerSecondName(), dto.getFreelancers().get(0).getFreelancerSecondName()),
                () -> assertEquals(entityRel.getFreelancerEmail(), dto.getFreelancers().get(0).getFreelancerEmail()),

                () -> assertEquals(entityRel1.getId(), dto.getFreelancers().get(1).getId()),
                () -> assertEquals(entityRel1.getFreelancerName(), dto.getFreelancers().get(1).getFreelancerName()),
                () -> assertEquals(entityRel1.getFreelancerSecondName(), dto.getFreelancers().get(1).getFreelancerSecondName()),
                () -> assertEquals(entityRel1.getFreelancerEmail(), dto.getFreelancers().get(1).getFreelancerEmail()),

                () -> assertEquals(entityRel2.getId(), dto.getOrders().get(0).getId()),
                () -> assertEquals(entityRel2.getOrderTitle(), dto.getOrders().get(0).getOrderTitle()),
                () -> assertEquals(entityRel2.getOrderDescription(), dto.getOrders().get(0).getOrderDescription()),
                () -> assertEquals(entityRel2.getOrderPrice(), dto.getOrders().get(0).getOrderPrice()),
                () -> assertEquals(entityRel2.getOrderTerm(), dto.getOrders().get(0).getOrderTerm()),

                () -> assertEquals(entityRel3.getId(), dto.getOrders().get(1).getId()),
                () -> assertEquals(entityRel3.getOrderTitle(), dto.getOrders().get(1).getOrderTitle()),
                () -> assertEquals(entityRel3.getOrderDescription(), dto.getOrders().get(1).getOrderDescription()),
                () -> assertEquals(entityRel3.getOrderPrice(), dto.getOrders().get(1).getOrderPrice()),
                () -> assertEquals(entityRel3.getOrderTerm(), dto.getOrders().get(1).getOrderTerm())
        );
    }

    @Test
    void toDtoFromEntityWithNullArgument () {
        assertNull(QualificationMapper.INSTANCE.toDto(null));
    }

    @Test
    void toEntityFromDtoWithNullArgument () {
        assertNull(QualificationMapper.INSTANCE.toEntity((QualificationDto) null));
    }

    @Test
    void toSimpleDtoFromEntityWithNullArgument () {
        assertNull(QualificationMapper.INSTANCE.toSimpleDto(null));
    }

    @Test
    void toEntityFromSimpleDtoWithNullArgument () {
        assertNull(QualificationMapper.INSTANCE.toEntity((QualificationSimpleDto) null));
    }
}