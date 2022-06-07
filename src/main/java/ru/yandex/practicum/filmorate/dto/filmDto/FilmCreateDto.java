package ru.yandex.practicum.filmorate.dto.filmDto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.validation.annotation.BetweenNumber;
import ru.yandex.practicum.filmorate.validation.annotation.IsAfter;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

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
    @NotNull @BetweenNumber(min = 1, max = 5)
    Long mpaId;
//    @NotEmpty
    List<Long> genresIds;
}
