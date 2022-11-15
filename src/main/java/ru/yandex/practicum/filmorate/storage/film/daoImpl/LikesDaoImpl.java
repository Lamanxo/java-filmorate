package ru.yandex.practicum.filmorate.storage.film.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.dao.LikesDao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class LikesDaoImpl implements LikesDao {

    private final JdbcTemplate jdbc;

    @Autowired
    public LikesDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Set<Long> getLikes(long filmId) {
        String sql = "select user_id from films_likes where film_id = ?";
        List<Long> usersLike = jdbc.queryForList(sql, Long.class, filmId);
        return new HashSet<>(usersLike);
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sql = "insert into films_likes (film_id, user_id) " +
                "values (?, ?)";
        jdbc.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        if (idCheck(filmId) == 0) {
            throw new IdNotFoundException("Film id " + filmId + " not found");
        }
        String sql = "delete from FILMS_LIKES where USER_ID = ? and FILM_ID = ?";
        jdbc.update(sql, userId, filmId);
    }

    private int idCheck(long id) {
        String sql = "select count(*) from FILMS_LIKES where FILM_ID = ?";
        return jdbc.queryForObject(sql, Integer.class, id);

    }
}
