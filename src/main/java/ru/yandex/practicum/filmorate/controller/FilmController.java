package ru.yandex.practicum.filmorate.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmIdExceptoin;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService fs) {
        this.filmService = fs;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getFilmStorage().getAllFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException, FilmIdExceptoin {
        return filmService.getFilmStorage().addFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm (@PathVariable("id") long id) throws FilmIdExceptoin {
        return filmService.getFilmStorage().getFilmById(id);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException, FilmIdExceptoin {
        return filmService.getFilmStorage().updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike (@PathVariable("id") long filmId, @PathVariable("userId") long userId) throws FilmIdExceptoin {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike (@PathVariable("id") long filmId, @PathVariable("userId") long userId) throws FilmIdExceptoin {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTop(@RequestParam(value = "count", defaultValue = "10", required = false) long count) {
        return filmService.getPopularFilms(count);
    }
}
