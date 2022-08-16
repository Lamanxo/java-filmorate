package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
@Data
@AllArgsConstructor

public class User {
    @Min(1)
    private int id;

    @NotBlank
    @NotNull
    @NotEmpty
    private String email;

    @Pattern(regexp = "^\\S*$")
    @NotEmpty
    @NotBlank
    @NotNull
    private String login;

    private String name;

    @Past
    @NotNull
    private LocalDate birthday;
}
