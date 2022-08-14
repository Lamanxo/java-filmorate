package ru.yandex.practicum.filmorate.model;
import lombok.*;


import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
}
