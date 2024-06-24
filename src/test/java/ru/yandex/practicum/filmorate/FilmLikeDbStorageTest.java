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
import ru.yandex.practicum.filmorate.dal.storage.FilmLikeDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmLikeDbStorage.class, UserDbStorage.class, FilmDbStorage.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dal"})
public class FilmLikeDbStorageTest {
    @Autowired
    private final FilmDbStorage filmDbStorage;
    @Autowired
    private final FilmLikeDbStorage filmLikeDbStorage;
    @Autowired
    private final UserDbStorage userDbStorage;

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void saveFilmLikeTest() {
        final Film film = filmDbStorage.getFilm(1L).get();
        final User user = userDbStorage.getUser(1L).get();
        final Like like = new Like(film.getId(), user.getId());

        filmLikeDbStorage.save(like);

        final Like getlike = filmLikeDbStorage.findLike(film.getId(), user.getId()).get();

        assertEquals(like, getlike);
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void deleteLikeTest() {
        final Film film = filmDbStorage.getFilm(1L).get();
        final User user = userDbStorage.getUser(1L).get();
        final User user2 = userDbStorage.getUser(2L).get();
        final Like like1 = new Like(film.getId(), user.getId());
        final Like like2 = new Like(film.getId(), user2.getId());

        filmLikeDbStorage.save(like1);
        filmLikeDbStorage.save(like2);
        final Like getLike1 = filmLikeDbStorage.findLike(film.getId(), user.getId()).get();
        final Like getLike2 = filmLikeDbStorage.findLike(film.getId(), user2.getId()).get();

        assertEquals(like1, getLike1);
        assertEquals(like2, getLike2);

        filmLikeDbStorage.delete(film.getId(), user.getId());

        Optional<Like> newLike1 = filmLikeDbStorage.findLike(film.getId(), user.getId());
        Optional<Like> newLike2 = filmLikeDbStorage.findLike(film.getId(), user2.getId());

        assertTrue(newLike1.isEmpty());
        assertTrue(newLike2.isPresent());
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void getPopularFilmsTest() {
        final Film film = filmDbStorage.getFilm(1L).get();
        final Film film2 = filmDbStorage.getFilm(2L).get();
        final User user = userDbStorage.getUser(1L).get();
        final User user2 = userDbStorage.getUser(2L).get();
        final Like like1 = new Like(film.getId(), user.getId());
        final Like like2 = new Like(film2.getId(), user2.getId());
        final Like like3 = new Like(film2.getId(), user.getId());

        filmLikeDbStorage.save(like1);
        filmLikeDbStorage.save(like2);
        filmLikeDbStorage.save(like3);

        final List<Film> popularFilms = filmLikeDbStorage.getPopularFilms(2);

        assertEquals(2, popularFilms.size());
        assertEquals(popularFilms.get(0), film2);
        assertEquals(popularFilms.get(1), film);
    }
}
