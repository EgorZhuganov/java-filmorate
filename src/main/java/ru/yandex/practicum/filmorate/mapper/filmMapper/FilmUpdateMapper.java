package ru.yandex.practicum.filmorate.mapper.filmMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.database.MpaRatingDao;

@Component
@RequiredArgsConstructor
public class FilmUpdateMapper implements FilmMapper<FilmUpdateDto, Film> {

    private final MpaRatingDao mpaRatingDao;

    public Film mapFrom(FilmUpdateDto fromObject, Film toObject) {
        toObject.setName(fromObject.getName());
        toObject.setDescription(fromObject.getDescription());
        toObject.setReleaseDate(fromObject.getReleaseDate());
        toObject.setDuration(fromObject.getDuration());
        toObject.setMpaRating(mpaRatingDao.findById(fromObject.getMpaId()));
        return toObject;
    }

    @Override
    public String getKey() {
        return FilmUpdateMapper.class.getName();
    }
}
