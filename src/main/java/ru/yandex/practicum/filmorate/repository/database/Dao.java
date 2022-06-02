package ru.yandex.practicum.filmorate.repository.database;

import java.util.Optional;

public interface Dao<K, T> {

    T insert(T t);

    boolean delete(K id);

    T update(T t);

    Optional<T> findById(K id);

}
