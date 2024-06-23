package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Service
public class FilmGenreDbStorage extends BaseDbStorage<Genre> {
    private static final String FIND_ALL_GENRES = "SELECT * FROM genres";
    private static final String FIND_BY_ID_GENRE = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String GET_ALL_ID_GENRE = "SELECT * " +
            "FROM genres g " +
            "JOIN films_genres fg ON g.genre_id = fg.genre_id " +
            "WHERE fg.film_id = ?";
    private static final String INSERT_GENRE_BY_ID = "INSERT INTO films_genres (film_id, genre_id) VALUES(?, ?)";

    public FilmGenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper, Genre.class);
    }

    public void save(Film film, Genre genre) {
        add(
                INSERT_GENRE_BY_ID,
                film.getId(),
                genre.getId()
        );
    }

    public List<Genre> valueGenre() {
        return jdbc.query(FIND_ALL_GENRES, new GenreRowMapper());
    }

    public Optional<Genre> getGenre(Long id) {
        return findOne(FIND_BY_ID_GENRE, id);
    }

    public List<Genre> getGenresByIdFilm(long id) {
        return jdbc.query(GET_ALL_ID_GENRE, new GenreRowMapper(), id);
    }

    private void add(String query, Object... params) {
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        });
    }
}
