package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmLikeDbStorage extends BaseDbStorage<Like> {
    private static final String INSERT_QUERY = "INSERT INTO likes(film_id, user_id)" +
            "VALUES (?, ?)";
    private static final String FIND_BY_FRIENDS = "SELECT * FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String DELETE_FRIENDS = "DELETE FROM likes WHERE film_id =? AND user_id =?";
    private static final String COUNT_LIKES = "SELECT films.*, COUNT(likes.user_id) AS like_count\n" +
            "FROM films\n" +
            "JOIN likes ON films.film_id = likes.film_id\n" +
            "GROUP BY films.film_id\n" +
            "ORDER BY like_count DESC\n" +
            "LIMIT ?;";

    public FilmLikeDbStorage(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper, Like.class);
    }

    public Like save(Like like) {
        add(
                INSERT_QUERY,
                like.getFilmId(),
                like.getUserId()
        );
        return like;
    }

    public Optional<Like> findLike(long filmId, long userId) {
        return findOne(FIND_BY_FRIENDS, filmId, userId);
    }

    public boolean delete(long filmId, long friendId) {
        int rowsDeleted = jdbc.update(DELETE_FRIENDS, filmId, friendId);
        return rowsDeleted > 0;
    }

    public List<Film> getPopularFilms(int count) {
        return jdbc.query(COUNT_LIKES, new FilmRowMapper(), count);
    }

    private void add(String query, Object... params) {
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        });
    }
}
