package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.WorkoutTypeRepository;
import kz.yossshhhi.model.WorkoutType;

import java.util.*;

public class WorkoutTypeDAO implements WorkoutTypeRepository {
    private final Map<Long, WorkoutType> workoutTypes;
    private Long id;

    public WorkoutTypeDAO() {
        this.workoutTypes = new LinkedHashMap<>();
        this.id = 0L;

        workoutTypes.put(++id, new WorkoutType(id, "Cardio"));
        workoutTypes.put(++id, new WorkoutType(id, "Power training"));
        workoutTypes.put(++id, new WorkoutType(id, "Functional training"));
        workoutTypes.put(++id, new WorkoutType(id, "Dance workouts"));
        workoutTypes.put(++id, new WorkoutType(id, "Yoga"));
        workoutTypes.put(++id, new WorkoutType(id, "Pilates"));
    }

    @Override
    public Optional<WorkoutType> findById(Long id) {
        return Optional.ofNullable(workoutTypes.get(id));
    }

    @Override
    public Optional<WorkoutType> findByName(String username) {
        return workoutTypes.values().stream()
                .filter(workoutType -> workoutType.getName().equals(username))
                .findFirst();
    }

    @Override
    public WorkoutType save(WorkoutType workoutType) {
        workoutType.setId(++id);
        workoutTypes.put(workoutType.getId(), workoutType);
        return workoutType;
    }

    @Override
    public List<WorkoutType> findAll() {
        return new ArrayList<>(workoutTypes.values());
    }

}
