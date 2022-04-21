package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

@RestController("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping //200 OK and return List
    public String findAll() {
        String userListAsJson = null;
        try {
            userListAsJson = new ObjectMapper().writeValueAsString(userService.findAll());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userListAsJson;
    }

    @GetMapping("/{id}") //200, OK orElseThrow NOT_FOUND
    public String findById(@PathVariable Long id) {
        String userAsJson = null;
        try {
            userAsJson = new ObjectMapper().writeValueAsString(userService
                    .findById(id) //TODO что возвращает и в каком виде?
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userAsJson;
    }

    @PostMapping //201 and return userReadDto
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@Valid @RequestBody UserCreateDto user) {
        UserReadDto userDto = userService.create(user);
        String userAsJson;
        try {
            userAsJson = new ObjectMapper().writeValueAsString(userDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return userAsJson;
    }

    @PutMapping //204 no content
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String update(@Valid @RequestBody UserUpdateDto user) {
        UserReadDto userDto = userService
                .update(user.getId(), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String userAsJson;
        try {
            userAsJson = new ObjectMapper().writeValueAsString(userDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return userAsJson;
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
