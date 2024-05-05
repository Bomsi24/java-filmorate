package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUser() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            log.error("Неверно указана почта");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");

        } else if (user.getLogin() == null || user.getLogin().isBlank() ||
                user.getLogin().contains(" ")) {
            log.error("неверно указан логин");
            throw new ValidationException("логин не может быть пустым и содержать пробелы;");

        } else if (user.getBirthday().isAfter(LocalDate.of(2022, 1, 1))) {
            log.error("указана неправильная дата рождения");
            throw new ValidationException("дата рождения не может быть в будущем.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя пользователя не указанно");
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        log.debug("Создан и присвоен id{}", user.getId());
        users.put(user.getId(), user);
        log.info("Создан и сохранен новый пользователь");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error("Не указан id пользователя");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
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
            throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean isDuplicatedUserEmail(User user) {
        List<String> usersEmail = users.values().stream()
                .map(User::getEmail)
                .toList();

        return usersEmail.contains(user.getEmail());
    }
}
