package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmIdExceptoin;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();
    private long id = 0;
    private void idGen() {
        if (films.size() != id) {
            id = films.size();
        }
        id++;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("All films {}", films.toString());
        return films.values();
    }

    @Override
    public Film addFilm (Film film) throws ValidationException, FilmIdExceptoin {
        idGen();
        film.setId(id);
        validate(film);
        film.setLikes(new HashSet<>());
        films.put(id, film);
        log.info("Film was created {}", film.toString());
        return film;
    }

    @Override
    public Film updateFilm (Film film) throws ValidationException, FilmIdExceptoin {
        validate(film);
        if (films.containsKey(film.getId())) {
            film.setLikes(new HashSet<>());
            films.put(film.getId(), film);
            log.info("Film was updated{}", film.toString());
        }
        return film;
    }

    @Override
    public void deleteFilm (long id) throws FilmIdExceptoin {
        if(films.containsKey(id)) {
            films.remove(id);
        } else {
            log.error("Film with id {} not found", id);
            throw new FilmIdExceptoin("Film with id " + id + " not found");
        }

    }
    @Override
    public Film getFilmById (long id) throws FilmIdExceptoin {
        if(films.containsKey(id)) {
            return films.get(id);
        } else {
            log.error("Film with id {} not found", id);
            throw new FilmIdExceptoin("Film with id " + id + " not found");
        }

    }

    @Override
    public void deleteAllFilms () {
        films.clear();
    }

    private void validate(Film film) throws ValidationException, FilmIdExceptoin {
        if (film.getName().isBlank()) {
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
        } else if (film.getId() < 0) {
            log.error("Film with id {} was found", film.getId());
            throw new FilmIdExceptoin("Film with id " + film.getId() + "was found");
        }

    }

}
