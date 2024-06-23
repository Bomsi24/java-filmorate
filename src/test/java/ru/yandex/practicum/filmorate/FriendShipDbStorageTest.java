package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.storage.FriendShipDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.model.FriendShip;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FriendShipDbStorage.class, UserDbStorage.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dal"})
public class FriendShipDbStorageTest {
    @Autowired
    private final FriendShipDbStorage friendShipDbStorage;
    @Autowired
    private final UserDbStorage userDbStorage;

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void saveFriendShipTest() {
        final User user = userDbStorage.getUser(1L).get();
        final User user2 = userDbStorage.getUser(2L).get();

        final FriendShip friendShip = new FriendShip(user.getId(), user2.getId());

        friendShipDbStorage.save(friendShip);

        final Optional<FriendShip> friendShip1 = friendShipDbStorage.findFriendShip(user.getId(), user2.getId());

        assertTrue(friendShip1.isPresent());
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void deleteFriendShipTest() {
        final User user = userDbStorage.getUser(1L).get();
        final User user2 = userDbStorage.getUser(2L).get();

        final FriendShip friendShip = new FriendShip(user.getId(), user2.getId());

        friendShipDbStorage.save(friendShip);

        final Optional<FriendShip> friendShip1 = friendShipDbStorage.findFriendShip(user.getId(), user2.getId());
        assertTrue(friendShip1.isPresent());

        friendShipDbStorage.delete(user.getId(), user2.getId());

        final Optional<FriendShip> friendShip2 = friendShipDbStorage.findFriendShip(user.getId(), user2.getId());
        assertTrue(friendShip2.isEmpty());
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void getFriendsTest() {
        final User user = userDbStorage.getUser(1L).get();
        final User user2 = userDbStorage.getUser(2L).get();
        final User user3 = userDbStorage.getUser(3L).get();

        final FriendShip friendShip = new FriendShip(user.getId(), user2.getId());
        final FriendShip friendShip2 = new FriendShip(user.getId(), user3.getId());

        friendShipDbStorage.save(friendShip);
        friendShipDbStorage.save(friendShip2);

        final List<User> users = friendShipDbStorage.getFriends(user.getId());

        assertEquals(users.get(0), user2);
        assertEquals(users.get(1), user3);
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void getMutualFriendsTest() {
        final User user = userDbStorage.getUser(1L).get();
        final User user2 = userDbStorage.getUser(2L).get();
        final User user3 = userDbStorage.getUser(3L).get();
        final User user4 = userDbStorage.getUser(4L).get();

        final FriendShip friendShip = new FriendShip(user.getId(), user2.getId());
        final FriendShip friendShip2 = new FriendShip(user.getId(), user3.getId());
        final FriendShip friendShip3 = new FriendShip(user4.getId(), user2.getId());
        final FriendShip friendShip4 = new FriendShip(user4.getId(), user3.getId());

        friendShipDbStorage.save(friendShip);
        friendShipDbStorage.save(friendShip2);
        friendShipDbStorage.save(friendShip3);
        friendShipDbStorage.save(friendShip4);

        final List<User> mutualFriends = friendShipDbStorage.getMutualFriends(user.getId(), user4.getId());

        assertEquals(mutualFriends.get(0), user2);
        assertEquals(mutualFriends.get(1), user3);
    }
}
