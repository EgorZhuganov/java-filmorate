package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users/{id}/friends")
@Slf4j
public class FriendController {

    private final UserService service;

    @Autowired
    public FriendController(UserService service) {
        this.service = service;
    }

    @PutMapping("/{friendId}") //add friend
    @ResponseStatus(OK)
    public UserReadDto addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        try {
            return service.addToFriends(id, friendId)
                    .orElseThrow(() -> {
                                log.warn("user with id {} or id {} not found for add friend", id, friendId);
                                return new ResponseStatusException(NOT_FOUND);
                            }
                    );
        } catch (UnsupportedOperationException e) {
            log.warn("user with id {} was trying add himself in friends", id);
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    @GetMapping //return list of friends
    @ResponseStatus(OK)
    public List<UserReadDto> findAllFriends(@PathVariable Long id) {
        return service.findAllFriends(id)
                .orElseThrow(() -> {
                    log.warn("user with id {} not found for find all friends", id);
                    return new ResponseStatusException(NOT_FOUND);
                });
    }

    @GetMapping("/common/{otherUserId}") //return common friends between two users
    @ResponseStatus(OK)
    public List<UserReadDto> findAllCommonFriends(@PathVariable Long id, @PathVariable Long otherUserId) {
        return service.findAllCommonFriends(id, otherUserId)
                .orElseThrow(() -> {
                            log.warn("user with id {} or id {} not found for find common friends", id, otherUserId);
                            return new ResponseStatusException(NOT_FOUND);
                        }
                );
    }

    @DeleteMapping("/{friendId}") //remove from friends
    @ResponseStatus(OK)
    public UserReadDto removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        try {
            return service.removeFromFriends(id, friendId).orElseThrow(() -> {
                log.warn("user with id {} or friend with id {} not found for unsubscribe", id, friendId);
                throw new ResponseStatusException(NOT_FOUND);
            });
        } catch (UnsupportedOperationException e) {
            log.warn("user with id {} was trying remove himself from friends", id);
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }
}