package ru.yandex.practicum.filmorate.dto.userDto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

@SpringBootTest
class UserUpdateDtoTest {

    @Autowired UserController controller;

    @Test
    void test0ifAllFieldsAreCorrectedShouldCreateUser(){
        UserCreateDto userCreateDto = new UserCreateDto(1L, "ya@mail.ru", "login",
                "MyDisplayName", LocalDate.of(1998,12,12));
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "another@mail.ru", "otherLogin",
                "MyDisplayName", LocalDate.of(1998,12,12));

        controller.create(userCreateDto);
        controller.update(userUpdateDto1);

        String createdUserAsJson = controller.findById(userUpdateDto1.getId());
        UserUpdateDto userFromJson;
        try {
            userFromJson = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(createdUserAsJson, UserUpdateDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(userFromJson.getId(), userUpdateDto1.getId());
        Assertions.assertEquals(userFromJson.getEmail(), userUpdateDto1.getEmail());
        Assertions.assertEquals(userFromJson.getLogin(), userUpdateDto1.getLogin());
        Assertions.assertEquals(userFromJson.getDisplayName(), userUpdateDto1.getDisplayName());
        Assertions.assertEquals(userFromJson.getBirthday(), userUpdateDto1.getBirthday());
    }

    @Test
    void test1ifIdNullShouldThrowViolationException(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(null, "mymail@mail.ru", "myLogin",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.update(userUpdateDto1));
    }

    @Test
    void test2ifEmailHasWrongTypeShouldThrowViolationException(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "yailru", "myLogin",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.update(userUpdateDto1));
    }

    @Test
    void test3ifLoginIsBlankShouldThrowViolationException(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "ya@mail.ru", "",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.update(userUpdateDto1));
    }

    @Test
    void test4ifLoginHas2SymbolShouldThrowViolationException(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "ya@mail.ru", "my",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.update(userUpdateDto1));

    }

    @Test
    void test5ifLoginHas11SymbolShouldThrowViolationException(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "ya@mail.ru", "myLogin8911",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.update(userUpdateDto1));
    }

    @Test
    void test6ifDisplayNameIsNullShouldThrowViolationException(){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "ya@mail.ru", "myLogin",
                null, LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.update(userUpdateDto1));
    }

    @Test
    void test7ifBirthdayIsDateOfFutureShouldThrowViolationException (){
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "ya@mail.ru", "myLogin",
                "MyDisplayName", LocalDate.of(2500,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.update(userUpdateDto1));
    }
}