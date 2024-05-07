package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.data.FilmRepository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmRepository repository = new FilmRepository();

    @GetMapping
    public Collection<Film> getFilms() {
        return repository.valueFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Указанно пустое название фильма");
            throw new ValidationException("Название фильма не может быть пустым");

        } else if (film.getDescription().length() > 200) {
            log.error("Указанно слишком длинное описание фильма");
            throw new ValidationException("максимальная длина описания — 200 символов");

        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Указанна неправильная дата релиза фильма ");
            throw new ValidationException("дата релиза не должна быть раньше раньше 28 декабря 1895 года");

        } else if (film.getDuration() < 1) {
            log.error("Указана отрицательная продолжительность фильма");
            throw new ValidationException("продолжительность фильма должна быть положительным числом");
        }

        film.setId(getNextId());
        log.debug("Создан и присвоен id {}", film.getId());
        repository.putFilms(film.getId(), film);
        log.info("Создан и сохранен новый фильм");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Id должен быть указан");
        }
        if (repository.containsFilmId(newFilm.getId())) {
            Film oldFilm = repository.getFilm(newFilm.getId());

            if (oldFilm.getDescription().isBlank()) {
                log.error("Пустое описание фильма");
                throw new ValidationException("Описание фильма не может быть пустым");

            } else if (oldFilm.getDescription().length() > 200) {
                log.error("Указанно слишком длинное описание фильма");
                throw new ValidationException("максимальная длина описания — 200 символов");
            }

            if (newFilm.getDuration() != null) {
                oldFilm.setDuration(newFilm.getDuration());
                log.info("Изменили продолжительность фильма");
            }
            if (newFilm.getReleaseDate() != null) {
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
                log.info("Изменили дату релиза");
            }
            if (newFilm.getDescription() != null) {
                oldFilm.setDescription(newFilm.getDescription());
                log.info("Изменили описание фильма");
            }
            if (newFilm.getName() != null) {
                oldFilm.setName(newFilm.getName());
                log.info("Изменили название фильма");
            }

            return oldFilm;

        } else {
            log.error("Указан несуществующий id {}", newFilm.getId());
            throw new ValidationException("Фильм с указанным Id не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = repository.getFilmsId()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
