package org.example.repository.mapper.impl;

import org.example.model.FreelancerEntity;
import org.example.repository.mapper.FreelancerResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FreelancerResultSetMapperImpl implements FreelancerResultSetMapper {
    @Override
    public FreelancerEntity map(ResultSet resultSet) throws SQLException {
            FreelancerEntity entity = new FreelancerEntity();
            entity.setId(resultSet.getLong("id"));
            entity.setFreelancerName(resultSet.getString("freelancerName"));
            entity.setFreelancerSecondName(resultSet.getString("freelancerSecondName"));
            entity.setFreelancerEmail(resultSet.getString("freelancerEmail"));
            return entity;
    }
}
