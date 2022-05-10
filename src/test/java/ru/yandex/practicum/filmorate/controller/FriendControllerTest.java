package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FriendControllerTest {

    FriendControllerTest() throws JsonProcessingException {
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    private final URI userUrl = URI.create("http://localhost:8080/users/");
    private static final Long WRONG_ID = 100500L;
    private final UserCreateDto userCreateDto1 = new UserCreateDto("ya1@mail.ru", "login1",
            "MyDisplayName1", LocalDate.of(1998, 12, 12));
    private final UserCreateDto userCreateDto2 = new UserCreateDto("ya2@mail.ru", "login2",
            "MyDisplayName2", LocalDate.of(1998, 12, 12));

    @Test
    void test1addFriendIfAddOneFriendShouldReturnStatusCode200AndReturnUpdatedUserWithOneFriend() throws Exception {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);

        String userReadDtoAsJson = mockMvc
                .perform(put(URI.create(userUrl.toString() + userReadDto1.getId() + "/friends/" + userReadDto2.getId())))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserReadDto userReadDto = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .readValue(userReadDtoAsJson, UserReadDto.class);

        assertNotNull(userReadDto);
        assertEquals(1, userReadDto.getFriends().size());
    }

    @Test
    void test2addFriendIfUserNotExistShouldReturnStatusCode404() throws Exception {
        UserReadDto userReadDto2 = userService.create(userCreateDto2);

        mockMvc
                .perform(put(URI.create(userUrl.toString() + WRONG_ID + "/friends/" + userReadDto2.getId())))
                .andExpect(status().isNotFound());
    }

    @Test
    void test3addFriendIfFriendNotExistShouldReturnStatusCode404() throws Exception {
        UserReadDto userReadDto2 = userService.create(userCreateDto2);

        mockMvc
                .perform(put(URI.create(userUrl.toString() + userReadDto2.getId() + "/friends/" + WRONG_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    void test4addFriendIfUserIsTryingAddHimselfToFriendsShouldReturnStatusCode400() throws Exception {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);

        mockMvc
                .perform(put(URI.create(userUrl.toString() + userReadDto1.getId() + "/friends/" + userReadDto1.getId())))
                .andExpect(status().isBadRequest());
    }

    //if add one user to friends another
    @Test
    void test5findAllFriendsShouldReturnStatusCode200AndListFriends() throws Exception {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        userService.addToFriends(userReadDto1.getId(), userReadDto2.getId());

        String listUserFriends = mockMvc
                .perform(get(URI.create(userUrl.toString() + userReadDto1.getId() + "/friends")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<UserReadDto> userReadDtoList = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .readValue(listUserFriends, new TypeReference<>() {
                });

        assertEquals(1, userReadDtoList.size());
    }

    @Test
    void test6findAllFriendsIfUserNotExistShouldReturnStatusCode404() throws Exception {
        mockMvc
                .perform(get(URI.create(userUrl.toString() + WRONG_ID + "/friends")))
                .andExpect(status().isNotFound());
    }

    @Test
    void test7findAllCommonFriendsIfUsersHaveOneCommonFriendShouldReturnStatusCode200AndListCommonUsersWith1Friend() throws Exception {
        UserCreateDto userCreateDto3 = new UserCreateDto("ya3@mail.ru", "login3",
                "MyDisplayName3", LocalDate.of(1998, 12, 12));
        UserCreateDto userCreateDto4 = new UserCreateDto("ya4@mail.ru", "login4",
                "MyDisplayName4", LocalDate.of(1998, 12, 12));

        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        UserReadDto userReadDto3 = userService.create(userCreateDto3);
        UserReadDto userReadDto4 = userService.create(userCreateDto4);

        userService.addToFriends(userReadDto1.getId(), userReadDto2.getId());
        userService.addToFriends(userReadDto1.getId(), userReadDto4.getId());
        userService.addToFriends(userReadDto2.getId(), userReadDto3.getId());
        userService.addToFriends(userReadDto2.getId(), userReadDto4.getId());

        String commonListUsersAsJson = mockMvc
                .perform(get(URI.create(userUrl.toString() + userReadDto1.getId() + "/friends/common/" + userReadDto2.getId())))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<UserReadDto> commonListUsers = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .readValue(commonListUsersAsJson, new TypeReference<>() {
                });

        assertEquals(1, commonListUsers.size());
    }

    @Test
    void test8findAllCommonFriendsIfUsersNotExist() throws Exception {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);

        mockMvc
                .perform(get(URI.create(userUrl.toString() + userReadDto1.getId() + "/friends/common/" + WRONG_ID)))
                .andExpect(status().isNotFound());

        mockMvc
                .perform(get(URI.create(userUrl.toString() + WRONG_ID + "/friends/common/" + userReadDto1.getId())))
                .andExpect(status().isNotFound());
    }

    @Test
    void test8removeFriendIfUserHasOneFriendShouldRemoveFriendAndReturnStatusCode200() throws Exception {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        userService.addToFriends(userReadDto1.getId(), userReadDto2.getId());

        String userReadDto1AsJson = mockMvc
                .perform(delete(URI.create(userUrl.toString() + userReadDto1.getId() + "/friends/" + userReadDto2.getId())))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        userReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .readValue(userReadDto1AsJson, UserReadDto.class);

        assertEquals(0, userReadDto1.getFriends().size());

    }

    @Test
    void test9removeFriendIfUserOrFriendNotExistShouldReturnStatusCode404() throws Exception {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);
        UserReadDto userReadDto2 = userService.create(userCreateDto2);
        userService.addToFriends(userReadDto1.getId(), userReadDto2.getId());

        mockMvc
                .perform(delete(URI.create(userUrl.toString() + userReadDto1.getId() + "/friends/" + WRONG_ID)))
                .andExpect(status().isNotFound());

        mockMvc
                .perform(delete(URI.create(userUrl.toString() + WRONG_ID + "/friends/" + userReadDto2.getId())))
                .andExpect(status().isNotFound());
    }

    @Test
    void test10removeFriendIfUserTryRemoveHimselfShouldReturnStatusCode400() throws Exception {
        UserReadDto userReadDto1 = userService.create(userCreateDto1);

        mockMvc
                .perform(delete(URI.create(userUrl.toString() + userReadDto1.getId() + "/friends/" + userReadDto1.getId())))
                .andExpect(status().isBadRequest());
    }
}