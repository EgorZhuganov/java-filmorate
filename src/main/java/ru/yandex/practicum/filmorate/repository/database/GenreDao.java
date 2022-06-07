package ru.yandex.practicum.filmorate.repository.database;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class GenreDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_GENRE_BY_ID_SQL = """
            SELECT genre_id, name
            FROM genre
            WHERE genre_id = ?;
            """;
    public static final String FIND_GENRE_BY_FILM_ID_SQL = """
            SELECT genre_id, name
            FROM genre
            WHERE genre_id IN (SELECT genre_id
                               FROM film_genre
                               WHERE film_id = ?);
            """;

    public List<Genre> findGenres(Set<Long> genresIds) {
        List<Genre> genres = new ArrayList<>();
        for (Long id: genresIds) {
            findById(id).ifPresent(genres::add);
        }
        return genres;
    }

    public Optional<Genre> findById(Long id) {
        SqlRowSet genreAsRowSet = jdbcTemplate.queryForRowSet(FIND_GENRE_BY_ID_SQL, id);
        Genre genre = null;
        if (genreAsRowSet.next()) {
            genre = getGenre(genreAsRowSet);
        }
        return ofNullable(genre);
    }

    public List<Genre> findGenresByFilmId(Long id) {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet genresAsRowSet = jdbcTemplate.queryForRowSet(FIND_GENRE_BY_FILM_ID_SQL, id);
        while (genresAsRowSet.next()) {
            genres.add(getGenre(genresAsRowSet));
        }
        return genres;
    }

    private Genre getGenre(SqlRowSet genreAsRowSet) {
        Genre genre;
        genre = Genre.builder()
                .id(genreAsRowSet.getLong("genre_id"))
                .name(genreAsRowSet.getString("name"))
                .build();
        return genre;
    }
}
