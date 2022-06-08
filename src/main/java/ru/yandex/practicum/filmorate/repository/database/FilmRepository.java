package ru.yandex.practicum.filmorate.repository.database;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;

import java.util.List;

public interface FilmRepository extends AbstractRepository<Long, Film> {

    List<Film> findPopularFilmsByLikes(int count);

    boolean insertLike(Long filmId, Long userId);

    boolean deleteLike(Long filmId, Long userId);

    boolean findLike(Long filmId, Long userId);

    List<Film> findCommonFilmsBetweenTwoUsers(Long userId, Long friendId);
}
