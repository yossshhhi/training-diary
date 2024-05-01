package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.WorkoutRepository;
import kz.yossshhhi.model.AggregateWorkoutData;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;

import java.time.LocalDate;
import java.util.*;

public class WorkoutDAO implements WorkoutRepository {
    private final DatabaseManager databaseManager;
    private final ResultSetMapper<Workout> resultSetMapper;
    private final ResultSetMapper<AggregateWorkoutData> aggregateWorkoutDataResultSetMapper;

    public WorkoutDAO(DatabaseManager databaseManager, ResultSetMapper<Workout> resultSetMapper, ResultSetMapper<AggregateWorkoutData> aggregateWorkoutDataResultSetMapper) {
        this.databaseManager = databaseManager;
        this.resultSetMapper = resultSetMapper;
        this.aggregateWorkoutDataResultSetMapper = aggregateWorkoutDataResultSetMapper;
    }

    @Override
    public Optional<Workout> findById(Long id) {
        List<Workout> workouts = databaseManager.executeQuery(
                "SELECT * FROM diary_schema.workout WHERE id = ?", resultSetMapper, id);
        return workouts.isEmpty() ? Optional.empty() : Optional.of(workouts.get(0));
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
        long generatedKey = databaseManager
                .executeUpdate("""
                        INSERT INTO diary_schema.workout
                        (user_id, workout_type_id, created_at, duration, burned_calories)
                        VALUES (?, ?, ?, ?, ?)
                        """, workout.getUserId(), workout.getWorkoutTypeId(), workout.getCreatedAt(),
                        workout.getDuration(), workout.getBurnedCalories());
        workout.setId(generatedKey);
        return workout;
    }

    private Workout update(Workout workout) {
        databaseManager.executeUpdate("""
            UPDATE diary_schema.workout
            SET user_id = ?, workout_type_id = ?, duration = ?, burned_calories = ?
            WHERE id = ?
            """, workout.getUserId(), workout.getWorkoutTypeId(),
                workout.getDuration(), workout.getBurnedCalories(), workout.getId());
        return workout;
    }


    @Override
    public void delete(Long id) {
        databaseManager.executeUpdate("DELETE FROM diary_schema.workout WHERE id = ?", id);
    }

    @Override
    public List<Workout> findAll() {
        return databaseManager.executeQuery("""
                SELECT w.*, u.username, t.name AS workout_type_name
                FROM diary_schema.workout AS w
                JOIN diary_schema.users AS u ON w.user_id = u.id
                JOIN diary_schema.workout_type AS t ON w.workout_type_id = t.id""", resultSetMapper);
    }

    @Override
    public List<Workout> findAllByUserId(Long userId) {
        return databaseManager.executeQuery("""
                SELECT w.*, u.username, t.name AS workout_type_name
                FROM diary_schema.workout AS w
                JOIN diary_schema.users AS u ON w.user_id = u.id
                JOIN diary_schema.workout_type AS t ON w.workout_type_id = t.id
                WHERE user_id = ?""", resultSetMapper, userId);
    }

    @Override
    public boolean existsByUserIdAndDateAndWorkoutTypeId(Long userId, LocalDate date, Long workoutTypeId) {
        List<Workout> workouts = databaseManager.executeQuery("""
                SELECT * FROM diary_schema.workout
                WHERE user_id = ?
                    AND created_at = ?
                    AND workout_type_id = ?""", resultSetMapper, userId, date, workoutTypeId);
        return !workouts.isEmpty();
    }

    @Override
    public AggregateWorkoutData getAggregateDataByUserIdAndAfterDate(Long userId, LocalDate date) {
        List<AggregateWorkoutData> result = databaseManager.executeQuery("""
                SELECT COUNT(*) AS workout_count, SUM(duration) AS total_duration, SUM(burned_calories) AS total_burned_calories
                FROM diary_schema.workout
                WHERE user_id = ? AND created_at > ?""", aggregateWorkoutDataResultSetMapper, userId, date);
        return result.isEmpty() ? AggregateWorkoutData.builder()
                .workoutCount(0L)
                .totalBurnedCalories(0L)
                .totalDuration(0L)
                .build() : result.get(0);
    }
}
