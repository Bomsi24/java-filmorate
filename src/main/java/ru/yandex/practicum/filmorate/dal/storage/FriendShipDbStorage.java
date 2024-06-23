package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.FriendShip;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class FriendShipDbStorage extends BaseDbStorage<FriendShip> {
    private static final String INSERT_QUERY = "INSERT INTO friends(user1_id, user2_Id, status_friend)" +
            "VALUES (?, ?, ?)";
    private static final String FIND_BY_FRIENDS = "SELECT * FROM friends WHERE user1_id = ? AND user2_id = ? " +
            "AND status_friend= 1";
    private static final String DELETE_FRIENDS = "DELETE FROM friends WHERE user1_id =? AND user2_id =?" +
            " AND status_friend = 1";
    private static final String FIND_MUTUAL_FRIENDS = "SELECT users.* \n" +
            "FROM users \n" +
            "JOIN (\n" +
            "    SELECT DISTINCT f1.user2_id AS common_friend \n" +
            "    FROM friends f1 \n" +
            "    JOIN friends f2 ON f1.user2_id = f2.user2_id \n" +
            "    WHERE f1.user1_id = ? AND f2.user1_id = ? \n" +
            "    AND f1.status_friend = 1 AND f2.status_friend = 1\n" +
            ") common_friends \n" +
            "ON users.user_id = common_friends.common_friend";
    private static final String FIND_ALL_FRIENDS_BY_ID = "SELECT * FROM users WHERE user_id IN " +
            "(SELECT user2_id FROM friends WHERE user1_id = ? AND status_friend = 1)";
    private final JdbcTemplate jdbcTemplate;

    public FriendShipDbStorage(JdbcTemplate jdbc, RowMapper<FriendShip> mapper, JdbcTemplate jdbcTemplate) {
        super(jdbc, mapper, FriendShip.class);
        this.jdbcTemplate = jdbcTemplate;
    }

    public FriendShip save(FriendShip friendShip) {
        add(
                INSERT_QUERY,
                friendShip.getUser1Id(),
                friendShip.getUser2Id(),
                friendShip.getStatusFriend());
        return friendShip;
    }

    public Optional<FriendShip> findFriendShip(long id, long friendId) {
        return findOne(FIND_BY_FRIENDS, id, friendId);
    }

    public boolean delete(long id, long friendId) {
        int rowsDeleted = jdbc.update(DELETE_FRIENDS, id, friendId);
        return rowsDeleted > 0;
    }


    public List<User> getFriends(long userId) {
        return jdbcTemplate.query(FIND_ALL_FRIENDS_BY_ID, new UserRowMapper(), userId);
    }


    public List<User> getMutualFriends(long userId1, long userId2) {
        return jdbcTemplate.query(FIND_MUTUAL_FRIENDS, new UserRowMapper(), userId1, userId2);
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
