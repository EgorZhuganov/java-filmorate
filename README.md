# Как кинопоиск только попроще
#### Инструкция по запуску (используемая DB - PostgreSQL):
1. Склонировать репозиторий
2. Открыть консоль базы данных и вставить: create database filmorate_repository
3. Перейти в Query console filmorate_repository и вставить: create schema filmorate
4. Перейти в src/main/resources/application.yml проекта и вставить данные в: url, username, password, относящиеся к вашей БД
5. Чтобы сразу посмотреть, как работают эндпоинты (о них дальше) можно открыть src/test/resources/for-test-data.sql скопировать все данные в файле и вставить их в консоли DB filmorate_repository.filmorate

## С помощью проекта можно:
- Храненить список фильмов в базе данных
- Храненить список пользователей в базе данных
- Использовать API для работы через HTTP

## Фичи
### Для фильма
- CRUD операции для фильма
- Найти N отсортированных по популярности фильмов
- Найти все общие фильмы между двумя пользователями отсортированные по популярности
### Для пользователя
- CRUD операции
- Может добавить/удалить лайк фильму
- Может добавить/удалить друга
- Может найти список общих друзей
- Может получить список рекомендуемых фильмов к просмотру

## Эндпоинты
Все эндпоинты начинаются с http://localhost:8080
### Относящиеся к фильмам
- /films - (GET: найти все фильмы, POST: добавить фильм, PUT: обновить фильм), /films/1 - (GET: найти фильм с id 1, DELETE: удалить фильм с id 1) 
- /films/{filmId}/like/{userId} - /films/1/like/2 (PUT: добавить лайк фильму 1 от юзера 2. DELETE: удалить лайк)
- /films/popular - (GET: найти 10 фильмов с наибольшим количеством лайков)
- /films/popular?count={count} - /films/popular?count=200 (GET: найти 200 фильмов с наибольшим количеством лайков)
- /films/common?userId={userId}&otherUserId={otherUserId} - common?userId=2&otherUserId=4 (GET: найти общие фильмы у пользователя 2 и 4 отсортированные по колиечеству лайков в порядке убывания)

### Относящиеся к пользователям
- /users - (GET: найти всех пользователей, POST: добавить пользователя, PUT: обновить пользователя), /users/1 - (GET: найти пользователя с id 1, DELETE: удалить пользователя с id 1) 
- /users/{id}/friends - /users/1/friends (GET: найти всех друзей пользователя 1) 
- /users/{id}/friends/{friendId} - /users/1/friends/3 (PUT: пользователь 1 добавляет в друзья пользователя 3, DELETE: пользователь 1 удаляет из друзей пользователя 3)
- /users/{id}/friends/common/{otherUserId} - /users/1/friends/common/3 (GET: найти общих пользователей между пользователем 1 и 3)
- /users/{userId}/recommendations - users/1/recommendations (GET: найти для пользователя рекомендуемые к просмотру фильмы)


## Технологии
- Java 17
- Maven
- Spring (SpringBoot, SprintTest, SpringValidation)
- JUnit 5

## Отношение

- mpa - рейтинг фильма, подробнее здесь: https://www.mtsu.edu/first-amendment/article/1247/motion-picture-ratings
- film - таблица фильмов
- users - таблица пользователей
- genre - таблица жанров, например (комедия, ужасы, боевик и т.д.)
- likes - таблица many-to-many между фильмом и пользователями, которые поставили лайк фильму
- film_genre - таблица many-to-many между жанром и фильмом
- user_friend - таблица many-to-many между пользователями, отображает друзей пользователя

![relationship_filmorate](https://user-images.githubusercontent.com/85733978/176130929-94696cc8-358a-4957-a74c-b2085c840c28.png)
