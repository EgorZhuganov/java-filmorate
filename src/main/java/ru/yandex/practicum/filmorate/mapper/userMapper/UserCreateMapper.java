package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserCreateMapper implements UserMapper<UserCreateDto, User> {

    @Override
    public User mapFrom(UserCreateDto object) {
        String displayName = object.getDisplayName();
        if (object.getDisplayName().isBlank())
            displayName = object.getLogin();

        return User.builder()
                .email(object.getEmail())
                .login(object.getLogin())
                .displayName(displayName)
                .birthday(object.getBirthday())
                .build();
    }

    @Override
    public String getKey() {
        return UserCreateMapper.class.getName();
    }
}
