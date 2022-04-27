package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    UserService service;
    @Autowired
    AbstractRepository<Long, User> repository;

    private UserCreateDto userCreateDto1 = new UserCreateDto("ya@mail.ru", "login",
            "MyDisplayName", LocalDate.of(1998, 12, 12));
    private UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "mymail@mail.ru", "myLogin",
            "MyDisplayName", LocalDate.of(1984, 12, 12));

    @Test
    void test1createOneUserShouldReturnFromRepositoryOneUser() {
        service.create(userCreateDto1);

        repository.findAll();

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void test2createOneUserShouldReturnFromRepositoryUserWithEqualsFields() {
        UserReadDto userReadDto1 = service.create(userCreateDto1);

        User user = repository.findById(userReadDto1.getId()).get();

        assertEquals(user.getLogin(), userCreateDto1.getLogin());
        assertEquals(user.getName(), userCreateDto1.getName());
        assertEquals(user.getBirthday(), userCreateDto1.getBirthday());
        assertEquals(user.getEmail(), userCreateDto1.getEmail());
    }

    @Test
    void test3updateShouldUpdateUserInRepositoryAndReturnEqualsFields() {
        UserReadDto userReadDto = service.create(userCreateDto1);

        service.update(userReadDto.getId(), userUpdateDto1);
        User user = repository.findById(userReadDto.getId()).get();

        assertEquals(user.getEmail(), userUpdateDto1.getEmail());
        assertEquals(user.getBirthday(), userUpdateDto1.getBirthday());
        assertEquals(user.getLogin(), userUpdateDto1.getLogin());
        assertEquals(user.getName(), userUpdateDto1.getName());
    }

    @Test
    void test4deleteShouldReturnOptionalEmptyAnd0UsersFromRepository() {
        UserReadDto userReadDto = service.create(userCreateDto1);

        service.delete(userReadDto.getId());

        assertEquals(Optional.empty(), repository.findById(userReadDto.getId()));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void test5findByIdShouldReturnUserReadDtoWithTheSameFields() {
        UserReadDto userReadDto = service.create(userCreateDto1);

        UserReadDto userReadDto1 = service.findById(userReadDto.getId()).get();

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

        UserReadDto userReadDto1 = service.create(userCreateDto1);
        UserReadDto userReadDto2 = service.create(userCreateDto2);
        UserReadDto userReadDto3 = service.create(userCreateDto3);

        List<UserReadDto> userReadDtoList = service.findAll();

        assertEquals(3, userReadDtoList.size());
    }
}