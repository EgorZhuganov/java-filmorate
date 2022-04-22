package ru.yandex.practicum.filmorate.mapper.filmMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

class FilmUpdateMapperTest {
    private final FilmUpdateMapper mapper = new FilmUpdateMapper();
    private final FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(1L, "12 стульев", "Во время " +
            "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
            "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
            "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
            LocalDate.of(1971,6,21), Duration.ofMinutes(161));

    @Test
    void mapFrom() {
        Film film = mapper.mapFrom(filmUpdateDto1);

        Assertions.assertEquals(filmUpdateDto1.getId(), film.getId());
        Assertions.assertEquals(filmUpdateDto1.getName(), film.getName());
        Assertions.assertEquals(filmUpdateDto1.getDescription(), film.getDescription());
        Assertions.assertEquals(filmUpdateDto1.getReleaseDate(), film.getReleaseDate());
        Assertions.assertEquals(filmUpdateDto1.getDuration(), film.getDuration());
    }
}