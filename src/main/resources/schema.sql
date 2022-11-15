

CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID   INT         NOT NULL AUTO_INCREMENT,
    USER_NAME VARCHAR(20),
    USER_EMAIL     VARCHAR(64) NOT NULL UNIQUE,
    USER_LOGIN     VARCHAR(20) NOT NULL UNIQUE,
    USER_BIRTHDAY  DATE,
    CONSTRAINT USER_ID_PK
        PRIMARY KEY (USER_ID)
);

CREATE TABLE IF NOT EXISTS MPA_RATINGS
(
    RATING_ID INT                   NOT NULL,
    RATING    CHARACTER VARYING(10) NOT NULL,
    CONSTRAINT MPA_RATINGS_PK
        PRIMARY KEY (RATING_ID)
);

CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID          INT                   NOT NULL AUTO_INCREMENT,
    FILM_NAME        CHARACTER VARYING(40) NOT NULL,
    FILM_DESCRIPTION CHARACTER VARYING(200),
    FILM_RELEASE_DATE     DATE,
    FILM_DURATION         INT,
    FILM_RATE        INT,
    MPA_ID           INT,
    PRIMARY KEY (FILM_ID),
    CONSTRAINT FILMS_MPA_FK
        FOREIGN KEY (MPA_ID) REFERENCES MPA_RATINGS (RATING_ID)
);

CREATE TABLE IF NOT EXISTS GENRES
(
    GENRE_ID INT                   NOT NULL,
    GENRE_NAME    CHARACTER VARYING(20) NOT NULL,
    CONSTRAINT GENRE_PK
        PRIMARY KEY (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS FILMS_GENRES
(
    FILM_ID  INT NOT NULL,
    GENRE_ID INT NOT NULL,
    CONSTRAINT FILMS_GENRES_PK
        PRIMARY KEY (FILM_ID, GENRE_ID),
    CONSTRAINT FILM_ID_FK
        FOREIGN KEY (FILM_ID) REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
    CONSTRAINT GENRE_ID_FK
        FOREIGN KEY (GENRE_ID) REFERENCES GENRES (GENRE_ID)

);

CREATE TABLE IF NOT EXISTS FILMS_LIKES
(
    FILM_ID INT NOT NULL,
    USER_ID INT NOT NULL,

    CONSTRAINT FILMS_ID_FK
        FOREIGN KEY (FILM_ID) REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
    CONSTRAINT USER_ID_FK
        FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FRIENDS
(
    USER_ID       INT     NOT NULL,
    FRIEND_ID     INT     NOT NULL,
    FRIEND_STATUS BOOLEAN NOT NULL,

    CONSTRAINT FRIENDS_USER_ID_FK
        FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    CONSTRAINT FRIEND_ID
        FOREIGN KEY (FRIEND_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE
);