package ru.yandex.practicum.filmorate.repository.database;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;

import java.util.List;

public interface FilmRepository extends AbstractRepository<Long, Film> {

    List<Film> findPopularFilmsByLikes(int count);

    boolean insertLike(Long userId, Long filmId);

    boolean deleteLike(Long userId, Long filmId);

}
