package ru.yandex.practicum.filmorate.dto.filmDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.annotation.IntegrationTest;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.ConstraintViolationException;
import java.time.Duration;
import java.time.LocalDate;

import static java.time.Duration.ofMinutes;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
class FilmCreateDtoTest {

    @Autowired
    private FilmService service;

    @Test
    void test0ifAllFieldsAreCorrectedShouldCreateFilm() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 2L, of(1L));

        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        assertEquals(filmReadDto1.getName(), filmCreateDto1.getName());
        assertEquals(filmReadDto1.getDescription(), filmCreateDto1.getDescription());
        assertEquals(filmReadDto1.getReleaseDate(), filmCreateDto1.getReleaseDate());
        assertEquals(filmReadDto1.getDuration(), filmCreateDto1.getDuration());
    }

    @Test
    void test2ifNameIsNullShouldThrowViolationException() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto(null, "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 2L, of(2L));

        assertThrows(ConstraintViolationException.class, () -> service.create(filmCreateDto1));
    }

    @Test
    void test3ifNameIsBlankShouldThrowViolationException() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("    ", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 2L, of(2L));

        assertThrows(ConstraintViolationException.class, () -> service.create(filmCreateDto1));
    }

    @Test
    void test4ifDescriptionLengthMoreThen200ShouldThrowViolationException() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ******** Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, ******* от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 2L, of(2L, 3L));

        assertThrows(ConstraintViolationException.class, () -> service.create(filmCreateDto1));
    }

    @Test
    void test5ifReleaseDateBeforeDateOfReleaseFirstFilmThrowViolationException() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1895, 12, 27), Duration.ofMinutes(161), 3L, of(3L, 1L));

        assertThrows(ConstraintViolationException.class, () -> service.create(filmCreateDto1));
    }

    @Test
    void test6ifReleaseDateBeforeDateOfReleaseFirstFilmShouldUsePatterForReturnMessage() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1895, 12, 28), Duration.ofMinutes(161), 1L, of(2L, 4L));

        Exception expectedEx = assertThrows(ConstraintViolationException.class, () ->
                service.create(filmCreateDto1)
        );

        assertTrue(expectedEx.getMessage().endsWith("дата должна быть позже чем \"1895-12-28\" вы ввели 1895-12-28"));
    }

    @Test
    void test7ifReleaseDateAfterDateOfReleaseFirstFilmShouldCreateFilm() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1895, 12, 29), Duration.ofMinutes(161), 1L, of(4L));

        assertDoesNotThrow(() -> service.create(filmCreateDto1));
    }

    @Test
    void test8ifDurationLessThen1SecondShouldThrowViolationException() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1971, 6, 21), Duration.ofSeconds(0), 2L, of(3L));

        assertThrows(ConstraintViolationException.class, () -> service.create(filmCreateDto1));
    }

    @Test
    void test9ifDurationMoreThen1MinuteShouldCreateFilm() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(2), 3L, of(2L));

        assertDoesNotThrow(() -> service.create(filmCreateDto1));
    }
}