package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.annotation.IntegrationTest;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private FriendService friendService;
    @Autowired
    @Qualifier("userDao")
    private AbstractRepository<Long, User> repository;

    private UserCreateDto userCreateDto1 = new UserCreateDto("ya@mail.ru", "login",
            "MyDisplayName", LocalDate.of(1998, 12, 12));
    private UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "mymail@mail.ru", "myLogin",
            "MyDisplayName", LocalDate.of(1984, 12, 12));

    @Test
    void test1createOneUserShouldReturnFromRepositoryOneUser() {
        userService.create(userCreateDto1);

        repository.findAll();

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void test2createOneUserShouldReturnFromRepositoryUserWithEqualsFields() {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);

        User user = repository.findById(userReadDto1.getId()).get();

        assertEquals(user.getLogin(), userCreateDto1.getLogin());
        assertEquals(user.getName(), userCreateDto1.getName());
        assertEquals(user.getBirthday(), userCreateDto1.getBirthday());
        assertEquals(user.getEmail(), userCreateDto1.getEmail());
    }

    @Test
    void test3updateShouldUpdateUserInRepositoryAndReturnEqualsFields() {
        UserReadDto userReadDto = userService.create(userCreateDto1);

        userService.update(userReadDto.getId(), userUpdateDto1);
        User user = repository.findById(userReadDto.getId()).get();

        assertEquals(user.getEmail(), userUpdateDto1.getEmail());
        assertEquals(user.getBirthday(), userUpdateDto1.getBirthday());
        assertEquals(user.getLogin(), userUpdateDto1.getLogin());
        assertEquals(user.getName(), userUpdateDto1.getName());
    }

    @Test
    void test4deleteShouldReturnOptionalEmptyAnd0UsersFromRepository() {
        UserReadDto userReadDto = userService.create(userCreateDto1);

        userService.delete(userReadDto.getId());

        assertEquals(Optional.empty(), repository.findById(userReadDto.getId()));
    }

    @Test
    void test5findByIdShouldReturnUserReadDtoWithTheSameFields() {
        UserReadDto userReadDto = userService.create(userCreateDto1);

        UserReadDto userReadDto1 = userService.findById(userReadDto.getId()).get();

        assertEquals(userReadDto1.getBirthday(), userCreateDto1.getBirthday());
        assertEquals(userReadDto1.getLogin(), userCreateDto1.getLogin());
        assertEquals(userReadDto1.getEmail(), userCreateDto1.getEmail());
        assertEquals(userReadDto1.getName(), userCreateDto1.getName());
    }

    @Test
    void test6findAllIfAdd3UserCreateDtoShouldReturnListWith3User() {
        UserCreateDto userCreateDto2 = new UserCreateDto("some@mail.ru", "login1",
                "MyDisplayName1", LocalDate.of(1998, 12, 12));
        UserCreateDto userCreateDto3 = new UserCreateDto("other@mail.ru", "login2",
                "MyDisplayName2", LocalDate.of(1998, 12, 12));

        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        UserReadDto userReadDto3 = userService.create(userCreateDto3);

        List<UserReadDto> userReadDtoList = userService.findAll();

        assertEquals(3, userReadDtoList.size());
    }

    @Test
    void test7deleteShouldBreakConnectBetweenTwoUsers() {
        UserCreateDto userCreateDto1 = new UserCreateDto("some@mail.ru", "login1",
                "MyDisplayName1", LocalDate.of(1998, 12, 12));
        UserCreateDto userCreateDto2 = new UserCreateDto("other@mail.ru", "login2",
                "MyDisplayName2", LocalDate.of(1998, 12, 12));
        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        friendService.addToFriends(userReadDto1.getId(), userReadDto2.getId());

        assertEquals(1, userService.findById(userReadDto1.getId()).get().getFriends().size());

        userService.delete(userReadDto2.getId());

        assertEquals(0, userService.findById(userReadDto1.getId()).get().getFriends().size());
    }
}