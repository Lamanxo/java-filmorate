package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;


@Getter
@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend (long userId, long friendId) throws UserIdException {
        if (userId <= 0 || friendId <= 0) {
            log.debug("Check user {} check friend {}", userId, friendId);
            throw new UserIdException("User with id: " + userId + " or user friend with id: " + friendId + " not found");
        }
        userStorage.getUserById(userId).getFriendsId().add(friendId);
        userStorage.getUserById(friendId).getFriendsId().add(userId);
    }

    public void removeFriend(long userId, long friendId) throws UserIdException {
        if (userId <= 0 || friendId <= 0) {
            log.debug("Check user {} check friend {}", userId, friendId);
            throw new UserIdException("User with id: " + userId + " or user friend with id: " + friendId + " not found");
        }
        userStorage.getUserById(userId).getFriendsId().remove(friendId);
        userStorage.getUserById(friendId).getFriendsId().remove(userId);
    }

    public List<User> getAllFriends (long userId) throws UserIdException {
        List<User> allFriendsList = new ArrayList<>();
        for (long friendId : userStorage.getUserById(userId).getFriendsId()) {
            allFriendsList.add(userStorage.getUserById(friendId));
        }
        return allFriendsList;
    }

    public List<User> getMutualFriends(long userId, long friendId) throws UserIdException {
        List<User> mutualFriends = new ArrayList<>();
        for (User user : getAllFriends(userId)) {
            for (User user1 : getAllFriends(friendId)) {
                if (user.equals(user1)) {
                    mutualFriends.add(user);
                }
            }
        }
        return mutualFriends;
    }
}
