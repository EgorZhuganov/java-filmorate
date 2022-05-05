package ru.yandex.practicum.filmorate.mapper.filmMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmReadDto;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class FilmReadMapper implements FilmMapper<Film, FilmReadDto> {

    public FilmReadDto mapFrom(Film object) {
        return new FilmReadDto(
                object.getId(),
                object.getName(),
                object.getDescription(),
                object.getReleaseDate(),
                object.getDuration(),
                object.getLikes()
        );
    }

    @Override
    public String getKey() {
        return FilmReadMapper.class.getName();
    }
}
