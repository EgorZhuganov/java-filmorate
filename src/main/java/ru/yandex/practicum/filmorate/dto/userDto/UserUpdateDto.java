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
public class UserUpdateDto {

    @NotNull
    Long id;
    @Email
    String email;
    @NotBlank @Size(min = 3, max = 10)
    String login;
    @NotNull
    String displayName;
    @PastOrPresent
    LocalDate birthday;

}
