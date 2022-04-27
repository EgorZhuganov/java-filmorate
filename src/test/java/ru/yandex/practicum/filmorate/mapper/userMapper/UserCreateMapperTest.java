package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


class UserCreateMapperTest {

    private final UserCreateMapper mapper = new UserCreateMapper();
    private final UserCreateDto userCreateDto1 = new UserCreateDto("myMail@mail.ru", "myLogin",
            "myDisplayName", LocalDate.of(2000,1,1));

    @Test
    void mapFrom() {
        User user = mapper.mapFrom(userCreateDto1);

        assertEquals(userCreateDto1.getEmail(), user.getEmail());
        assertEquals(userCreateDto1.getLogin(), user.getLogin());
        assertEquals(userCreateDto1.getName(), user.getName());
        assertEquals(userCreateDto1.getBirthday(), user.getBirthday());
    }
}