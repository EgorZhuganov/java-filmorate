package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserUpdateMapper implements UserMapper<UserUpdateDto, User> {

    @Override
    public User mapFrom(UserUpdateDto object) {
        String displayName = object.getDisplayName();
        if (object.getDisplayName().isBlank())
            displayName = object.getLogin();

        return User.builder()
                .id(object.getId())
                .email(object.getEmail())
                .login(object.getLogin())
                .displayName(displayName)
                .birthday(object.getBirthday())
                .build();
    }

    @Override
    public User mapFrom(UserUpdateDto fromObject, User toObject) {
        return mapFrom(fromObject);
    }

    @Override
    public String getKey() {
        return UserUpdateMapper.class.getName();
    }
}

