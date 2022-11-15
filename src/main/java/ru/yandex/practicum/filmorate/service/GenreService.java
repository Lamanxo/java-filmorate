package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreDao;

import java.util.Collection;

@Service
public class GenreService {
    private final GenreDao gDao;

    @Autowired
    public GenreService(GenreDao gDao) {

        this.gDao = gDao;
    }

    public Genre getGenre(int id) {

        return gDao.getGenre(id);
    }

    public Collection<Genre> getAllGenres() {

        return gDao.getAllGenres();
    }

}
