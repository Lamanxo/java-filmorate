package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Set<Film> films = new HashSet<>(List.of());
    private int id = 0;
    private void idGen() {
        if (films.size() != id) {
            id = films.size();
        }
        id++;
    }

    @GetMapping
    public Set<Film> findFilms () {
        log.info("All films {}", films.toString());
        return films;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        idGen();
        film.setId(id);
        validate(film);
        films.add(film);
        log.info("Film was created {}", film.toString());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        validate(film);
        for (Film f : films) {
            if (f.getId() == film.getId()) {
                films.remove(f);
                films.add(film);
                log.info("Film was updated{}", film.toString());
            }
        }

        return film;
    }

    private void validate(Film film) throws ValidationException {
        if (film.getName().isBlank() || film.getId() < 0) {
            log.error("Film name field is empty{}", film.toString());
            throw new ValidationException("Film name field is empty");
        } else if (film.getDescription().length() > 200) {
            log.error("Film description length is more than 200 chars{}", film.toString());
            throw  new ValidationException("Film description length is more than 200 chars");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Film release date is before 1895,12,28{}", film.toString());
            throw new ValidationException("Film release date is before 1895,12,28");
        } else if (film.getDuration() < 0) {
            log.error("Film duration cannot be less than zero{}", film.toString());
            throw new ValidationException("Film duration cannot be less than zero");
        }

    }
}
