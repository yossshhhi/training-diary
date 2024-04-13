package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.dao.repository.WorkoutDiaryRepository;
import kz.yossshhhi.dao.repository.WorkoutRepository;
import kz.yossshhhi.dao.repository.WorkoutTypeRepository;
import kz.yossshhhi.model.ExtraOption;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.model.WorkoutDiary;
import kz.yossshhhi.model.WorkoutType;

import java.time.LocalDate;
import java.util.*;

/**
 * Service class for managing workout diaries.
 */
public class WorkoutDiaryService {

    /**
     * Repository for managing workout entities.
     */
    private final WorkoutRepository workoutRepository;

    /**
     * Repository for managing workout diary entities.
     */
    private final WorkoutDiaryRepository diaryRepository;

    /**
     * Repository for managing extra option entities.
     */
    private final ExtraOptionRepository extraOptionRepository;

    /**
     * Repository for managing workout type entities.
     */
    private final WorkoutTypeRepository workoutTypeRepository;

    /**
     * Constructs a new instance of WorkoutDiaryService.
     *
     * @param workoutRepository     Repository for managing workout entities.
     * @param diaryRepository       Repository for managing workout diary entities.
     * @param extraOptionRepository Repository for managing extra option entities.
     * @param workoutTypeRepository Repository for managing workout type entities.
     */
    public WorkoutDiaryService(WorkoutRepository workoutRepository, WorkoutDiaryRepository diaryRepository, ExtraOptionRepository extraOptionRepository, WorkoutTypeRepository workoutTypeRepository) {
        this.workoutRepository = workoutRepository;
        this.diaryRepository = diaryRepository;
        this.extraOptionRepository = extraOptionRepository;
        this.workoutTypeRepository = workoutTypeRepository;
    }

    /**
     * Creates a new workout and adds it to the workout diary for the specified user.
     *
     * @param workout The workout to create.
     * @param userId  The ID of the user associated with the workout diary.
     * @return The created workout.
     * @throws IllegalArgumentException If a workout with the same date and workout type already exists in the diary.
     */
    public Workout create(Workout workout, Long userId) {
        Optional<WorkoutDiary> workoutDiaryOptional = diaryRepository.findByUserId(userId);
        WorkoutDiary workoutDiary;
        if (workoutDiaryOptional.isEmpty()) {
            workoutDiary = WorkoutDiary.builder()
                    .userId(userId)
                    .workoutList(new ArrayList<>())
                    .build();
            diaryRepository.save(workoutDiary);
        } else {
            workoutDiary = workoutDiaryOptional.get();
        }
        if (diaryRepository.existsByDateAndWorkoutTypeId(workoutDiary.getId(), workout.getCreatedAt(), workout.getWorkoutTypeId())) {
            throw new IllegalArgumentException("Workout with the same date and workout type already exists in the diary");
        }
        Workout save = workoutRepository.save(workout);
        workoutDiary.getWorkoutList().add(save);
        return save;
    }

    /**
     * Finds all workouts recorded by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of workouts recorded by the user.
     */
    public List<Workout> findAllByUserId(Long userId) {
        Optional<WorkoutDiary> workoutDiary = diaryRepository.findByUserId(userId);
        if (workoutDiary.isPresent()) {
            return workoutDiary.get().getWorkoutList();
        }
        return Collections.emptyList();
    }

    /**
     * Finds all workouts.
     *
     * @return A list of all workouts.
     */
    public List<Workout> findAll() {
        return workoutRepository.findAll();
    }

    /**
     * Finds a workout by its ID.
     *
     * @param id The ID of the workout.
     * @return The found workout.
     * @throws IllegalArgumentException If the workout is not found.
     */
    public Workout findById(Long id) {
        return workoutRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Workout not found"));
    }

    /**
     * Deletes a workout by its ID.
     *
     * @param id The ID of the workout to delete.
     */
    public void delete(Long id) {
        workoutRepository.delete(id);
    }

    /**
     * Retrieves statistics for workouts within a specified number of days for a given user.
     *
     * @param days   The number of days to consider for the statistics.
     * @param userId The ID of the user for whom to retrieve statistics.
     * @return A map containing statistics such as total workouts completed, duration, calories burned, and distance covered.
     */
    public Map<String, Integer> getStatistics(Integer days, Long userId) {
        LocalDate dayAfter = LocalDate.now().minusDays(days + 1);
        List<Workout> workoutList = findAllByUserId(userId).stream()
                .filter(workout -> workout.getCreatedAt().isAfter(dayAfter))
                .toList();

        Map<String, Integer> statistics = new LinkedHashMap<>();
        String workoutNumber = "Total workouts completed in " + days + " days: ";
        String durationStatistic = "Total time spent on training in " + days + " days: ";
        String caloriesStatistic = "Total calories burned in " + days + " days: ";
        String distanceStatistic = "The entire distance covered in " + days + " days: ";
        Integer duration = 0;
        Integer calories = 0;
        Integer distance = 0;

        Optional<ExtraOption> distanceCovered = extraOptionRepository.findByName("Distance covered");
        for (Workout workout : workoutList) {
            duration += workout.getDuration();
            calories += workout.getBurnedCalories();
            if (distanceCovered.isPresent() && workout.getExtraOptions().containsKey(distanceCovered.get())) {
                distance += workout.getExtraOptions().get(distanceCovered.get());
            }
        }
        statistics.put(workoutNumber, workoutList.size());
        statistics.put(durationStatistic, duration);
        statistics.put(caloriesStatistic, calories);
        statistics.put(distanceStatistic, distance);
        return statistics;
    }

    /**
     * Converts a list of workouts to a string representation.
     *
     * @param workoutList The list of workouts to convert.
     * @return A string representation of the workout list.
     */
    public String workoutListToString(List<Workout> workoutList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Workout workout : workoutList) {
            stringBuilder.append(workout.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Retrieves a string representation of all workout types.
     *
     * @return A string containing the IDs and names of all workout types.
     */
    public String getWorkoutTypesToString() {
        List<WorkoutType> workoutTypes = workoutTypeRepository.findAll();
        StringBuilder stringBuilder = new StringBuilder();
        for (WorkoutType workoutType : workoutTypes) {
            stringBuilder.append(workoutType.getId()).append(". ")
                    .append(workoutType.getName()).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Retrieves a string representation of all extra options.
     *
     * @return A string containing the IDs and names of all extra options.
     */
    public String getExtraOptionsToString() {
        List<ExtraOption> extraOptions = extraOptionRepository.findAll();
        StringBuilder stringBuilder = new StringBuilder();
        for (ExtraOption extraOption : extraOptions) {
            stringBuilder.append(extraOption.getId()).append(". ")
                    .append(extraOption.getName()).append("\n");
        }
        return stringBuilder.toString();
    }
}
