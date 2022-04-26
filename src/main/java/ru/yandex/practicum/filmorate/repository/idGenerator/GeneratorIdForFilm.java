package ru.yandex.practicum.filmorate.repository.idGenerator;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public class GeneratorIdForFilm extends AbstractAtomicLongGenerator<Film> {
}
