package ru.yandex.practicum.filmorate.storage.film.dao;

import java.util.Set;

public interface LikesDao {

    Set<Long> getLikes(long filmId);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);
}
