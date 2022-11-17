package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmIdExceptoin;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserIdException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.film.daoImpl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.dao.FriendsDao;
import ru.yandex.practicum.filmorate.storage.user.daoImpl.*;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {


	private final UserDbStorage us;
	private final FriendsDao friendsDao;
	private final FilmDbStorage fs;
	private final MpaDao mpaDao;
	private final GenreDao genreDao;

	private final User user = new User(1, "mail@mail.ru", "mail", "Test",
			LocalDate.of(2022, 1, 1), new HashSet<>());
	private final Film film = new Film(1, "Batman", "DarkWings", LocalDate.of(2022, 1, 1),
			90,5, new HashSet<>(), new HashSet<>(), new Mpa(1,null));


	@Test
	public void testCrudUserAndFriends(){
		//WithNormalData
		us.createUser(user);
		User user1 = us.getUserById(user.getId());
		assertEquals(user, user1);
		assertEquals(user.getId(), 1);
		user.setName("NotTest");
		us.update(user);
		assertEquals(user, us.getUserById(user.getId()));
		us.deleteUserById(user.getId());
		assertThrows(UserIdException.class, ()-> us.getUserById(user.getId()));
		//WithInvalidData
		us.createUser(user);
		user.setId(22);
		assertThrows(UserIdException.class, ()-> us.update(user));
		assertThrows(UserIdException.class, ()-> us.getUserById(55));
		assertThrows(UserIdException.class, ()-> us.deleteUserById(44));
		//FriendsNormalData
		User friend = new User(1, "friend@mail.ru", "friend", "Fred",
				LocalDate.of(2000, 1, 1), new HashSet<>());
		us.createUser(friend);
		friendsDao.addFriend(2,3);
		assertTrue(us.getUserById(2).getFriendsId().contains(friendsDao.getFriends(2).get(0)));
		friendsDao.deleteFriend(2,3);
		assertEquals(0, us.getUserById(2).getFriendsId().size());
		//FriendsInvalidData
		assertThrows(UserIdException.class, ()-> friendsDao.addFriend(2,33));
		assertThrows(UserIdException.class, ()-> friendsDao.deleteFriend(2,33));
		assertThrows(UserIdException.class, ()-> friendsDao.getFriends(33));

	}

	@Test
	public void testCrudFilm() {
		//WithNormalData
		fs.addFilm(film);
		assertEquals(film,fs.getFilmById(1));
		film.setName("BatmanVsSups");
		fs.updateFilm(film);
		assertEquals(film,fs.getFilmById(1));
		//WithInvalidData
		film.setId(55);
		assertThrows(FilmIdExceptoin.class, ()-> fs.updateFilm(film));
		assertThrows(FilmIdExceptoin.class, ()-> fs.getFilmById(55));
		assertThrows(FilmIdExceptoin.class,()-> fs.deleteFilm(66));
		//DeleteFilm
		fs.deleteFilm(1);
		assertThrows(FilmIdExceptoin.class, ()-> fs.getFilmById(1));
	}

	@Test
	public void testMpaAndGenre(){
		//Mpa
		assertEquals(5, mpaDao.getAllMpa().size());
		assertThrows(IdNotFoundException.class, () -> mpaDao.getMpafromDB(23));
		assertEquals("G", mpaDao.getMpafromDB(1).getName());
		//MpaWithIncorrectId
		assertThrows(IdNotFoundException.class, () -> mpaDao.getMpafromDB(123));
		//Genre
		assertEquals(6, genreDao.getAllGenres().size());
		assertEquals("Комедия", genreDao.getGenre(1).getName());
	}


}