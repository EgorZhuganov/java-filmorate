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
class UserCreateDtoTest {

    @Autowired UserController controller;

    @Test
    void test0ifAllFieldsAreCorrectedShouldCreateUser(){
        UserCreateDto userCreateDto1 = new UserCreateDto(1L, "ya@mail.ru", "login",
                "MyDisplayName", LocalDate.of(1998,12,12));

        controller.create(userCreateDto1);

        String createdUserAsJson = controller.findById(userCreateDto1.getId());
        UserCreateDto userFromJson;
        try {
            userFromJson = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(createdUserAsJson, UserCreateDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(userFromJson.getId(), userCreateDto1.getId());
        Assertions.assertEquals(userFromJson.getEmail(), userCreateDto1.getEmail());
        Assertions.assertEquals(userFromJson.getLogin(), userCreateDto1.getLogin());
        Assertions.assertEquals(userFromJson.getDisplayName(), userCreateDto1.getDisplayName());
        Assertions.assertEquals(userFromJson.getBirthday(), userCreateDto1.getBirthday());
    }

    @Test
    void test1ifIdNullShouldThrowViolationException(){
        UserCreateDto userCreateDto1 = new UserCreateDto(null, "mymail@mail.ru", "myLogin",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.create(userCreateDto1));
    }

    @Test
    void test2ifEmailHasWrongTypeShouldThrowViolationException(){
        UserCreateDto userCreateDto1 = new UserCreateDto(1L, "yailru", "myLogin",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.create(userCreateDto1));
    }

    @Test
    void test3ifLoginIsBlankShouldThrowViolationException(){
        UserCreateDto userCreateDto1 = new UserCreateDto(1L, "ya@mail.ru", "",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.create(userCreateDto1));
    }

    @Test
    void test4ifLoginHas2SymbolShouldThrowViolationException(){
        UserCreateDto userCreateDto1 = new UserCreateDto(1L, "ya@mail.ru", "my",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.create(userCreateDto1));
    }

    @Test
    void test5ifLoginHas11SymbolShouldThrowViolationException(){
        UserCreateDto userCreateDto1 = new UserCreateDto(1L, "ya@mail.ru", "myLogin8911",
                "MyDisplayName", LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.create(userCreateDto1));
    }

    @Test
    void test6ifDisplayNameIsNullShouldThrowViolationException(){
        UserCreateDto userCreateDto1 = new UserCreateDto(1L, "ya@mail.ru", "myLogin",
                null, LocalDate.of(1984,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.create(userCreateDto1));
    }

    @Test
    void test7ifBirthdayIsDateOfFutureShouldThrowViolationException (){
        UserCreateDto userCreateDto1 = new UserCreateDto(1L, "ya@mail.ru", "myLogin",
                "MyDisplayName", LocalDate.of(2500,12,12));

        Assertions.assertThrows(ConstraintViolationException.class, () -> controller.create(userCreateDto1));
    }
}