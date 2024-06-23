package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ReleaseDateValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface ReleaseDate {
    String message() default "Дата должна быть после 28 декабря 1895 г.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}



