package ru.yandex.practicum.filmorate.storage.user.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserIdException;
import ru.yandex.practicum.filmorate.storage.user.dao.FriendsDao;

import java.util.List;

@Component
public class FriendsDaoImpl implements FriendsDao {

    private final JdbcTemplate jdbc;

    @Autowired
    public FriendsDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new UserIdException("User and Friend id is same");
        } else if (idCheck(userId) == 0 || idCheck(friendId) == 0) {
            throw  new UserIdException("User or Friend id not exist");
        }
        String sql = "insert into FRIENDS (USER_ID, FRIEND_ID, FRIEND_STATUS) VALUES (?, ?, false)";
        jdbc.update(sql, userId, friendId);
        checkOrUpdateFriendStatus(userId, friendId);
    }

    @Override
    public List<Long> getFriends(long userId) {
        if (idCheck(userId) == 0) {
            throw new UserIdException("User ID not found");
        }
        String sql = "select FRIEND_ID from FRIENDS where USER_ID = ?";
        return jdbc.queryForList(sql, Long.class, userId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        if (idCheck(userId) == 0 || idCheck(friendId) == 0) {
            throw new UserIdException("User or Friend id is incorrect");
        }
        String sql = "delete from FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
        jdbc.update(sql, userId, friendId);
    }

    private void checkOrUpdateFriendStatus (long userId, long friendId) {
        String sql = "select COUNT(*) from FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
        int response = jdbc.queryForObject(sql, Integer.class, friendId, userId);
        if (response == 1) {
            String sql1 = "update FRIENDS set FRIEND_STATUS = true where USER_ID = ? AND FRIEND_ID = ?";
            jdbc.update(sql1, friendId, userId);
            jdbc.update(sql1, userId, friendId);
        }
    }

    private int idCheck (long id) {
        String sql = "select count(*) from USERS where USER_ID = ?";
        int response =jdbc.queryForObject(sql, Integer.class, id);
        return response;
    }
}
