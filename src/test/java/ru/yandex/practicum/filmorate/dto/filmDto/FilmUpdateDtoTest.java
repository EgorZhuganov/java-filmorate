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
class FilmUpdateDtoTest {

    @Autowired FilmService service;

    private final FilmCreateDto filmCreateDto1 = new FilmCreateDto(1L, "12 ст-в", "Во время " +
            "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
            "можно надежнее. И вот Ипполит ******** Воробьянинов, ******** Старгородский предводитель дворянства и " +
            "светский лев, а ныне — скромный делопроизводитель ЗАГСа, ******* от умирающей тещи...",
            LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

    @Test
    void test0ifAllFieldsAreCorrectedShouldUpdateFilm() {
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(1L, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

        service.create(filmCreateDto1);
        service.update(filmCreateDto1.getId(), filmUpdateDto1);

        FilmReadDto filmReadDto1 = service.findById(filmUpdateDto1.getId()).get();

        assertEquals(filmReadDto1.getId(), filmUpdateDto1.getId());
        assertEquals(filmReadDto1.getName(), filmUpdateDto1.getName());
        assertEquals(filmReadDto1.getDescription(), filmUpdateDto1.getDescription());
        assertEquals(filmReadDto1.getDuration(), filmUpdateDto1.getDuration());
        assertEquals(filmReadDto1.getReleaseDate(), filmUpdateDto1.getReleaseDate());
    }

    @Test
    void test1ifIdNullShouldThrowViolationException() {
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(null, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

        assertThrows(ConstraintViolationException.class, () -> service.update(filmCreateDto1.getId(), filmUpdateDto1));
    }

    @Test
    void test2ifIdNullShouldReturnSpecifiedMessage() {
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(null, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

        Exception ex = assertThrows(ConstraintViolationException.class, () -> service.update(filmCreateDto1.getId(), filmUpdateDto1));

        assertTrue(ex.getMessage().endsWith("не должно равняться null"));
    }

    @Test
    void test3ifNameIsBlankShouldThrowViolationException() {
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(1L, "      ", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

        assertThrows(ConstraintViolationException.class, () -> service.update(filmCreateDto1.getId(), filmUpdateDto1));
    }

    @Test
    void test4ifNameIsBlankShouldReturnSpecifiedMessage() {
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(1L, "      ", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

        Exception ex = assertThrows(ConstraintViolationException.class, () -> service.update(filmCreateDto1.getId(), filmUpdateDto1));

        assertTrue(ex.getMessage().endsWith("не должно быть пустым"));
    }

    @Test
    void test5ifDescriptionSizeLessThen200SymbolShouldThrowViolationException() {
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(1L, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

        assertThrows(ConstraintViolationException.class, () -> service.update(filmCreateDto1.getId(), filmUpdateDto1));
    }

    @Test
    void test6ifDescriptionSizeLessThen200SymbolShouldReturnSpecifiedMessage() {
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(1L, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

        Exception ex = assertThrows(ConstraintViolationException.class, () -> service.update(filmCreateDto1.getId(), filmUpdateDto1));

        assertTrue(ex.getMessage().endsWith("размер должен находиться в диапазоне от 200 до 2147483647"));
    }

    @Test
    void test7ifReleaseDateBeforeDateOfReleaseFirstFilmShouldThrowViolationException() {
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(1L, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1895, 12, 27), Duration.ofMinutes(161));

        assertThrows(ConstraintViolationException.class, () -> service.update(filmCreateDto1.getId(), filmUpdateDto1));
    }

    @Test
    void test8ifReleaseDateBeforeDateOfReleaseFirstFilmShouldReturnSpecifiedMessage() {
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(1L, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1895, 12, 27), Duration.ofMinutes(161));

        Exception ex = assertThrows(ConstraintViolationException.class, () -> service.update(filmCreateDto1.getId(), filmUpdateDto1));

        assertTrue(ex.getMessage().endsWith("дата должна быть позже чем \"1895-12-27\""));
    }

    @Test
    void test9ifDurationLessThen1SecondShouldThrowViolationException() {
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(1L, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofSeconds(0));

        assertThrows(ConstraintViolationException.class, () -> service.update(filmCreateDto1.getId(), filmUpdateDto1));
    }

    @Test
    void test10ifDurationLessThen1SecondShouldReturnSpecifiedMessage() {
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(1L, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
                "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
                LocalDate.of(1971, 6, 21), Duration.ofSeconds(0));

        Exception ex = assertThrows(ConstraintViolationException.class, () -> service.update(filmCreateDto1.getId(), filmUpdateDto1));

        assertTrue(ex.getMessage().endsWith("должно быть длиннее или равно 1 с"));
    }
}