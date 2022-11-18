package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmIdExceptoin;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.dao.LikesDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Getter
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikesDao ldao;
    @Autowired

    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, LikesDao ldao) {
        this.filmStorage = filmStorage;
        this.ldao = ldao;
    }

    public void addLike(long filmId, long userId) throws FilmIdExceptoin {
        if (filmId <= 0 || userId <= 0) {
            log.debug("Check filmId {} check userId {}", filmId, userId);
            throw new FilmIdExceptoin("Film with id: " + filmId + " or user with id: " + userId + " not found");
        }
        ldao.addLike(filmId,userId);
    }

    public void removeLike(long filmId, long userId) throws FilmIdExceptoin {
        if (filmId <= 0 || userId <= 0) {
            log.debug("Check filmId {} check userId {}", filmId, userId);
            throw new FilmIdExceptoin("Film with id: " + filmId + " or user with id: " + userId + " not found");
        }
        ldao.removeLike(filmId,userId);
    }

    public List<Film> getPopularFilms(long count) {
        return filmStorage.getAllFilms().stream()
                .sorted((p0, p1) -> p1.getLikes().size() - p0.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

}
