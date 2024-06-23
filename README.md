# java-filmorate
## База данных
1. Пользователи: Эта таблица хранит информацию о пользователях приложения.
2. Фильмы: Эта таблица хранит информацию о фильмах. 
3. Друзья: Хранит id друзей пользователя и статус подтвержденгия дружбы.
4. Лайки: Эта таблица хранит id пользователей которые положительно оценили фильм.
5. Жанры: Эта таблица хранит различные жанры фильмов.

![База данных Filmorate](https://i.ibb.co/MZhVbr3/image.jpg)

### Примеры запросов к базе данных:

Выводит имена друзей для пользователя с имейл адресом super@mail.ru.
```SQL
SELECT friend.name AS friend_name
FROM users AS u
JOIN friends AS f ON f.user1_id = u.user_id
JOIN users AS friend ON friend.user_id = f.user2_id
WHERE u.email = 'super@mail.ru';
```
Вывод названия жанров у фильма Аврора.
```SQL
SELECT g.name
FROM films as f
JOIN films_genres AS fg ON fg.film_id = f.film_id
JOIN genres AS g ON g.genre_id = genf.genre_id
WHERE f.name = 'Аврора';
```
Вывод названия фильмов и количества лайков у каждого фильма, отсортированные по убыванию лайков.
```SQL
SELECT f.name, COUNT(l.user_id) AS like_count
FROM films AS f
JOIN likes AS l ON l.film_id = f.film_id
GROUP BY f.name
ORDER BY like_count DESC;
```
