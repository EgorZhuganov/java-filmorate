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

    @Autowired FilmService service;
    @Autowired AbstractRepository<Long, Film> repository;

    private final FilmCreateDto filmCreateDto1 = new FilmCreateDto(1L, "12 ст-в", "Во время " +
            "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
            "можно надежнее. И вот Ипполит ******** Воробьянинов, ******** Старгородский предводитель дворянства и " +
            "светский лев, а ныне — скромный делопроизводитель ЗАГСа, ******* от умирающей тещи...",
            LocalDate.of(1971,6,21), Duration.ofMinutes(161));
    private final FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(1L, "12 стульев", "Во время " +
            "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
            "можно надежнее. И вот Ипполит Матвеевич Воробьянинов, бывший Старгородский предводитель дворянства и " +
            "светский лев, а ныне — скромный делопроизводитель ЗАГСа, узнает от умирающей тещи...",
            LocalDate.of(1971,6,21), Duration.ofMinutes(161));

    @Test
    void test1createOneFilmShouldReturnFromRepositoryOneFilm() {
        service.create(filmCreateDto1);

        repository.findAll();

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void test2createOneFilmShouldReturnFromRepositoryFilmWithEqualsFields() {
        service.create(filmCreateDto1);

        Film film = repository.findById(filmCreateDto1.getId()).get();

        assertEquals(film.getId(), filmCreateDto1.getId());
        assertEquals(film.getName(), filmCreateDto1.getName());
        assertEquals(film.getDescription(), filmCreateDto1.getDescription());
        assertEquals(film.getReleaseDate(), filmCreateDto1.getReleaseDate());
        assertEquals(film.getDuration(), filmCreateDto1.getDuration());
    }

    @Test
    void test3updateShouldUpdateFilmInRepositoryAndReturnEqualsFields() {
        service.create(filmCreateDto1);

        service.update(filmCreateDto1.getId(), filmUpdateDto1);
        Film film = repository.findById(filmCreateDto1.getId()).get();

        assertEquals(film.getId(), filmUpdateDto1.getId());
        assertEquals(film.getName(), filmUpdateDto1.getName());
        assertEquals(film.getDescription(), filmUpdateDto1.getDescription());
        assertEquals(film.getReleaseDate(), filmUpdateDto1.getReleaseDate());
        assertEquals(film.getDuration(), filmUpdateDto1.getDuration());
    }

    @Test
    void test4deleteShouldReturnOptionalEmptyAnd0FilmsFromRepository() {
        service.create(filmCreateDto1);

        service.delete(filmCreateDto1.getId());

        assertEquals(Optional.empty(), repository.findById(filmCreateDto1.getId()));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void test5findByIdShouldReturnFilmReadDtoWithTheSameFields() {
        service.create(filmCreateDto1);

        FilmReadDto filmReadDto = service.findById(filmCreateDto1.getId()).get();

        assertEquals(filmReadDto.getId(), filmCreateDto1.getId());
        assertEquals(filmReadDto.getName(), filmCreateDto1.getName());
        assertEquals(filmReadDto.getDescription(), filmCreateDto1.getDescription());
        assertEquals(filmReadDto.getDuration(), filmCreateDto1.getDuration());
        assertEquals(filmReadDto.getReleaseDate(), filmCreateDto1.getReleaseDate());
    }

    @Test
    void test6findAllIfAdd3FilmCreateDtoShouldReturnListWith3Film() {
        FilmCreateDto filmCreateDto2 = new FilmCreateDto(2L, "12 ст-в", "Анатолий Ефремович " +
                "Новосельцев, рядовой служащий одного статистического управления, — человек робкий и застенчивый. " +
                "Для него неплохо бы получить вакантное место зав. отделом, но он не знает как подступиться к этому " +
                "делу. Старый приятель Самохвалов советует ему приударить за Людмилой Прокопьевной Калугиной, — " +
                "сухарем в юбке и директором заведения",
                LocalDate.of(1977,10,26), Duration.ofMinutes(151));
        FilmCreateDto filmCreateDto3 = new FilmCreateDto(3L, "Бриллиантовая рука", "Кинороман из " +
                "жизни контрабандистов с прологом и эпилогом. В южном городке орудует шайка «валютчиков», " +
                "возглавляемая Шефом и его помощником Графом. Скромный советский служащий и примерный семьянин " +
                "Семен Семеныч Горбунков отправляется в зарубежный круиз на теплоходе...",
                LocalDate.of(1969,4,28), Duration.ofMinutes(100));

        service.create(filmCreateDto1);
        service.create(filmCreateDto2);
        service.create(filmCreateDto3);

        List<FilmReadDto> filmReadDtoList = service.findAll();

        assertEquals(3, filmReadDtoList.size());
    }
}
