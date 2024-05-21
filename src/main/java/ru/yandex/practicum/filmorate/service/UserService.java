package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final InMemoryUserStorage userStorage;

    public User getUser(Long id) {
        if (!userStorage.containsUserId(id)) {
            log.error("Отсутствует пользователь");
            throw new NotFoundException("Отсутствует пользователь");
        }
        return userStorage.getUser(id);
    }

    public User addFriends(Long id, Long friendId) {
        if (!(userStorage.containsUserId(id) && userStorage.containsUserId(friendId))) {
            log.error("Отсутствует пользователь");
            throw new NotFoundException("Отсутствует пользователь");
        }
        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(friendId);
        log.info("Пользователи: 1{},2{}", user, userFriend);

        userFriend.addFriends(id);
        user.addFriends(friendId);
        log.info("Добавлен пользователь 1:{}, 2:{}", user.getFriends(), userFriend.getFriends());
        return user;

    }

    public User deleteFriend(Long id, Long friendId) {
        if (!(userStorage.containsUserId(id) && userStorage.containsUserId(friendId))) {
            throw new NotFoundException("Отсутствует пользователь");
        }
        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(friendId);
        log.info("Пользователи: 1{},2{}", user, userFriend);

        user.removeFriend(friendId);
        userFriend.removeFriend(id);
        log.info("Список друзей после удаления: user: {}, userFriend: {}",
                user.getFriends(), userFriend.getFriends());
        return user;
    }

    public List<User> allFriends(Long id) {
        User user = userStorage.getUser(id);
        if (user == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
        Set<Long> userFriends = user.getFriends();
        log.info("Список друзей {}", userFriends);

        return userFriends.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public List<User> allMutualFriends(Long id, Long otherId) {
        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(otherId);
        if (user == null || userFriend == null) {
            log.error("Нету одного из пользователей");
            throw new NotFoundException("Нету одного из пользователей ");
        }

        return user.getFriends().stream()
                .filter(idUser -> userFriend.getFriends().contains(idUser))
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Указана пустая почта");
            throw new ValidationException("электронная почта не может быть пустой");

        } else if (!(user.getEmail().contains("@"))) {
            log.error("Указан неверный формат почты");
            throw new ValidationException("электронная почта должна содержать символ @");

        } else if (user.getLogin() == null || user.getLogin().isBlank() ||
                user.getLogin().contains(" ")) {
            log.error("неверно указан логин");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");

        } else if (user.getBirthday().isAfter(LocalDate.of(2022, 1, 1))) {
            log.error("указана неправильная дата рождения");
            throw new ValidationException("дата рождения не может быть в будущем");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя пользователя не указанно");
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        log.debug("Создан и присвоен id{}", user.getId());
        userStorage.putUsers(user.getId(), user);
        log.info("Создан и сохранен новый пользователь");
        return user;
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.error("Не указан id пользователя");
            throw new ValidationException("Id должен быть указан");
        }
        if (userStorage.containsUserId(newUser.getId())) {
            User oldUser = userStorage.getUser(newUser.getId());
            if ((newUser.getEmail() == null || newUser.getEmail().isBlank())) {
                log.error("Указан некорректный эмейл");
                throw new ValidationException("Неверный эмейл");
            }
            if (isDuplicatedUserEmail(newUser)) {
                log.error("Указана существующая почта");
                throw new ValidationException("Указан существующий эмейл");
            }
            if (newUser.getEmail() != null) {
                log.info("Обновили эмейл");
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getName() != null) {
                log.info("Обновили имя пользователя");
                oldUser.setName(newUser.getName());
            }
            if (newUser.getLogin() != null) {
                log.info("Обновили логин");
                oldUser.setLogin(newUser.getLogin());
            }
            if (newUser.getBirthday() != null) {
                log.info("Обновили дату рождения");
                oldUser.setBirthday(newUser.getBirthday());
            }

            return oldUser;
        } else {
            log.error("Не найден пользователь с id {}", newUser.getId());
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
    }

    public Collection<User> getUser() {
        return userStorage.valueUsers();
    }

    private boolean isDuplicatedUserEmail(User user) {
        List<String> usersEmail = userStorage.valueUsers().stream()
                .map(User::getEmail)
                .toList();

        return usersEmail.contains(user.getEmail());
    }

    private long getNextId() {
        long currentMaxId = userStorage.getUsersId()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
