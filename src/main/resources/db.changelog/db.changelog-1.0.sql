--liquibase formatted sql

--changeset zhuganov:1
create table genre
(
    genre_id smallserial primary key,
    name     varchar(30) not null
);
--changeset zhuganov:2
create table subscriber
(
    subscriber_id bigint primary key,
    user_id       bigint not null
);
--changeset zhuganov:3
create table friend
(
    friend_id bigint primary key,
    user_id   bigint not null
);
--changeset zhuganov:4
create table users
(
    user_id       bigserial primary key,
    email         varchar(128) not null,
    login         varchar(40)  not null,
    name          varchar(128) not null,
    birthday      date         not null,
    friend_id     bigint references friend (friend_id),
    subscriber_id bigint references subscriber (subscriber_id)
);
--changeset zhuganov:5
create table film
(
    film_id      bigserial primary key,
    name         varchar(150)    not null,
    description  varchar(200)    not null,
    release_date date            not null check ( release_date > '1895-12-28'),
    duration     interval minute not null check ( duration > '1' ),
    rating       varchar(6)      not null,
    genre_id     int references genre (genre_id),
    like_id      bigint references users (user_id)
);
--changeset zhuganov:6
create table film_genre
(
    id       bigserial primary key,
    film_id  bigint references film (film_id)     not null,
    genre_id smallint references genre (genre_id) not null,
    UNIQUE (film_id, genre_id)
);