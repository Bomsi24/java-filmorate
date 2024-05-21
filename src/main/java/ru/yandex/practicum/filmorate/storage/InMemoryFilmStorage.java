package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public void putFilms(Long id, Film film) {
        films.put(id, film);
    }

    @Override
    public Collection<Film> valueFilms() {
        return films.values();
    }

    @Override
    public boolean containsFilmId(Long id) {
        return films.containsKey(id);
    }

    @Override
    public Film getFilm(Long id) {
        return films.get(id);
    }

    @Override
    public Set<Long> getFilmsId() {
        return films.keySet();
    }
}
