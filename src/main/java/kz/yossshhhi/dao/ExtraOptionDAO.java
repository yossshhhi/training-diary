package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.model.ExtraOption;
import kz.yossshhhi.util.ResultSetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ExtraOptionDAO implements ExtraOptionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ResultSetMapper<ExtraOption> resultSetMapper;

    private final String SAVE_SQL = "INSERT INTO diary_schema.extra_option (workout_id, type_id, value) VALUES (?, ?, ?)";
    private final String FIND_ALL_BY_WORKOUT_ID_SQL = """
                SELECT o.*, t.name AS type_name
                FROM diary_schema.extra_option AS o
                JOIN diary_schema.extra_option_type AS t ON o.type_id = t.id
                WHERE workout_id = ?""";

    @Override
    public ExtraOption save(ExtraOption extraOption) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, extraOption.getWorkoutId());
            ps.setLong(2, extraOption.getTypeId());
            ps.setInt(3, extraOption.getValue());
            return ps;
        }, keyHolder);

        Long generatedKey = (Long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        if (generatedKey != null) {
            extraOption.setId(generatedKey);
        } else {
            throw new RuntimeException("Failed to retrieve auto-generated key");
        }
        return extraOption;
    }

    @Override
    public List<ExtraOption> findAllByWorkoutId(Long workoutId) {
        return jdbcTemplate.query(FIND_ALL_BY_WORKOUT_ID_SQL, resultSetMapper, workoutId);
    }

    @Override
    public void deleteAll(List<ExtraOption> extraOptions) {
        List<Long> ids = extraOptions.stream()
                .map(ExtraOption::getId)
                .toList();
        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = String.format("""
            DELETE FROM diary_schema.extra_option
            WHERE id IN (%s)
            """, placeholders);
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            for (int i = 0; i < ids.size(); i++) {
                ps.setLong(i + 1, ids.get(i));
            }
            return ps;
        });
    }
}
