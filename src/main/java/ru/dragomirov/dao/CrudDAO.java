package ru.dragomirov.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T, ID> {
    void save(T entity);
    Optional<T> findById(ID id);
    List<T> findByAll();
    Optional<T> update(T entity);
    void delete(ID id);
}
