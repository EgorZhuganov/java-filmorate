package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;
import ru.yandex.practicum.filmorate.mapper.userMapper.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.*;

@Service
@Validated
@Slf4j
public class UserService {

    private final AbstractRepository<Long, User> repository;
    private final Map<String, UserMapper<?, ?>> mapper;

    @Autowired
    public UserService(AbstractRepository<Long, User> repository, List<UserMapper<?, ?>> mappers) {
        this.repository = repository;
        this.mapper = mappers.stream().collect(toMap(UserMapper::getKey, Function.identity()));
    }

    public List<UserReadDto> findAll() {
        var userReadMapper = (UserReadMapper) mapper.get(UserReadMapper.class.getName());
        return repository.findAll()
                .stream()
                .map(userReadMapper::mapFrom)
                .collect(toList());
    }

    public Optional<UserReadDto> findById(Long id) {
        var userReadMapper = (UserReadMapper) mapper.get(UserReadMapper.class.getName());
        return repository.findById(id).map(userReadMapper::mapFrom);
    }

    public UserReadDto create(@Valid UserCreateDto userCreateDto) throws ConstraintViolationException {
        var userCreateMapper = (UserCreateMapper) mapper.get(UserCreateMapper.class.getName());
        var userReadMapper = (UserReadMapper) mapper.get(UserReadMapper.class.getName());
        return Optional.of(userCreateDto)
                .map(userCreateMapper::mapFrom)
                .map(user -> {
                    log.info("user {} was registered", user.getEmail());
                    return repository.insert(user);
                })
                .map(userReadMapper::mapFrom)
                .orElseThrow();
    }

    public Optional<UserReadDto> update(Long id, @Valid UserUpdateDto userUpdateDto) throws ConstraintViolationException {
        var userReadMapper = (UserReadMapper) mapper.get(UserReadMapper.class.getName());
        var userUpdateMapper = (UserUpdateMapper) mapper.get(UserUpdateMapper.class.getName());
        return repository.findById(id)
                .map(userModel -> userUpdateMapper.mapFrom(userUpdateDto, userModel))
                .map(updatedUser -> {
                    log.info("user with id {} was updated", updatedUser.getId());
                    return repository.update(updatedUser);
                })
                .map(userReadMapper::mapFrom);
    }

    public boolean delete(Long id) {
        var maybeUser = repository.findById(id);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            for (Long friendId : user.getFriends()) {
                repository.findById(friendId).map(friend -> removeFromFriends(id, friendId));
            }
            return repository.delete(id);
        }
        return false;
    }

    public Optional<UserReadDto> addToFriends(Long userId, Long friendId) {
        UserReadMapper userReadMapper = (UserReadMapper) mapper.get(UserReadMapper.class.getName());
        var maybeUser = repository.findById(userId);
        var maybeFriend = repository.findById(friendId);
        UserReadDto userReadDto = null;
        if (maybeUser.isPresent() && maybeFriend.isPresent()) {
            User user = maybeUser.get();
            User friend = maybeFriend.get();
            user.getFriends().add(friend.getId());
            friend.getFriends().add(user.getId());
            repository.update(user);
            repository.update(friend);
            userReadDto = userReadMapper.mapFrom(user);
        }
        return ofNullable(userReadDto);
    }

    public Optional<List<UserReadDto>> findAllFriends(Long userId) {
        UserReadMapper userReadMapper = (UserReadMapper) mapper.get(UserReadMapper.class.getName());
        return repository.findById(userId)
                .map(user -> user
                        .getFriends()
                        .stream()
                        .map(repository::findById)
                        .filter(Optional::isPresent)
                        .map(friend -> userReadMapper.mapFrom(friend.get()))
                        .collect(toList())
                );
    }

    public Optional<List<UserReadDto>> findAllCommonFriends(Long userId, Long otherUserId) {
        UserReadMapper userReadMapper = (UserReadMapper) mapper.get(UserReadMapper.class.getName());
        List<UserReadDto> commonFriends = null;
        var maybeUser = repository.findById(userId);
        var maybeOtherUser = repository.findById(otherUserId);
        if (maybeUser.isPresent() && maybeOtherUser.isPresent()) {
            Set<Long> userFriends = new HashSet<>(maybeUser.get().getFriends());
            Set<Long> otherUserFriends = new HashSet<>(maybeOtherUser.get().getFriends());
            userFriends.retainAll(otherUserFriends);
            commonFriends = userFriends
                    .stream()
                    .map(repository::findById)
                    .filter(Optional::isPresent)
                    .map(commonFriend -> userReadMapper.mapFrom(commonFriend.get()))
                    .collect(toList());
        }
        return ofNullable(commonFriends);
    }

    public boolean removeFromFriends(Long userId, Long friendId) {
        var maybeUser = repository.findById(userId);
        var maybeFriend = repository.findById(friendId);
        if (maybeUser.isPresent() && maybeFriend.isPresent()) {
            User user = maybeUser.get();
            User friend = maybeFriend.get();
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
            repository.update(user);
            repository.update(friend);
            return true;
        }
        return false;
    }
}
