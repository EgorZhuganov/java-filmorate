package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmReadDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.mapper.filmMapper.FilmCreateMapper;
import ru.yandex.practicum.filmorate.mapper.filmMapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.filmMapper.FilmReadMapper;
import ru.yandex.practicum.filmorate.mapper.filmMapper.FilmUpdateMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.database.FilmRepository;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Optional.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toMap;

@Service
@Validated
@Slf4j
public class FilmService {

    private final FilmRepository repository;
    private final Map<String, FilmMapper<?, ?>> mapper;
    private final UserService userService;

    @Autowired
    public FilmService(FilmRepository repository, List<FilmMapper<?, ?>> mappers, UserService userService) {
        this.repository = repository;
        this.mapper = mappers.stream().collect(toMap(FilmMapper::getKey, Function.identity()));
        this.userService = userService;
    }

    public List<FilmReadDto> findAll() {
        var filmReadMapper = (FilmReadMapper) mapper.get(FilmReadMapper.class.getName());
        return repository.findAll()
                .stream()
                .map(filmReadMapper::mapFrom)
                .collect(toList());
    }

    public Optional<FilmReadDto> findById(Long id) {
        var filmReadMapper = (FilmReadMapper) mapper.get(FilmReadMapper.class.getName());
        return repository.findById(id).map(filmReadMapper::mapFrom);
    }

    public FilmReadDto create(@Valid FilmCreateDto filmCreateDto) {
        var filmCreateMapper = (FilmCreateMapper) mapper.get(FilmCreateMapper.class.getName());
        var filmReadMapper = (FilmReadMapper) mapper.get(FilmReadMapper.class.getName());
        return of(filmCreateDto)
                .map(filmCreateMapper::mapFrom)
                .map(film -> {
                    log.info("film {} was created", film.getName());
                    return repository.insert(film);
                })
                .map(filmReadMapper::mapFrom)
                .orElseThrow();
    }

    public Optional<FilmReadDto> update(Long id, @Valid FilmUpdateDto filmUpdateDto) throws ConstraintViolationException {
        var filmReadMapper = (FilmReadMapper) mapper.get(FilmReadMapper.class.getName());
        var filmUpdateMapper = (FilmUpdateMapper) mapper.get(FilmUpdateMapper.class.getName());
        return repository.findById(id)
                .map(filmModel -> filmUpdateMapper.mapFrom(filmUpdateDto, filmModel))
                .map(updatedFilm -> {
                    log.info("film with id {} was updated", updatedFilm.getId());
                    return repository.update(updatedFilm);
                })
                .map(filmReadMapper::mapFrom);
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }

    public Optional<FilmReadDto> addLike(Long filmId, Long userId) {
        if (repository.findLike(filmId, userId)) {
            log.warn("like from user id {} to film id {}", userId, filmId);
            throw new IllegalArgumentException("like already exist");
        }
        var filmReadMapper = (FilmReadMapper) mapper.get(FilmReadMapper.class.getName());
        var maybeFilm = repository.findById(filmId);
        var maybeUser = userService.findById(userId);
        FilmReadDto filmReadDto = null;
        if (maybeUser.isPresent() && maybeFilm.isPresent()) {
            Film film = maybeFilm.get();
            film.getLikes().add(userId);
            repository.insertLike(filmId, userId);
            filmReadDto = filmReadMapper.mapFrom(film);
        }
        return ofNullable(filmReadDto);
    }

    public Optional<FilmReadDto> removeLike(Long filmId, Long userId) {
        var filmReadMapper = (FilmReadMapper) mapper.get(FilmReadMapper.class.getName());
        var maybeFilm = repository.findById(filmId);
        var maybeUser = userService.findById(userId);
        FilmReadDto filmReadDto = null;
        if (maybeUser.isPresent() && maybeFilm.isPresent()) {
            Film film = maybeFilm.get();
            film.getLikes().remove(userId);
            repository.deleteLike(filmId, userId);
            filmReadDto = filmReadMapper.mapFrom(film);
        }
        return ofNullable(filmReadDto);
    }

    //If we have two films with the same count likes, then sorting will be by ID, first - film with bigger id
    public List<FilmReadDto> findPopularFilmsByLikes(int count) {
        var filmReadMapper = (FilmReadMapper) mapper.get(FilmReadMapper.class.getName());
        return repository.findPopularFilmsByLikes(count)
                .stream()
                .map(filmReadMapper::mapFrom)
                .collect(toList());
    }

    public List<FilmReadDto> findCommonFilmsSortedByLikesDesc(Long userId, Long otherUserId) {
        var filmReadMapper = (FilmReadMapper) mapper.get(FilmReadMapper.class.getName());
        Optional<UserReadDto> user = userService.findById(userId);
        Optional<UserReadDto> friend = userService.findById(otherUserId);
        if (user.isPresent() && friend.isPresent()) {
            return repository
                    .findCommonFilmsBetweenTwoUsers(userId, otherUserId)
                    .stream()
                    .map(filmReadMapper::mapFrom)
                    .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                    .collect(toList());
        }
        return new ArrayList<>();
    }

    public List<FilmReadDto> findRecommendedFilms(Long userId) {
        var filmReadMapper = (FilmReadMapper) mapper.get(FilmReadMapper.class.getName());
        if (userService.findById(userId).isEmpty()) {
            log.warn("user with id {} not exist", userId);
            throw new IllegalArgumentException(format("User with id %d not exist", userId));
        }
        List<Film> filmsFirstUser = repository.findFilmsThatUserLikes(userId);
        int countMatches = 0;
        List<Film> filmsOtherUser = new ArrayList<>();
        List<UserReadDto> users = userService.findAll();
        for (UserReadDto userReadDto : users) {
            if (userReadDto.getId().equals(userId)) {
                continue;
            }
            List<Film> films = repository.findFilmsThatUserLikes(userReadDto.getId());
            int currentCountMatches = films.stream().filter(filmsFirstUser::contains).toList().size();
            if(currentCountMatches > countMatches)  {
                countMatches = currentCountMatches;
                filmsOtherUser = films;
            }
        }

        return filmsOtherUser.stream()
                .filter(film -> !filmsFirstUser.contains(film))
                .map(filmReadMapper::mapFrom)
                .toList();
    }
}