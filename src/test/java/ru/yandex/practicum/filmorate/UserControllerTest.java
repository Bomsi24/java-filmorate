package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    public void init() {
        userController = new UserController();
        user = new User(1L, "test@mail.ru", "login", "Тест",
                LocalDate.of(2000, 1, 1));
    }

    @Test
    public void testCreateUserWithNullEmail() {
        user.setEmail(null);
        assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    public void testCreateUserWithIncorectEmail() {
        user.setEmail("testmail.ru");
        assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    public void testCreateUserWithBlankLogin() {
        user.setLogin("");
        assertThrows(ValidationException.class, () ->
                userController.create(user));
    }

    @Test
    public void testCreateUserWithIncorectLogin() {
        user.setLogin("login login");
        assertThrows(ValidationException.class, () ->
                userController.create(user));
    }

    @Test
    public void testCreateUserWithTheWrongDateOfBirth() {
        user.setBirthday(LocalDate.of(2023,1,1));
        assertThrows(ValidationException.class, () ->
                userController.create(user));
    }

    @Test
    public void testUpdateIdNotSpecified() {
        userController.create(user);
        user.setId(null);
        assertThrows(ValidationException.class, () ->
                userController.update(user));
    }

    @Test
    public void testUpdateFalscheEmail() {
        userController.create(user);
        user.setEmail("");
        assertThrows(ValidationException.class, () ->
                userController.update(user));
    }

    @Test
    public void testUpdateExistingMail() {
        userController.create(user);
        user.setEmail("test@mail.ru");
        assertThrows(ValidationException.class, () ->
                userController.update(user));
    }

    @Test
    public void testUpdateNonExistentId() {
        userController.create(user);
        user.setId(3L);
        assertThrows(ValidationException.class, () ->
                userController.update(user));
    }
}
