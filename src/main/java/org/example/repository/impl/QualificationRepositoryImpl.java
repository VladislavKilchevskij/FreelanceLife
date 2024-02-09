package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.impl.ConnectionManagerImpl;
import org.example.exception.RepositoryException;
import org.example.model.QualificationEntity;
import org.example.repository.*;
import org.example.repository.mapper.QualificationResultSetMapper;
import org.example.repository.mapper.impl.QualificationResultSetMapperImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QualificationRepositoryImpl implements QualificationRepository {
    private final ConnectionManager manager;
    private final QualificationResultSetMapper resultSetMapper;
    private final OrderRelationRepository orderRelationRepository;
    private final FreelancerRelationRepository freelancerRelationRepository;

    private enum QualifSQL {
        FIND_ALL("SELECT * FROM qualification"),
        FIND_BY_ID("SELECT * FROM qualification WHERE id=?"),
        SAVE("INSERT INTO qualification (qualificationName) VALUE (?)"),
        UPDATE("UPDATE qualification SET qualificationName=? WHERE id=?"),
        DELETE("DELETE FROM qualification WHERE id=?");

        private final String value;

        QualifSQL(String value) {
            this.value = value;
        }

        private String getValue() {
            return value;
        }
    }

    public QualificationRepositoryImpl() {
        this.manager = ConnectionManagerImpl.getInstance();
        this.resultSetMapper = new QualificationResultSetMapperImpl();
        this.orderRelationRepository = new OrderRelationRepositoryImpl();
        this.freelancerRelationRepository = new FreelancerRelationRepositoryImpl();
    }

    public QualificationRepositoryImpl(ConnectionManager manager, QualificationResultSetMapper mapper,
                                       OrderRelationRepository orderRelationRepository, FreelancerRelationRepository freelancerRelationRepository) {
        this.manager = manager;
        this.resultSetMapper = mapper;
        this.orderRelationRepository = orderRelationRepository;
        this.freelancerRelationRepository = freelancerRelationRepository;
    }

    @Override
    public Optional<QualificationEntity> findById(Long id) {
        Connection connection = null;
        try {
            connection = manager.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(QualifSQL.FIND_BY_ID.getValue())) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                QualificationEntity qualification = null;
                if (resultSet.next()) {
                    qualification = resultSetMapper.map(resultSet);
                    qualification.setOrders(orderRelationRepository.findAllByRelationId(id, connection));
                    qualification.setFreelancers(freelancerRelationRepository.findAllByRelationId(id, connection));
                }
                connection.commit();
                return Optional.ofNullable(qualification);
            }
        } catch (SQLException e) {
            RepositoryUtil.connectionRollback(connection);
            throw new RepositoryException(e);
        } finally {
            RepositoryUtil.connectionClose(connection);
        }
    }

    @Override
    public List<QualificationEntity> findAll() {
        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(QualifSQL.FIND_ALL.getValue())) {
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

    @Override
    public QualificationEntity save(QualificationEntity entity) {
        try (var connection = manager.getConnection();
             var preparedStatement = connection.prepareStatement(QualifSQL.SAVE.getValue(), Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getQualificationName());
            preparedStatement.executeUpdate();
            var keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                entity.setId(keys.getObject(1, Long.class));
            }
            return entity;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(QualificationEntity entity) {
        try (var connection = manager.getConnection();
             var preparedStatement = connection.prepareStatement(QualifSQL.UPDATE.getValue())) {
            preparedStatement.setString(1, entity.getQualificationName());
            preparedStatement.setLong(2, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (var connection = manager.getConnection();
             var preparedStatement = connection.prepareStatement(QualifSQL.DELETE.getValue())) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}