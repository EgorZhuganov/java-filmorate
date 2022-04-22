package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


class UserCreateMapperTest {

    private final UserCreateMapper mapper = new UserCreateMapper();
    private final UserCreateDto userCreateDto1 = new UserCreateDto(1L, "myMail@mail.ru", "myLogin",
            "myDisplayName", LocalDate.of(2000,1,1));

    @Test
    void mapFrom() {
        User user = mapper.mapFrom(userCreateDto1);

        Assertions.assertEquals(userCreateDto1.getId(), user.getId());
        Assertions.assertEquals(userCreateDto1.getEmail(), user.getEmail());
        Assertions.assertEquals(userCreateDto1.getLogin(), user.getLogin());
        Assertions.assertEquals(userCreateDto1.getDisplayName(), user.getDisplayName());
        Assertions.assertEquals(userCreateDto1.getBirthday(), user.getBirthday());
    }
}