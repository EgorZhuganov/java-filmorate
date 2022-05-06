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
import ru.yandex.practicum.filmorate.dto.filmDto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmReadDto;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmUpdateDto;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {

    FilmControllerTest() throws JsonProcessingException {
    }

    @Autowired
    private MockMvc mockMvc;

    private final URI url = URI.create("http://localhost:8080/films");

    private FilmCreateDto filmCreateDto1 = new FilmCreateDto("12 ст-в", "Во время " +
            "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
            "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
            LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));

    private String filmAsJson = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
            .writeValueAsString(filmCreateDto1);

    @Test
    void test1createShouldReturnStatusCode201AndFilmReadDtoAsJson() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(filmAsJson))
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
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("[]"));
    }

    @Test
    void test3findAllIfRepositoryHasOneFilmShouldReturnStatus200AndListFilms() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post(url).contentType(APPLICATION_JSON).content(filmAsJson))
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

        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(filmsListAsJson));
    }


    @Test
    void test4findByIdIfRepositoryHasOneFilmWithThisIdShouldReturnFilmAndStatusCode200() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post(url).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();

        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), FilmReadDto.class);

        mockMvc.perform(get(url + "/" + filmReadDto1.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    void test5findByIdIfRepositoryHasOneFilmButMeTryFindFilmWithWrongId() throws Exception {
        mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(filmAsJson));
        mockMvc.perform(get(url + "/" + 100500)) //wrong id
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    void test6updateIfRepositoryHasFilmShouldReturnStatusCode204AndFilmAsJson() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post(url).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();
        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), FilmReadDto.class);
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161));
        String filmUpdateDto1AsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .writeValueAsString(filmUpdateDto1);

        mockMvc.perform(put(url).contentType(APPLICATION_JSON).content(filmUpdateDto1AsJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void test7updateTryUpdateFilmWithWrongIdShouldReturnStatusCode404AndEmptyResponse() throws Exception {
        mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(filmAsJson));
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(100500L, "12 стульев", "Во время " +
                "революции и последовавшего за ней краткого периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит Матвеевич Воробьянинов...",
                LocalDate.of(1971, 6, 21), Duration.ofMinutes(161)); //user with wrong id
        String filmUpdateDto1AsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(filmUpdateDto1);

        mockMvc.perform(put(url).contentType(APPLICATION_JSON).content(filmUpdateDto1AsJson))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    void test8removeIfRepositoryHasFilmShouldDeleteAndReturn204StatusCodeAndEmptyBody() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post(url).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();
        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), FilmReadDto.class);

        mockMvc.perform(delete(url + "/" + filmReadDto1.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(""));
    }

    @Test
    void test10removeIfRepositoryHasNotFilmWithThisIdShouldReturn404StatusCodeAndEmptyBody() throws Exception {
        mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(filmAsJson));
        mockMvc.perform(delete(url + "/" + 100500)) //wrong id
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    void test11createIfRequestHasNotValidFieldsShouldReturnStatusCode400() throws Exception {
        FilmCreateDto filmCreateDto1 = new FilmCreateDto(null, "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(999, 6, 21), Duration.ofMinutes(0));

        String filmAsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .writeValueAsString(filmCreateDto1);

        mockMvc.perform(post(url)
                        .contentType(APPLICATION_JSON)
                        .content(filmAsJson))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void test12updateIfRequestHasNotValidFieldsShouldReturnStatusCode400() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post(url).contentType(APPLICATION_JSON).content(filmAsJson))
                .andReturn();
        FilmReadDto filmReadDto1 = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(mvcResult.getResponse().getContentAsString(), FilmReadDto.class);
        FilmUpdateDto filmUpdateDto1 = new FilmUpdateDto(filmReadDto1.getId(), null, "Во время " +
                "******* * *********** ** *** ******* периода военного коммунизма многие прятали свои ценности как " +
                "можно надежнее. И вот Ипполит ******** Воробьянинов, ********...",
                LocalDate.of(999, 6, 21), Duration.ofMinutes(0));
        String filmUpdateAsJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .writeValueAsString(filmUpdateDto1);

        mockMvc.perform(put(url)
                        .contentType(APPLICATION_JSON)
                        .content(filmUpdateAsJson))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}