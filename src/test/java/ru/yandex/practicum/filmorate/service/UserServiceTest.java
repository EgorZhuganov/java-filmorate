package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.memory.UserRepositoryInMemory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired UserService service;
    @Autowired UserRepositoryInMemory repository;

    private UserCreateDto userCreateDto1 = new UserCreateDto(1L, "ya@mail.ru", "login",
            "MyDisplayName", LocalDate.of(1998, 12, 12));
    UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "mymail@mail.ru", "myLogin",
            "MyDisplayName", LocalDate.of(1984, 12, 12));

    @Test
    void test1createOneUserShouldReturnFromRepositoryOneUser() {
        service.create(userCreateDto1);

        repository.findAll();

        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void test2createOneUserShouldReturnFromRepositoryUserWithEqualsFields() {
        service.create(userCreateDto1);

        User user = repository.findById(userCreateDto1.getId()).get();

        Assertions.assertEquals(user.getLogin(), userCreateDto1.getLogin());
        Assertions.assertEquals(user.getId(), userCreateDto1.getId());
        Assertions.assertEquals(user.getDisplayName(), userCreateDto1.getDisplayName());
        Assertions.assertEquals(user.getBirthday(), userCreateDto1.getBirthday());
        Assertions.assertEquals(user.getEmail(), userCreateDto1.getEmail());
    }

    @Test
    void test3updateShouldUpdateUserInRepositoryAndReturnEqualsFields() {
        service.create(userCreateDto1);

        service.update(userCreateDto1.getId(), userUpdateDto1);
        User user = repository.findById(userCreateDto1.getId()).get();

        Assertions.assertEquals(user.getEmail(), userUpdateDto1.getEmail());
        Assertions.assertEquals(user.getBirthday(), userUpdateDto1.getBirthday());
        Assertions.assertEquals(user.getLogin(), userUpdateDto1.getLogin());
        Assertions.assertEquals(user.getDisplayName(), userUpdateDto1.getDisplayName());
    }

    @Test
    void test4deleteShouldReturnOptionalEmptyAnd0UsersFromRepository() {
        service.create(userCreateDto1);

        service.delete(userCreateDto1.getId());

        Assertions.assertEquals(Optional.empty(), repository.findById(userCreateDto1.getId()));
        Assertions.assertEquals(0, repository.findAll().size());
    }

    @Test
    void test5findByIdShouldReturnUserReadDtoWithTheSameFields() {
        service.create(userCreateDto1);

        UserReadDto userReadDto1 = service.findById(userCreateDto1.getId()).get();

        Assertions.assertEquals(userReadDto1.getBirthday(), userCreateDto1.getBirthday());
        Assertions.assertEquals(userReadDto1.getLogin(), userCreateDto1.getLogin());
        Assertions.assertEquals(userReadDto1.getEmail(), userCreateDto1.getEmail());
        Assertions.assertEquals(userReadDto1.getDisplayName(), userCreateDto1.getDisplayName());
        Assertions.assertEquals(userReadDto1.getId(), userCreateDto1.getId());
    }

    @Test
    void test6findAllIfAdd3UserCreateDtoShouldReturnListWith3User() {
        UserCreateDto userCreateDto2 = new UserCreateDto(2L, "some@mail.ru", "login1",
                "MyDisplayName1", LocalDate.of(1998, 12, 12));
        UserCreateDto userCreateDto3 = new UserCreateDto(3L, "other@mail.ru", "login2",
                "MyDisplayName2", LocalDate.of(1998, 12, 12));

        service.create(userCreateDto1);
        service.create(userCreateDto2);
        service.create(userCreateDto3);

        List<UserReadDto> userReadDtoList = service.findAll();

        Assertions.assertEquals(3, userReadDtoList.size());
    }
}