package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmIdExceptoin;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService fs;

    public FilmController(FilmService fs) {
        this.fs = fs;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return fs.getFilmStorage().getAllFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException, FilmIdExceptoin {
        return fs.getFilmStorage().addFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm (@PathVariable("id") long id) throws FilmIdExceptoin {
        return fs.getFilmStorage().getFilmById(id);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException, FilmIdExceptoin {
        return fs.getFilmStorage().updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike (@PathVariable("id") long filmId, @PathVariable("userId") long userId) throws FilmIdExceptoin {
        fs.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike (@PathVariable("id") long filmId, @PathVariable("userId") long userId) throws FilmIdExceptoin {
        fs.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(value = "count", defaultValue = "10", required = false) long count) {
        return fs.getPopularFilms(count);
    }
}
