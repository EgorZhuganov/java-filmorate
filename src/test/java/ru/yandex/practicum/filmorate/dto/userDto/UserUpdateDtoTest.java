package ru.yandex.practicum.filmorate.dto.userDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

@SpringBootTest
class UserUpdateDtoTest {

    @Autowired UserService service;
    private UserCreateDto userCreateDto = new UserCreateDto(1L, "ya@mail.ru", "login",
            "MyDisplayName", LocalDate.of(1998,12,12));


    @Test
    void test0ifAllFieldsAreCorrectedShouldCreateUser(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "another@mail.ru", "otherLogin",
                "MyDisplayName", LocalDate.of(1998,12,12));

        service.create(userCreateDto);
        service.update(userCreateDto.getId(), userUpdateDto1);

        UserReadDto userReadDto1 = service.findById(userUpdateDto1.getId()).get();

        Assertions.assertEquals(userReadDto1.getId(), userUpdateDto1.getId());
        Assertions.assertEquals(userReadDto1.getEmail(), userUpdateDto1.getEmail());
        Assertions.assertEquals(userReadDto1.getLogin(), userUpdateDto1.getLogin());
        Assertions.assertEquals(userReadDto1.getDisplayName(), userUpdateDto1.getDisplayName());
        Assertions.assertEquals(userReadDto1.getBirthday(), userUpdateDto1.getBirthday());
    }

    @Test
    void test1ifIdNullShouldThrowViolationException(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(null, "mymail@mail.ru", "myLogin",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.update(userCreateDto.getId(), userUpdateDto1));
    }

    @Test
    void test2ifEmailHasWrongTypeShouldThrowViolationException(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "yailru", "myLogin",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.update(userCreateDto.getId(), userUpdateDto1));
    }

    @Test
    void test3ifLoginIsBlankShouldThrowViolationException(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "ya@mail.ru", "",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.update(userCreateDto.getId(), userUpdateDto1));
    }

    @Test
    void test4ifLoginHas2SymbolShouldThrowViolationException(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "ya@mail.ru", "my",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.update(userCreateDto.getId(), userUpdateDto1));

    }

    @Test
    void test5ifLoginHas11SymbolShouldThrowViolationException(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "ya@mail.ru", "myLogin8911",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.update(userCreateDto.getId(), userUpdateDto1));
    }

    @Test
    void test6ifDisplayNameIsNullShouldThrowViolationException(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "ya@mail.ru", "myLogin",
                null, LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.update(userCreateDto.getId(), userUpdateDto1));
    }

    @Test
    void test7ifBirthdayIsDateOfFutureShouldThrowViolationException (){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "ya@mail.ru", "myLogin",
                "MyDisplayName", LocalDate.of(2500,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.update(userCreateDto.getId(), userUpdateDto1));
    }
}