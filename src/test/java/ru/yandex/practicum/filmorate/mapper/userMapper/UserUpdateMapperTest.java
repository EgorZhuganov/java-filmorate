package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserUpdateMapperTest {

    private final UserUpdateMapper mapper = new UserUpdateMapper();
    private final User user = User
            .builder()
            .id(1L)
            .email("myMail@mail.ru")
            .login("myLogin")
            .name("myDisplayName")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();

    private final UserUpdateDto userUpdateDto = new UserUpdateDto(1L, "ya@mail.ru", "myLogin",
            "myDisplayName", LocalDate.of(1984, 12, 12));

    @Test
    void mapFrom() {
        User updatedUser = mapper.mapFrom(userUpdateDto, user);

        assertEquals(userUpdateDto.getEmail(), updatedUser.getEmail());
        assertEquals(userUpdateDto.getLogin(), updatedUser.getLogin());
        assertEquals(userUpdateDto.getName(), updatedUser.getName());
        assertEquals(userUpdateDto.getBirthday(), updatedUser.getBirthday());
    }
}