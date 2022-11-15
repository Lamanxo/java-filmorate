package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface GenreDao {

    Genre getGenre (int id);

    Collection<Genre> getAllGenres();

    void addGenresToFilm(Film film);

    void updateGenresOfFilm(Film film);

    Set<Genre> getFilmGenres(int filmId);

}
