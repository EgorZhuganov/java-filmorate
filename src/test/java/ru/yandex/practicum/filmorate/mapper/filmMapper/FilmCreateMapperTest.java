package ru.yandex.practicum.filmorate.mapper.filmMapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.annotation.IntegrationTest;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmCreateDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmCreateMapperTest {

    @Autowired
    private final FilmCreateMapper mapper;
    private final FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
            "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
            "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
            "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
            LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L, of(1L, 2L));

    @Test
    void mapFrom() {
        Film film = mapper.mapFrom(filmCreateDto1);

        assertEquals(filmCreateDto1.getName(), film.getName());
        assertEquals(filmCreateDto1.getReleaseDate(), film.getReleaseDate());
        assertEquals(filmCreateDto1.getDuration(), film.getDuration());
    }

    @Test
    void test1mapFromIfIdsGenresNotExistShouldMapWithoutNotExistingGenres() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L, of(100500L, 150L));
        Film film = mapper.mapFrom(filmCreateDto1);

        assertThat(film.getGenres()).hasSize(0);
    }

    @Test
    void test2mapFromIf2IdsGenresExistShouldMapWithExistingGenres() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L, of(1L, 2L));
        Film film = mapper.mapFrom(filmCreateDto1);

        assertThat(film.getGenres()).hasSize(2);
    }

    @Test
    void test3mapFromIf1From2GenreIdExistShouldMapWithExistingGenres() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L, of(100500L, 2L));
        Film film = mapper.mapFrom(filmCreateDto1);

        assertThat(film.getGenres()).hasSize(1);
    }
}