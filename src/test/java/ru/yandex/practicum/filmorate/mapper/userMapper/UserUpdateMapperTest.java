package ru.yandex.practicum.filmorate.mapper.userMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserUpdateMapperTest {

    private final UserUpdateMapper mapper = new UserUpdateMapper();
    private final User user = User
            .builder()
            .id(1L)
            .email("myMail@mail.ru")
            .login("myLogin")
            .displayName("myDisplayName")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();

    private final UserUpdateDto userUpdateDto = new UserUpdateDto(1L, "ya@mail.ru", "myLogin",
            "myDisplayName", LocalDate.of(1984,12,12));

    @Test
    void mapFrom() {
        User updatedUser = mapper.mapFrom(userUpdateDto, user);

        Assertions.assertEquals(userUpdateDto.getId(), updatedUser.getId());
        Assertions.assertEquals(userUpdateDto.getEmail(), updatedUser.getEmail());
        Assertions.assertEquals(userUpdateDto.getLogin(), updatedUser.getLogin());
        Assertions.assertEquals(userUpdateDto.getDisplayName(), updatedUser.getDisplayName());
        Assertions.assertEquals(userUpdateDto.getBirthday(), updatedUser.getBirthday());
    }
}