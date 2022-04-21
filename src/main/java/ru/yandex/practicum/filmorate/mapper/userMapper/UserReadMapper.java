package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserReadMapper implements UserMapper<User, UserReadDto> {

    @Override
    public UserReadDto mapFrom(User object) {
        return new UserReadDto(
                object.getId(),
                object.getEmail(),
                object.getLogin(), //TODO подумать об удалении login из представления после спринта
                object.getDisplayName(),
                object.getBirthday()
        );
    }

    @Override
    public String getKey() {
        return UserReadMapper.class.getName();
    }
}
