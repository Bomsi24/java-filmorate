package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendShip;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendShipMapper implements RowMapper<FriendShip> {
    @Override
    public FriendShip mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FriendShip.builder()
                .user1Id(rs.getLong("user1_id"))
                .user2Id(rs.getLong("user2_id"))
                .build();
    }
}
