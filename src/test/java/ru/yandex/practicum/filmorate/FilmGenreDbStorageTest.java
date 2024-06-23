package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmGenreDbStorage.class, FilmDbStorage.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dal"})
public class FilmGenreDbStorageTest {
    @Autowired
    private final FilmGenreDbStorage filmGenreDbStorage;
    @Autowired
    private final FilmDbStorage filmDbStorage;

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void getGenreByIdTest() {
        final Genre genre = filmGenreDbStorage.getGenre(1L).get();

        assertThat(genre).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void saveGenreForFilmTest() {
        final Film film = filmDbStorage.getFilm(1L).get();
        final Genre genre = filmGenreDbStorage.getGenre(1l).get();

        filmGenreDbStorage.save(film, genre);

        final List<Genre> genres = filmGenreDbStorage.getGenresByIdFilm(film.getId());

        assertEquals(1, genres.size());
        assertThat(genres.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(genres.get(0)).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void allGenresTest() {
        final List<Genre> genres = filmGenreDbStorage.valueGenre();

        assertEquals(6,  genres.size());
        assertThat(genres.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(genres.get(0)).hasFieldOrPropertyWithValue("name", "Комедия");

        assertThat( genres.get(1)).hasFieldOrPropertyWithValue("id", 2L);
        assertThat( genres.get(1)).hasFieldOrPropertyWithValue("name", "Драма");

    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void getGenresByIdFilmTest() {
        final Film film = filmDbStorage.getFilm(1L).get();
        final Genre genre = filmGenreDbStorage.getGenre(1l).get();
        final Genre genre2 = filmGenreDbStorage.getGenre(2l).get();

        filmGenreDbStorage.save(film, genre);
        filmGenreDbStorage.save(film, genre2);

        final List<Genre> genres = filmGenreDbStorage.getGenresByIdFilm(film.getId());

        assertEquals(2, genres.size());
        assertThat(genres.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(genres.get(0)).hasFieldOrPropertyWithValue("name", "Комедия");

        assertThat( genres.get(1)).hasFieldOrPropertyWithValue("id", 2L);
        assertThat( genres.get(1)).hasFieldOrPropertyWithValue("name", "Драма");
    }

}
