package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    void putFilms(Long id, Film film);

    List<Film> valueFilms();

    boolean containsFilmId(Long id);

    Film getFilm(Long id);

    Set<Long> getFilmsId();

}
