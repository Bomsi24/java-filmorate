package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mapper.UserMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.FriendShip;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dal.storage.FriendShipDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.UserDbStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserDbStorage userDbStorage;
    private final FriendShipDbStorage friendShipDbStorage;

    public User getUser(Long id) {
        return checkUsers(id);
    }

    public User addFriends(Long userId, Long friendId) {
        User user = checkUsers(userId);
        checkUsers(friendId);

        Optional<FriendShip> friendShip = friendShipDbStorage.findFriendShip(userId, friendId);
        log.info("Проверяем друга{} ", friendShip);
        if (friendShip.isPresent()) {
            log.error("Пользователь: {} уже состоит в дружбе с пользователем: {}", friendId, userId);
            throw new ValidationException("Пользователь: " + friendId
                    + " уже состоит в дружбе с пользователем: " + userId);
        }

        FriendShip newFriendShip = new FriendShip(userId, friendId);
        friendShipDbStorage.save(newFriendShip);
        log.info("Пользователь {} добавлен в список друзей пользователя {}", friendId, userId);
        log.info("Друзья {}", newFriendShip);

        return user;
    }

    public User deleteFriend(Long userId, Long friendId) {
        User user = checkUsers(userId);
        checkUsers(friendId);
        friendShipDbStorage.delete(userId, friendId);
        return user;
    }

    public List<User> allFriends(Long id) {
        log.info("Используем allFriends ");
        checkUsers(id);
        log.info("Возвращаем список друзей пользователя с id:{}", id);
        log.info("Список друзей{}", friendShipDbStorage.getFriends(id));

        return friendShipDbStorage.getFriends(id);
    }

    public List<User> allMutualFriends(Long userId1, Long userId2) {
        log.info("Используем allMutualFriends ");
        checkUsers(userId1);
        checkUsers(userId2);

        return friendShipDbStorage.getMutualFriends(userId1, userId2);
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Имейл должен быть указан");
        }

        Optional<User> newUser = userDbStorage.findByEmail(user.getEmail());
        if (newUser.isPresent()) {
            log.error("Почта{} уже используется", user.getEmail());
            throw new ValidationException("Данная почта " + user.getEmail()
                    + " уже используется");
        }
        userDbStorage.save(user);
        log.info("Создан и сохранен новый пользователь {}", user.getId());

        return user;
    }

    public User update(User user) {
        User oldUser = checkUsers(user.getId());
        Optional<User> user1 = userDbStorage.findByEmail(user.getEmail());
        if (user1.isPresent()) {
            log.error("Почта{} уже используется", user.getEmail());
            throw new ValidationException("Данная почта " + user.getEmail()
                    + " уже используется");
        }

        User newUser = UserMapper.updateUser(oldUser, user);
        log.info("Обновили данные о пользователе{}", user.getId());
        return userDbStorage.update(newUser);
    }

    public List<User> getUsers() {
        log.info("Значение{}", userDbStorage.allUsers());
        return userDbStorage.allUsers();
    }

    private User checkUsers(Long user) {
        return userDbStorage.getUser(user).orElseThrow(() -> {
            log.error("Пользователь не найден: {}", user);
            return new NotFoundException("Пользователь не найден: " + user);
        });
    }
}
