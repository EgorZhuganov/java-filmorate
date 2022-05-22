--liquibase formatted sql

--changeset zhuganov:1
create table genre
(
    genre_id smallserial primary key,
    name     varchar(30) not null
);
--changeset zhuganov:2
create table users
(
    user_id  bigserial primary key,
    email    varchar(128) not null unique,
    login    varchar(40) unique,
    name     varchar(128) not null,
    birthday date         not null
);
--changeset zhuganov:3
create table user_friend
(
    id        bigserial primary key,
    user_id   bigint references users (user_id) not null,
    friend_id bigint references users (user_id) not null,
    is_Friend boolean                           not null,
    UNIQUE (user_id, friend_id)

);
--changeset zhuganov:4
create table film
(
    film_id      bigserial primary key,
    name         varchar(150)    not null,
    description  varchar(200)    not null,
    release_date date            not null check ( release_date > '1895-12-28'),
    duration     interval minute not null check ( duration > '1' ),
    rating       varchar(6)      not null
);
--changeset zhuganov:5
create table likes
(
    id      bigserial primary key,
    user_id bigint references users (user_id) not null,
    film_id bigint references film (film_id)  not null,
    UNIQUE (user_id, film_id)

);
--changeset zhuganov:6
create table film_genre
(
    id       bigserial primary key,
    film_id  bigint references film (film_id)     not null,
    genre_id smallint references genre (genre_id) not null,
    UNIQUE (film_id, genre_id)
);