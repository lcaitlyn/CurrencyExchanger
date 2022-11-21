package edu.lcaitlyn.CurrencyExchanger.repositories;

import java.util.List;

public interface CrudRepository<T> {
    T findById(Long id);
    T findByName(String name);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(Long id);
    void delete(String name);
}