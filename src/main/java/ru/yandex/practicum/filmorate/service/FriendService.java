package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.userDto.UserReadDto;
import ru.yandex.practicum.filmorate.mapper.userMapper.UserReadMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.database.FriendDao;
import ru.yandex.practicum.filmorate.repository.database.UserDao;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendDao friendDao;
    private final UserDao userDao;
    private final UserReadMapper userReadMapper;

    /**
     *
     * @param userId - Long
     * @param friendId - Long
     * @return {@code Optional<UserReadDao>}
     * @throws UnsupportedOperationException if user tries to add himself as friend
     * @throws IllegalArgumentException if user already friends with another user
     */
    public Optional<UserReadDto> addToFriends(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new UnsupportedOperationException("attempt to add in friends user with the same id");
        }
        var maybeUser = userDao.findById(userId);
        var maybeFriend = userDao.findById(friendId);
        UserReadDto userReadDto = null;
        if (maybeUser.isPresent() && maybeFriend.isPresent()) {
            User user = maybeUser.get();
            User friend = maybeFriend.get();
            if (user.getFriends().contains(friendId)){
                log.warn("user {} already friend with user {}", user.getName(), friend.getName());
                throw new IllegalArgumentException(format(
                        "user %s already friend with user %s",user.getName(), friend.getName()));
            }
            user.getFriends().add(friend.getId());
            friendDao.insert(userId, friendId);
            userReadDto = userReadMapper.mapFrom(user);
        }
        return ofNullable(userReadDto);
    }

    public Optional<List<UserReadDto>> findAllFriends(Long userId) {
        return userDao.findById(userId)
                .map(user -> user
                        .getFriends()
                        .stream()
                        .map(userDao::findById)
                        .filter(Optional::isPresent)
                        .map(friend -> userReadMapper.mapFrom(friend.get()))
                        .collect(toList())
                );
    }

    public Optional<List<UserReadDto>> findAllCommonFriends(Long userId, Long otherUserId) {
        List<UserReadDto> commonFriends = null;
        var maybeUser = userDao.findById(userId);
        var maybeOtherUser = userDao.findById(otherUserId);
        if (maybeUser.isPresent() && maybeOtherUser.isPresent()) {
            Set<Long> userFriends = new HashSet<>(maybeUser.get().getFriends());
            Set<Long> otherUserFriends = new HashSet<>(maybeOtherUser.get().getFriends());
            userFriends.retainAll(otherUserFriends);
            commonFriends = userFriends
                    .stream()
                    .map(userDao::findById)
                    .filter(Optional::isPresent)
                    .map(commonFriend -> userReadMapper.mapFrom(commonFriend.get()))
                    .collect(toList());
        }
        return ofNullable(commonFriends);
    }

    /**
     *
     * @param userId - Long
     * @param friendId - Long
     * @return {@code Optional<UserReadDto>}
     * @throws UnsupportedOperationException if a user tries to remove himself as a friend
     */
    public Optional<UserReadDto> removeFromFriends(Long userId, Long friendId) throws UnsupportedOperationException {
        if (userId.equals(friendId)) {
            throw new UnsupportedOperationException("attempt to remove from friends user with the same id");
        }
        var maybeUser = userDao.findById(userId);
        var maybeFriend = userDao.findById(friendId);
        UserReadDto userReadDto = null;
        if (maybeUser.isPresent() && maybeFriend.isPresent()) {
            User user = maybeUser.get();
            user.getFriends().remove(friendId);
            friendDao.delete(userId, friendId);
            userReadDto = userReadMapper.mapFrom(user);
        }
        return ofNullable(userReadDto);
    }
}
