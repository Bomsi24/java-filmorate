package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT film_id, name, description, release_date, duration\n" +
            "FROM films\n" +
            "WHERE film_id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, " +
            "release_date = ?, duration = ? WHERE film_id = ?";
    JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, JdbcTemplate jdbcTemplate) {
        super(jdbc, mapper, Film.class);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film save(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
        );
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );
        return film;
    }

    @Override
    public List<Film> valueFilms() {
        return jdbc.query(FIND_ALL_QUERY, new FilmRowMapper());
    }

    @Override
    public Optional<Film> getFilm(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }
}
