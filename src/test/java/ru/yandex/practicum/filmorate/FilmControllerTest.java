package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmControllerTest {
    FilmService filmService;
    Film film;

    @BeforeEach
    public void init() {
        filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        film = new Film(1L, "test", "description", LocalDate.of(2000, 1, 1),
                1, Collections.emptySet());
    }

    @Test
    public void testCreateFilmWithNullName() {
        film.setName(null);
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                filmService.create(film));

        Assertions.assertEquals("Название фильма не может быть пустым", thrown.getMessage());
    }

    @Test
    public void testCreateFilmWithLongDescription() {
        film.setDescription("Это очень длинное описание,Это очень длинное описание,Это очень длинное описание," +
                "Это очень длинное описание,Это очень длинное описание,Это очень длинное описание," +
                "Это очень длинное описание, Это очень длинное описание");
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                filmService.create(film));

        Assertions.assertEquals("максимальная длина описания — 200 символов", thrown.getMessage());
    }

    @Test
    public void testCreateFilmWithInvalidReleaseDate() {
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                filmService.create(film));

        Assertions.assertEquals("дата релиза не должна быть раньше раньше 28 декабря 1895 года",
                thrown.getMessage());
    }

    @Test
    public void testCreateFilmWithNegativeDuration() {
        film.setDuration(-10);
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                filmService.create(film));

        Assertions.assertEquals("продолжительность фильма должна быть положительным числом",
                thrown.getMessage());
    }

    @Test
    public void testUpdateIdNotSpecified() {
        filmService.create(film);
        film.setId(null);
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                filmService.update(film));

        Assertions.assertEquals("Id должен быть указан", thrown.getMessage());
    }

    @Test
    public void testUpdateEmptyDescription() {
        filmService.create(film);
        film.setDescription("");
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                filmService.update(film));

        Assertions.assertEquals("Описание фильма не может быть пустым", thrown.getMessage());
    }

    @Test
    public void testUpdateFilmWithLongDescription() {
        filmService.create(film);
        film.setDescription("Это очень длинное описание,Это очень длинное описание,Это очень длинное описание," +
                "Это очень длинное описание,Это очень длинное описание,Это очень длинное описание," +
                "Это очень длинное описание, Это очень длинное описание");
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                filmService.update(film));

        Assertions.assertEquals("максимальная длина описания — 200 символов", thrown.getMessage());
    }

    @Test
    public void testUpdateNonExistentId() {
        filmService.create(film);
        film.setId(3L);
        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                filmService.update(film));

        Assertions.assertEquals("Фильм с указанным Id не найден", thrown.getMessage());
    }

}
