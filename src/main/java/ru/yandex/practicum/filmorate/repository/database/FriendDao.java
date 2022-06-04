package ru.yandex.practicum.filmorate.repository.database;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class FriendDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String ADD_FRIEND_SQL = """
            INSERT INTO user_friend (user_id, friend_id)
            VALUES (?, ?);
            """;
    private static final String DELETE_FRIEND_SQL = """
            DELETE FROM user_friend
            WHERE (USER_ID, FRIEND_ID) = (?, ?);
            """;
    private static final String DELETE_ALL_FRIEND_FROM_USER_SQL = """
            DELETE FROM user_friend
            WHERE user_id ?;
            """;
    private static final String FIND_ALL_FRIENDS_BY_ID_SQL = """
            SELECT user_id
            FROM users
            WHERE user_id IN (SELECT uf.friend_id
                             FROM user_friend uf
                             WHERE user_id = ?);
            """;

    public boolean insert(Long userId, Long friendId) {
        var insertRow = jdbcTemplate.update(ADD_FRIEND_SQL, userId, friendId);
        return insertRow > 0;
    }

    public boolean delete(Long userId, Long friendId) {
        return jdbcTemplate.update(DELETE_FRIEND_SQL, userId, friendId) > 0;
    }

    public void deleteAllFriends(Long id) {
        jdbcTemplate.update(DELETE_ALL_FRIEND_FROM_USER_SQL, id);
    }


    public Collection<Long> findAllFriend(Long userId) {
        SqlRowSet friendsAsRowSet = jdbcTemplate.queryForRowSet(FIND_ALL_FRIENDS_BY_ID_SQL, userId);
        Collection<Long> friends = new HashSet<>();
        while (friendsAsRowSet.next()) {
            Long friendId = friendsAsRowSet.getLong("user_id");
            friends.add(friendId);
        }
        return friends;
    }
}
