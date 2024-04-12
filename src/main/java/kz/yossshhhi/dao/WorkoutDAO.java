package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.WorkoutRepository;
import kz.yossshhhi.model.Workout;

import java.time.LocalDate;
import java.util.*;

public class WorkoutDAO implements WorkoutRepository {
    private final Map<Long, Workout> workouts;
    private Long id;

    public WorkoutDAO() {
        this.workouts = new LinkedHashMap<>();
        this.id = 0L;
    }

    @Override
    public Optional<Workout> findById(Long id) {
        return Optional.ofNullable(workouts.get(id));
    }

    @Override
    public Workout save(Workout workout) {
        workout.setId(++id);
        workouts.put(workout.getId(), workout);
        return workout;
    }

    @Override
    public void edit(Workout workout) {
        workouts.put(workout.getId(), workout);
    }

    @Override
    public void delete(Long id) {
        workouts.remove(id);
    }

    @Override
    public List<Workout> findAll() {
        return new ArrayList<>(workouts.values());
    }
}
