package ru.yandex.practicum.filmorate.dto.userDto;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Value
public class UserReadDto {

    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    Set<Long> friends;

}
