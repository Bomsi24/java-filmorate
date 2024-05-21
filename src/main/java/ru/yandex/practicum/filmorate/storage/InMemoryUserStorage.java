package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public void putUsers(Long id, User user) {
        users.put(id, user);
    }

    public Collection<User> valueUsers() {
        return users.values();
    }

    public boolean containsUserId(Long id) {
        return users.containsKey(id);
    }

    public User getUser(Long id) {
        return users.get(id);
    }

    public Set<Long> getUsersId() {
        return users.keySet();
    }
}
