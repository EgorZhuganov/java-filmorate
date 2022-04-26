package ru.yandex.practicum.filmorate.repository.idGenerator;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class AtomicLongGenerator implements GenerateNextIdService<Long>  {

    private final AtomicLong atomicLong;

    public AtomicLongGenerator () {
        atomicLong = new AtomicLong(1L);
    }

    @Override
    public Long getNextId() {
        return atomicLong.getAndIncrement();
    }
}
