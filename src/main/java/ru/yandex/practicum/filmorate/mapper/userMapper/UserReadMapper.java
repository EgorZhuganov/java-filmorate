package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserReadMapper implements UserMapper<User, UserReadDto> {

    public UserReadDto mapFrom(User object) {
        return new UserReadDto(
                object.getId(),
                object.getEmail(),
                object.getLogin(), //TODO подумать об удалении login из представления после спринта
                object.getName(),
                object.getBirthday()
        );
    }

    @Override
    public String getKey() {
        return UserReadMapper.class.getName();
    }
}
