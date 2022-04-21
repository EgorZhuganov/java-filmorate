package ru.yandex.practicum.filmorate.dto.userDto;

import lombok.Value;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Value
public class UserUpdateDto {

    @NotNull
    Long id;
    @Email
    String email;
    @NotBlank @Min(3) @Max(10)
    String login;
    @NotNull
    String displayName;
    @PastOrPresent
    LocalDate birthday;

}
