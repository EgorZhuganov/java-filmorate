package ru.yandex.practicum.filmorate.repository.idGenerator;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
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
