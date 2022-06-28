package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmReadDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final FilmService filmService;

    @GetMapping //200 OK and return List
    public List<UserReadDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}") //200, OK orElseThrow NOT_FOUND
    public UserReadDto findById(@PathVariable Long id) {
        return userService
                .findById(id)
                .orElseThrow(() -> {
                            log.warn("user with id: {} not found", id);
                            return new ResponseStatusException(NOT_FOUND);
                        }
                );
    }

    @PostMapping //201 and return userReadDto
    @ResponseStatus(CREATED)
    public UserReadDto create(@RequestBody UserCreateDto user) {
        try {
            return userService.create(user);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    @PutMapping //200 OK orElseThrow NOT_FOUND
    @ResponseStatus(OK)
    public UserReadDto update(@RequestBody UserUpdateDto user) {
        try {
            return userService
                    .update(user.getId(), user)
                    .orElseThrow(() -> {
                                log.warn("user with id: {} not found for update", user.getId());
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
        if (!userService.delete(id)) {
            log.warn("user with id: {} not found for remove", id);
            throw new ResponseStatusException(NOT_FOUND);
        }
    }

    @GetMapping("/{userId}/recommendations")
    public List<FilmReadDto> getRecommendations(@PathVariable("userId") Long userId) {
        try {
            return filmService.findRecommendedFilms(userId);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }
}
