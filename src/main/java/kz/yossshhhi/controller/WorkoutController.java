package kz.yossshhhi.controller;

import kz.yossshhhi.model.ExtraOption;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.service.ExtraOptionService;
import kz.yossshhhi.service.ExtraOptionTypeService;
import kz.yossshhhi.service.WorkoutService;
import kz.yossshhhi.service.WorkoutTypeService;

import java.time.LocalDate;
import java.util.*;

/**
 * Controller class responsible for handling workout operations.
 */
public class WorkoutController {
    /**
     * Service for managing workout diary entries.
     */
    private final WorkoutService workoutService;

    /**
     * Service for managing extra options related to workouts.
     */
    private final ExtraOptionService extraOptionService;

    /**
     * Service for managing workout types.
     */
    private final WorkoutTypeService workoutTypeService;

    /** Service for managing extra option types. */
    private final ExtraOptionTypeService extraOptionTypeService;

    /**
     * Constructor to initialize the WorkoutController with required services.
     *
     * @param workoutService  Service for workout operations.
     * @param extraOptionService   Service for extra option operations.
     * @param workoutTypeService   Service for workout type operations.
     * @param extraOptionTypeService Service for managing extra option types.
     */
    public WorkoutController(WorkoutService workoutService, ExtraOptionService extraOptionService, WorkoutTypeService workoutTypeService, ExtraOptionTypeService extraOptionTypeService) {
        this.workoutService = workoutService;
        this.extraOptionService = extraOptionService;
        this.workoutTypeService = workoutTypeService;
        this.extraOptionTypeService = extraOptionTypeService;
    }

    /**
     * Creates a new workout record.
     *
     * @param userId          ID of the user performing the workout.
     * @param duration        Duration of the workout in minutes.
     * @param burnedCalories  Number of calories burned during the workout.
     * @param workoutTypeId   ID of the workout type.
     * @param extraOptions    Map of extra options associated with the workout and their values.
     * @return The created workout record.
     */
    public Workout create(Long userId, Integer duration, Integer burnedCalories, Long workoutTypeId, Map<Long, Integer> extraOptions) {
        Workout workout = Workout.builder()
                .userId(userId)
                .createdAt(LocalDate.now())
                .duration(duration)
                .burnedCalories(burnedCalories)
                .workoutTypeId(workoutTypeId)
                .build();
        Workout saved = workoutService.create(workout);
        saved.setExtraOptions(createExtraOptionList(extraOptions, saved.getId()));
        return saved;
    }

    /**
     * Creates a list of extra options based on the input map and associates them with the specified workout.
     *
     * @param extraOptionsInput A map containing extra option IDs as keys and their corresponding values.
     * @param workoutId The ID of the workout to associate the extra options with.
     * @return The list of created extra options.
     */
    public List<ExtraOption> createExtraOptionList(Map<Long, Integer> extraOptionsInput, Long workoutId) {
        return extraOptionService.saveAllByWorkout(extraOptionsInput, workoutId);
    }

    /**
     * Updates the extra options associated with a workout. It deletes the old list of extra options
     * and creates a new list based on the provided input map.
     *
     * @param oldList The old list of extra options to be deleted.
     * @param extraOptionsInput A map containing new extra option IDs as keys and their corresponding values.
     * @param workoutId The ID of the workout to associate the new extra options with.
     */
    public void updateExtraOptions(List<ExtraOption> oldList, Map<Long, Integer> extraOptionsInput, Long workoutId) {
        if (!oldList.isEmpty())
            extraOptionService.deleteAll(oldList);
        createExtraOptionList(extraOptionsInput, workoutId);
    }

    /**
     * Retrieves the list of workouts for the specified user.
     *
     * @param userId The ID of the user.
     * @return A string representation of the user's workout list.
     * @throws IllegalArgumentException If the user has no training records yet.
     */
    public String getUserWorkoutList(Long userId) {
        List<Workout> workoutList = workoutService.findAllByUserId(userId);
        if (workoutList.isEmpty()) {
            throw new IllegalArgumentException("You don't have any training records yet");
        }
        return workoutService.workoutListToString(workoutList);
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
     */
    public void delete(Long workoutId) {
        workoutService.delete(workoutId);
    }

    /**
     * Updates the provided workout in the database.
     *
     * @param workout The workout entity to be updated.
     */
    public void update(Workout workout) {
        workoutService.update(workout);
    }

    /**
     * Finds a workout entry by its ID.
     *
     * @param id The ID of the workout entry.
     * @return The workout entry with the specified ID.
     */
    public Workout findById(Long id) {
        return workoutService.findById(id);
    }

    /**
     * Retrieves statistics for workouts within the specified number of days for the specified user.
     *
     * @param days   The number of days to consider for statistics.
     * @param userId The ID of the user.
     * @return A string representation of the statistics.
     */
    public String getStatistics(Long userId, Integer days) {
        return workoutService.getStatistics(userId, days);
    }

    /**
     * Retrieves a string representation of all workout types.
     *
     * @return A string representation of all workout types.
     */
    public String getWorkoutTypesToString() {
        return workoutTypeService.getWorkoutTypesToString();
    }

    /**
     * Retrieves a string representation of all extra option types related to workouts.
     *
     * @return A string representation of all extra option types.
     */
    public String getExtraOptionsToString() {
        return extraOptionTypeService.getExtraOptionTypesToString();
    }
}
