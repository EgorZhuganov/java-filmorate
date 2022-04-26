package ru.yandex.practicum.filmorate.dto.userDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserUpdateDtoTest {

    @Autowired UserService service;
    private UserCreateDto userCreateDto = new UserCreateDto("ya@mail.ru", "login",
            "MyDisplayName", LocalDate.of(1998,12,12));


    @Test
    void test0ifAllFieldsAreCorrectedShouldUpdateUser(){
        UserReadDto userReadDto1 = service.create(userCreateDto);

        UserUpdateDto userUpdateDto1 = new UserUpdateDto(userReadDto1.getId(), "another@mail.ru", "otherLogin",
                "MyDisplayName", LocalDate.of(1998,12,12));
        service.update(userReadDto1.getId(), userUpdateDto1);
        UserReadDto updatedUserReadDto = service.findById(userReadDto1.getId()).get();

        assertEquals(updatedUserReadDto.getEmail(), userUpdateDto1.getEmail());
        assertEquals(updatedUserReadDto.getLogin(), userUpdateDto1.getLogin());
        assertEquals(updatedUserReadDto.getName(), userUpdateDto1.getName());
        assertEquals(updatedUserReadDto.getBirthday(), userUpdateDto1.getBirthday());
    }

    @Test
    void test1ifIdNullShouldThrowViolationException(){
        UserReadDto userReadDto1 = service.create(userCreateDto);

        UserUpdateDto userUpdateDto1 = new UserUpdateDto(null, "mymail@mail.ru", "myLogin",
                "MyDisplayName", LocalDate.of(1984,12,12));

        assertThrows(ConstraintViolationException.class, () -> service.update(userReadDto1.getId(), userUpdateDto1));
    }

    @Test
    void test2ifEmailHasWrongTypeShouldThrowViolationException(){
        UserReadDto userReadDto1 = service.create(userCreateDto);

        UserUpdateDto userUpdateDto1 = new UserUpdateDto(userReadDto1.getId(), "yailru", "myLogin",
                "MyDisplayName", LocalDate.of(1984,12,12));

        assertThrows(ConstraintViolationException.class, () -> service.update(userReadDto1.getId(), userUpdateDto1));
    }

    @Test
    void test3ifLoginIsBlankShouldThrowViolationException(){
        UserReadDto userReadDto1 = service.create(userCreateDto);

        UserUpdateDto userUpdateDto1 = new UserUpdateDto(userReadDto1.getId(), "ya@mail.ru", "",
                "MyDisplayName", LocalDate.of(1984,12,12));

        assertThrows(ConstraintViolationException.class, () -> service.update(userReadDto1.getId(), userUpdateDto1));
    }

    @Test
    void test4ifLoginHas2SymbolShouldThrowViolationException(){
        UserReadDto userReadDto1 = service.create(userCreateDto);

        UserUpdateDto userUpdateDto1 = new UserUpdateDto(userReadDto1.getId(), "ya@mail.ru", "my",
                "MyDisplayName", LocalDate.of(1984,12,12));

        assertThrows(ConstraintViolationException.class, () -> service.update(userReadDto1.getId(), userUpdateDto1));

    }

    @Test
    void test5ifLoginHas11SymbolShouldThrowViolationException(){
        UserReadDto userReadDto1 = service.create(userCreateDto);

        UserUpdateDto userUpdateDto1 = new UserUpdateDto(userReadDto1.getId(), "ya@mail.ru", "myLogin8911",
                "MyDisplayName", LocalDate.of(1984,12,12));

        assertThrows(ConstraintViolationException.class, () -> service.update(userReadDto1.getId(), userUpdateDto1));
    }

    @Test
    void test6ifDisplayNameIsNullShouldThrowViolationException(){
        UserReadDto userReadDto1 = service.create(userCreateDto);

        UserUpdateDto userUpdateDto1 = new UserUpdateDto(userReadDto1.getId(), "ya@mail.ru", "myLogin",
                null, LocalDate.of(1984,12,12));

        assertThrows(ConstraintViolationException.class, () -> service.update(userReadDto1.getId(), userUpdateDto1));
    }

    @Test
    void test7ifBirthdayIsDateOfFutureShouldThrowViolationException (){
        UserReadDto userReadDto1 = service.create(userCreateDto);

        UserUpdateDto userUpdateDto1 = new UserUpdateDto(userReadDto1.getId(), "ya@mail.ru", "myLogin",
                "MyDisplayName", LocalDate.of(2500,12,12));

        assertThrows(ConstraintViolationException.class, () -> service.update(userReadDto1.getId(), userUpdateDto1));
    }
}