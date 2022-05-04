package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users/{id}/friends")
@Slf4j
public class FriendController {

    private final FriendService service;

    @Autowired
    public FriendController(FriendService service) {
        this.service = service;
    }

    //users/{id}/friends/{friendId} - добавление в друзья.
    @PutMapping("/{friendId}")
    @ResponseStatus(OK)
    public UserReadDto add(@PathVariable Long id, @PathVariable Long friendId) {
        return service.addToFriends(id, friendId)
                .orElseThrow(() -> {
                            log.warn("user with id {} or id {} not found for add friend", id, friendId);
                            return new ResponseStatusException(NOT_FOUND);
                        }
                );
    }

    //users/{id}/friends - возвращаем список друзей пользователя.
    @GetMapping
    @ResponseStatus(OK)
    public List<UserReadDto> findAll(@PathVariable Long id) {
        return service.findAllFriends(id)
                .orElseThrow(() -> {
                    log.warn("user with id {} not found for find all friends", id);
                    return new ResponseStatusException(NOT_FOUND);
                });
    }

    //users/{id}/friends/common/{otherId} - список друзей общих с другим пользователем.
    @GetMapping("/common/{otherUserId}")
    @ResponseStatus(OK)
    public List<UserReadDto> findAllCommon(Long id, Long otherUserId) {
        return service.findAllCommonFriends(id, otherUserId)
                .orElseThrow(() -> {
                            log.warn("user with id {} or id {} not found for find common friends", id, otherUserId);
                            return new ResponseStatusException(NOT_FOUND);
                        }
                );
    }

    //users/{id}/friends/{friendId} - удаление из друзей.
    @DeleteMapping("/{friendId}")
    @ResponseStatus(NO_CONTENT)
    public void remove(@PathVariable Long id, @PathVariable Long friendId) {
        if (!service.removeFromFriends(id, friendId)) {
            log.warn("user with id {} or friend with id {} not found for unsubscribe", id, friendId);
            throw new ResponseStatusException(NOT_FOUND);
        }
    }
}