package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(email, name, login, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, name = ?, login = ?, birthday = ?" +
            " WHERE user_id = ?";
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper, JdbcTemplate jdbcTemplate) {
        super(jdbc, mapper, User.class);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> allUsers() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new UserRowMapper());
    }

    @Override
    public Optional<User> getUser(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Optional<User> findByEmail(String email) {
        return findOne(FIND_BY_EMAIL_QUERY, email);
    }

    @Override
    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public User save(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }
}
