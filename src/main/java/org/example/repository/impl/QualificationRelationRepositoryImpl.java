package org.example.repository.impl;

import org.example.exception.RepositoryException;
import org.example.model.QualificationEntity;
import org.example.repository.QualificationRelationRepository;
import org.example.repository.mapper.QualificationResultSetMapper;
import org.example.repository.mapper.impl.QualificationResultSetMapperImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QualificationRelationRepositoryImpl implements QualificationRelationRepository {
    private final QualificationResultSetMapper resultSetMapper;

    private static final String FIND_WITH_FOREIGN_KEY = "SELECT * FROM qualification WHERE id=?";
    private static final String FIND_ALL_WITH_FOREIGN_KEY = "SELECT id, qualificationName FROM qualification " +
                                                            "RIGHT JOIN (SELECT * FROM freelancer_qualification WHERE freelancerId=?) AS fq " +
                                                            "ON id = fq.qualificationId";

    public QualificationRelationRepositoryImpl() {
        this.resultSetMapper = new QualificationResultSetMapperImpl();
    }

    public QualificationRelationRepositoryImpl(QualificationResultSetMapper resultSetMapper) {
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public Optional<QualificationEntity> findByRelationId(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_WITH_FOREIGN_KEY)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            QualificationEntity qualification = null;
            if (resultSet.next()) {
                qualification = resultSetMapper.map(resultSet);
            }
            return Optional.ofNullable(qualification);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public List<QualificationEntity> findAllByRelationId(Long relId, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_WITH_FOREIGN_KEY)) {
            preparedStatement.setLong(1, relId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<QualificationEntity> qualifications = new ArrayList<>();
            while (resultSet.next()) {
                qualifications.add(resultSetMapper.map(resultSet));
            }
            return qualifications;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}