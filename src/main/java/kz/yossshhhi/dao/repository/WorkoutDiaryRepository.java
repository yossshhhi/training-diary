package kz.yossshhhi.dao.repository;

import kz.yossshhhi.model.WorkoutDiary;

import java.time.LocalDate;
import java.util.Optional;

/**
 * This interface represents a repository for managing workout diary entries.
 */
public interface WorkoutDiaryRepository {

    /**
     * Retrieves a workout diary entry by its unique identifier.
     *
     * @param id The unique identifier of the workout diary entry.
     * @return An {@link Optional} containing the workout diary entry if found, or empty otherwise.
     */
    Optional<WorkoutDiary> findById(Long id);

    /**
     * Retrieves workout diary entries by the user's unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return An {@link Optional} containing the workout diary entry if found, or empty otherwise.
     */
    Optional<WorkoutDiary> findByUserId(Long id);

    /**
     * Saves a workout diary entry.
     *
     * @param workoutDiary The workout diary entry to be saved.
     * @return The saved workout diary entry.
     */
    WorkoutDiary save(WorkoutDiary workoutDiary);

    /**
     * Checks if a workout diary entry exists for the specified date and workout type ID.
     *
     * @param workoutDiaryId The ID of the workout diary entry to exclude from the check.
     * @param date           The date for which to check the existence of a workout diary entry.
     * @param workoutTypeId  The ID of the workout type to check.
     * @return {@code true} if a workout diary entry exists for the specified date and workout type ID, {@code false} otherwise.
     */
    boolean existsByDateAndWorkoutTypeId(Long workoutDiaryId, LocalDate date, Long workoutTypeId);
}

