package ru.yandex.practicum.filmorate.dal.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Film;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static Film updateFilm(Film film, Film oldFilm) {
        if (oldFilm.hasName()) {
            film.setName(oldFilm.getName());
        }
        if (oldFilm.hasDescription()) {
            film.setDescription(oldFilm.getDescription());
        }
        if (oldFilm.hashDuration()) {
            film.setDuration(oldFilm.getDuration());
        }
        if (oldFilm.hashReleaseDate()) {
            film.setReleaseDate(oldFilm.getReleaseDate());
        }
        if (film.hashMpa()) {
            film.setMpa(oldFilm.getMpa());
        }
        return film;
    }
}
