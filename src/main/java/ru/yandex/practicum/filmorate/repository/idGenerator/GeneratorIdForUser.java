package ru.yandex.practicum.filmorate.repository.idGenerator;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class GeneratorIdForUser extends AbstractAtomicLongGenerator<User> {
}
