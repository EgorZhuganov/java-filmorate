package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbstractRepository<K, T> {

    List<T> findAll();

    Optional<T> findById(K id);

    boolean delete(K id);

    T update(T entity);

    T save (T entity);
}