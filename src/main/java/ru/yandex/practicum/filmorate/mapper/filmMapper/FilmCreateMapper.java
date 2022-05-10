package ru.yandex.practicum.filmorate.mapper.filmMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmCreateDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;

@Component
public class FilmCreateMapper implements FilmMapper<FilmCreateDto, Film> {

    public Film mapFrom(FilmCreateDto object) {
        return Film.builder()
                .name(object.getName())
                .description(object.getDescription())
                .releaseDate(object.getReleaseDate())
                .duration(object.getDuration())
                .likes(new HashSet<>())
                .build();
    }

    @Override
    public String getKey() {
        return FilmCreateMapper.class.getName();
    }
}
