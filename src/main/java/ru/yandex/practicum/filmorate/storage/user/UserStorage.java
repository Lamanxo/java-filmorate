package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.UserIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {

    Collection<User> getAllUsers();

    User createUser(User user) throws ValidationException, UserIdException;

    User update(User user) throws ValidationException, UserIdException;

    void deleteAllUsers();

    void deleteUserById(long id) throws UserIdException;

    User getUserById(long id) throws UserIdException;
}
