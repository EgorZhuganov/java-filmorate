package ru.yandex.practicum.filmorate.repository.idGenerator;

import org.springframework.stereotype.Service;
@Service
public interface GenerateNextIdService<T, EntityType> {

    T getNextId();

}
