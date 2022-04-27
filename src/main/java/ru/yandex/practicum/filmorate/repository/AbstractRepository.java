package ru.yandex.practicum.filmorate.repository;


import java.util.List;
import java.util.Optional;

public interface AbstractRepository<K, T> {

    List<T> findAll();

    Optional<T> findById(K id);

    boolean delete(K id);

    T update(T entity);

    T insert(T entity);
}