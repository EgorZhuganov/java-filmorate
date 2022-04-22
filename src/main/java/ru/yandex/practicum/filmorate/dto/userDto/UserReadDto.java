package ru.yandex.practicum.filmorate.dto.userDto;

import lombok.*;

import java.time.LocalDate;

@Value
public class UserReadDto {

    Long id;
    String email;
    String login; //TODO подумать об удалении после спринта
    String displayName;
    LocalDate birthday;

}
