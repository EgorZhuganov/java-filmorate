package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
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

    UserControllerTest() throws JsonProcessingException {
    }

    @Autowired
    private MockMvc mockMvc;

    private final URI url = URI.create("http://localhost:8080/users");

    private final UserCreateDto userCreateDto1 = new UserCreateDto("ya@mail.ru", "login",
            "MyDisplayName", LocalDate.of(1998, 12, 12));

    private String userAsJson = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writeValueAsString(userCreateDto1);


    @Test
    void test1createShouldReturnStatusCode201AndReturnUserReadDtoAsJson() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(userAsJson))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        UserReadDto userReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), UserReadDto.class);

        Assertions.assertNotNull(userReadDto1);
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
        MvcResult mvcResult = mockMvc
                .perform(post(url).contentType(APPLICATION_JSON).content(userAsJson))
                .andReturn();
        UserReadDto userReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), UserReadDto.class);

        List<UserReadDto> userReadDtoList = new ArrayList<>();
        userReadDtoList.add(userReadDto1);

        String userListAsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(userReadDtoList);

        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(userListAsJson));
    }


    @Test
    void test4findByIdIfRepositoryHasOneUserWithThisIdShouldReturnUserAndStatusCode200() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post(url).contentType(APPLICATION_JSON).content(userAsJson))
                .andReturn();

        UserReadDto userReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), UserReadDto.class);

        mockMvc.perform(get(url + "/" + userReadDto1.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    void test5findByIdIfRepositoryHasOneUserButMeTryFindUserWithWrongId() throws Exception {
        mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(userAsJson));
        mockMvc.perform(get(url + "/" + 10500)) //wrong id
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    void test6updateIfRepositoryHasUserShouldReturnStatusCode204AndUserAsJson() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post(url).contentType(APPLICATION_JSON).content(userAsJson))
                .andReturn();
        UserReadDto userReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), UserReadDto.class);
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(userReadDto1.getId(), "ya2@mail.ru", "login2",
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
        UserUpdateDto userUpdateDto2 = new UserUpdateDto(100500L, "ya2@mail.ru", "login2",
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
        MvcResult mvcResult = mockMvc
                .perform(post(url).contentType(APPLICATION_JSON).content(userAsJson))
                .andReturn();
        UserReadDto userReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), UserReadDto.class);

        mockMvc.perform(delete(url + "/" + userReadDto1.getId().toString()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
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
        UserCreateDto userCreateDto1 = new UserCreateDto("wrongEmail", null,
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
        MvcResult mvcResult = mockMvc
                .perform(post(url).contentType(APPLICATION_JSON).content(userAsJson))
                .andReturn();
        UserReadDto userReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), UserReadDto.class);
        UserUpdateDto userUpdateDto1 = new UserUpdateDto(userReadDto1.getId(), "wrongEmail", null,
                "MyDisplayName", LocalDate.of(1998, 12, 12));
        String userUpdateAsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(userUpdateDto1);

        mockMvc.perform(put(url)
                        .contentType(APPLICATION_JSON)
                        .content(userUpdateAsJson))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}