package ru.yandex.practicum.filmorate.dto.filmDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.annotation.IntegrationTest;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.ConstraintViolationException;
import java.time.Duration;
import java.time.LocalDate;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
class FilmUpdateDtoTest {

    @Autowired
    private FilmService service;

    private final FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 ст-в", "Во время " +
            "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
            "можно надежнее. И вот Ипполит ******** Воробьянинов...",
            LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L, of(2L, 3L));

    @Test
    void test0ifAllFieldsAreCorrectedShouldUpdateFilm() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L);


        FilmReadDto filmReadDto2 = service.update(filmReadDto1.getId(), filmUpdateDto1).get();

        assertEquals(filmReadDto2.getId(), filmUpdateDto1.getId());
        assertEquals(filmReadDto2.getName(), filmUpdateDto1.getName());
        assertEquals(filmReadDto2.getDescription(), filmUpdateDto1.getDescription());
        assertEquals(filmReadDto2.getDuration(), filmUpdateDto1.getDuration());
        assertEquals(filmReadDto2.getReleaseDate(), filmUpdateDto1.getReleaseDate());
    }

    @Test
    void test1ifIdNullShouldThrowViolationException() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(null, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L);

        assertThrows(ConstraintViolationException.class, () -> service.update(filmReadDto1.getId(), filmUpdateDto1));
    }

    @Test
    void test2ifIdNullShouldReturnSpecifiedMessage() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(null, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L);

        Exception ex = assertThrows(ConstraintViolationException.class, () -> service.update(filmReadDto1.getId(), filmUpdateDto1));

        assertTrue(ex.getMessage().endsWith("не должно равняться null"));
    }

    @Test
    void test3ifNameIsBlankShouldThrowViolationException() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "      ", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L);

        assertThrows(ConstraintViolationException.class, () -> service.update(filmReadDto1.getId(), filmUpdateDto1));
    }

    @Test
    void test4ifNameIsBlankShouldReturnSpecifiedMessage() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "      ", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L);

        Exception ex = assertThrows(ConstraintViolationException.class, () -> service.update(filmReadDto1.getId(), filmUpdateDto1));

        assertTrue(ex.getMessage().endsWith("не должно быть пустым"));
    }

    @Test
    void test5ifDescriptionSizeMoreThen200SymbolShouldThrowViolationException() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L);

        assertThrows(ConstraintViolationException.class, () -> service.update(filmReadDto1.getId(), filmUpdateDto1));
    }

    @Test
    void test6ifDescriptionSizeMoreThen200SymbolShouldReturnSpecifiedMessage() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161), 1L);

        Exception ex = assertThrows(ConstraintViolationException.class, () -> service.update(filmReadDto1.getId(), filmUpdateDto1));

        assertTrue(ex.getMessage().endsWith("размер должен находиться в диапазоне от 0 до 200"));
    }

    @Test
    void test7ifReleaseDateBeforeDateOfReleaseFirstFilmShouldThrowViolationException() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1895, 12, 27), Duration.ofMinutes(161), 1L);

        assertThrows(ConstraintViolationException.class, () -> service.update(filmReadDto1.getId(), filmUpdateDto1));
    }

    @Test
    void test8ifReleaseDateBeforeDateOfReleaseFirstFilmShouldReturnSpecifiedMessage() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1895, 12, 27), Duration.ofMinutes(161), 1L);

        Exception ex = assertThrows(ConstraintViolationException.class, () -> service.update(filmReadDto1.getId(), filmUpdateDto1));
        System.out.println(ex.getMessage());
        assertTrue(ex.getMessage().endsWith("дата должна быть позже чем \"1895-12-28\" вы ввели 1895-12-27"));
    }

    @Test
    void test9ifDurationLessThen1SecondShouldThrowViolationException() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1971, 6, 21), Duration.ofSeconds(0), 1L);

        assertThrows(ConstraintViolationException.class, () -> service.update(filmReadDto1.getId(), filmUpdateDto1));
    }

    @Test
    void test10ifDurationLessThen1SecondShouldReturnSpecifiedMessage() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1971, 6, 21), Duration.ofSeconds(0), 1L);

        Exception ex = assertThrows(ConstraintViolationException.class, () -> service.update(filmReadDto1.getId(), filmUpdateDto1));

        assertTrue(ex.getMessage().endsWith("должно быть длиннее или равно 1 с"));
    }
}