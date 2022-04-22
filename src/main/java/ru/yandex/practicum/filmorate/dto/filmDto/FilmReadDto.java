package ru.yandex.practicum.filmorate.dto.filmDto;

import lombok.Value;

import java.time.Duration;
import java.time.LocalDate;

@Value
public class FilmReadDto {

    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;

}