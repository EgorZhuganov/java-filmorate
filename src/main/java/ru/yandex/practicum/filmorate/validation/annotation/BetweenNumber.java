package ru.yandex.practicum.filmorate.validation.annotation;

import ru.yandex.practicum.filmorate.validation.BetweenNumberValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = BetweenNumberValidation.class)
@Documented
public @interface BetweenNumber {

    String message() default "номер MPA рейтинга должен быть между {min} и {max} вы ввели ${validatedValue}";

    long min();
    long max();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        BetweenNumber[] value();
    }
}
