package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaDao;

import java.util.Collection;

@Service
public class MpaService {
    private final MpaDao mDao;

    @Autowired
    public MpaService(MpaDao mDao) {
        this.mDao = mDao;
    }

    public Collection<Mpa> getAllMpaRatings() {

        return mDao.getAllMpa();
    }
    public Mpa getMpaRating(int id) {

        return mDao.getMpafromDB(id);
    }


}
