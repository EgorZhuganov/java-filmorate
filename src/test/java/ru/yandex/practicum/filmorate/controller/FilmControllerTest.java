package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.annotation.IntegrationTest;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmReadDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@IntegrationTest
class FilmControllerTest {

    FilmControllerTest() throws JsonProcessingException {
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmService filmService;
    private final URI filmUrl = URI.create("http://localhost:8080/films");
    private final URI userUrl = URI.create("http://localhost:8080/users");
    private static final Long WRONG_ID = 100500L;
    private FilmCreateDto filmCreateDto1 = FilmCreateDto.builder()
            .name("12 ст-в")
            .description("Во время  ******* * *********** ** *** ******* периода военного коммунизма многие " +
                    "прятали свои ценности как можно надежнее. И вот Ипполит ******** Воробьянинов, ********...")
            .releaseDate(LocalDate.of(1971, 6, 21))
            .duration(ofMinutes(161))
            .mpaId(1L)
            .build();
    private final UserCreateDto userCreateDto1 = new UserCreateDto("ya@mail.ru", "login",
            "MyDisplayName", LocalDate.of(1998, 12, 12));
    private final String filmAsJson = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
            .writeValueAsString(filmCreateDto1);
    private final String userAsJson = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writeValueAsString(userCreateDto1);

    @Test
    void test1createShouldReturnStatusCode201AndFilmReadDtoAsJson() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), FilmReadDto.class);

        Assertions.assertNotNull(filmReadDto1);
    }

    @Test
    void test2findAllIfRepositoryIsEmpty() throws Exception {
        mockMvc.perform(get(filmUrl))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("[]"));
    }

    @Test
    void test3findAllIfRepositoryHasOneFilmShouldReturnStatus200AndListFilms() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();

        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), FilmReadDto.class);

        List<FilmReadDto> filmReadDtoList = new ArrayList<>();
        filmReadDtoList.add(filmReadDto1);

        String filmsListAsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .writeValueAsString(filmReadDtoList);

        mockMvc.perform(get(filmUrl))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(filmsListAsJson));
    }


    @Test
    void test4findByIdIfRepositoryHasOneFilmWithThisIdShouldReturnFilmAndStatusCode200() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();

        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), FilmReadDto.class);

        mockMvc.perform(get(filmUrl + "/" + filmReadDto1.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    void test5findByIdIfRepositoryHasOneFilmButMeTryFindFilmWithWrongId() throws Exception {
        mockMvc.perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson));
        mockMvc.perform(get(filmUrl + "/" + WRONG_ID)) //wrong id
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    void test6updateIfRepositoryHasFilmShouldReturnStatusCode204AndFilmAsJson() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();
        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), FilmReadDto.class);
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1971, 6, 21), ofMinutes(161), 1L);

        String filmUpdateDto1AsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .writeValueAsString(filmUpdateDto1);

        mockMvc.perform(put(filmUrl).contentType(APPLICATION_JSON).content(filmUpdateDto1AsJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void test7updateTryUpdateFilmWithWrongIdShouldReturnStatusCode404AndEmptyResponse() throws Exception {
        mockMvc.perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson));
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(WRONG_ID, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1971, 6, 21), ofMinutes(161), 1L); //user with wrong id
        String filmUpdateDto1AsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(filmUpdateDto1);

        mockMvc.perform(put(filmUrl).contentType(APPLICATION_JSON).content(filmUpdateDto1AsJson))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    void test8removeIfRepositoryHasFilmShouldDeleteAndReturn204StatusCodeAndEmptyBody() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();
        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), FilmReadDto.class);

        mockMvc.perform(delete(filmUrl + "/" + filmReadDto1.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(""));
    }

    @Test
    void test10removeIfRepositoryHasNotFilmWithThisIdShouldReturn404StatusCodeAndEmptyBody() throws Exception {
        mockMvc.perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson));
        mockMvc.perform(delete(filmUrl + "/" + WRONG_ID)) //wrong id
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    void test11createIfRequestHasNotValidFieldsShouldReturnStatusCode400() throws Exception {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto(null, "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(999, 6, 21), ofMinutes(0), 1L);

        String filmAsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .writeValueAsString(filmCreateDto1);

        mockMvc.perform(post(filmUrl)
                        .contentType(APPLICATION_JSON)
                        .content(filmAsJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void test12updateIfRequestHasNotValidFieldsShouldReturnStatusCode400() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();
        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), FilmReadDto.class);
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), null, "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(999, 6, 21), ofMinutes(0), 1L);
        String filmUpdateAsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .writeValueAsString(filmUpdateDto1);

        mockMvc.perform(put(filmUrl)
                        .contentType(APPLICATION_JSON)
                        .content(filmUpdateAsJson))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void test13addLikeIfUserAndFilmWereCreatedShouldReturnStatusCode200() throws Exception {
        MvcResult mvcFilmReadDtoResult = mockMvc
                .perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();
        MvcResult mvcUserReadDtoResult = mockMvc
                .perform(post(userUrl).contentType(APPLICATION_JSON).content(userAsJson))
                .andReturn();
        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcFilmReadDtoResult.getResponse().getContentAsString(), FilmReadDto.class);
        UserReadDto userReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcUserReadDtoResult.getResponse().getContentAsString(), UserReadDto.class);

        mockMvc.perform(put(URI.create(filmUrl + "/" + filmReadDto1.getId() + "/like/" + userReadDto1.getId()))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void test14addLikeIfUserAndFilmWereCreatedShouldReturnStatusCode200FilmReadDtoWithListLike() throws Exception {
        MvcResult mvcFilmReadDtoResult = mockMvc
                .perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();
        MvcResult mvcUserReadDtoResult = mockMvc
                .perform(post(userUrl).contentType(APPLICATION_JSON).content(userAsJson))
                .andReturn();
        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcFilmReadDtoResult.getResponse().getContentAsString(), FilmReadDto.class);
        UserReadDto userReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcUserReadDtoResult.getResponse().getContentAsString(), UserReadDto.class);

        MvcResult mvcFilmReadDtoWithLike = mockMvc
                .perform(put(URI.create(filmUrl + "/" + filmReadDto1.getId() + "/like/" + userReadDto1.getId()))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        FilmReadDto filmReadDto = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcFilmReadDtoWithLike.getResponse().getContentAsString(), FilmReadDto.class);

        assertEquals(1, filmReadDto.getLikes().size());
    }

    @Test
    void test15addLikeIfFilmNotExistShouldReturnStatusCode404() throws Exception {
        MvcResult mvcUserReadDtoResult = mockMvc
                .perform(post(userUrl).contentType(APPLICATION_JSON).content(userAsJson))
                .andReturn();
        UserReadDto userReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcUserReadDtoResult.getResponse().getContentAsString(), UserReadDto.class);

        mockMvc.perform(put(URI.create(filmUrl + "/" + WRONG_ID + "/like/" + userReadDto1.getId()))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void test16addLikeIfUserNotExistShouldReturnStatusCode404() throws Exception {
        MvcResult mvcFilmReadDtoResult = mockMvc
                .perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();
        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcFilmReadDtoResult.getResponse().getContentAsString(), FilmReadDto.class);

        mockMvc.perform(put(URI.create(filmUrl + "/" + filmReadDto1.getId() + "/like/" + WRONG_ID))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void test17removeLikeIfUserAddLikeToFilmShouldReturnStatusCode200AndRemoveLike() throws Exception {
        MvcResult mvcFilmReadDtoResult = mockMvc
                .perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();
        MvcResult mvcUserReadDtoResult = mockMvc
                .perform(post(userUrl).contentType(APPLICATION_JSON).content(userAsJson))
                .andReturn();
        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcFilmReadDtoResult.getResponse().getContentAsString(), FilmReadDto.class);
        UserReadDto userReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcUserReadDtoResult.getResponse().getContentAsString(), UserReadDto.class);

        MvcResult mvcFilmReadDtoWithoutLike = mockMvc
                .perform(delete(URI.create(filmUrl + "/" + filmReadDto1.getId() + "/like/" + userReadDto1.getId()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        FilmReadDto filmReadDto = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcFilmReadDtoWithoutLike.getResponse().getContentAsString(), FilmReadDto.class);

        assertEquals(0, filmReadDto.getLikes().size());
    }

    @Test
    void test18removeLikeIfUserNotExistShouldReturnStatus404NotFound() throws Exception {
        MvcResult mvcFilmReadDtoResult = mockMvc
                .perform(post(filmUrl).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();
        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcFilmReadDtoResult.getResponse().getContentAsString(), FilmReadDto.class);

        mockMvc.perform(delete(URI.create(filmUrl + "/" + filmReadDto1.getId() + "/like/" + WRONG_ID))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    void test19removeLikeIfFilmNotExistShouldStatus404NotFound() throws Exception {
        MvcResult mvcUserReadDtoResult = mockMvc
                .perform(post(userUrl).contentType(APPLICATION_JSON).content(userAsJson))
                .andReturn();
        UserReadDto userReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcUserReadDtoResult.getResponse().getContentAsString(), UserReadDto.class);

        mockMvc.perform(delete(URI.create(filmUrl + "/" + WRONG_ID + "/like/" + userReadDto1.getId()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void test20findPopularFilmsByLikesIfAddMoreThan10FilmsShouldReturnOnly10() throws Exception {
        for (int i = 0; i < 12; i++) { //create 12 films
            filmCreateDto1 = new FilmCreateDto("12 ст-в" + " версия " + i, "Во время " +
                    "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                    "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                    LocalDate.of(1971, 6, 21), ofMinutes(161), 1L);
            filmService.create(filmCreateDto1);
        }

        MvcResult mvcUserReadDtoResult = mockMvc
                .perform(get(filmUrl + "/popular").contentType(APPLICATION_JSON))
                .andReturn();

        List<FilmReadDto> filmReadDtoList = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcUserReadDtoResult.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertEquals(10, filmReadDtoList.size());
    }

    //if created 12 films, but query param is 5 films, should return 5 films
    @Test
    void test21findPopularFilmsByLikes() throws Exception {
        for (int i = 0; i < 12; i++) { //create 12 films
            filmCreateDto1 = new FilmCreateDto("12 ст-в" + " версия " + i, "Во время " +
                    "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                    "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                    LocalDate.of(1971, 6, 21), ofMinutes(161), 1L);
            filmService.create(filmCreateDto1);
        }

        MvcResult mvcUserReadDtoResult = mockMvc
                .perform(get(filmUrl + "/popular").param("count", "5").contentType(APPLICATION_JSON))
                .andReturn();

        List<FilmReadDto> filmReadDtoList = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcUserReadDtoResult.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertEquals(5, filmReadDtoList.size());
    }
}