package ru.yandex.practicum.filmorate.repository.database;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class UserDao implements AbstractRepository<Long, User> {

    private final JdbcTemplate jdbcTemplate;
    private final FriendDao friendDao;
    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE  user_id = ?
            """;
    private static final String INSERT_SQL = """
            INSERT INTO users (email, login, name, birthday)
            VALUES (?,?,?,?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE users
            SET name = ?,
                email = ?,
                login = ?,
                birthday = ?
            WHERE user_id = ?;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT user_id, email, login, name, birthday
            FROM users
            WHERE user_id = ?;
            """;
    private static final String FIND_ALL_USERS_SQL = """
            SELECT user_id, email, login, name, birthday
            FROM users;
            """;

    @Override
    public User insert(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_SQL, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setObject(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public boolean delete(Long id) {
        friendDao.deleteAllFriends(id);
        return jdbcTemplate.update(DELETE_SQL, id) > 0;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(UPDATE_SQL,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        SqlRowSet userAsRowSet = jdbcTemplate.queryForRowSet(FIND_BY_ID_SQL, id);
        User user = null;
        if (userAsRowSet.next()) {
            user = User.builder()
                    .id(userAsRowSet.getLong("user_id"))
                    .email(userAsRowSet.getString("email"))
                    .login(userAsRowSet.getString("login"))
                    .name(userAsRowSet.getString("name"))
                    .birthday(userAsRowSet.getDate("birthday").toLocalDate())
                    .build();

            user.setFriends(new HashSet<>(friendDao.findAllFriend(user.getId())));
        }
        return ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        SqlRowSet userAsRowSet = jdbcTemplate.queryForRowSet(FIND_ALL_USERS_SQL);
        List<User> usersList = new ArrayList<>();
        while (userAsRowSet.next()) {
            User user = User.builder()
                    .id(userAsRowSet.getLong("user_id"))
                    .email(userAsRowSet.getString("email"))
                    .login(userAsRowSet.getString("login"))
                    .name(userAsRowSet.getString("name"))
                    .birthday(userAsRowSet.getDate("birthday").toLocalDate())
                    .build();

            user.setFriends(new HashSet<>(friendDao.findAllFriend(user.getId())));
            usersList.add(user);
        }
        return usersList;
    }
}
