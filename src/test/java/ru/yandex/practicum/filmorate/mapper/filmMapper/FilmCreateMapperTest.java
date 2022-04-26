package ru.yandex.practicum.filmorate.mapper.filmMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmCreateDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

class FilmCreateMapperTest {
    private final FilmCreateMapper mapper = new FilmCreateMapper();
    private final FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
            "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
            "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
            "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
            LocalDate.of(1971,6,21), Duration.ofMinutes(161));

    @Test
    void mapFrom() {
        Film film = mapper.mapFrom(filmCreateDto1);

        Assertions.assertEquals(filmCreateDto1.getName(), film.getName());
        Assertions.assertEquals(filmCreateDto1.getDescription(), film.getDescription());
        Assertions.assertEquals(filmCreateDto1.getReleaseDate(), film.getReleaseDate());
        Assertions.assertEquals(filmCreateDto1.getDuration(), film.getDuration());
    }
}