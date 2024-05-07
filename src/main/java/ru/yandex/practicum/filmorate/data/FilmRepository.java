package ru.yandex.practicum.filmorate.data;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
public class FilmRepository {

    private final Map<Long, Film> films = new HashMap<>();

    public void putFilms(Long id, Film film) {
        films.put(id, film);
    }

    public Collection<Film> valueFilms() {
        return films.values();
    }

    public boolean containsFilmId(Long id) {
        return films.containsKey(id);
    }

    public Film getFilm(Long id) {
        return films.get(id);
    }

    public Set<Long> getFilmsId() {
        return films.keySet();
    }
}
