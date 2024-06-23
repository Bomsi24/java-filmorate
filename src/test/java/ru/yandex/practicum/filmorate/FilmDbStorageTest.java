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
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmDbStorage.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dal"})
public class FilmDbStorageTest {
    @Autowired
    private final FilmDbStorage filmDbStorage;

    @Test
    void saveFilmTest() {
        final Film film = generateFilm(
                "Test film 1", "Film1 description",
                LocalDate.of(2007,05,31),135);

        filmDbStorage.save(film);

        final List<Film> films = filmDbStorage.valueFilms();

        assertEquals(1, films.size());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "Test film 1");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "Film1 description");
        assertThat(films.get(0)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", 135);
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void updateFilmTest() {
        final Film film = generateFilm(
                "Test film 1", "Film1 description",
                LocalDate.of(2007,05,31),135);
        film.setId(1L);

        filmDbStorage.update(film);

        final Film updatedUser = filmDbStorage.getFilm(1L).get();

        assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(film).hasFieldOrPropertyWithValue("name", "Test film 1");
        assertThat(film).hasFieldOrPropertyWithValue("description", "Film1 description");
        assertThat(film).hasFieldOrProperty("releaseDate");
        assertThat(film).hasFieldOrPropertyWithValue("duration", 135);
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void allFilmsTest() {
        final List<Film> films = filmDbStorage.valueFilms();

        assertEquals(3, films.size());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "Test film 1");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "Film1 description");
        assertThat(films.get(0)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", 135);

        assertThat(films.get(1)).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("name", "Test film 2");
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("description", "Film2 description");
        assertThat(films.get(1)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("duration", 120);
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void getFilmByIdTest() {

        final Film film = filmDbStorage.getFilm(1L).get();

        assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(film).hasFieldOrPropertyWithValue("name", "Test film 1");
        assertThat(film).hasFieldOrPropertyWithValue("description", "Film1 description");
        assertThat(film).hasFieldOrProperty("releaseDate");
        assertThat(film).hasFieldOrPropertyWithValue("duration", 135);
    }


    private Film generateFilm(
           String name,
           String description,
           LocalDate releaseDate,
           Integer duration) {
        return Film.builder()
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .build();
    }
}
