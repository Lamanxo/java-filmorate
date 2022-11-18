package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final UserService userService;


    @GetMapping
    public Collection<User> findAll() {
        return userService.getUserStorage().getAllUsers();
    }

    @PostMapping
    public User create(@RequestBody User user)  {
        return userService.getUserStorage().createUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException, UserIdException {
        return userService.getUserStorage().update(user);
    }

    @GetMapping("/{id}")
    public User find(@PathVariable("id") long id) throws UserIdException {
        return userService.getUserStorage().getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long id,
                          @PathVariable("friendId") long friendId) throws UserIdException {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") long id,
                             @PathVariable("friendId") long friendId) throws UserIdException {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") long id) throws UserIdException {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") long id,
                                       @PathVariable("otherId") long otherId) throws UserIdException {
        return userService.getMutualFriends(id, otherId);
    }


}
