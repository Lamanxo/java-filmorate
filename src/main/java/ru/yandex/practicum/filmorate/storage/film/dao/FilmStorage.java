package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.exception.FilmIdExceptoin;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film addFilm (Film film) ;

    Film updateFilm (Film film) ;

    void deleteFilm (long id) ;

    Film getFilmById (long id) ;

    void deleteAllFilms ();

}
