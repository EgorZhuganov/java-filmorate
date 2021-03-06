package ru.yandex.practicum.filmorate.mapper.filmMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmCreateDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.database.GenreDao;
import ru.yandex.practicum.filmorate.repository.database.MpaRatingDao;

import java.util.ArrayList;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class FilmCreateMapper implements FilmMapper<FilmCreateDto, Film> {

    private final MpaRatingDao mpaRatingDao;
    private final GenreDao genreDao;

    public Film mapFrom(FilmCreateDto object) {
        Film film = Film.builder()
                .name(object.getName())
                .description(object.getDescription())
                .releaseDate(object.getReleaseDate())
                .duration(object.getDuration())
                .likes(new HashSet<>())
                .mpaRating(mpaRatingDao.findById(object.getMpaId()))
                .genres(new ArrayList<>())
                .build();

        if (object.getGenresIds() != null && !object.getGenresIds().isEmpty()) {
            film.setGenres(genreDao.findGenres(new HashSet<>(object.getGenresIds())));
        }

        return film;
    }

    @Override
    public String getKey() {
        return FilmCreateMapper.class.getName();
    }
}
