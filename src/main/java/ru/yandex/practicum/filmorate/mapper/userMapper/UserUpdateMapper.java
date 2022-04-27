package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserUpdateMapper implements UserMapper<UserUpdateDto, User> {

    public User mapFrom(UserUpdateDto fromObject, User toObject) {
        String name = fromObject.getName();
        if (fromObject.getName().isBlank())
            name = fromObject.getLogin();

        toObject.setBirthday(fromObject.getBirthday());
        toObject.setEmail(fromObject.getEmail());
        toObject.setLogin(fromObject.getLogin());
        toObject.setName(name);
        return toObject;
    }

    @Override
    public String getKey() {
        return UserUpdateMapper.class.getName();
    }
}

