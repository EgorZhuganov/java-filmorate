package ru.yandex.practicum.filmorate.dto.filmDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.ConstraintViolationException;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmCreateDtoTest {

    @Autowired
    private FilmService service;

    @Test
    void test0ifAllFieldsAreCorrectedShouldCreateFilm() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

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
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

        assertThrows(ConstraintViolationException.class, () -> service.create(filmCreateDto1));
    }

    @Test
    void test3ifNameIsBlankShouldThrowViolationException() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("    ", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

        assertThrows(ConstraintViolationException.class, () -> service.create(filmCreateDto1));
    }

    @Test
    void test4ifDescriptionLengthMoreThen200ShouldThrowViolationException() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ******** Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, ******* от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

        assertThrows(ConstraintViolationException.class, () -> service.create(filmCreateDto1));
    }

    @Test
    void test5ifReleaseDateBeforeDateOfReleaseFirstFilmThrowViolationException() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1895, 12, 27), Duration.ofMinutes(161));

        assertThrows(ConstraintViolationException.class, () -> service.create(filmCreateDto1));
    }

    @Test
    void test6ifReleaseDateBeforeDateOfReleaseFirstFilmShouldUsePatterForReturnMessage() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1895, 12, 28), Duration.ofMinutes(161));

        Exception expectedEx = assertThrows(ConstraintViolationException.class, () ->
                service.create(filmCreateDto1)
        );

        assertTrue(expectedEx.getMessage().endsWith("дата должна быть позже чем \"1895-12-28\""));
    }

    @Test
    void test7ifReleaseDateAfterDateOfReleaseFirstFilmShouldCreateFilm() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1895, 12, 29), Duration.ofMinutes(161));

        assertDoesNotThrow(() -> service.create(filmCreateDto1));
    }

    @Test
    void test8ifDurationLessThen1SecondShouldThrowViolationException() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1971, 6, 21), Duration.ofSeconds(0));

        assertThrows(ConstraintViolationException.class, () -> service.create(filmCreateDto1));
    }

    @Test
    void test9ifDurationMoreThen1SecondShouldCreateFilm() {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 стульев", "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(1971, 6, 21), Duration.ofSeconds(2));

        assertDoesNotThrow(() -> service.create(filmCreateDto1));
    }
}