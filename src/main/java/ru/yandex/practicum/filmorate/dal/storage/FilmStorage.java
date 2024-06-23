package ru.yandex.practicum.filmorate.dal.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film save(Film film);

    Film update(Film film);

    List<Film> valueFilms();

    Optional<Film> getFilm(Long id);
}
