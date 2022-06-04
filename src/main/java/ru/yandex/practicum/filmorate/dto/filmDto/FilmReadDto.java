package ru.yandex.practicum.filmorate.dto.filmDto;

import lombok.Value;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Value
public class FilmReadDto {

    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;
    MpaRating mpa;
    Set<Long> likes;

}
