package ru.yandex.practicum.filmorate.repository.memory;

import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserRepositoryInMemory extends BaseRepositoryInMemory<Long, User> {

    public UserRepositoryInMemory(Map<Long, User> storage) {
        super(storage);
    }

}