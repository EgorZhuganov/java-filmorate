package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmReadDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.mapper.filmMapper.FilmCreateMapper;
import ru.yandex.practicum.filmorate.mapper.filmMapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.filmMapper.FilmReadMapper;
import ru.yandex.practicum.filmorate.mapper.filmMapper.FilmUpdateMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;

import static java.util.Comparator.*;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toMap;

@Service
@Validated
@Slf4j
public class FilmService {

    private final AbstractRepository<Long, Film> repository;
    private final Map<String, FilmMapper<?, ?>> mapper;
    private final UserService userService;

    @Autowired
    public FilmService(AbstractRepository<Long, Film> repository, List<FilmMapper<?, ?>> mappers, UserService userService) {
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

    public FilmReadDto create(@Valid FilmCreateDto filmCreateDto) throws ConstraintViolationException {
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
        var filmReadMapper = (FilmReadMapper) mapper.get(FilmReadMapper.class.getName());
        var maybeFilm = repository.findById(filmId);
        var maybeUser = userService.findById(userId);
        FilmReadDto filmReadDto = null;
        if (maybeUser.isPresent() && maybeFilm.isPresent()) {
            Film film = maybeFilm.get();
            film.getLikes().add(userId);
            repository.update(film);
            filmReadDto = filmReadMapper.mapFrom(film);
        }
        return ofNullable(filmReadDto);
    }


    public boolean removeLike(Long filmId, Long userId) {
        var maybeFilm = repository.findById(filmId);
        var maybeUser = userService.findById(userId);
        if (maybeUser.isPresent() && maybeFilm.isPresent()) {
            Film film = maybeFilm.get();
            film.getLikes().remove(userId);
            repository.update(film);
            return true;
        }
        return false;
    }

    //If we have two films with the same count likes, then sorting will be by ID, first - film with bigger id
    public List<FilmReadDto> findPopularFilmsByLikes(int count) {
        var filmReadMapper = (FilmReadMapper) mapper.get(FilmReadMapper.class.getName());
        Comparator<Film> comparator = comparingLong(film -> film.getLikes().size());
        comparator = comparator.thenComparing(Film::getId).reversed();

        return repository.findAll()
                .stream()
                .sorted(comparator)
                .limit(count)
                .map(filmReadMapper::mapFrom)
                .collect(toList());
    }
}
