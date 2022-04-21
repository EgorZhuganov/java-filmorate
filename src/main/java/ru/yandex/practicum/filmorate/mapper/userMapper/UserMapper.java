package ru.yandex.practicum.filmorate.mapper.userMapper;

import ru.yandex.practicum.filmorate.mapper.Mapper;

public interface UserMapper<F, T> extends Mapper<F, T> {

    String getKey();

}
