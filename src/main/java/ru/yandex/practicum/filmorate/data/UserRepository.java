package ru.yandex.practicum.filmorate.data;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class UserRepository {
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
