package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor

public class User {
    @Min(1)
    private long id;

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
    private Set<Long> friendsId;
}
