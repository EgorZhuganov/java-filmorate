package ru.yandex.practicum.filmorate.dto.filmDto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.validation.annotation.BetweenNumber;
import ru.yandex.practicum.filmorate.validation.annotation.IsAfter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmCreateDto {

    private static final String DATE_OF_RELEASE_FIRST_FILM = "1895-12-28";

    @NotBlank
    String name;
    @NotBlank @Size(max = 200)
    String description;
    @IsAfter(minDate = DATE_OF_RELEASE_FIRST_FILM)
    LocalDate releaseDate;
    @DurationMin(minutes = 1)
    Duration duration;
    @NotNull @BetweenNumber(min =  1, max = 5)
    Long mpaId;

}
