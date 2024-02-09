package org.example.repository;

import java.util.List;
import java.util.Optional;

public interface DefaultRepository<T, K> {
    T save(T t);

    Optional<T> findById(K id);

    List<T> findAll();

    void update(T t);

    boolean deleteById(K id);


}
