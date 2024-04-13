package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.WorkoutDiaryRepository;
import kz.yossshhhi.model.WorkoutDiary;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WorkoutDiaryDAO implements WorkoutDiaryRepository {
    private final Map<Long, WorkoutDiary> workoutDiaries;
    private Long id;

    public WorkoutDiaryDAO() {
        this.workoutDiaries = new HashMap<>();
        this.id = 0L;
    }

    @Override
    public Optional<WorkoutDiary> findById(Long id) {
        return Optional.ofNullable(workoutDiaries.get(id));
    }

    @Override
    public boolean existsByDateAndWorkoutTypeId(Long workoutDiaryId, LocalDate date, Long workoutTypeId) {
        Optional<WorkoutDiary> diary = findById(workoutDiaryId);
        return diary.map(workoutDiary -> workoutDiary.getWorkoutList().stream()
                .anyMatch(workout -> workout.getCreatedAt().equals(date) && workout.getWorkoutTypeId().equals(workoutTypeId))).orElse(false);
    }

    @Override
    public Optional<WorkoutDiary> findByUserId(Long id) {
        return workoutDiaries.values().stream()
                .filter(diary -> diary.getUserId().equals(id))
                .findFirst();
    }

    @Override
    public WorkoutDiary save(WorkoutDiary workoutDiary) {
        workoutDiary.setId(++id);
        workoutDiaries.put(workoutDiary.getId(), workoutDiary);
        return workoutDiary;
    }
}
