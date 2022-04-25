package ru.yandex.practicum.filmorate.dto.filmDto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.validation.annotation.IsAfter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmUpdateDto {

    private static final String DATE_OF_RELEASE_FIRST_FILM = "1895-12-28";
    @NotNull
    Long id;
    @NotBlank
    String name;
    @NotBlank @Size(max = 200)
    String description;
    @IsAfter(minDate = DATE_OF_RELEASE_FIRST_FILM)
    LocalDate releaseDate;
    @DurationMin(seconds = 1)
    Duration duration;

}
