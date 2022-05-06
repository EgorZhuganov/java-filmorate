package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.model.User;

import static java.util.Set.copyOf;

@Component
public class UserReadMapper implements UserMapper<User, UserReadDto> {

    public UserReadDto mapFrom(User object) {
        return new UserReadDto(
                object.getId(),
                object.getEmail(),
                object.getLogin(),
                object.getName(),
                object.getBirthday(),
                copyOf(object.getFriends())
        );
    }

    @Override
    public String getKey() {
        return UserReadMapper.class.getName();
    }
}
