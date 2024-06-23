package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dal.storage.FilmMpaDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.FilmLikeDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.UserDbStorage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmLikeDbStorage likeDbStorage;
    private final FilmMpaDbStorage mpaDbStorage;
    private final FilmGenreDbStorage genreDbStorage;

    public Film getFilm(Long id) {
        log.info("Запуск метода getFilm");
        Film film = checkFilm(id);

        List<Genre> genre = genreDbStorage.getGenresByIdFilm(film.getId());
        if (genre != null && !genre.isEmpty()) {
            film.setGenres(genre);
        }
        Long idMpa = mpaDbStorage.getMpaId(film.getId());
        Optional<Mpa> mpa = mpaDbStorage.getMpa(idMpa);
        mpa.ifPresent(film::setMpa);

        log.info("Возвращаем фильм {}", film);
        return film;
    }

    public Film addLike(Long filmId, Long userId) {
        log.info("Запуск метода addLike");
        Film film = checkFilm(filmId);
        userDbStorage.getUser(userId).orElseThrow(() -> {
            log.error("Пользователь не найден{}", userId);
            return new NotFoundException("Пользователь  не найден " + userId);
        });

        Optional<Like> like = likeDbStorage.findLike(filmId, userId);
        if (like.isPresent()) {
            log.error("Пользователь уже поставил лайк{} на фильм {}", userId, filmId);
            throw new ValidationException("Пользователь уже поставил лайк " +
                    userId + " на фильм " + filmId);
        }
        likeDbStorage.save(new Like(filmId, userId));
        log.info("Добавлен лайк {} фильму{}", userId, filmId);
        return film;
    }

    public Film deleteLike(Long filmId, Long userId) {
        log.info("Запуск метода deleteLike");
        Film film = checkFilm(filmId);
        userDbStorage.getUser(userId).orElseThrow(() -> {
            log.error("Пользователь не найден{}", userId);
            return new NotFoundException("Пользователь  не найден " + userId);
        });
        if (!likeDbStorage.delete(filmId, userId)) {
            log.error("Лайк {} у фильма {} не удален", userId, filmId);
            throw new ValidationException("Лайк " + userId + " у фильма " +
                    film + " не удален");
        }
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Запуск метода getPopularFilms");
        return likeDbStorage.getPopularFilms(count);
    }

    public Film create(Film film) {
        log.info("Запуск метода create");
        Optional<Film> newFilm = filmDbStorage.getFilm(film.getId());
        if (newFilm.isPresent()) {
            log.error("Фильм {} уже существует", film.getId());
            throw new ValidationException("Фильм уже существует");
        }

        filmDbStorage.save(film);

        if (film.hashMpa()) {
            Mpa mpa = mpaDbStorage.getMpa(film.getMpa().getId()).orElseThrow(() ->
                    new ValidationException("Такого mpa нет"));
            mpaDbStorage.updateMpa(mpa, film.getId()); //вот
        }

        if (film.hashGenres()) {
            if (film.getGenres().stream().allMatch(genre ->
                    genreDbStorage.getGenre(genre.getId()).isEmpty())) {
                throw new ValidationException("Такого жанра нет");

            } else {
                genreDbStorage.valueGenre().forEach(genre ->
                        film.getGenres().stream()
                                .filter(filmGenre -> Objects.equals(filmGenre.getId(), genre.getId()))
                                .findFirst()
                                .ifPresent(filmGenre -> genreDbStorage.save(film, genre))
                );
            }
        }
        log.info("Фильм {} создан", film);
        return film;
    }

    public Film update(Film film) {
        log.info("Запуск метода update");
        Optional<Mpa> mpa = mpaDbStorage.getMpa(film.getMpa().getId());
        mpa.ifPresent(film::setMpa);

        log.info("Mpa фильма{}", film.getMpa());
        Film oldFilm = checkFilm(film.getId());
        Film newFilm = FilmMapper.updateFilm(oldFilm, film);
        return filmDbStorage.update(newFilm);
    }

    public List<Film> getFilms() {
        return filmDbStorage.valueFilms();
    }

    public List<Mpa> getAllMpa() {
        return mpaDbStorage.valueMpa();
    }

    public Mpa getMpa(Long id) {
        return mpaDbStorage.getMpa(id).orElseThrow(() -> {
            log.error("Mpa{} не найдено", id);
            return new NotFoundException("Mpa " + id + " не найден");
        });
    }

    public List<Genre> getAllGenre() {
        return genreDbStorage.valueGenre();
    }

    public Genre getGenre(Long id) {
        return genreDbStorage.getGenre(id).orElseThrow(() -> {
            log.error("Genre{} не найден", id);
            return new NotFoundException("Genre " + id + " не найден");
        });
    }

    private Film checkFilm(Long id) {
        if (id == null) {
            log.error("Фильм с id{} не найден", id);
            throw new NotFoundException("Фильм с id: " + id + " не найден.");
        }

        return filmDbStorage.getFilm(id).orElseThrow(() -> {
            log.error("Фильм с id{} не найден", id);
            return new NotFoundException("Фильм с id: " + id + " не найден.");
        });
    }
}
