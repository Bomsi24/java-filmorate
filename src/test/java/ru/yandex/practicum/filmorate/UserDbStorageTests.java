package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDbStorage.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dal"})
public class UserDbStorageTests {

    @Autowired
    private final UserDbStorage userDbStorage;

    @Test
    void saveUserTest() {
        final User user = generateUser(
                "user1@yandex.ru", "user1", "User1 Name",
                LocalDate.of(1990, 03, 24));

        userDbStorage.save(user);

        final List<User> users = userDbStorage.allUsers();

        assertEquals(1, users.size());
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("email", "user1@yandex.ru");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("login", "user1");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("name", "User1 Name");
        assertThat(users.get(0)).hasFieldOrProperty("birthday");
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void updateUserTest() {
        final User user = generateUser("user3@yandex.ru", "user3", "User3 Name", LocalDate.of(1990, 03, 26));
        user.setId(1L);

        userDbStorage.update(user);

        final User updatedUser = userDbStorage.getUser(1L).get();

        assertThat(updatedUser).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(updatedUser).hasFieldOrPropertyWithValue("email", "user3@yandex.ru");
        assertThat(updatedUser).hasFieldOrPropertyWithValue("login", "user3");
        assertThat(updatedUser).hasFieldOrPropertyWithValue("name", "User3 Name");
        assertThat(updatedUser).hasFieldOrProperty("birthday");
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void allUsersTest() {
        final List<User> users = userDbStorage.allUsers();

        assertEquals(3, users.size());
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("email", "user1@yandex.ru");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("login", "user1");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("name", "User1 Name");
        assertThat(users.get(0)).hasFieldOrProperty("birthday");

        assertThat(users.get(1)).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("email", "user2@yandex.ru");
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("login", "user2");
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("name", "User2 Name");
        assertThat(users.get(1)).hasFieldOrProperty("birthday");
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void getUserByIdTest() {
        final User user = userDbStorage.getUser(1L).get();

        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(user).hasFieldOrPropertyWithValue("email", "user1@yandex.ru");
        assertThat(user).hasFieldOrPropertyWithValue("login", "user1");
        assertThat(user).hasFieldOrPropertyWithValue("name", "User1 Name");
        assertThat(user).hasFieldOrProperty("birthday");
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void getUserByEmailTest() {
        final User user = userDbStorage.findByEmail("user1@yandex.ru").get();

        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(user).hasFieldOrPropertyWithValue("email", "user1@yandex.ru");
        assertThat(user).hasFieldOrPropertyWithValue("login", "user1");
        assertThat(user).hasFieldOrPropertyWithValue("name", "User1 Name");
        assertThat(user).hasFieldOrProperty("birthday");
    }

    private User generateUser(
            String email,
            String login,
            String name,
            LocalDate birthday) {
        return User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }
}
