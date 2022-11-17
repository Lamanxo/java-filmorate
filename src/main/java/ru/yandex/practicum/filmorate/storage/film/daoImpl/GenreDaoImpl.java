package ru.yandex.practicum.filmorate.storage.film.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbc;
    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Genre getGenre (int id) {
        if (idCheck(id) == 0) {
            throw new IdNotFoundException("Genre id not exist");
        }
        String sql = "select GENRE_ID, GENRE_NAME from GENRES where GENRE_ID = ?";
        return jdbc.queryForObject(sql,this::makeGenre, id);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sql = "select GENRE_ID, GENRE_NAME from GENRES";
        return jdbc.query(sql, this::makeGenre);
    }
    @Override
    public Set<Genre> getFilmGenres(int filmId) {
        Set<Genre> genresMap = new HashSet<>();
        String sql = "select GENRE_ID from FILMS_GENRES where FILM_ID = ?";
        List<Integer> genreIds = jdbc.queryForList(sql, Integer.class, filmId);
        for (int id : genreIds) {
            genresMap.add(getGenre(id));
        }
        return genresMap;
    }

    @Override
    public void addGenresToFilm(Film film) {
        String sql = "insert into FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbc.update(sql, film.getId(), genre.getId());
        }
    }

    @Override
    public void updateGenresOfFilm(Film film) {
        String delete = "delete from FILMS_GENRES where FILM_ID = ?";
        jdbc.update(delete, film.getId());
        addGenresToFilm(film);
    }

    private Genre makeGenre(ResultSet rs, int ruwNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME"));

    }

    private int idCheck (int id) {
        String sql = "select count(*) from GENRES where GENRE_ID = ?";
        int response =jdbc.queryForObject(sql, Integer.class, id);
        return response;
    }
}
