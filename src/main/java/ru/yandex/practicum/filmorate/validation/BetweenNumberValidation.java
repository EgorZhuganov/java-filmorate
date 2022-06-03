package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.validation.annotation.BetweenNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.lang.Long.parseLong;

public class BetweenNumberValidation implements ConstraintValidator<BetweenNumber, Long> {

    private long minInterval;
    private long maxInterval;

    @Override
    public void initialize(BetweenNumber constraintAnnotation) {
        minInterval = constraintAnnotation.min();
        maxInterval = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value <= maxInterval && value >= minInterval;
    }
}