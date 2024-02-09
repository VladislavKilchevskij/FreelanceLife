package org.example.repository;

import org.example.model.FreelancerEntity;

public interface FreelancerRepository extends DefaultRepository<FreelancerEntity, Long> {
    boolean containsFreelancerByEmail(String email);

}
