# java-filmorate
## База данных
1. Пользователи: Эта таблица хранит информацию о пользователях приложения.
2. Фильмы: Эта таблица хранит информацию о фильмах. 
3. Друзья: Хранит id друзей пользователя и статус подтвержденгия дружбы.
4. Лайки: Эта таблица хранит id пользователей которые положительно оценили фильм.
5. Жанры: Эта таблица хранит различные жанры фильмов.

![База данных Filmorate](https://i.ibb.co/9s7bJXk/film.png)

### Примеры запросов к базе данных:

Выводит имена друзей и статус их дружбы для пользователя с имейл адресом super@mail.ru.
```SQL
SELECT friend.name, f.status_friend
FROM user AS u
JOIN friend_table AS table ON table.user_id = u.user_id
JOIN friends AS f ON f.friend_id = table.friend_id
JOIN user AS friend ON f.friend_id = friend.user_id
WHERE u.email = 'super@mail.ru';
```
Вывод названия жанров у фильма Аврора.
```SQL
SELECT g.name
FROM film as f
JOIN genre_film AS genf ON genf.film_id = f.film_id
JOIN genre AS g ON g.genre_id = f.genre_id
WHERE f.name = 'Аврора';
```
Вывод названия фильмов и количества лайков у каждого фильма, отсортированные по убыванию лайков.
```SQL
SELECT f.name, COUNT(l.id_user)
FROM film AS f
JOIN film_like AS l ON l.film_id = f.film_id
GROUP BY f.name
ORDER BY COUNT(l.id_user) DESC;
```
