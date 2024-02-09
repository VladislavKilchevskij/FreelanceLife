package org.example.repository;

import org.example.model.QualificationEntity;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface QualificationRelationRepository {
    Optional<QualificationEntity> findByRelationId(Long id, Connection connection);

    List<QualificationEntity> findAllByRelationId(Long relId, Connection connection);
}
