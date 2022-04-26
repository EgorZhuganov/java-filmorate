package ru.yandex.practicum.filmorate.repository.memory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;
import ru.yandex.practicum.filmorate.repository.idGenerator.GenerateNextIdService;

import java.io.Serializable;
import java.util.*;

@Repository
public abstract class BaseRepositoryInMemory<K extends Serializable, E extends BaseModel<K>> implements AbstractRepository<K, E> {

    private final Map<K, E> storage;
    private final GenerateNextIdService<K> nextIdService;

    public BaseRepositoryInMemory(Map<K, E> storage, GenerateNextIdService<K> idService) {
        this.storage = storage;
        this.nextIdService = idService;
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
    public E insert(E entity) {
        entity.setId(nextIdService.getNextId());
        storage.put(entity.getId(), entity);
        return entity;
    }
}