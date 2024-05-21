package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    public Film getFilm(Long id) {
        if (!filmStorage.containsFilmId(id)) {
            log.error("Отсутствует фильм");
            throw new NotFoundException("Отсутствует фильм");
        }
        return filmStorage.getFilm(id);
    }

    public Film addLike(Long id, Long userId) {
        if (!filmStorage.containsFilmId(id)) {
            log.error("Отсутствует фильм");
            throw new NotFoundException("Отсутствует фильм");
        }
        if (!userStorage.containsUserId(userId)) {
            log.error("Отсутствует пользователь");
            throw new NotFoundException("Отсутствует пользователь");
        }

        Film film = filmStorage.getFilm(id);
        film.addLike(userId);

        log.info("Лайк добавлен {}", film.getLikes());
        return film;
    }

    public Film deleteLike(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        User user = userStorage.getUser(userId);
        if (film == null || user == null) {
            log.error("Отсутствует пользователь");
            throw new NotFoundException("Отсутствует пользователь");
        }

        film.deleteLike(userId);
        log.info("Лайк удален {}", film.getLikes());
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.valueFilms().stream()
                .filter(film -> film.getLikes() != null)
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film create(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Указанно пустое название фильма");
            throw new ValidationException("Название фильма не может быть пустым");

        } else if (film.getDescription().length() > 200) {
            log.error("Указанно слишком длинное описание фильма");
            throw new ValidationException("максимальная длина описания — 200 символов");

        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Указанна неправильная дата релиза фильма ");
            throw new ValidationException("дата релиза не должна быть раньше раньше 28 декабря 1895 года");

        } else if (film.getDuration() < 1) {
            log.error("Указана отрицательная продолжительность фильма");
            throw new ValidationException("продолжительность фильма должна быть положительным числом");
        }

        film.setId(getNextId());
        log.debug("Создан и присвоен id {}", film.getId());
        filmStorage.putFilms(film.getId(), film);
        log.info("Создан и сохранен новый фильм");
        return film;
    }

    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Id должен быть указан");
        }
        if (filmStorage.containsFilmId(newFilm.getId())) {
            Film oldFilm = filmStorage.getFilm(newFilm.getId());

            if (oldFilm.getDescription().isBlank()) {
                log.error("Пустое описание фильма");
                throw new ValidationException("Описание фильма не может быть пустым");

            } else if (oldFilm.getDescription().length() > 200) {
                log.error("Указанно слишком длинное описание фильма");
                throw new ValidationException("максимальная длина описания — 200 символов");
            }

            if (newFilm.getDuration() != null) {
                oldFilm.setDuration(newFilm.getDuration());
                log.info("Изменили продолжительность фильма");
            }
            if (newFilm.getReleaseDate() != null) {
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
                log.info("Изменили дату релиза");
            }
            if (newFilm.getDescription() != null) {
                oldFilm.setDescription(newFilm.getDescription());
                log.info("Изменили описание фильма");
            }
            if (newFilm.getName() != null) {
                oldFilm.setName(newFilm.getName());
                log.info("Изменили название фильма");
            }

            return oldFilm;

        } else {
            log.error("Указан несуществующий id: {}", newFilm.getId());
            throw new NotFoundException("Фильм с указанным Id не найден");
        }
    }

    public Collection<Film> getFilms() {
        return filmStorage.valueFilms();
    }

    private long getNextId() {
        long currentMaxId = filmStorage.getFilmsId()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
