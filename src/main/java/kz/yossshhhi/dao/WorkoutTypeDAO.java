package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.WorkoutTypeRepository;
import kz.yossshhhi.model.WorkoutType;
import kz.yossshhhi.util.ResultSetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class WorkoutTypeDAO implements WorkoutTypeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ResultSetMapper<WorkoutType> resultSetMapper;

    private final String FIND_BY_ID_SQL = "SELECT * FROM diary_schema.workout_type WHERE id = ?";
    private final String FIND_BY_NAME_SQL = "SELECT * FROM diary_schema.workout_type WHERE name = ?";
    private final String SAVE_SQL = "INSERT INTO diary_schema.workout_type (name) VALUES (?)";
    private final String FIND_ALL_SQL = "SELECT * FROM diary_schema.workout_type";

    @Override
    public Optional<WorkoutType> findById(Long id) {
        List<WorkoutType> workoutTypes = jdbcTemplate.query(FIND_BY_ID_SQL, resultSetMapper, id);
        return workoutTypes != null && !workoutTypes.isEmpty() ? Optional.of(workoutTypes.get(0)) : Optional.empty();
    }

    @Override
    public Optional<WorkoutType> findByName(String name) {
        List<WorkoutType> workoutTypes = jdbcTemplate.query(FIND_BY_NAME_SQL, resultSetMapper, name);
        return workoutTypes != null && !workoutTypes.isEmpty() ? Optional.of(workoutTypes.get(0)) : Optional.empty();
    }

    @Override
    public WorkoutType save(WorkoutType workoutType) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, workoutType.getName());
            return ps;
        }, keyHolder);

        Long generatedKey = (Long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        if (generatedKey != null) {
            workoutType.setId(generatedKey);
        } else {
            throw new RuntimeException("Failed to retrieve auto-generated key");
        }
        return workoutType;
    }

    @Override
    public List<WorkoutType> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, resultSetMapper);
    }
}
