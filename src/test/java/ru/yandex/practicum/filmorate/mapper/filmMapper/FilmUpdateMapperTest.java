package ru.yandex.practicum.filmorate.mapper.filmMapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.annotation.IntegrationTest;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmUpdateMapperTest {

    @Autowired
    private final FilmUpdateMapper mapper;
    private final FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(1L, "12 стульев", "Во время " +
            "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
            "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
            "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
            LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L);
    private Film film = Film.builder().build();
    @Test
    void mapFrom() {
        film = mapper.mapFrom(filmUpdateDto1, film);

        assertEquals(filmUpdateDto1.getName(), film.getName());
        assertEquals(filmUpdateDto1.getDescription(), film.getDescription());
        assertEquals(filmUpdateDto1.getReleaseDate(), film.getReleaseDate());
        assertEquals(filmUpdateDto1.getDuration(), film.getDuration());
    }
}