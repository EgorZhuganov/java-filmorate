package ru.yandex.practicum.filmorate.mapper.filmMapper;

import ru.yandex.practicum.filmorate.mapper.Mapper;

public interface FilmMapper<F, T> extends Mapper<F, T> {

    String getKey();

}
