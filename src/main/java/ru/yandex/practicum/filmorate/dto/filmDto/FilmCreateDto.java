package ru.yandex.practicum.filmorate.dto.filmDto;

import lombok.*;
import lombok.experimental.FieldDefaults;
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
public class FilmCreateDto {

    private static final String DATE_OF_RELEASE_FIRST_FILM = "1895-12-28";
    @NotNull
    Long id; //TODO удалить поле после ТЗ подумать об автогенерации ID
    @NotBlank
    String name;
    @Size(min = 200)
    String description;
    @IsAfter(minDate = DATE_OF_RELEASE_FIRST_FILM)
    LocalDate releaseDate;
    @Positive
    Duration duration;
}
