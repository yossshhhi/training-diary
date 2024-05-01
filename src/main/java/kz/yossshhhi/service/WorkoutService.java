package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.*;
import kz.yossshhhi.model.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Service class for managing workout.
 */
public class WorkoutService {

    /**
     * Repository for managing workout entities.
     */
    private final WorkoutRepository workoutRepository;

    /**
     * Repository for managing extra option entities.
     */
    private final ExtraOptionRepository extraOptionRepository;

    /**
     * Constructs a new instance of WorkoutService.
     *
     * @param workoutRepository     Repository for managing workout entities.
     * @param extraOptionRepository Repository for managing extra option entities.
     */
    public WorkoutService(WorkoutRepository workoutRepository, ExtraOptionRepository extraOptionRepository) {
        this.workoutRepository = workoutRepository;
        this.extraOptionRepository = extraOptionRepository;
    }

    /**
     * Creates a new workout.
     *
     * @param workout The workout to create.
     * @return The created workout.
     * @throws IllegalArgumentException If a workout with the same date and workout type already exists.
     */
    public Workout create(Workout workout) {
        if (workoutRepository.existsByUserIdAndDateAndWorkoutTypeId(workout.getUserId(), workout.getCreatedAt(), workout.getWorkoutTypeId())) {
            throw new IllegalArgumentException("Workout with the same date and workout type already exists in the diary");
        }
        Workout save = workoutRepository.save(workout);
        saveExtraOptions(workout.getExtraOptions(), save.getId());
        return save;
    }

    /**
     * Finds all workouts recorded by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of workouts recorded by the user.
     */
    public List<Workout> findAllByUserId(Long userId) {
        List<Workout> workouts = workoutRepository.findAllByUserId(userId);
        fillExtraOptions(workouts);
        return workouts;
    }

    /**
     * Finds all workouts.
     *
     * @return A list of all workouts.
     */
    public List<Workout> findAll() {
        List<Workout> workouts = workoutRepository.findAll();
        fillExtraOptions(workouts);
        return workouts;
    }

    /**
     * Finds a workout by its ID.
     *
     * @param id The ID of the workout.
     * @return The found workout.
     * @throws IllegalArgumentException If the workout is not found.
     */
    public Workout findById(Long id) {
        Workout workout = workoutRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Workout not found"));
        workout.setExtraOptions(extraOptionRepository.findAllByWorkoutId(workout.getId()));
        return workout;
    }

    /**
     * Deletes a workout by its ID.
     *
     * @param id The ID of the workout to delete.
     */
    public void delete(Long id) {
        List<ExtraOption> options = extraOptionRepository.findAllByWorkoutId(id);
        if (!options.isEmpty())
            extraOptionRepository.deleteAll(options);
        workoutRepository.delete(id);
    }

    /**
     * Updates a workout.
     *
     * @param workout The workout to update.
     */
    public void update(Workout workout) {
        workoutRepository.save(workout);
        saveExtraOptions(workout.getExtraOptions(), workout.getId());
    }

    /**
     * Generates a statistics report for the specified user based on the aggregate workout data
     * obtained for the last specified number of days.
     *
     * @param userId The ID of the user for whom the statistics report is generated.
     * @param days   The number of days for which the statistics report is calculated.
     * @return A formatted string containing the statistics report.
     */
    public AggregateWorkoutData getStatistics(Long userId, Integer days) {
        LocalDate dayAfter = LocalDate.now().minusDays(days);
        return workoutRepository.getAggregateDataByUserIdAndAfterDate(userId, dayAfter);
    }

    /**
     * Fills the extra options for each workout by querying the repository.
     * For each workout in the provided list, the method retrieves the corresponding
     * extra options from the repository and sets them in the workout object.
     * @param workouts The list of workouts for which to fill the extra options.
     */
    private void fillExtraOptions(List<Workout> workouts) {
        for (Workout workout : workouts) {
            workout.setExtraOptions(extraOptionRepository.findAllByWorkoutId(workout.getId()));
        }
    }

    private void saveExtraOptions(List<ExtraOption> options, Long workoutId) {
        for (ExtraOption option : options) {
            option.setWorkoutId(workoutId);
            extraOptionRepository.save(option);
        }
    }
}
