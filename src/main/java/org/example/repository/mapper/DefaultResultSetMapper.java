package org.example.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DefaultResultSetMapper<E> {
    E map(ResultSet resultSet) throws SQLException;
}
