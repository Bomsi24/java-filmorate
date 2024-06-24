package ru.yandex.practicum.filmorate.dal.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public static User updateUser(User user, User oldUser) {
        if (oldUser.hasEmail()) {
            user.setEmail(oldUser.getEmail());
            log.info("Обновили эмейл: {}", oldUser.getEmail());
        }
        if (oldUser.hasLogin()) {
            user.setLogin(oldUser.getLogin());
            log.info("Обновли логин: {}", oldUser.getLogin());
        }
        if (oldUser.hasName()) {
            user.setName(oldUser.getName());
            log.info("Обновили Имя: {}", oldUser.getName());
        }
        if (oldUser.hasBirthday()) {
            user.setBirthday(oldUser.getBirthday());
            log.info("Обновили ДеньРождения: {}", oldUser.getBirthday());
        }

        return user;
    }
}
