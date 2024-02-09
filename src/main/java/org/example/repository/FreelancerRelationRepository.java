package org.example.repository;

import org.example.model.FreelancerEntity;

import java.sql.Connection;
import java.util.List;

public interface FreelancerRelationRepository {

    List<FreelancerEntity> findAllByRelationId(Long relId, Connection connection);
}
