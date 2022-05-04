package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;

@Component
public class UserCreateMapper implements UserMapper<UserCreateDto, User> {

    public User mapFrom(UserCreateDto object) {
        String displayName = object.getName();
        if (object.getName().isBlank())
            displayName = object.getLogin();

        return User.builder()
                .email(object.getEmail())
                .login(object.getLogin())
                .name(displayName)
                .birthday(object.getBirthday())
                .friends(new HashSet<>())
                .build();
    }

    @Override
    public String getKey() {
        return UserCreateMapper.class.getName();
    }
}
