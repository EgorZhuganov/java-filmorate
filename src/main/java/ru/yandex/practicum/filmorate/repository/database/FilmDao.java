package ru.yandex.practicum.filmorate.repository.database;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

import static java.time.Duration.ofSeconds;
import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class FilmDao implements FilmRepository {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRatingDao mpaRatingDao;

    private static final String DELETE_SQL = """
            DELETE FROM film
            WHERE  film_id = ?
            """;
    private static final String INSERT_SQL = """
            INSERT INTO film (name, description, release_date, duration, mpa_id)
            VALUES (?,?,?,?,?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE film
            SET name = ?,
                description = ?,
                release_date = ?,
                duration = ?,
                mpa_id = ?
            WHERE film_id = ?;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT film_id, name, description, release_date, duration, mpa_id
            FROM film
            WHERE film_id = ?;
            """;
    private static final String FIND_ALL_LIKES_SQL = """
            SELECT user_id
            FROM users
            WHERE user_id IN (SELECT user_id
                              FROM likes
                              WHERE film_id = ?);
            """;
    private static final String FIND_ALL_FILMS_SQL = """
            SELECT film_id, name, description, release_date, duration, mpa_id
            FROM film;
            """;
    private static final String INSERT_LIKE_INTO_FILM_SQL = """
            INSERT INTO likes (film_id, user_id)
            VALUES (?, ?);
            """;
    public static final String DELETE_LIKE_FROM_FILM_SQL = """
            DELETE FROM likes
            WHERE film_id = ? and user_id = ?;
            """;
    public static final String FIND_N_POPULAR_FILMS_BY_LIKES = """
            SELECT f.film_id, name, description, release_date, duration, mpa_id
            FROM film f
                     LEFT JOIN likes l on f.film_id = l.film_id
            GROUP BY f.film_id
            ORDER BY count(l.user_id) desc, f.film_id desc
            LIMIT ?;
            """;

    @Override
    public Film insert(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_SQL, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setObject(3, LocalDate.of(film.getReleaseDate().getYear(), film.getReleaseDate().getMonth(), film.getReleaseDate().getDayOfMonth()));
            stmt.setObject(4, film.getDuration());
            stmt.setLong(5, film.getMpaRating().getId());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public Optional<Film> findById(Long id) {
        return of(jdbcTemplate.queryForRowSet(FIND_BY_ID_SQL, id))
                .filter(SqlRowSet::next)
                .map(this::buildFilm);
    }

    @Override
    public List<Film> findAll() {
//        SqlRowSet filmAsRowSet = jdbcTemplate.queryForRowSet(FIND_ALL_FILMS_SQL);
//        List<Film> films = new ArrayList<>();
//        while (filmAsRowSet.next()) {
//            Film film = buildFilm(filmAsRowSet);
//            films.add(film);
//        }
//        return films;
        return of(jdbcTemplate.queryForRowSet(FIND_ALL_FILMS_SQL))
                .filter(SqlRowSet::next)
                .map(this::buildFilm)
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> findPopularFilmsByLikes(int count) {
//        SqlRowSet filmAsRowSet = jdbcTemplate.queryForRowSet(FIND_N_POPULAR_FILMS_BY_LIKES, count);
//        List<Film> films = new ArrayList<>();
//        while (filmAsRowSet.next()) {
//            Film film = buildFilm(filmAsRowSet);
//            films.add(film);
//        }
//        return films;
        return of(jdbcTemplate.queryForRowSet(FIND_N_POPULAR_FILMS_BY_LIKES, count))
                .filter(SqlRowSet::next)
                .map(this::buildFilm)
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(
                UPDATE_SQL,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating().getId(),
                film.getId()
        );

        return film;
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(DELETE_SQL, id) > 0;
    }

    @Override
    public boolean insertLike(Long filmId, Long userId) {
        return jdbcTemplate.update(INSERT_LIKE_INTO_FILM_SQL, filmId, userId) > 0;
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        return jdbcTemplate.update(DELETE_LIKE_FROM_FILM_SQL, filmId, userId) > 0;
    }

    private Film buildFilm(SqlRowSet filmAsRowSet) {
        Film film = Film.builder()
                .id(filmAsRowSet.getLong("film_id"))
                .name(filmAsRowSet.getString("name"))
                .description(filmAsRowSet.getString("description"))
                .mpaRating(mpaRatingDao.findById(filmAsRowSet.getLong("mpa_id")))
                .releaseDate(filmAsRowSet.getDate("release_date").toLocalDate())
                .duration(ofSeconds(filmAsRowSet.getLong("duration")))
                .build();

        SqlRowSet likesAsRowSet = jdbcTemplate.queryForRowSet(FIND_ALL_LIKES_SQL, film.getId());
        Set<Long> likes = new HashSet<>();
        while (likesAsRowSet.next()) {
            Long likeId = likesAsRowSet.getLong("user_id");
            likes.add(likeId);
        }
        film.setLikes(likes);
        return film;
    }
}