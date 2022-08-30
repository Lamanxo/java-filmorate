package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.UserIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    private void idGen() {
        if (users.size() != id) {
            id = users.size();
        }
        id++;
    }
    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }
    @Override
    public User createUser(User user) throws ValidationException, UserIdException {
        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("User name was empty and replaced with login{}", user.toString());
        }
        idGen();
        user.setId(id);
        validate(user);
        user.setFriendsId(new HashSet<>());
        users.put(id, user);
        log.info("User was created{}", user.toString());
        return user;
    }
    @Override
    public User update(User user) throws ValidationException, UserIdException {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("User name was empty and replaced with login{}", user.toString());
        }
        validate(user);
            if (users.containsKey(user.getId())) {
                user.setFriendsId(new HashSet<>());
                users.put(user.getId(), user);
                log.info("User was updated{}", user.toString());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

        return user;
    }

    public void validate(User user) throws ValidationException, UserIdException {
        if (user.getEmail().isBlank() || user.getEmail().isEmpty()) {
            log.error("Email field is empty{}", user.toString());
            throw new ValidationException("Email field is empty");
        } else if (!user.getEmail().contains("@")) {
            log.error("Wrong email without <@>{}", user.toString());
            throw new ValidationException("Wrong email without <@>");
        } else if (user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Login field is empty or contains spaces{}", user.toString());
            throw new ValidationException("Login field is empty or contains spaces");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Birthdate error{}", user.toString());
            throw new ValidationException("Birthdate error");
        } else if ( user.getId() < 0) {
            log.error("Id must be greater than zero {}", user.getId());
            throw new UserIdException("Id must be greater than zero");
        }
    }
    
    @Override
    public void deleteAllUsers() {
        users.clear();
        log.info("All users was deleted{}", users.toString());
    }
    
    @Override
    public void deleteUserById(long id) throws UserIdException {
            if (users.containsKey(id)) {
                users.remove(id);
                log.info("User with id {} was removed", id);

            } else {
                log.error("Id {} not found", id);
                throw new UserIdException("Id not found");
            }

    }

    @Override
    public User getUserById(long id) throws UserIdException {
            if (users.containsKey(id)) {
                log.info("User with id {} was found", id);
                return users.get(id);
            }

        log.error("Id not found{}", id);
        throw new UserIdException("Id not found");
    }
}
