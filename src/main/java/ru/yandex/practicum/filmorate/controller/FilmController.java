package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Long id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id,
                        @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Long id,
                           @PathVariable Long userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(
            @RequestParam(required = false, defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa() {
        return filmService.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpa(@PathVariable Long id) {
        return filmService.getMpa(id);
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenre() {
        return filmService.getAllGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable Long id) {
        return filmService.getGenre(id);
    }
}
