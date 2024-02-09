package org.example.repository.impl;

import org.example.exception.RepositoryException;
import org.example.model.FreelancerEntity;
import org.example.repository.FreelancerRelationRepository;
import org.example.repository.mapper.FreelancerResultSetMapper;
import org.example.repository.mapper.impl.FreelancerResultSetMapperImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FreelancerRelationRepositoryImpl implements FreelancerRelationRepository {
    private final FreelancerResultSetMapper resultSetMapper;

    private static final String FIND_ALL_WITH_FOREIGN_KEY = "SELECT fr.id, freelancerName, freelancerSecondName, freelancerEmail " +
                                                            "FROM freelancer AS fr " +
                                                            "RIGHT JOIN (SELECT*FROM freelancer_qualification WHERE qualificationId=?) AS fq " +
                                                            "ON fr.id =fq.freelancerId";

    public FreelancerRelationRepositoryImpl() {
        this.resultSetMapper = new FreelancerResultSetMapperImpl();
    }

    @Override
    public List<FreelancerEntity> findAllByRelationId(Long relId, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_WITH_FOREIGN_KEY)) {
            preparedStatement.setLong(1, relId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<FreelancerEntity> freelancers = new ArrayList<>();
            while (resultSet.next()) {
                freelancers.add(resultSetMapper.map(resultSet));
            }
            return freelancers;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}
