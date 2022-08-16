package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.controller.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class ValidatorTest {

    @Test
    public void validateFilmWithNormalFields () throws ValidationException {
        final FilmController fc = new FilmController();
        Film film1 = new Film(1,"Terminator 2", "judjement day", LocalDate.of(1990,01,01),90);
        fc.create(film1);
        assertEquals(1, fc.find().size());
    }

    @Test
    public void validateFilmWithEmptyName(){
        final FilmController fc = new FilmController();
        Film film1 = new Film(1," ", "judjement day", LocalDate.of(1990,01,01),90);
        assertThrows(ValidationException.class, () -> fc.create(film1));
    }

    @Test
    public void validateFilmWithWrongDate(){
        final FilmController fc = new FilmController();
        Film film1 = new Film(1,"Terminator 2", "judjement day", LocalDate.of(1790,01,01),90);
        assertThrows(ValidationException.class, () -> fc.create(film1));
    }

    @Test
    public void validateFilmWithWrongDuration(){
        final FilmController fc = new FilmController();
        Film film1 = new Film(1,"Terminator 2", "judjement day", LocalDate.of(1990,01,01),-90);
        assertThrows(ValidationException.class, () -> fc.create(film1));
    }

    @Test
    public void validateUserWithNormalFields() throws ValidationException {
        final UserController uc = new UserController();
        User user = new User(1, "johndoe69@gmail.com", "johndoe","John Doe", LocalDate.of(1986,12,12));
        uc.create(user);
        assertEquals(1, uc.find().size());
    }

    @Test
    public void validateUserWithEmptyEmail(){
        final UserController uc = new UserController();
        User user = new User(1, " ", "johndoe","John Doe", LocalDate.of(1986,12,12));
        assertThrows(ValidationException.class, () -> uc.create(user));
    }

    @Test
    public void validateUserWithWrongEmail(){
        final UserController uc = new UserController();
        User user = new User(1, "johndoe69gmail.com", "johndoe","John Doe", LocalDate.of(1986,12,12));
        assertThrows(ValidationException.class, () -> uc.create(user));
    }

    @Test
    public void validateUserWithEmptyLogin(){
        final UserController uc = new UserController();
        User user = new User(1, "johndoe69@gmail.com", " ","John Doe", LocalDate.of(1986,12,12));
        assertThrows(ValidationException.class, () -> uc.create(user));
    }

    @Test
    public void validateUserWithLoginWithSpaces(){
        final UserController uc = new UserController();
        User user = new User(1, "johndoe69@gmail.com", "john doe","John Doe", LocalDate.of(1986,12,12));
        assertThrows(ValidationException.class, () -> uc.create(user));
    }

    @Test
    public void replaceNameWithLoginField() throws ValidationException {
        final UserController uc = new UserController();
        User user = new User(1, "johndoe69@gmail.com", "johndoe"," ", LocalDate.of(1986,12,12));
        uc.create(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void validateUserWithWrongBirthdate(){
        final UserController uc = new UserController();
        User user = new User(1, "johndoe69@gmail.com", "johndoe","John Doe", LocalDate.of(2986,12,12));
        assertThrows(ValidationException.class, () -> uc.create(user));
    }

}
