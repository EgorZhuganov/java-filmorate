package ru.yandex.practicum.filmorate.repository.memory;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.repository.idGenerator.GenerateNextIdService;

import java.util.Map;

@Repository
public class UserRepositoryInMemory extends BaseRepositoryInMemory<Long, User> {

    @Autowired
    public UserRepositoryInMemory(Map<Long, User> storage, GenerateNextIdService<Long, User> idService) {
        super(storage, idService);
    }

}