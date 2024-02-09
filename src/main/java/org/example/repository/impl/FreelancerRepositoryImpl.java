package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.impl.ConnectionManagerImpl;
import org.example.exception.RepositoryException;
import org.example.model.FreelancerEntity;
import org.example.model.QualificationEntity;
import org.example.repository.FreelancerRepository;
import org.example.repository.QualificationRelationRepository;
import org.example.repository.mapper.FreelancerResultSetMapper;
import org.example.repository.mapper.impl.FreelancerResultSetMapperImpl;

import java.sql.*;
import java.util.*;

public class FreelancerRepositoryImpl implements FreelancerRepository {
    private final ConnectionManager manager;
    private final FreelancerResultSetMapper resultSetMapper;
    private final QualificationRelationRepository relationRepository;

    private enum FreelancerSQL {
        FIND_ALL("SELECT * FROM freelancer"),
        FIND_BY_ID("SELECT * FROM freelancer WHERE id=?"),
        FIND_BY_EMAIL("SELECT * FROM freelancer WHERE freelancerEmail=?"),
        SAVE("INSERT INTO freelancer (freelancerName, freelancerSecondName, freelancerEmail) VALUE (?, ?, ?)"),
        UPDATE("UPDATE freelancer SET freelancerName=?, freelancerSecondName=?, freelancerEmail=? WHERE id=?"),
        DELETE("DELETE FROM freelancer WHERE id=?"),
        SAVE_RELATION("INSERT INTO freelancer_qualification (freelancerId, qualificationId) VALUE (?, ?)"),
        DELETE_RELATIONS("DELETE FROM freelancer_qualification WHERE freelancerId=?");

        private final String value;

        FreelancerSQL(String value) {
            this.value = value;
        }

        private String getValue() {
            return value;
        }
    }

    public FreelancerRepositoryImpl() {
        this.manager = ConnectionManagerImpl.getInstance();
        this.resultSetMapper = new FreelancerResultSetMapperImpl();
        this.relationRepository = new QualificationRelationRepositoryImpl();
    }

    public FreelancerRepositoryImpl(ConnectionManager manager, FreelancerResultSetMapper mapper,
                                    QualificationRelationRepository relationRepository) {
        this.manager = manager;
        this.resultSetMapper = mapper;
        this.relationRepository = relationRepository;
    }

    @Override
    public Optional<FreelancerEntity> findById(Long id) {
        Connection connection = null;
        try {
            connection = manager.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(FreelancerSQL.FIND_BY_ID.getValue())) {
                preparedStatement.setObject(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                FreelancerEntity entity = null;
                if (resultSet.next()) {
                    entity = resultSetMapper.map(resultSet);
                    entity.setQualifications(relationRepository.findAllByRelationId(id, connection));
                }
                connection.commit();
                return Optional.ofNullable(entity);
            }
        } catch (SQLException e) {
            RepositoryUtil.connectionRollback(connection);
            throw new RepositoryException(e);
        } finally {
            RepositoryUtil.connectionClose(connection);
        }
    }

    @Override
    public List<FreelancerEntity> findAll() {
        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FreelancerSQL.FIND_ALL.getValue())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<FreelancerEntity> producers = new ArrayList<>();
            while (resultSet.next()) {
                producers.add(resultSetMapper.map(resultSet));
            }
            return producers;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean containsFreelancerByEmail(String email) {
        try (var connection = manager.getConnection();
             var preparedStatement = connection.prepareStatement(FreelancerSQL.FIND_BY_EMAIL.getValue())) {
            preparedStatement.setString(1, email);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public FreelancerEntity save(FreelancerEntity entity) {
        Connection connection = null;
        try {
            connection = manager.getConnection();
            connection.setAutoCommit(false);
            try (var preparedStatement = connection.prepareStatement(FreelancerSQL.SAVE.getValue(), Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, entity.getFreelancerName());
                preparedStatement.setString(2, entity.getFreelancerSecondName());
                preparedStatement.setString(3, entity.getFreelancerEmail());
                var relations = entity.getQualifications();
                preparedStatement.executeUpdate();
                var keys = preparedStatement.getGeneratedKeys();
                if (keys.next()) {
                    var entityId = keys.getObject(1, Long.class);
                    entity.setId(entityId);
                    if (relations != null && !relations.isEmpty()) saveRelations(entityId, relations, connection);
                }
            }
            connection.commit();
            return entity;
        } catch (SQLException e) {
            RepositoryUtil.connectionRollback(connection);
            throw new RepositoryException(e);
        } finally {
            RepositoryUtil.connectionClose(connection);
        }
    }

    @Override
    public void update(FreelancerEntity entity) {
        Connection connection = null;
        try {
            connection = manager.getConnection();
            connection.setAutoCommit(false);
            try (var preparedStatement = connection.prepareStatement(FreelancerSQL.UPDATE.getValue())) {
                preparedStatement.setString(1, entity.getFreelancerName());
                preparedStatement.setString(2, entity.getFreelancerSecondName());
                preparedStatement.setString(3, entity.getFreelancerEmail());
                preparedStatement.setLong(4, entity.getId());
                var relations = entity.getQualifications();
                preparedStatement.executeUpdate();
                deleteRelations(entity.getId(), connection);
                if (relations != null && !relations.isEmpty()) saveRelations(entity.getId(), relations, connection);
            }
            connection.commit();
        } catch (SQLException e) {
            RepositoryUtil.connectionRollback(connection);
            throw new RepositoryException(e);
        } finally {
            RepositoryUtil.connectionClose(connection);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (var connection = manager.getConnection();
             var preparedStatement = connection.prepareStatement(FreelancerSQL.DELETE.getValue())) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private void saveRelations(Long entityId, List<QualificationEntity> relations, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FreelancerSQL.SAVE_RELATION.getValue())) {
            for (QualificationEntity relation : relations) {
                preparedStatement.setLong(1, entityId);
                preparedStatement.setLong(2, relation.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private void deleteRelations(Long entityId, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FreelancerSQL.DELETE_RELATIONS.getValue())) {
            preparedStatement.setLong(1, entityId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}