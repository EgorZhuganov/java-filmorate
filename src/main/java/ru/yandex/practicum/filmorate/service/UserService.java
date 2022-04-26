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
                .map(user -> {
                    log.info("user {} was updated", user.getEmail());
                    return repository.update(user);
                })
                .map(userReadMapper::mapFrom);
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }
}
