--liquibase formatted sql

--changeset zhuganov:1
create table if not exists mpa
(
    mpa_id smallint primary key,
    name   varchar(8) not null
);
--changeset zhuganov:2
create table if not exists genre
(
    genre_id serial primary key,
    name     varchar(30) not null
);
--changeset zhuganov:3
create table if not exists users
(
    user_id  bigserial primary key,
    email    varchar(128) not null unique,
    login    varchar(40) unique,
    name     varchar(128) not null,
    birthday date         not null
);
--changeset zhuganov:4
create table if not exists user_friend
(
    id        bigserial primary key,
    user_id   bigint references users (user_id) not null,
    friend_id bigint references users (user_id) not null,
    UNIQUE (user_id, friend_id)

);
--changeset zhuganov:5
create table if not exists film
(
    film_id      bigserial primary key,
    name         varchar(150) not null,
    description  varchar(200) not null,
    release_date date         not null check (release_date > '1895-12-28'),
    duration     int          not null check (duration > '1'),
    mpa_id       smallint     not null references mpa (mpa_id)
);
--changeset zhuganov:6
create table if not exists likes
(
    id      bigserial primary key,
    user_id bigint references users (user_id) not null,
    film_id bigint references film (film_id)  not null,
    UNIQUE (user_id, film_id)

);
--changeset zhuganov:7
create table if not exists film_genre
(
    id       bigserial primary key,
    film_id  bigint references film (film_id)   not null,
    genre_id bigint references genre (genre_id) not null,
    UNIQUE (film_id, genre_id)
);

--changeset zhuganov:8
create table if not exists genre
(
    genre_id bigint primary key,
    name     varchar(30) not null
);
--changeset zhuganov:9
INSERT INTO mpa(mpa_id, name)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG13'),
       (4, 'R'),
       (5, 'NC17');

--changeset zhuganov:10
INSERT INTO genre(genre_id, name)
VALUES (1, 'Comedy'),
       (2, 'Horror'),
       (3, '18+'),
       (4, 'Drama'),
       (5, 'Fantasy'),
       (6, 'Documentary'),
       (7, 'Science fiction');