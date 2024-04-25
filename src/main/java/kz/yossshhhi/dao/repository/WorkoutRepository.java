package kz.yossshhhi.dao.repository;

import kz.yossshhhi.model.AggregateWorkoutData;
import kz.yossshhhi.model.Workout;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * This interface represents a repository for managing Workout entities.
 */
public interface WorkoutRepository {

    /**
     * Finds a Workout entity by its unique identifier.
     *
     * @param id The unique identifier of the Workout entity to find.
     * @return An Optional containing the found Workout entity, or empty if not found.
     */
    Optional<Workout> findById(Long id);

    /**
     * Saves a new Workout entity or updates an existing one.
     *
     * @param workout The Workout entity to save or update.
     * @return The saved or updated Workout entity.
     */
    Workout save(Workout workout);

    /**
     * Deletes a Workout entity by its unique identifier.
     *
     * @param id The unique identifier of the Workout entity to delete.
     */
    void delete(Long id);

    /**
     * Retrieves all Workout entities stored in the repository.
     *
     * @return A list containing all Workout entities.
     */
    List<Workout> findAll();
    /**
     * Retrieves a list of workouts associated with the given user ID.
     *
     * @param userId The unique identifier of the user.
     * @return A list of workouts associated with the specified user ID.
     */
    List<Workout> findAllByUserId(Long userId);

    /**
     * Checks if a workout exists for the specified user ID, date, and workout type ID.
     *
     * @param userId The unique identifier of the user.
     * @param date The date of the workout.
     * @param workoutTypeId The unique identifier of the workout type.
     * @return {@code true} if a workout exists for the specified criteria, {@code false} otherwise.
     */
    boolean existsByUserIdAndDateAndWorkoutTypeId(Long userId, LocalDate date, Long workoutTypeId);

    /**
     * Retrieves aggregated workout data for a specific user after a given date.
     *
     * @param userId The ID of the user.
     * @param date   The date after which the workout data is aggregated.
     * @return An {@link AggregateWorkoutData} object containing aggregated workout data.
     */
    AggregateWorkoutData getAggregateDataByUserIdAndAfterDate(Long userId, LocalDate date);
}

