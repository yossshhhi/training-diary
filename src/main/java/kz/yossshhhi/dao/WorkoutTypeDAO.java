package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.WorkoutTypeRepository;
import kz.yossshhhi.model.WorkoutType;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;

import java.util.*;

public class WorkoutTypeDAO implements WorkoutTypeRepository {
    private final DatabaseManager databaseManager;
    private final ResultSetMapper<WorkoutType> resultSetMapper;

    public WorkoutTypeDAO(DatabaseManager databaseManager, ResultSetMapper<WorkoutType> resultSetMapper) {
        this.databaseManager = databaseManager;
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public Optional<WorkoutType> findById(Long id) {
        List<WorkoutType> workoutTypes = databaseManager.executeQuery(
                "SELECT * FROM diary_schema.workout_type WHERE id = ?",
                resultSetMapper, id);
        return workoutTypes.isEmpty() ? Optional.empty() : Optional.of(workoutTypes.get(0));
    }

    @Override
    public Optional<WorkoutType> findByName(String name) {
        List<WorkoutType> workoutTypes = databaseManager.executeQuery(
                "SELECT * FROM diary_schema.workout_type WHERE name = ?",
                resultSetMapper, name);
        return workoutTypes.isEmpty() ? Optional.empty() : Optional.of(workoutTypes.get(0));
    }

    @Override
    public WorkoutType save(WorkoutType workoutType) {
        long generatedKey = databaseManager.executeUpdate(
                "INSERT INTO diary_schema.workout_type (name) VALUES (?)",
                workoutType.getName());
        workoutType.setId(generatedKey);
        return workoutType;
    }

    @Override
    public List<WorkoutType> findAll() {
        return databaseManager.executeQuery(
                "SELECT * FROM diary_schema.workout_type",
                resultSetMapper);
    }

}
