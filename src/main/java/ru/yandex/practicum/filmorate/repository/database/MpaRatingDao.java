package ru.yandex.practicum.filmorate.repository.database;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

@Component
@RequiredArgsConstructor
public class MpaRatingDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_MPA_BY_ID = """
            SELECT mpa_id, name
            FROM mpa
            WHERE mpa_id = ?;
            """;

    public MpaRating findById(Long mpaId) {
        SqlRowSet mpaRatingAsRowSet = jdbcTemplate.queryForRowSet(FIND_MPA_BY_ID, mpaId);
        MpaRating mpaRating;
        if (mpaRatingAsRowSet.next()) {
            mpaRating = MpaRating.builder()
                    .id(mpaRatingAsRowSet.getLong("mpa_id"))
                    .name(mpaRatingAsRowSet.getString("name"))
                    .build();
        } else {
            throw new IllegalArgumentException("Mpa with this id is not exist");
        }
        return mpaRating;
    }

}
