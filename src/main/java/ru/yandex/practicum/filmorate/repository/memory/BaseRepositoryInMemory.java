package ru.yandex.practicum.filmorate.repository.memory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;

import java.io.Serializable;
import java.util.*;

@Repository
public abstract class BaseRepositoryInMemory<K extends Serializable, E extends BaseModel<K>> implements AbstractRepository<K, E> {

    private final Map<K, E> storage;

    public BaseRepositoryInMemory(Map<K, E> storage) {
        this.storage = storage;
    }

    @Override
    public List<E> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public boolean delete(K id) {
        return storage.remove(id) != null;
    }

    @Override
    public E update(E entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public E save(E entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }
}