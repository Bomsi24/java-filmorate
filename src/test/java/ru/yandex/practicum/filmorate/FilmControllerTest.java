package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmControllerTest {
    FilmController filmController;
    Film film;

    @BeforeEach
    public void init() {
        filmController = new FilmController();
        film = new Film(1L, "Матрица", "Описание",
                LocalDate.of(2007, 10, 1), 120);
    }

    @Test
    public void testCreateFilmWithNullName() {
        film.setName(null);
        assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    public void testCreateFilmWithLongDescription() {
        film.setDescription("Это очень длинное описание,Это очень длинное описание,Это очень длинное описание," +
                "Это очень длинное описание,Это очень длинное описание,Это очень длинное описание," +
                "Это очень длинное описание, Это очень длинное описание");
        assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    public void testCreateFilmWithInvalidReleaseDate() {
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    public void testCreateFilmWithNegativeDuration() {
        film.setDuration(-10);
        assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    public void testUpdateIdNotSpecified() {
        filmController.create(film);
        film.setId(null);
        assertThrows(ValidationException.class, () ->
                filmController.update(film));
    }

    @Test
    public void testUpdateEmptyDescription() {
        filmController.create(film);
        film.setDescription("");
        assertThrows(ValidationException.class, () ->
                filmController.update(film));
    }

    @Test
    public void testUpdateFilmWithLongDescription() {
        filmController.create(film);
        film.setDescription("Это очень длинное описание,Это очень длинное описание,Это очень длинное описание," +
                "Это очень длинное описание,Это очень длинное описание,Это очень длинное описание," +
                "Это очень длинное описание, Это очень длинное описание");
        assertThrows(ValidationException.class, () -> {
            filmController.update(film);
        });
    }

    @Test
    public void testUpdateNonExistentId() {
        filmController.create(film);
        film.setId(3L);
        assertThrows(ValidationException.class, () ->
                filmController.update(film));
    }
}
