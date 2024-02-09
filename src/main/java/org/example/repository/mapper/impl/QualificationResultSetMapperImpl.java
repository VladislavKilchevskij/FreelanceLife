package org.example.repository.mapper.impl;

import org.example.model.QualificationEntity;
import org.example.repository.mapper.QualificationResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QualificationResultSetMapperImpl implements QualificationResultSetMapper {
    @Override
    public QualificationEntity map(ResultSet resultSet) throws SQLException {
        QualificationEntity entity = new QualificationEntity();
        entity.setId(resultSet.getLong("id"));
        entity.setQualificationName(resultSet.getString("qualificationName"));
        return entity;
    }
}
