package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.mapper.userMapper.UserMapper;
import ru.yandex.practicum.filmorate.mapper.userMapper.UserReadMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;

import java.util.*;
import java.util.function.Function;

import static java.util.Optional.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class FriendService {

    private final AbstractRepository<Long, User> repository;
    private final Map<String, UserMapper<?, ?>> mapper;

    @Autowired
    public FriendService(AbstractRepository<Long, User> repository, List<UserMapper<?, ?>> mappers) {
        this.repository = repository;
        this.mapper = mappers.stream().collect(toMap(UserMapper::getKey, Function.identity()));
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
        //.orElseThrow();
    }

    public Optional<List<UserReadDto>> findAllCommonFriends(Long userId, Long otherUserId) {
        UserReadMapper userReadMapper = (UserReadMapper) mapper.get(UserReadMapper.class.getName());
        List<UserReadDto> commonFriends = new ArrayList<>();
        var maybeUser = repository.findById(userId);
        var maybeOtherUser = repository.findById(otherUserId);
        if (maybeUser.isPresent() && maybeOtherUser.isPresent()) {
            UserReadDto userDto = userReadMapper.mapFrom(maybeUser.get());
            UserReadDto otherUser = userReadMapper.mapFrom(maybeOtherUser.get());
            userDto.getFriends().retainAll(otherUser.getFriends());
            commonFriends = userDto
                    .getFriends()
                    .stream()
                    .map(repository::findById)
                    .filter(Optional::isPresent)
                    .map(user -> userReadMapper.mapFrom(user.get()))
                    .collect(toList());
        } else {
            return empty();
        }
        return of(commonFriends);
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