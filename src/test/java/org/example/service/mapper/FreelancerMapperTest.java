package org.example.service.mapper;

import org.example.model.FreelancerEntity;
import org.example.model.QualificationEntity;
import org.example.service.dto.FreelancerDto;
import org.example.service.dto.FreelancerSimpleDto;
import org.example.service.dto.QualificationSimpleDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FreelancerMapperTest {
    @Test
    void toEntityFromSimpleDto() {
        var simpleDto = new FreelancerSimpleDto();
        simpleDto.setId(1L);
        simpleDto.setFreelancerName("test");
        simpleDto.setFreelancerSecondName("test1");
        simpleDto.setFreelancerEmail("test2");

        var entity = FreelancerMapper.INSTANCE.toEntity(simpleDto);
        var nullEntity = FreelancerMapper.INSTANCE.toEntity((FreelancerSimpleDto) null);
        assertAll(
                () -> assertNull(nullEntity),
                () -> assertEquals(1L, entity.getId()),
                () -> assertEquals("test", entity.getFreelancerName()),
                () -> assertEquals("test1", entity.getFreelancerSecondName()),
                () -> assertEquals("test2", entity.getFreelancerEmail())
        );
    }

    @Test
    void toEntityFromDtoWhenDtoNullAndNotNull() {
        var dto = new FreelancerDto();
        dto.setId(1L);
        dto.setFreelancerName("test");
        dto.setFreelancerSecondName("test1");
        dto.setFreelancerEmail("test2");
        var simpleDto = new QualificationSimpleDto();
        simpleDto.setId(1L);
        simpleDto.setQualificationName("test22");
        var simpleDto1 = new QualificationSimpleDto();
        simpleDto1.setId(2L);
        simpleDto1.setQualificationName("test23");
        List<QualificationSimpleDto> list = List.of(simpleDto, simpleDto1);
        dto.setQualifications(list);

        var entity = FreelancerMapper.INSTANCE.toEntity(dto);
        var nullEntity = FreelancerMapper.INSTANCE.toEntity((FreelancerDto) null);

        assertAll(
                () -> assertNull(nullEntity),
                () -> assertEquals(1L, entity.getId()),
                () -> assertEquals("test", entity.getFreelancerName()),
                () -> assertEquals("test1", entity.getFreelancerSecondName()),
                () -> assertEquals("test2", entity.getFreelancerEmail()),
                () -> assertAll(
                        () -> assertEquals(simpleDto.getId(), entity.getQualifications().get(0).getId()),
                        () -> assertEquals(simpleDto.getQualificationName(), entity.getQualifications().get(0).getQualificationName()),
                        () -> assertEquals(simpleDto1.getId(), entity.getQualifications().get(1).getId()),
                        () -> assertEquals(simpleDto1.getQualificationName(), entity.getQualifications().get(1).getQualificationName())
                )
        );
    }

    @Test
    void toDtoFromEntity() {
        var entity = new FreelancerEntity();
        entity.setId(1L);
        entity.setFreelancerName("test");
        entity.setFreelancerSecondName("test1");
        entity.setFreelancerEmail("test2");
        var qualif1 = new QualificationEntity();
        qualif1.setId(1L);
        qualif1.setQualificationName("test22");
        var qualif2 = new QualificationEntity();
        qualif2.setId(2L);
        qualif2.setQualificationName("test23");
        List<QualificationEntity> list = List.of(qualif1, qualif2);
        entity.setQualifications(list);

        var dto = FreelancerMapper.INSTANCE.toDto(entity);
        var nullDto = FreelancerMapper.INSTANCE.toDto(null);

        assertAll(
                () -> assertNull(nullDto),
                () -> assertEquals(1L, dto.getId()),
                () -> assertEquals("test", dto.getFreelancerName()),
                () -> assertEquals("test1", dto.getFreelancerSecondName()),
                () -> assertEquals("test2", dto.getFreelancerEmail()),
                () -> assertAll(
                        () -> assertEquals(qualif1.getId(), dto.getQualifications().get(0).getId()),
                        () -> assertEquals(qualif1.getQualificationName(), dto.getQualifications().get(0).getQualificationName()),
                        () -> assertEquals(qualif2.getId(), dto.getQualifications().get(1).getId()),
                        () -> assertEquals(qualif2.getQualificationName(), dto.getQualifications().get(1).getQualificationName())
                )
        );
    }

    @Test
    void toSimpleDtoFromEntity() {
        var entity = new FreelancerEntity();
        entity.setId(1L);
        entity.setFreelancerName("test");
        entity.setFreelancerSecondName("test1");
        entity.setFreelancerEmail("test2");

        var simpleDto = FreelancerMapper.INSTANCE.toSimpleDto(entity);
        var nullDto = FreelancerMapper.INSTANCE.toSimpleDto(null);

        assertAll(
                () -> assertNull(nullDto),
                () -> assertEquals(1L, simpleDto.getId()),
                () -> assertEquals("test", simpleDto.getFreelancerName()),
                () -> assertEquals("test1", simpleDto.getFreelancerSecondName()),
                () -> assertEquals("test2", simpleDto.getFreelancerEmail())
        );
    }
}