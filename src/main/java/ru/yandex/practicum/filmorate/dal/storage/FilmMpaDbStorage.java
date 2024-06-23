package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Service
public class FilmMpaDbStorage extends BaseDbStorage<Mpa> {
    private static final String UPDATE_MPA_ID = "UPDATE films" +
            " SET mpa_id = ?" +
            " WHERE film_id = ?";
    private static final String FIND_ALL_MPA = "SELECT * FROM mpa_table";
    private static final String FIND_BY_ID_MPA = "SELECT * FROM mpa_table WHERE mpa_id = ?";
    private static final String GET_MPA_ID = "SELECT mpa_id\n" +
            "FROM films\n" +
            "WHERE film_id = ?;";

    public FilmMpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper, Mpa.class);
    }

    public Mpa updateMpa(Mpa mpa, Long id) {
        update(
                UPDATE_MPA_ID,
                mpa.getId(),
                id
        );
        return mpa;
    }

    public List<Mpa> valueMpa() {
        return jdbc.query(FIND_ALL_MPA, new MpaRowMapper());
    }

    public Optional<Mpa> getMpa(Long id) {
        return findOne(FIND_BY_ID_MPA, id);
    }

    public Long getMpaId(Long fim) {
        return jdbc.queryForObject(GET_MPA_ID, Long.class, fim);
    }
}
