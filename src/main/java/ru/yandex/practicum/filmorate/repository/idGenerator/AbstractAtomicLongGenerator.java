package ru.yandex.practicum.filmorate.repository.idGenerator;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractAtomicLongGenerator<EntityType> implements GenerateNextIdService<Long, EntityType>  {

    private final AtomicLong atomicLong;

    public AbstractAtomicLongGenerator() {
        atomicLong = new AtomicLong(1L);
    }

    @Override
    public Long getNextId() {
        return atomicLong.getAndIncrement();
    }
}
