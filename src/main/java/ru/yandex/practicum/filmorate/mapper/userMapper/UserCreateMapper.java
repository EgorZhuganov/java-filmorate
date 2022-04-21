package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserCreateMapper implements UserMapper<UserCreateDto, User> {

    @Override
    public User mapFrom(UserCreateDto object) {
        return User.builder()
                .id(object.getId()) //TODO удалить id при создании, поле должно автогенерироваться
                .email(object.getEmail())
                .login(object.getLogin())
                .displayName(object.getDisplayName())
                .birthday(object.getBirthday())
                .build();
    }


    @Override
    public String getKey() {
        return UserCreateMapper.class.getName();
    }
}