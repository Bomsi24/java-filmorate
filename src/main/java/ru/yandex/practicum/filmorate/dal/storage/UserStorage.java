package ru.yandex.practicum.filmorate.dal.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> allUsers();

    Optional<User> getUser(Long id);

    User update(User user);

    User save(User user);

}
