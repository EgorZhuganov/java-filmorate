package ru.yandex.practicum.filmorate.mapper.filmMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class FilmUpdateMapper implements FilmMapper<FilmUpdateDto, Film> {

    public Film mapFrom(FilmUpdateDto fromObject, Film toObject) {
        toObject.setName(fromObject.getName());
        toObject.setDescription(fromObject.getDescription());
        toObject.setReleaseDate(fromObject.getReleaseDate());
        toObject.setDuration(fromObject.getDuration());
        return toObject;
    }

    @Override
    public String getKey() {
        return FilmUpdateMapper.class.getName();
    }
}
