package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserReadMapperTest {

    private final UserReadMapper mapper = new UserReadMapper();
    private final User user = User
            .builder()
            .id(1L)
            .email("myMail@mail.ru")
            .login("myLogin")
            .displayName("myDisplayName")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();

    @Test
    void mapFrom() {
        UserReadDto userDto = mapper.mapFrom(user);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLogin(), userDto.getLogin());
        assertEquals(user.getDisplayName(), userDto.getDisplayName());
        assertEquals(user.getBirthday(), userDto.getBirthday());
    }
}