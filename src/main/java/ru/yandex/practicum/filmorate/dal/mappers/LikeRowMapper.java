package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Like.builder()
                .filmId(rs.getLong("film_id"))
                .userId(rs.getLong("user_id"))
                .build();
    }
}
