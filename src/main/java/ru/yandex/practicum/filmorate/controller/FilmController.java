package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmReadDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @GetMapping //200 OK
    public List<FilmReadDto> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}") //200 OK orElseThrow NOT_FOUND
    public FilmReadDto findById(@PathVariable Long id) {
        return filmService
                .findById(id)
                .orElseThrow(() -> {
                            log.warn("film with id: {} not found", id);
                            return new ResponseStatusException(NOT_FOUND);
                        }
                );
    }

    @PostMapping //201 CREATED or BAD_REQUSET
    @ResponseStatus(CREATED)
    public FilmReadDto create(@RequestBody FilmCreateDto film) {
        try {
            return filmService.create(film);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    @PutMapping //200 OK orElseThrow NOT_FOUND
    @ResponseStatus(OK)
    public FilmReadDto update(@RequestBody FilmUpdateDto film) {
        try {
            return filmService
                    .update(film.getId(), film)
                    .orElseThrow(() -> {
                                log.warn("film with id: {} not found for update", film.getId());
                                return new ResponseStatusException(NOT_FOUND);
                            }
                    );
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}") //204 NO_CONTENT
    @ResponseStatus(NO_CONTENT)
    public void remove(@PathVariable Long id) {
        if (!filmService.delete(id)) {
            log.warn("film with id: {} not found for remove", id);
            throw new ResponseStatusException(NOT_FOUND);
        }
    }

    @PutMapping("/{filmId}/like/{userId}") //200 OK orElseThrow NOT_FOUND
    @ResponseStatus(OK)
    public FilmReadDto addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.addLike(filmId, userId).orElseThrow(() -> {
            log.warn("film with id: {} or user with id: {} not found for add like", filmId, userId);
            return new ResponseStatusException(NOT_FOUND);
        });
    }

    @DeleteMapping("/{filmId}/like/{userId}") //204 NO_CONTENT orElseThrow NOT_FOUND
    @ResponseStatus(NO_CONTENT)
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        if (!filmService.removeLike(filmId, userId)) {
            log.warn("film with id {} or user with id {} not found", filmId, userId);
            throw new ResponseStatusException(NOT_FOUND);
        }
    }

    //popular  - first 10 films by likes.
    //popular?count={count} - list {count} films by likes.
    @GetMapping("/popular")
    @ResponseStatus(OK)
    public List<FilmReadDto> findPopularFilmsByLikes(@RequestParam(required = false, defaultValue = "10") int count) {
        return filmService.findPopularFilmsByLikes(count);
    }
}
