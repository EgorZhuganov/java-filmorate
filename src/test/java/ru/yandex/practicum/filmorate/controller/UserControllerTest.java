package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserUpdateDto;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    UserControllerTest() throws JsonProcessingException {}
    @Autowired
    private MockMvc mockMvc;

    private final URI url = URI.create("http://localhost:8080/users");

    private final UserCreateDto userCreateDto1 = new UserCreateDto(1L, "ya@mail.ru", "login",
            "MyDisplayName", LocalDate.of(1998, 12, 12));

    private String userAsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(userCreateDto1);

    @Test
    void test1createShouldReturnStatusCode201AndUserCreatedDtoAsJson() throws Exception {
        mockMvc.perform(post(url)
                        .contentType(APPLICATION_JSON).content(userAsJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(userAsJson));
    }

    @Test
    void test2findAllIfRepositoryIsEmpty() throws Exception {
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("[]"));
    }

    @Test
    void test3findAllIfRepositoryHasOneUserShouldReturnStatus200AndListUsers() throws Exception {
        List<UserCreateDto> userCreateDtoList = new ArrayList<>();
        userCreateDtoList.add(userCreateDto1);

        String userListAsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(userCreateDtoList);

        mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(userAsJson));
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(userListAsJson));
    }


    @Test
    void test4findByIdIfRepositoryHasOneUserWithThisIdShouldReturnUserAndStatusCode200() throws Exception {
        mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(userAsJson));
        mockMvc.perform(get(url + "/" + userCreateDto1.getId().toString()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(userAsJson));
    }

    @Test
    void test5findByIdIfRepositoryHasOneUserButMeTryFindUserWithWrongId() throws Exception {
        mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(userAsJson));
        mockMvc.perform(get(url + "/" + 2)) //wrong id
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    void test6updateIfRepositoryHasUserShouldReturnStatusCode204AndUserAsJson() throws Exception {
        mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(userAsJson));
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "ya2@mail.ru", "login2",
                "MyDisplayName2", LocalDate.of(1998, 12, 12));
        String userUpdateDto1AsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(userUpdateDto1);

        mockMvc.perform(put(url).contentType(APPLICATION_JSON).content(userUpdateDto1AsJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(userUpdateDto1AsJson));
    }

    @Test
    void test7updateTryUpdateUserWithWrongIdShouldReturnStatusCode404AndEmptyResponse() throws Exception {
        mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(userAsJson));
        UserUpdateDto userUpdateDto2 = new UserUpdateDto(2L, "ya2@mail.ru", "login2",
                "MyDisplayName2", LocalDate.of(1998, 12, 12)); //user with wrong id
        String userUpdateDto2AsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(userUpdateDto2);

        mockMvc.perform(put(url).contentType(APPLICATION_JSON).content(userUpdateDto2AsJson))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    void test8removeIfRepositoryHasUserShouldDeleteAndReturn204StatusCodeAndEmptyBody() throws Exception {
        mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(userAsJson));
        mockMvc.perform(delete(url + "/" + userCreateDto1.getId().toString()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(""));
    }

    @Test
    void test9removeIfRepositoryHasNotUserShouldReturn404StatusCodeAndEmptyBody() throws Exception {
        mockMvc.perform(delete(url + "/" + userCreateDto1.getId().toString()))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    void test10removeIfRepositoryHasNotUserWithThisIdShouldReturn404StatusCodeAndEmptyBody() throws Exception {
        mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(userAsJson));
        mockMvc.perform(delete(url + "/" + 100500)) //wrong id
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    void test11createIfRequestHasNotValidFieldsShouldReturnStatusCode400() throws Exception {
        UserCreateDto userCreateDto1 = new UserCreateDto(1L, "wrongEmail", null,
                "MyDisplayName", LocalDate.of(1998, 12, 12));

        String userAsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(userCreateDto1);

        mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(userAsJson))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void test12updateIfRequestHasNotValidFieldsShouldReturnStatusCode400() throws Exception {
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(1L, "wrongEmail", null,
                "MyDisplayName", LocalDate.of(1998, 12, 12));

        String userUpdateAsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(userUpdateDto1);

        mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(userAsJson));

        mockMvc.perform(put(url)
                        .contentType(APPLICATION_JSON)
                        .content(userUpdateAsJson))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}