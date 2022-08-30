package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.FilmIdExceptoin;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film addFilm (Film film) throws ValidationException, FilmIdExceptoin;

    Film updateFilm (Film film) throws ValidationException, FilmIdExceptoin;

    void deleteFilm (long id) throws FilmIdExceptoin;

    Film getFilmById (long id) throws FilmIdExceptoin;

    void deleteAllFilms ();

}
