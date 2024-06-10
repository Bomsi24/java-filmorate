# java-filmorate
1. Пользователи: Эта таблица хранит информацию о пользователях приложения.
2. Фильмы: Эта таблица хранит информацию о фильмах. 
3. Друзья: Хранит id друзей пользователя и статус подтвержденгия дружбы.
4. Лайки: Эта таблица хранит id пользователей которые положительно оценили фильм.
5. Жанры: Эта таблица хранит различные жанры фильмов.

![База данных Filmorate](https://i.ibb.co/6mJVCkT/filmorate.jpg)

Примеры запросов к базе данных:

Выводит имена друзей и статуса их дружбы для пользователя с имейл адресом super@mail.ru
```SQL
SELECT friend.name, f.status_friend
FROM user AS u
JOIN friend_table AS table ON table.user_id = u.user_id
JOIN friends AS f ON f.friend_id = table.friend_id
JOIN user AS friend ON f.friend_id = friend.user_id
WHERE u.email = 'super@mail.ru';
```
