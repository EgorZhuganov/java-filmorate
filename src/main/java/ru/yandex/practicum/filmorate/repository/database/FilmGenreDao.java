package ru.yandex.practicum.filmorate.repository.database;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmGenreDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String ADD_FILMGENRE_SQL = """
            INSERT INTO film_genre (film_id, genre_id)
            VALUES (?, ?);
            """;
    private static final String DELETE_FILM_ID_FROM_FILMGENRE_SQL = """
            DELETE FROM film_genre
            WHERE film_id = ?;
            """;

    public boolean insert(Long filmId, Long genreId) {
        return jdbcTemplate.update(ADD_FILMGENRE_SQL, filmId, genreId) > 0;
    }

    public boolean deleteFilmId(Long filmId) {
        return jdbcTemplate.update(DELETE_FILM_ID_FROM_FILMGENRE_SQL, filmId) > 0;
    }
}
