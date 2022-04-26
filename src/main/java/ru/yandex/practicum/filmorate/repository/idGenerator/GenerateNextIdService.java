package ru.yandex.practicum.filmorate.repository.idGenerator;


public interface GenerateNextIdService<T, EntityType> {

    T getNextId();

}
