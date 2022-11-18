package ru.yandex.practicum.filmorate.storage.film.daoImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmIdExceptoin;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.film.dao.LikesDao;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Component("FilmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private JdbcTemplate jdbc;
    private MpaDao mpaDao;
    private LikesDao likesDao;
    private GenreDao genreDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, MpaDao mpaDao, LikesDao likesDao, GenreDao genreDao) {
        this.jdbc = jdbc;
        this.mpaDao = mpaDao;
        this.likesDao = likesDao;
        this.genreDao = genreDao;
    }

    public Collection<Film> getAllFilms() {
        String sql = "select * from FILMS";
        return jdbc.query(sql, this::makeFilm);
    }

    public Film addFilm (Film film) {

        validate(film);
        if (film.getMpa() != null) {
            film.setMpa(mpaDao.getMpafromDB(film.getMpa().getId()));
        } else {
            throw new ValidationException("MPA is null");
        }

        String sql = "insert into FILMS (FILM_NAME, FILM_DESCRIPTION, FILM_RELEASE_DATE, FILM_DURATION, " +
                " FILM_RATE, MPA_ID) VALUES (?,?,?,?,?,?)";
        jdbc.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getRate(), film.getMpa().getId());
        String count = "select (FILM_ID) from FILMS ORDER BY FILM_ID DESC LIMIT 1";
        film.setId(jdbc.queryForObject(count, Long.class));
        if (film.getGenres() != null) {
            genreDao.addGenresToFilm(film);
        }
        return getFilmById(film.getId());
    }

    public Film updateFilm (Film film) {
        if (idCheck((int)film.getId()) == 0) {
            throw new FilmIdExceptoin("Film with ID " + film.getId() + " not found");
        }
        if (film.getGenres() != null) {
            genreDao.updateGenresOfFilm(film);
        }

        String sql = "update FILMS set FILM_NAME = ?, FILM_DESCRIPTION = ?, FILM_RELEASE_DATE = ?, FILM_DURATION = ?, " +
                "FILM_RATE = ?, MPA_ID = ? where FILM_ID = ?";
        jdbc.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getRate(), film.getMpa().getId(), film.getId());

        return getFilmById(film.getId());
    }
    public void deleteFilm (long id) {
        if (idCheck((int)id) == 0) {
            throw new FilmIdExceptoin("Film with ID " + id + " not found");
        }
        String sql = "delete from FILMS where FILM_ID = ?";
        jdbc.update(sql, id);
    }

    public Film getFilmById (long id) {
        if (idCheck((int)id) == 0) {
            throw new FilmIdExceptoin("Film with ID " + id + " not found");
        }
        String sql = "select * from FILMS where FILM_ID = ?";
        return jdbc.queryForObject(sql,this::makeFilm,id);
    }
    public void deleteAllFilms (){
        String sql = "delete from FILMS";
        jdbc.update(sql);
    }

    private Film makeFilm(ResultSet rs, int ruwNum) throws SQLException {
        return new Film(rs.getInt("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("FILM_DESCRIPTION"),
                rs.getDate("FILM_RELEASE_DATE").toLocalDate(),
                rs.getInt("FILM_DURATION"),
                rs.getInt("FILM_RATE"),
                new HashSet<Long>(likesDao.getLikes(rs.getInt("FILM_ID"))),
                new HashSet<Genre>(genreDao.getFilmGenres(rs.getInt("FILM_ID"))),
                mpaDao.getMpafromDB(rs.getInt("MPA_ID"))

        );

    }

    private int idCheck (int id) {
        String sql = "select count(*) from FILMS where FILM_ID = ?";
        int response =jdbc.queryForObject(sql, Integer.class, id);
        return response;
    }

    private void validate(Film film) {
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
