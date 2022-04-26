package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User implements BaseModel<Long> {

    private Long id;
    private String email;
    private String login;
    private String displayName;
    private LocalDate birthday;

}
