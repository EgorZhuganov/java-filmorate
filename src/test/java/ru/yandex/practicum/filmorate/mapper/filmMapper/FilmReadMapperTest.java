package ru.yandex.practicum.filmorate.mapper.filmMapper;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmReadDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmReadMapperTest {

    private final FilmReadMapper mapper = new FilmReadMapper();
    private final Film film = Film.builder()
            .id(1L)
            .name("12 стульев")
            .description("Во время революции и последовавшего за ней краткого периода военного коммунизма многие " +
                    "прятали свои ценности как можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший " +
                    "Старгородский предводитель дворянства и светский лев, а ныне — скромный делопроизводитель " +
                    "ЗАГСа, узнает от умирающей тещи...")
            .releaseDate(LocalDate.of(1971, 6, 21))
            .duration(Duration.ofMinutes(161))
            .build();

    @Test
    void mapFrom() {
        FilmReadDto filmReadDto1 = mapper.mapFrom(film);

        assertEquals(filmReadDto1.getId(), film.getId());
        assertEquals(filmReadDto1.getName(), film.getName());
        assertEquals(filmReadDto1.getDescription(), film.getDescription());
        assertEquals(filmReadDto1.getReleaseDate(), film.getReleaseDate());
        assertEquals(filmReadDto1.getDuration(), film.getDuration());
    }
}