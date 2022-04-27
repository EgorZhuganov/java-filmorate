package ru.yandex.practicum.filmorate.dto.userDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserCreateDtoTest {

    @Autowired
    private UserService service;

    @Test
    void test0ifAllFieldsAreCorrectedShouldCreateUser() {
        UserCreateDto userCreateDto1 = new UserCreateDto("ya@mail.ru", "login",
                "MyDisplayName", LocalDate.of(1998, 12, 12));

        UserReadDto userReadDto1 = service.create(userCreateDto1);

        assertEquals(userReadDto1.getEmail(), userCreateDto1.getEmail());
        assertEquals(userReadDto1.getLogin(), userCreateDto1.getLogin());
        assertEquals(userReadDto1.getName(), userCreateDto1.getName());
        assertEquals(userReadDto1.getBirthday(), userCreateDto1.getBirthday());
    }

    @Test
    void test2ifEmailHasWrongTypeShouldThrowViolationException() {
        UserCreateDto userCreateDto1 = new UserCreateDto("yailru", "myLogin",
                "MyDisplayName", LocalDate.of(1984, 12, 12));

        assertThrows(ConstraintViolationException.class, () -> service.create(userCreateDto1));
    }

    @Test
    void test3ifLoginIsBlankShouldThrowViolationException() {
        UserCreateDto userCreateDto1 = new UserCreateDto("ya@mail.ru", "",
                "MyDisplayName", LocalDate.of(1984, 12, 12));

        assertThrows(ConstraintViolationException.class, () -> service.create(userCreateDto1));
    }

    @Test
    void test4ifLoginHas2SymbolShouldThrowViolationException() {
        UserCreateDto userCreateDto1 = new UserCreateDto("ya@mail.ru", "my",
                "MyDisplayName", LocalDate.of(1984, 12, 12));

        assertThrows(ConstraintViolationException.class, () -> service.create(userCreateDto1));
    }

    @Test
    void test5ifLoginHas11SymbolShouldThrowViolationException() {
        UserCreateDto userCreateDto1 = new UserCreateDto("ya@mail.ru", "myLogin8911",
                "MyDisplayName", LocalDate.of(1984, 12, 12));

        assertThrows(ConstraintViolationException.class, () -> service.create(userCreateDto1));
    }

    @Test
    void test6ifDisplayNameIsNullShouldThrowViolationException() {
        UserCreateDto userCreateDto1 = new UserCreateDto("ya@mail.ru", "myLogin",
                null, LocalDate.of(1984, 12, 12));

        assertThrows(ConstraintViolationException.class, () -> service.create(userCreateDto1));
    }

    @Test
    void test7ifBirthdayIsDateOfFutureShouldThrowViolationException() {
        UserCreateDto userCreateDto1 = new UserCreateDto("ya@mail.ru", "myLogin",
                "MyDisplayName", LocalDate.of(2500, 12, 12));

        assertThrows(ConstraintViolationException.class, () -> service.create(userCreateDto1));
    }
}