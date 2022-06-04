create table if not exists mpa
(
    mpa_id bigint primary key,
    name   varchar(8) not null
);

create table if not exists genre
(
    genre_id integer auto_increment primary key,
    name     varchar(30) not null
);

create table if not exists users
(
    user_id  bigint auto_increment primary key,
    email    varchar(128) not null unique,
    login    varchar(40) unique,
    name     varchar(128) not null,
    birthday date         not null
);

create table if not exists user_friend
(
    id        bigint auto_increment primary key,
    user_id   bigint references users (user_id) not null,
    friend_id bigint references users (user_id) not null,
    UNIQUE (user_id, friend_id)

);

create table if not exists film
(
    film_id      bigint auto_increment primary key,
    name         varchar(150) not null,
    description  varchar(200) not null,
    release_date date         not null check (release_date > '1895-12-28'),
    duration     int          not null check (duration > '1'),
    mpa_id       varchar(6)   not null references mpa (mpa_id)
);

create table if not exists likes
(
    id      bigint auto_increment primary key,
    user_id bigint references users (user_id) not null,
    film_id bigint references film (film_id)  not null,
    UNIQUE (user_id, film_id)

);

create table if not exists film_genre
(
    id       bigint auto_increment primary key,
    film_id  bigint references film (film_id)     not null,
    genre_id smallint references genre (genre_id) not null,
    UNIQUE (film_id, genre_id)
);