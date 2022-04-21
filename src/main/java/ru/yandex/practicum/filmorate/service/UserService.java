package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;
import ru.yandex.practicum.filmorate.mapper.userMapper.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Service
//@Transactional(readOnly = true)
public class UserService {

    private final AbstractRepository<Long, User> userRepository;
    private final Map<String, UserMapper<?, ?>> mapper;

    @Autowired
    public UserService(AbstractRepository<Long, User> userRepository, List<UserMapper<?, ?>> mappers) {
        this.userRepository = userRepository;
        this.mapper = mappers.stream().collect(toMap(UserMapper::getKey, Function.identity()));
    }

    public List<UserReadDto> findAll() {
        var userReadMapper = (UserReadMapper) mapper.get(UserReadMapper.class.getName());
        return userRepository.findAll()
                .stream()
                .map(userReadMapper::mapFrom)
                .collect(toList());
    }

    public Optional<UserReadDto> findById(Long id) {
        var userReadMapper = (UserReadMapper) mapper.get(UserReadMapper.class.getName());
        return userRepository.findById(id).map(userReadMapper::mapFrom);
    }

    //@Transactional
    public UserReadDto create(UserCreateDto userCreateDto) {
        var userCreateMapper = (UserCreateMapper) mapper.get(UserCreateMapper.class.getName());
        var userReadMapper = (UserReadMapper) mapper.get(UserReadMapper.class.getName());
        return Optional.of(userCreateDto)
                .map(userCreateMapper::mapFrom)
                .map(userRepository::save)
                .map(userReadMapper::mapFrom)
                .orElseThrow();
    }

    //@Transactional
    public Optional<UserReadDto> update(Long id, UserUpdateDto userUpdateDto) {
        var userReadMapper = (UserReadMapper) mapper.get(UserReadMapper.class.getName());
        var userUpdateMapper = (UserUpdateMapper) mapper.get(UserUpdateMapper.class.getName());
        return userRepository.findById(id)
                .map(userModel -> userUpdateMapper.mapFrom(userUpdateDto, userModel))
                .map(userRepository::update)
                .map(userReadMapper::mapFrom);
    }

    //@Transactional
    public boolean delete(Long id) {
        return userRepository.delete(id);
    }
}
