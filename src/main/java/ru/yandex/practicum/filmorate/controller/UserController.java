package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping //200 OK and return List
    @ResponseBody
    public List<UserReadDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}") //200, OK orElseThrow NOT_FOUND
    @ResponseBody
    public UserReadDto findById(@PathVariable Long id) {
        return userService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping //201 and return userReadDto
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserReadDto create(@RequestBody UserCreateDto user) {
        return userService.create(user);
    }

    @PutMapping //200 OK orElseThrow NOT_FOUND
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserReadDto update(@RequestBody UserUpdateDto user) {
        return userService
                .update(user.getId(), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}") //204 no content
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean remove(@PathVariable Long id) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return true;
    }
}
