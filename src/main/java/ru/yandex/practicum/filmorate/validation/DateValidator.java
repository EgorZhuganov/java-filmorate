package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.validation.annotation.IsAfter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static java.lang.Integer.parseInt;

public class DateValidator implements ConstraintValidator<IsAfter, LocalDate> {

    private String validDate;

    @Override
    public void initialize(IsAfter constraintAnnotation) {
        validDate = constraintAnnotation.minDate();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        String[] splitDate = validDate.split("-");
        return date.isAfter(LocalDate.of(parseInt(splitDate[0]), parseInt(splitDate[1]), parseInt(splitDate[2])));
    }
}
