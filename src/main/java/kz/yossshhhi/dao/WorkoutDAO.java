package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.WorkoutRepository;
import kz.yossshhhi.model.AggregateWorkoutData;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.util.ResultSetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkoutDAO implements WorkoutRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ResultSetMapper<Workout> resultSetMapper;
    private final ResultSetMapper<AggregateWorkoutData> aggregateWorkoutDataResultSetMapper;

    private final String FIND_BY_ID_SQL = "SELECT * FROM diary_schema.workout WHERE id = ?";
    private final String SAVE_SQL = """
            INSERT INTO diary_schema.workout
            (user_id, workout_type_id, created_at, duration, burned_calories)
            VALUES (?, ?, ?, ?, ?)""";
    private final String UPDATE_SQL = """
            UPDATE diary_schema.workout
            SET workout_type_id = ?, duration = ?, burned_calories = ?
            WHERE id = ?""";
    private final String DELETE_SQL = "DELETE FROM diary_schema.workout WHERE id = ?";
    private final String FIND_ALL_SQL = """
            SELECT w.*, u.username, t.name AS workout_type_name
            FROM diary_schema.workout AS w
            JOIN diary_schema.users AS u ON w.user_id = u.id
            JOIN diary_schema.workout_type AS t ON w.workout_type_id = t.id""";
    private final String FIND_ALL_BY_USER_ID_SQL = """
            SELECT w.*, u.username, t.name AS workout_type_name
            FROM diary_schema.workout AS w
            JOIN diary_schema.users AS u ON w.user_id = u.id
            JOIN diary_schema.workout_type AS t ON w.workout_type_id = t.id
            WHERE user_id = ?
            ORDER BY w.id""";
    private final String EXISTS_SQL = """
            SELECT * FROM diary_schema.workout
            WHERE user_id = ? AND created_at = ? AND workout_type_id = ?""";
    private final String STATISTIC_SQL = """
            SELECT COUNT(*) AS workout_count, SUM(duration) AS total_duration, SUM(burned_calories) AS total_burned_calories
            FROM diary_schema.workout
            WHERE user_id = ? AND created_at > ?""";


    @Override
    public Optional<Workout> findById(Long id) {
        List<Workout> workouts = jdbcTemplate.query(FIND_BY_ID_SQL, resultSetMapper, id);
        return workouts != null && !workouts.isEmpty() ? Optional.of(workouts.get(0)) : Optional.empty();
    }

    @Override
    public Workout save(Workout workout) {
        if (workout.getId() == null) {
            return insert(workout);
        } else {
            return update(workout);
        }
    }

    private Workout insert(Workout workout) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, workout.getUserId());
            ps.setLong(2, workout.getWorkoutTypeId());
            ps.setDate(3, Date.valueOf(workout.getCreatedAt()));
            ps.setInt(4, workout.getDuration());
            ps.setInt(5, workout.getBurnedCalories());
            return ps;
        }, keyHolder);

        Long generatedKey = (Long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        if (generatedKey != null) {
            workout.setId(generatedKey);
        } else {
            throw new RuntimeException("Failed to retrieve auto-generated key");
        }
        return workout;
    }

    private Workout update(Workout workout) {
        jdbcTemplate.update(UPDATE_SQL, workout.getWorkoutTypeId(), workout.getDuration(),
                workout.getBurnedCalories(), workout.getId());
        return workout;
    }


    @Override
    public boolean delete(Long id) {
        int rowsAffected = jdbcTemplate.update(DELETE_SQL, id);
        return rowsAffected > 0;
    }

    @Override
    public List<Workout> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, resultSetMapper);
    }

    @Override
    public List<Workout> findAllByUserId(Long userId) {
        return jdbcTemplate.query(FIND_ALL_BY_USER_ID_SQL, resultSetMapper, userId);
    }

    @Override
    public boolean existsByUserIdAndDateAndWorkoutTypeId(Long userId, LocalDate date, Long workoutTypeId) {
        List<Workout> workouts = jdbcTemplate.query(EXISTS_SQL, resultSetMapper, userId, date, workoutTypeId);
        return workouts != null && !workouts.isEmpty();
    }

    @Override
    public AggregateWorkoutData getAggregateDataByUserIdAndAfterDate(Long userId, LocalDate date) {
        List<AggregateWorkoutData> result = jdbcTemplate.query(STATISTIC_SQL, aggregateWorkoutDataResultSetMapper, userId, date);
        return result != null && !result.isEmpty() ? result.get(0) :
                AggregateWorkoutData.builder()
                        .workoutCount(0L)
                        .totalBurnedCalories(0L)
                        .totalDuration(0L)
                        .build();
    }
}
