package ru.yandex.practicum.filmorate.mapper;

public interface Mapper<F, T> {

    T mapFrom(F object);

    default T mapFrom(F fromObject, T toObject){
        return toObject;
    }

}
