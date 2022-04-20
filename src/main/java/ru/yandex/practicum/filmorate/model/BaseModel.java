package ru.yandex.practicum.filmorate.model;

import java.io.Serializable;

public interface BaseModel<T extends Serializable> {

    void setId(T id);

    T getId();

}
