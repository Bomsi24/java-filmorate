package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    void putUsers(Long id, User user);

    List<User> valueUsers();

    boolean containsUserId(Long id);

    User getUser(Long id);

    Set<Long> getUsersId();
}
