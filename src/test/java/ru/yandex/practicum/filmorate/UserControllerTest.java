package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    UserService userController;
    User user;

    @BeforeEach
    public void init() {
        userController = new UserService(new InMemoryUserStorage());
        user = new User(1L, "test@mail.ru", "login", "Тест",
                LocalDate.of(2000, 1, 1), Collections.emptySet());
    }

    @Test
    public void testCreateUserWithNullEmail() {
        user.setEmail(null);
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                userController.create(user));

        Assertions.assertEquals("электронная почта не может быть пустой",
                thrown.getMessage());
    }

    @Test
    public void testCreateUserWithIncorectEmail() {
        user.setEmail("testmail.ru");
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                userController.create(user));

        Assertions.assertEquals("электронная почта должна содержать символ @",
                thrown.getMessage());
    }

    @Test
    public void testCreateUserWithBlankLogin() {
        user.setLogin("");
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                userController.create(user));

        Assertions.assertEquals("логин не может быть пустым и содержать пробелы",
                thrown.getMessage());
    }

    @Test
    public void testCreateUserWithIncorectLogin() {
        user.setLogin("login login");
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                userController.create(user));

        Assertions.assertEquals("логин не может быть пустым и содержать пробелы",
                thrown.getMessage());
    }

    @Test
    public void testCreateUserWithTheWrongDateOfBirth() {
        user.setBirthday(LocalDate.of(2023, 1, 1));
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                userController.create(user));

        Assertions.assertEquals("дата рождения не может быть в будущем",
                thrown.getMessage());
    }

    @Test
    public void testUpdateIdNotSpecified() {
        userController.create(user);
        user.setId(null);
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                userController.update(user));

        Assertions.assertEquals("Id должен быть указан", thrown.getMessage());
    }

    @Test
    public void testUpdateFalscheEmail() {
        userController.create(user);
        user.setEmail("");
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                userController.update(user));

        Assertions.assertEquals("Неверный эмейл", thrown.getMessage());
    }

    @Test
    public void testUpdateExistingMail() {
        userController.create(user);
        user.setEmail("test@mail.ru");
        ValidationException thrown = assertThrows(ValidationException.class, () ->
                userController.update(user));

        Assertions.assertEquals("Указан существующий эмейл", thrown.getMessage());
    }

    @Test
    public void testUpdateNonExistentId() {
        userController.create(user);
        user.setId(3L);
        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                userController.update(user));

        Assertions.assertEquals("Пользователь с id = 3 не найден", thrown.getMessage());
    }
}
