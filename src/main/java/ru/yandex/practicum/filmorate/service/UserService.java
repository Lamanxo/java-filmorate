package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.dao.FriendsDao;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Getter
@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendsDao friendsDao;

    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendsDao friendsDao) {
        this.userStorage = userStorage;
        this.friendsDao = friendsDao;
    }

    public void addFriend (long userId, long friendId) throws UserIdException {
        if (userId <= 0 || friendId <= 0) {
            log.debug("Check user {} check friend {}", userId, friendId);
            throw new UserIdException("User with id: " + userId + " or user friend with id: " + friendId + " not found");
        }
        friendsDao.addFriend(userId,friendId);
    }

    public void removeFriend(long userId, long friendId)  {
        if (userId <= 0 || friendId <= 0) {
            log.debug("Check user {} check friend {}", userId, friendId);
            throw new UserIdException("User with id: " + userId + " or user friend with id: " + friendId + " not found");
        }
        friendsDao.deleteFriend(userId,friendId);
    }

    public List<User> getAllFriends (long userId)  {
        if (userId <= 0) {
            log.debug("User {}", userId);
            throw new UserIdException(String.format("user with id:%s not found", userId));
        }
        List<User> allFriendsList = new ArrayList<>();
        for (long friendId : friendsDao.getFriends(userId)) {
            allFriendsList.add(userStorage.getUserById(friendId));
        }
        return allFriendsList;
    }

    public List<User> getMutualFriends(long userId, long friendId) throws UserIdException {
        Set<Long> setMutualFriends = new HashSet<>(userStorage.getUserById(friendId).getFriendsId());
        setMutualFriends.retainAll(userStorage.getUserById(userId).getFriendsId());
        return setMutualFriends
                .stream()
                .map(id -> {
                    try {
                        return userStorage.getUserById(id);
                    } catch (UserIdException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
