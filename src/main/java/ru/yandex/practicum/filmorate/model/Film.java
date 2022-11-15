package ru.yandex.practicum.filmorate.model;
import lombok.*;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    @Min(1)
    long id;

    @NotNull
    @NotBlank
    @NotEmpty
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    private LocalDate releaseDate;

    @Min(1)
    private Integer duration;
    private int rate;
    private Set<Long> likes;
    private Set<Genre> genres;
    private Mpa mpa;

}
