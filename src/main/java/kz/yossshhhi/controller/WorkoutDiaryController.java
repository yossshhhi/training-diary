package kz.yossshhhi.controller;

import kz.yossshhhi.model.ExtraOption;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.service.ExtraOptionService;
import kz.yossshhhi.service.WorkoutDiaryService;
import kz.yossshhhi.service.WorkoutTypeService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class responsible for handling workout diary operations.
 */
public class WorkoutDiaryController {
    /**
     * Service for managing workout diary entries.
     */
    private final WorkoutDiaryService workoutDiaryService;

    /**
     * Service for managing extra options related to workouts.
     */
    private final ExtraOptionService extraOptionService;

    /**
     * Service for managing workout types.
     */
    private final WorkoutTypeService workoutTypeService;

    /**
     * Constructor to initialize the WorkoutDiaryController with required services.
     *
     * @param workoutDiaryService  Service for workout diary operations.
     * @param extraOptionService   Service for extra option operations.
     * @param workoutTypeService   Service for workout type operations.
     */
    public WorkoutDiaryController(WorkoutDiaryService workoutDiaryService, ExtraOptionService extraOptionService, WorkoutTypeService workoutTypeService) {
        this.workoutDiaryService = workoutDiaryService;
        this.extraOptionService = extraOptionService;
        this.workoutTypeService = workoutTypeService;
    }

    /**
     * Creates a new workout record and adds it to the workout diary.
     *
     * @param userId          ID of the user performing the workout.
     * @param duration        Duration of the workout in minutes.
     * @param burnedCalories  Number of calories burned during the workout.
     * @param workoutTypeId   ID of the workout type.
     * @param extraOptions    Map of extra options associated with the workout and their values.
     * @return The created workout record.
     */
    public Workout create(Long userId, Integer duration, Integer burnedCalories, Long workoutTypeId, Map<ExtraOption, Integer> extraOptions) {
        Workout workout = Workout.builder()
                .createdAt(LocalDate.now())
                .duration(duration)
                .burnedCalories(burnedCalories)
                .workoutTypeId(workoutTypeId)
                .extraOptions(extraOptions)
                .build();

        return workoutDiaryService.create(workout, userId);
    }

    /**
     * Creates a map of extra options from the input data.
     *
     * @param extraOptionsInput  Map containing IDs and values of extra options.
     * @return A map of ExtraOption objects and their corresponding values.
     * @throws IllegalArgumentException If the input data is invalid.
     */
    public Map<ExtraOption, Integer> createExtraOptionMap(Map<Long, String> extraOptionsInput) {
        Map<ExtraOption, Integer> extraOptions = new HashMap<>();
        try {
            for (Map.Entry<Long, String> optionEntry : extraOptionsInput.entrySet()) {
                ExtraOption option = extraOptionService.findById(optionEntry.getKey());
                extraOptions.put(option, Integer.parseInt(optionEntry.getValue()));
            }
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Invalid extra option data (extra option values must be numbers)");
        }
        return extraOptions;
    }

    /**
     * Retrieves the list of workouts for the specified user.
     *
     * @param userId The ID of the user.
     * @return A string representation of the user's workout list.
     * @throws IllegalArgumentException If the user has no training records yet.
     */
    public String getUserWorkoutList(Long userId) {
        List<Workout> workoutList = workoutDiaryService.findAllByUserId(userId);
        if (workoutList.isEmpty()) {
            throw new IllegalArgumentException("You don't have any training records yet");
        }
        return workoutDiaryService.workoutListToString(workoutList);
    }

    /**
     * Checks if a workout type with the specified ID exists.
     *
     * @param workoutTypeId The ID of the workout type to check.
     * @return {@code true} if the workout type exists, {@code false} otherwise.
     */
    public boolean existsWorkoutTypeById(Long workoutTypeId) {
        return workoutTypeService.existsById(workoutTypeId);
    }

    /**
     * Deletes a workout entry for the specified user.
     *
     * @param workoutId The ID of the workout entry to delete.
     * @param userId    The ID of the user.
     */
    public void delete(Long workoutId, Long userId) {
        List<Workout> allByUserId = workoutDiaryService.findAllByUserId(userId);
        Workout workout = workoutDiaryService.findById(workoutId);
        allByUserId.remove(workout);
        workoutDiaryService.delete(workoutId);
    }

    /**
     * Finds a workout entry by its ID.
     *
     * @param id The ID of the workout entry.
     * @return The workout entry with the specified ID.
     */
    public Workout findById(Long id) {
        return workoutDiaryService.findById(id);
    }

    /**
     * Retrieves statistics for workouts within the specified number of days for the specified user.
     *
     * @param days   The number of days to consider for statistics.
     * @param userId The ID of the user.
     * @return A string representation of the statistics.
     */
    public String getStatistics(Integer days, Long userId) {
        Map<String, Integer> statistics = workoutDiaryService.getStatistics(days, userId);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> statisticEntry : statistics.entrySet()) {
            stringBuilder.append(statisticEntry.getKey())
                    .append(statisticEntry.getValue())
                    .append("\n");
        }
        return stringBuilder.append("\n").toString();
    }

    /**
     * Retrieves a string representation of all workout types.
     *
     * @return A string representation of all workout types.
     */
    public String getWorkoutTypesToString() {
        return workoutDiaryService.getWorkoutTypesToString();
    }

    /**
     * Retrieves a string representation of all extra options related to workouts.
     *
     * @return A string representation of all extra options.
     */
    public String getExtraOptionsToString() {
        return workoutDiaryService.getExtraOptionsToString();
    }
}
