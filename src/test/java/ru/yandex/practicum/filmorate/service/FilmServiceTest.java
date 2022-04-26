package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmReadDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmServiceTest {

    @Autowired
    private FilmService service;
    @Autowired
    private AbstractRepository<Long, Film> repository;

    private final FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 ст-в", "Во время " +
            "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
            "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
            LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

    @Test
    void test1createOneFilmShouldReturnFromRepositoryOneFilm() {
        service.create(filmCreateDto1);

        repository.findAll();

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void test2createOneFilmShouldReturnFromRepositoryFilmWithEqualsFields() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        Film film = repository.findById(filmReadDto1.getId()).get();

        assertEquals(film.getName(), filmCreateDto1.getName());
        assertEquals(film.getDescription(), filmCreateDto1.getDescription());
        assertEquals(film.getReleaseDate(), filmCreateDto1.getReleaseDate());
        assertEquals(film.getDuration(), filmCreateDto1.getDuration());
    }

    @Test
    void test3updateShouldUpdateFilmInRepositoryAndReturnEqualsFields() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

        service.update(filmReadDto1.getId(), filmUpdateDto1);
        Film film = repository.findById(filmReadDto1.getId()).get();

        assertEquals(film.getId(), filmUpdateDto1.getId());
        assertEquals(film.getName(), filmUpdateDto1.getName());
        assertEquals(film.getDescription(), filmUpdateDto1.getDescription());
        assertEquals(film.getReleaseDate(), filmUpdateDto1.getReleaseDate());
        assertEquals(film.getDuration(), filmUpdateDto1.getDuration());
    }

    @Test
    void test4deleteShouldReturnOptionalEmptyAnd0FilmsFromRepository() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        service.delete(filmReadDto1.getId());

        assertEquals(Optional.empty(), repository.findById(filmReadDto1.getId()));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void test5findByIdShouldReturnFilmReadDtoWithTheSameFields() {
        FilmReadDto filmReadDto1 = service.create(filmCreateDto1);

        FilmReadDto filmReadDto2 = service.findById(filmReadDto1.getId()).get();

        assertEquals(filmReadDto2.getName(), filmCreateDto1.getName());
        assertEquals(filmReadDto2.getDescription(), filmCreateDto1.getDescription());
        assertEquals(filmReadDto2.getDuration(), filmCreateDto1.getDuration());
        assertEquals(filmReadDto2.getReleaseDate(), filmCreateDto1.getReleaseDate());
    }

    @Test
    void test6findAllIfAdd3FilmCreateDtoShouldReturnListWith3Film() {
        FilmCreateDto filmCreateDto2 = new FilmCreateDto("12 ст-в", "Анатолий Ефремович " +
                "Новосельцев, рядовой служащий одного статистического управления, — человек робкий и застенчивый. " +
                "Для него неплохо бы получить вакантное место зав. отделом, но...",
                LocalDate.of(1977, 10, 26), Duration.ofMinutes(151));
        FilmCreateDto filmCreateDto3 = new FilmCreateDto("Бриллиантовая рука", "Кинороман из " +
                "жизни контрабандистов с прологом и эпилогом. В южном городке орудует шайка «валютчиков», " +
                "возглавляемая Шефом и его помощником Графом...",
                LocalDate.of(1969, 4, 28), Duration.ofMinutes(100));

        service.create(filmCreateDto1);
        service.create(filmCreateDto2);
        service.create(filmCreateDto3);

        List<FilmReadDto> filmReadDtoList = service.findAll();

        assertEquals(3, filmReadDtoList.size());
    }
}
