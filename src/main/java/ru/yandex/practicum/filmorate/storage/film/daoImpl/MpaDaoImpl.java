package ru.yandex.practicum.filmorate.storage.film.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class MpaDaoImpl implements MpaDao {
    private JdbcTemplate jdbc;
    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Mpa getMpafromDB (int id) {
        if (idCheck(id) == 0) {
            throw new IdNotFoundException("MPA rating with ID " + id + " not found");
        }
        String sql = "select RATING_ID, RATING from MPA_RATINGS where RATING_ID =?";
        return jdbc.queryForObject(sql, this::makeMpa, id);
    }

    @Override
    public Collection<Mpa> getAllMpa(){
        String sql = "select * from MPA_RATINGS";
        return jdbc.query(sql, this::makeMpa);
    }

    private Mpa makeMpa(ResultSet rs, int ruwNum) throws SQLException {
        return new Mpa(rs.getInt("RATING_ID"),
                rs.getString("RATING"));

    }

    private int idCheck (int id) {
        String sql = "select count(*) from MPA_RATINGS where RATING_ID = ?";
        int response =jdbc.queryForObject(sql, Integer.class, id);
        return response;
    }

}
