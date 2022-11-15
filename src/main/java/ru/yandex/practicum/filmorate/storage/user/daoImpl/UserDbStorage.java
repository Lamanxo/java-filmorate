package ru.yandex.practicum.filmorate.storage.user.daoImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.dao.FriendsDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbc;
    private final FriendsDao friendsDao;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbc, FriendsDao friendsDao) {
        this.jdbc = jdbc;
        this.friendsDao = friendsDao;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * from USERS";
        return jdbc.query(sql, this::makeUser);
    }

    private User makeUser(ResultSet rs, int ruwNum) throws SQLException {
        return new User(rs.getLong("USER_ID"),
                rs.getString("USER_EMAIL"),
                rs.getString("USER_LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("USER_BIRTHDAY").toLocalDate(),
                new HashSet<>(friendsDao.getFriends(rs.getLong("USER_ID")))
                );
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        validate(user);
        String sql = "insert into USERS (USER_NAME, USER_EMAIL, USER_LOGIN, USER_BIRTHDAY) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, user.getName(),user.getEmail(),user.getLogin(),user.getBirthday());
        String count = "select count(USER_ID) from USERS";
        user.setId(jdbc.queryForObject(count, Long.class));
        return user;

    }

    public void validate(User user)  {
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
    public User update(User user) {
        if (idCheck(user.getId()) == 0) {
            throw new UserIdException("User not found");
        }
        String sql = "update USERS set USER_NAME =?, USER_EMAIL =?, USER_LOGIN =?, USER_BIRTHDAY =? where USER_ID =?";
        jdbc.update(sql, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void deleteAllUsers() {
        String sql = "delete from USERS";
        jdbc.update(sql);
    }

    @Override
    public void deleteUserById(long id){
        if (idCheck(id) == 0) {
            throw new UserIdException("User not found");
        }
        String sql = "delete from USERS where user_id = ?";
        jdbc.update(sql, id);
    }

    private int idCheck (long id) {
        String sql = "select count(*) from USERS where USER_ID = ?";
        int response =jdbc.queryForObject(sql, Integer.class, id);
        return response;
    }

    @Override
    public User getUserById(long id) {
        SqlRowSet userRows = jdbc.queryForRowSet("select * from users where user_id = ?", id);

        if(userRows.next()) {
            User user = new User(
                    userRows.getLong("USER_ID"),
                    userRows.getString("USER_EMAIL"),
                    userRows.getString("USER_LOGIN"),
                    userRows.getString("USER_NAME"),
                    userRows.getDate("USER_BIRTHDAY").toLocalDate(),
                    new HashSet<>(friendsDao.getFriends(id))
            );

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());

            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new UserIdException("User not found");
        }
    }

}
