package ru.yandex.practicum.filmorate.mapper.filmMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class FilmUpdateMapper implements FilmMapper<FilmUpdateDto, Film> {

    @Override
    public Film mapFrom(FilmUpdateDto object) {
        return Film.builder()
                .id(object.getId())
                .name(object.getName())
                .description(object.getDescription())
                .duration(object.getDuration())
                .releaseDate(object.getReleaseDate())
                .build();
    }

    @Override
    public Film mapFrom(FilmUpdateDto fromObject, Film toObject) {
        return mapFrom(fromObject);
    }

    @Override
    public String getKey() {
        return FilmUpdateMapper.class.getName();
    }
}
