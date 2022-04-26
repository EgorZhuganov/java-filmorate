package ru.yandex.practicum.filmorate.mapper.filmMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmCreateDto;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class FilmCreateMapper implements FilmMapper<FilmCreateDto, Film> {

    @Override
    public Film mapFrom(FilmCreateDto object) {
        return Film.builder()
                .name(object.getName())
                .description(object.getDescription())
                .releaseDate(object.getReleaseDate())
                .duration(object.getDuration())
                .build();
    }

    @Override
    public String getKey() {
        return FilmCreateMapper.class.getName();
    }
}
