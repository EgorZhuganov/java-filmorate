package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTestFriendsTest {

    @Autowired
    private UserService userService;
    private UserCreateDto userCreateDto1 = new UserCreateDto("ya1@mail.ru", "login1",
            "MyDisplayName1", LocalDate.of(1998, 12, 12));
    private UserCreateDto userCreateDto2 = new UserCreateDto("ya2@mail.ru", "login2",
            "MyDisplayName2", LocalDate.of(1998, 12, 12));
    private UserCreateDto userCreateDto3 = new UserCreateDto("ya3@mail.ru", "login3",
            "MyDisplayName3", LocalDate.of(1998, 12, 12));
    private UserCreateDto userCreateDto4 = new UserCreateDto("ya4@mail.ru", "login4",
            "MyDisplayName4", LocalDate.of(1998, 12, 12));

    @Test
    void test1addToFriendsIfAllRightShouldReturnSizeFriendInBothUsersAs1() {
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        UserReadDto userReadDto3 = userService.create(userCreateDto3);

        userReadDto2 = userService.addToFriends(userReadDto2.getId(), userReadDto3.getId()).orElseThrow();

        assertEquals(1, userReadDto2.getFriends().size());
        assertEquals(1, userService.findById(userReadDto2.getId()).get().getFriends().size());
        assertEquals(1, userService.findById(userReadDto3.getId()).get().getFriends().size());
    }

    @Test
    void test2addToFriendsIfUserNotExistInRepositoryShouldReturnEmptyOptional() {
        UserReadDto userReadDto3 = userService.create(userCreateDto3);

        Optional<UserReadDto> maybeUser = userService.addToFriends(100500L, userReadDto3.getId());

        assertEquals(Optional.empty(), maybeUser);
    }

    @Test
    void test3addToFriendsIfFriendNotExistInRepositoryShouldReturnEmptyOptional() {
        UserReadDto userReadDto2 = userService.create(userCreateDto2);

        Optional<UserReadDto> maybeUser1 = userService.addToFriends(userReadDto2.getId(), 100500L);
        Optional<UserReadDto> maybeUser2 = userService.addToFriends(100500L, userReadDto2.getId());

        assertEquals(Optional.empty(), maybeUser1);
        assertEquals(Optional.empty(), maybeUser2);
    }

    @Test
    void test4addToFriendsIfTryAddToUserToHimselfShouldReturnUnsupportedOperationException() {
        UserReadDto userReadDto2 = userService.create(userCreateDto2);

        assertThrows(UnsupportedOperationException.class,
                () -> userService.addToFriends(userReadDto2.getId(), userReadDto2.getId()));
    }

    @Test
    void test5findAllFriendsIfAddTwoFriendToUserShouldReturnFriendsSize2() {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        UserReadDto userReadDto3 = userService.create(userCreateDto3);

        userService.addToFriends(userReadDto1.getId(), userReadDto2.getId()).orElseThrow();
        userService.addToFriends(userReadDto1.getId(), userReadDto3.getId()).orElseThrow();

        assertEquals(2, userService.findAllFriends(userReadDto1.getId()).get().size());
    }

    @Test
    void test6findAllFriendsIfTryToFindFriendsInNotExistUserShouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), userService.findAllFriends(100500L));
    }

    @Test
    void test7findAllCommonFriendsIfUseThisMethodShouldReturnFromUserAllFriendsWhichWereAdded() {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        UserReadDto userReadDto3 = userService.create(userCreateDto3);
        UserReadDto userReadDto4 = userService.create(userCreateDto4);

        userService.addToFriends(userReadDto1.getId(), userReadDto2.getId()).orElseThrow();
        userService.addToFriends(userReadDto1.getId(), userReadDto3.getId()).orElseThrow();
        userService.addToFriends(userReadDto1.getId(), userReadDto4.getId()).orElseThrow();
        userService.addToFriends(userReadDto2.getId(), userReadDto3.getId()).orElseThrow();
        userService.addToFriends(userReadDto2.getId(), userReadDto4.getId()).orElseThrow();

        userService.findAllCommonFriends(userReadDto1.getId(), userReadDto2.getId()).orElseThrow();

        assertEquals(3, userService.findById(userReadDto1.getId()).orElseThrow().getFriends().size());
    }

    @Test
    void test8findAllCommonFriendsIfUsersHaveTwoCommonFriendsShouldReturnTwoCommonFriendsAsListUserReadDto() {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        UserReadDto userReadDto3 = userService.create(userCreateDto3);
        UserReadDto userReadDto4 = userService.create(userCreateDto4);

        userService.addToFriends(userReadDto1.getId(), userReadDto2.getId()).orElseThrow();
        userService.addToFriends(userReadDto1.getId(), userReadDto3.getId()).orElseThrow();
        userService.addToFriends(userReadDto1.getId(), userReadDto4.getId()).orElseThrow();
        userService.addToFriends(userReadDto2.getId(), userReadDto3.getId()).orElseThrow();
        userService.addToFriends(userReadDto2.getId(), userReadDto4.getId()).orElseThrow();

        List<UserReadDto> commonFriends = userService.findAllCommonFriends(userReadDto1.getId(), userReadDto2.getId()).orElseThrow();

        assertEquals(2, commonFriends.size());
    }

    @Test
    void test9findAllCommonFriendsShouldReturnEmptyOptionalIfOneOfUsersNotExist() {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        var commonFriends1 = userService.findAllCommonFriends(100500L, userReadDto1.getId());
        var commonFriends2 = userService.findAllCommonFriends(userReadDto1.getId(), 100500L);

        assertEquals(Optional.empty(), commonFriends1);
        assertEquals(Optional.empty(), commonFriends2);
    }

    @Test
    void test10removeFromFriendsIfUserAddedTwoFriendsShouldReturnUserReadDtoWithOneFriend() {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        UserReadDto userReadDto3 = userService.create(userCreateDto3);

        userService.addToFriends(userReadDto1.getId(), userReadDto2.getId()).orElseThrow();
        userService.addToFriends(userReadDto1.getId(), userReadDto3.getId()).orElseThrow();

        UserReadDto updatedUserReadDto = userService.removeFromFriends(userReadDto1.getId(), userReadDto2.getId()).get();

        assertEquals(1, updatedUserReadDto.getFriends().size());
    }

    @Test
    void test11removeFromFriendsIfUsersIdsAreCorrectShouldReturnListFriendsWithoutRemovedUser() {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        UserReadDto userReadDto3 = userService.create(userCreateDto3);

        userService.addToFriends(userReadDto1.getId(), userReadDto2.getId()).orElseThrow();
        userService.addToFriends(userReadDto1.getId(), userReadDto3.getId()).orElseThrow();

        userService.removeFromFriends(userReadDto1.getId(), userReadDto2.getId());

        assertEquals(1, userService.findById(userReadDto1.getId()).orElseThrow().getFriends().size());
        assertFalse(userService.findById(userReadDto1.getId()).orElseThrow().getFriends().contains(userReadDto2.getId()));
    }

    @Test
    void test12removeFromFriendsIfUserNotExistShouldReturnFalseEmptyOptional() {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        UserReadDto userReadDto3 = userService.create(userCreateDto3);

        userService.addToFriends(userReadDto1.getId(), userReadDto2.getId()).orElseThrow();
        userService.addToFriends(userReadDto1.getId(), userReadDto3.getId()).orElseThrow();

        assertEquals(Optional.empty(), userService.removeFromFriends(100500L, userReadDto2.getId()));
        assertEquals(Optional.empty(), userService.removeFromFriends(userReadDto1.getId(), 100500L));
    }
}