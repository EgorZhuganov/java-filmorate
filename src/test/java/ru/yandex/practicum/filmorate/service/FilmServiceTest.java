package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.annotation.IntegrationTest;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmReadDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.Duration.ofSeconds;
import static java.time.LocalDate.of;
import static java.util.List.of;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
class FilmServiceTest {

    @Autowired
    private FilmService filmService;
    @Autowired
    @Qualifier("filmDao")
    private AbstractRepository<Long, Film> repository;
    @Autowired
    private UserService userService;

    private final FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 ст-в", "Во время " +
            "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
            "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
            of(1971, 6, 21), Duration.ofMinutes(161), 1L, of(1L, 2L));

    @Test
    void test1createOneFilmShouldReturnFromRepositoryOneFilm() {
        filmService.create(filmCreateDto1);

        repository.findAll();

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void test2createOneFilmShouldReturnFromRepositoryFilmWithEqualsFields() {
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);

        Film film = repository.findById(filmReadDto1.getId()).get();

        assertEquals(film.getName(), filmCreateDto1.getName());
        assertEquals(film.getDescription(), filmCreateDto1.getDescription());
        assertEquals(film.getReleaseDate(), filmCreateDto1.getReleaseDate());
        assertEquals(film.getDuration(), filmCreateDto1.getDuration());
    }

    @Test
    void test3updateShouldUpdateFilmInRepositoryAndReturnEqualsFields() {
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                of(1971, 6, 21), Duration.ofMinutes(161), 1L);

        filmService.update(filmReadDto1.getId(), filmUpdateDto1);
        Film film = repository.findById(filmReadDto1.getId()).get();

        assertEquals(film.getId(), filmUpdateDto1.getId());
        assertEquals(film.getName(), filmUpdateDto1.getName());
        assertEquals(film.getDescription(), filmUpdateDto1.getDescription());
        assertEquals(film.getReleaseDate(), filmUpdateDto1.getReleaseDate());
        assertEquals(film.getDuration(), filmUpdateDto1.getDuration());
    }

    @Test
    void test4deleteShouldReturnOptionalEmptyAnd0FilmsFromRepository() {
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);

        filmService.delete(filmReadDto1.getId());

        assertEquals(empty(), repository.findById(filmReadDto1.getId()));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void test5findByIdShouldReturnFilmReadDtoWithTheSameFields() {
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);

        FilmReadDto filmReadDto2 = filmService.findById(filmReadDto1.getId()).get();

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
                of(1977, 10, 26), Duration.ofMinutes(151), 1L, of(1L, 2L));
        FilmCreateDto filmCreateDto3 = new FilmCreateDto("Бриллиантовая рука", "Кинороман из " +
                "жизни контрабандистов с прологом и эпилогом. В южном городке орудует шайка «валютчиков», " +
                "возглавляемая Шефом и его помощником Графом...",
                of(1969, 4, 28), Duration.ofMinutes(100), 1L, of(1L, 2L));

        filmService.create(filmCreateDto1);
        filmService.create(filmCreateDto2);
        filmService.create(filmCreateDto3);

        List<FilmReadDto> filmReadDtoList = filmService.findAll();

        assertEquals(3, filmReadDtoList.size());
    }

    @Test
    void test7addLikeIfAddOneLikeShouldReturnFilmWithOneLike() {
        UserCreateDto userCreateDto1 = new UserCreateDto("some@mail.ru", "login1",
                "MyDisplayName1", of(1998, 12, 12));

        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);

        filmService.addLike(filmReadDto1.getId(), userReadDto1.getId());

        assertEquals(1, filmService.findById(filmReadDto1.getId()).get().getLikes().size());
    }

    @Test
    void test8addLikeIfAddLikeTwiceByOneUserShouldThrowIllegalArgumentException() {
        UserCreateDto userCreateDto1 = new UserCreateDto("some@mail.ru", "login1",
                "MyDisplayName1", of(1998, 12, 12));

        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);

        filmService.addLike(filmReadDto1.getId(), userReadDto1.getId());

        assertThatThrownBy(() -> filmService.addLike(filmReadDto1.getId(), userReadDto1.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void test9addLikeIfUserNotExistShouldReturnEmptyOptionalAndFilmWithoutLikes() {
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);

        assertEquals(empty(), filmService.addLike(filmReadDto1.getId(), 100500L));
        assertEquals(0, filmService.findById(filmReadDto1.getId()).get().getLikes().size());
    }

    @Test
    void test10addLikeIfFilmNotExistShouldReturnEmptyOptional() {
        UserCreateDto userCreateDto1 = new UserCreateDto("some@mail.ru", "login1",
                "MyDisplayName1", of(1998, 12, 12));

        UserReadDto userReadDto1 = userService.create(userCreateDto1);

        assertEquals(empty(), filmService.addLike(100500L, userReadDto1.getId()));
    }

    @Test
    void test11removeLikeShouldReturn0LikesIfAddOneAndRemoveOne() {
        UserCreateDto userCreateDto1 = new UserCreateDto("some@mail.ru", "login1",
                "MyDisplayName1", of(1998, 12, 12));

        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);

        filmService.addLike(filmReadDto1.getId(), userReadDto1.getId());
        filmService.removeLike(filmReadDto1.getId(), userReadDto1.getId());

        assertEquals(0, filmService.findById(filmReadDto1.getId()).get().getLikes().size());
    }

    @Test
    void test12removeLikeShouldReturnFilmReadDtoWith1LikeIfAddTwoAndRemoveOne() {
        UserCreateDto userCreateDto1 = new UserCreateDto("some@mail.ru", "login1",
                "MyDisplayName1", of(1998, 12, 12));
        UserCreateDto userCreateDto2 = new UserCreateDto("other@mail.ru", "login2",
                "MyDisplayName2", of(1998, 12, 12));

        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);

        filmService.addLike(filmReadDto1.getId(), userReadDto1.getId());
        filmService.addLike(filmReadDto1.getId(), userReadDto2.getId());
        var filmReadDto = filmService.removeLike(filmReadDto1.getId(), userReadDto1.getId());

        assertEquals(1, filmService.findById(filmReadDto1.getId()).get().getLikes().size());
        assertEquals(1, filmReadDto.get().getLikes().size());
    }

    @Test
    void test13removeLikeIfAddOneLikeShouldReturnEmptyOptionalIfUserNotExistAndDontDeleteLike() {
        UserCreateDto userCreateDto1 = new UserCreateDto("some@mail.ru", "login1",
                "MyDisplayName1", of(1998, 12, 12));

        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);

        filmService.addLike(filmReadDto1.getId(), userReadDto1.getId());

        assertEquals(Optional.empty(), filmService.removeLike(filmReadDto1.getId(), 100500L));
        assertEquals(1, filmService.findById(filmReadDto1.getId()).get().getLikes().size());
    }

    @Test
    void test14removeLikeShouldReturnEmptyOptionalIfFilmNotExistAndDontDeleteLike() {
        UserCreateDto userCreateDto1 = new UserCreateDto("some@mail.ru", "login1",
                "MyDisplayName1", of(1998, 12, 12));

        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);

        filmService.addLike(filmReadDto1.getId(), userReadDto1.getId());

        assertEquals(Optional.empty(), filmService.removeLike(100500L, userReadDto1.getId()));
        assertEquals(1, filmService.findById(filmReadDto1.getId()).get().getLikes().size());
    }

    //+ user
    //+ 2 films
    //+ 1 like to one of the films
    //return 2 films, the first will be with likes
    @Test
    void test15findPopularFilmsByLikes() {
        UserCreateDto userCreateDto1 = new UserCreateDto("some@mail.ru", "login1",
                "MyDisplayName1", of(1998, 12, 12));
        FilmCreateDto filmCreateDto2 = new FilmCreateDto("Бриллиантовая рука", "Кинороман из " +
                "жизни контрабандистов с прологом и эпилогом. В южном городке орудует шайка «валютчиков», " +
                "возглавляемая Шефом и его помощником Графом...",
                of(1969, 4, 28), Duration.ofMinutes(100), 1L, of(1L, 2L));

        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);
        FilmReadDto filmReadDto2 = filmService.create(filmCreateDto2);

        filmService.addLike(filmReadDto1.getId(), userReadDto1.getId());

        List<FilmReadDto> filmReadDtoList = filmService.findPopularFilmsByLikes(10);

        assertEquals(1, filmReadDtoList.get(0).getLikes().size());
        assertEquals(2, filmReadDtoList.size());
        assertEquals(filmReadDto1.getId(), filmReadDtoList.get(0).getId());
    }

    //+ user
    //+ 2 films
    //+ 1 like to one of the films
    //return list of 1 film with sorting by likes, return 1 film with like
    @Test
    void test16findPopularFilmsByLikes() {
        UserCreateDto userCreateDto1 = new UserCreateDto("some@mail.ru", "login1",
                "MyDisplayName1", of(1998, 12, 12));
        FilmCreateDto filmCreateDto2 = new FilmCreateDto("Бриллиантовая рука", "Кинороман из " +
                "жизни контрабандистов с прологом и эпилогом. В южном городке орудует шайка «валютчиков», " +
                "возглавляемая Шефом и его помощником Графом...",
                of(1969, 4, 28), Duration.ofMinutes(100), 1L, of(1L, 2L));

        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);
        FilmReadDto filmReadDto2 = filmService.create(filmCreateDto2);

        filmService.addLike(filmReadDto1.getId(), userReadDto1.getId());

        List<FilmReadDto> filmReadDtoList = filmService.findPopularFilmsByLikes(1);

        assertEquals(1, filmReadDtoList.size());
        assertEquals(filmReadDto1.getId(), filmReadDtoList.get(0).getId());
    }

    //+ user
    //+ 2 films
    //no one film haven't got likes, then sorting will be by ID likes
    //return list of 2 films with sorting by id, first will be with bigger id
    @Test
    void test17findPopularFilmsByLikes() {
        FilmCreateDto filmCreateDto2 = new FilmCreateDto("Бриллиантовая рука", "Кинороман из " +
                "жизни контрабандистов с прологом и эпилогом. В южном городке орудует шайка «валютчиков», " +
                "возглавляемая Шефом и его помощником Графом...",
                of(1969, 4, 28), Duration.ofMinutes(100), 1L, of(1L, 2L));

        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);
        FilmReadDto filmReadDto2 = filmService.create(filmCreateDto2);

        List<FilmReadDto> filmReadDtoList = filmService.findPopularFilmsByLikes(2);

        assertEquals(2, filmReadDtoList.size());
        assertEquals(filmReadDto2.getId(), filmReadDtoList.get(0).getId());
    }

    //+ 2 user
    //+ 2 films
    //one film has two likes, other 1 like
    //return first film on first place
    @Test
    void test18findPopularFilmsByLikes() {
        UserCreateDto userCreateDto1 = new UserCreateDto("some@mail.ru", "login1",
                "MyDisplayName1", of(1998, 12, 12));
        UserCreateDto userCreateDto2 = new UserCreateDto("other@mail.ru", "login2",
                "MyDisplayName2", of(1998, 12, 12));
        FilmCreateDto filmCreateDto2 = new FilmCreateDto("Бриллиантовая рука", "Кинороман из " +
                "жизни контрабандистов с прологом и эпилогом. В южном городке орудует шайка «валютчиков», " +
                "возглавляемая Шефом и его помощником Графом...",
                of(1969, 4, 28), Duration.ofMinutes(100), 1L, of(1L, 2L));

        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        FilmReadDto filmReadDto1 = filmService.create(filmCreateDto1);
        FilmReadDto filmReadDto2 = filmService.create(filmCreateDto2);

        filmService.addLike(filmReadDto1.getId(), userReadDto1.getId());
        filmService.addLike(filmReadDto1.getId(), userReadDto2.getId());
        filmService.addLike(filmReadDto2.getId(), userReadDto1.getId());

        List<FilmReadDto> filmReadDtoList = filmService.findPopularFilmsByLikes(2);

        assertEquals(2, filmReadDtoList.size());
        assertEquals(filmReadDto1.getId(), filmReadDtoList.get(0).getId());
    }

    @Test
    void test18findPopularFilmsByLikesIfNoOneFilmExistAndCountIs5ShouldReturn0Films() {
        List<FilmReadDto> filmReadDtoList = filmService.findPopularFilmsByLikes(5);

        assertEquals(0, filmReadDtoList.size());
    }

    @Test
    void test19getCommonFilmsIf2CommonFilmsExistingShouldReturn2FilmsFromList() {
        List<FilmReadDto> filmList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            filmList.add(filmService.create(FilmCreateDto.builder()
                    .name("Film" + i)
                    .description("Descr" + i)
                    .releaseDate(of(2021, 12, 2))
                    .duration(ofSeconds(1500))
                    .mpaId(1L)
                    .build()));
        }

        UserReadDto user1 = userService.create(new UserCreateDto("email1@mail.ru", "login1", "name1", of(2001, 1, 1)));
        UserReadDto user2 = userService.create(new UserCreateDto("email2@mail.ru", "login2", "name2", of(2002, 1, 1)));
        UserReadDto user3 = userService.create(new UserCreateDto("email3@mail.ru", "login3", "name3", of(2003, 1, 1)));

        filmService.addLike(filmList.get(0).getId(), user1.getId());
        filmService.addLike(filmList.get(0).getId(), user2.getId());
        filmService.addLike(filmList.get(0).getId(), user3.getId());
        filmService.addLike(filmList.get(1).getId(), user1.getId());
        filmService.addLike(filmList.get(1).getId(), user2.getId());
        filmService.addLike(filmList.get(2).getId(), user1.getId());
        filmService.addLike(filmList.get(3).getId(), user2.getId());

        assertThat(filmService.getCommonFilms(user1.getId(), user2.getId())).hasSize(2);
    }

    @Test
    void test20getCommonFilmsIfCommonFilmsNotExistingShouldReturn0FilmsFromList() {
        List<FilmReadDto> filmList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            filmList.add(filmService.create(FilmCreateDto.builder()
                    .name("Film" + i)
                    .description("Descr" + i)
                    .releaseDate(of(2021, 12, 2))
                    .duration(ofSeconds(1500))
                    .mpaId(1L)
                    .build()));
        }
        UserReadDto user1 = userService.create(new UserCreateDto("email1@mail.ru", "login1", "name1", of(2001, 1, 1)));
        UserReadDto user2 = userService.create(new UserCreateDto("email2@mail.ru", "login2", "name2", of(2002, 1, 1)));
        UserReadDto user3 = userService.create(new UserCreateDto("email3@mail.ru", "login3", "name3", of(2003, 1, 1)));

        filmService.addLike(filmList.get(0).getId(), user1.getId());
        filmService.addLike(filmList.get(0).getId(), user2.getId());

        assertThat(filmService.getCommonFilms(user2.getId(), user3.getId())).hasSize(0);
    }
}
