package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Set<User> users = new HashSet<>();
    private int id = 0;

    private void idGen() {
        if (users.size() != id) {
            id = users.size();
        }

        id++;
    }

    @GetMapping
    public Set<User> find() {
        log.info("All users{}", users.toString());
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("User name was empty and replaced with login{}", user.toString());
        }
        idGen();
        user.setId(id);
        validate(user);
        users.add(user);
        log.info("User was created{}", user.toString());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("User name was empty and replaced with login{}", user.toString());
        }
        validate(user);
        for (User u : users) {
            if (u.getId() == user.getId()) {
                users.remove(u);
                users.add(user);
                log.info("User was updated{}", user.toString());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        return user;
    }

    private void validate(User user) throws ValidationException {
        if (user.getEmail().isBlank() || user.getEmail().isEmpty()) {
            log.error("Email field is empty{}", user.toString());
            throw new ValidationException("Email field is empty");
        } else if (!user.getEmail().contains("@")) {
            log.error("Wrong email without <@>{}", user.toString());
            throw new ValidationException("Wrong email without <@>");
        } else if (user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Login field is empty or contains spaces{}", user.toString());
            throw new ValidationException("Login field is empty or contains spaces");
        } else if (user.getBirthday().isAfter(LocalDate.now()) || user.getId() < 0) {
            log.error("Birthdate error{}", user.toString());
            throw new ValidationException("Birthdate error");
        }

    }

}
