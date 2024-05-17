package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.data.UserRepository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserRepository repository = new UserRepository();

    @GetMapping
    public Collection<User> getUser() {
        return repository.valueUsers();
    }

    @PostMapping
    public User create(@RequestBody User user) {
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
        repository.putUsers(user.getId(), user);
        log.info("Создан и сохранен новый пользователь");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error("Не указан id пользователя");
            throw new ValidationException("Id должен быть указан");
        }
        if (repository.containsUserId(newUser.getId())) {
            User oldUser = repository.getUser(newUser.getId());
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
        long currentMaxId = repository.getUsersId()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean isDuplicatedUserEmail(User user) {
        List<String> usersEmail = repository.valueUsers().stream()
                .map(User::getEmail)
                .toList();

        return usersEmail.contains(user.getEmail());
    }
}
