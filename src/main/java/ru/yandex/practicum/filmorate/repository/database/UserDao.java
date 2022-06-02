package ru.yandex.practicum.filmorate.repository.database;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class UserDao implements Dao<Long, User> {

    private final JdbcTemplate jdbcTemplate;
    private static final String DELETE_SQL = """
            DELETE FROM USERS
            WHERE  USER_ID = ?
            """;

    private static final String INSERT_SQL = """
            INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
            VALUES (?,?,?,?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE USERS
            SET NAME = ?,
                EMAIL = ?,
                LOGIN = ?,
                BIRTHDAY = ?
            WHERE USER_ID = ?;
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT EMAIL,
                   LOGIN,
                   NAME,
                   BIRTHDAY
            FROM USERS
            WHERE USER_ID = ?;
            """;

    private static final String FIND_ALL_FRIENDS = """
            SELECT USER_ID
            FROM USERS
            WHERE USER_ID IN (SELECT uf.FRIEND_ID
                             FROM USER_FRIEND uf
                             WHERE USER_ID = ?);
            """;

    @Override
    public User insert(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_SQL, new String[]{"FILM_ID"});
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
        return jdbcTemplate.update(DELETE_SQL, id) > 0;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(UPDATE_SQL,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday()
        );
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        SqlRowSet userAsRowSet = jdbcTemplate.queryForRowSet(FIND_BY_ID_SQL);
        SqlRowSet friendsAsRowSet = jdbcTemplate.queryForRowSet(FIND_ALL_FRIENDS);
        User user = null;
        Set<Long> friendsList = new HashSet<>();
        if (friendsAsRowSet.next()) {
            Long friendId = friendsAsRowSet.getLong("USER_ID");
            friendsList.add(friendId);
        }

        if (userAsRowSet.next()) {
            user = User.builder()
                    .email(userAsRowSet.getString("name"))
                    .login(userAsRowSet.getString("login"))
                    .name(userAsRowSet.getString("name"))
                    .birthday(userAsRowSet.getDate("birthday").toLocalDate())
                    .friends(friendsList)
                    .build();
        }
        return ofNullable(user);
    }
}
