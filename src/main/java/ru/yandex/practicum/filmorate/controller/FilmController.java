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

    @GetMapping //200 OK and return List
    public List<FilmReadDto> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}") //200, OK orElseThrow NOT_FOUND
    public FilmReadDto findById(@PathVariable Long id) {
        return filmService
                .findById(id)
                .orElseThrow(() -> {
                            log.warn("film with id: {} not found", id);
                            return new ResponseStatusException(NOT_FOUND);
                        }
                );
    }

    @PostMapping //201 and return userReadDto
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

    @DeleteMapping("/{id}") //204 no content
    @ResponseStatus(NO_CONTENT)
    public void remove(@PathVariable Long id) {
        if (!filmService.delete(id)) {
            log.warn("film with id: {} not found for remove", id);
            throw new ResponseStatusException(NOT_FOUND);
        }
    }
}
