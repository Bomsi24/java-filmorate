CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS friends (
   user1_id BIGINT REFERENCES users (user_id),
   user2_id BIGINT REFERENCES users (user_id),
   status_friend INT,
   FOREIGN KEY (user1_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS mpa_table (
    mpa_id BIGINT PRIMARY KEY,
    mpa_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    release_date DATE,
    duration INT CHECK (duration > 0),
    mpa_id BIGINT,
    FOREIGN KEY (mpa_id) REFERENCES mpa_table(mpa_id)
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films_genres (
    film_id BIGINT,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id),
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);


