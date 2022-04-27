package ru.yandex.practicum.filmorate.dto.userDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class UserCreateDto {

    @Email
    String email;
    @NotBlank
    String login;
    @NotNull
    String name;
    @PastOrPresent
    LocalDate birthday;

}
