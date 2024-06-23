package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Long id;
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    @Size(max = 200)
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private Mpa mpa;
    List<Genre> genres = new ArrayList<>();

    public void setOneGenres(Genre genre) {
        genres.add(genre);
    }

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hashDuration() {
        return !(duration == null || duration == 0);
    }

    public boolean hashReleaseDate() {
        return !(releaseDate == null);
    }

    public boolean hashMpa() {
        return !(mpa == null);
    }

    public boolean hashGenres() {
        return !(genres == null || genres.isEmpty());
    }
}