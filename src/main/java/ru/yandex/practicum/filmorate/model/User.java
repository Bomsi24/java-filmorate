package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String login;
    @NotNull
    @NotBlank
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public void addFriends(Long id) {
        friends.add(id);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }
}