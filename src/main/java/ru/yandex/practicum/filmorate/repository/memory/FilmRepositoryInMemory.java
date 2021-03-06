package ru.yandex.practicum.filmorate.repository.memory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.idGenerator.GenerateNextIdService;

import java.util.Map;

@Repository
public class FilmRepositoryInMemory extends BaseRepositoryInMemory<Long, Film> {

    @Autowired
    public FilmRepositoryInMemory(Map<Long, Film> storage, GenerateNextIdService<Long, Film> idService) {
        super(storage, idService);
    }

}