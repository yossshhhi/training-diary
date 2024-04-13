package kz.yossshhhi.dao.repository;

import kz.yossshhhi.model.WorkoutType;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents a repository for managing workout types.
 */
public interface WorkoutTypeRepository {

    /**
     * Retrieves a workout type by its ID.
     *
     * @param id The ID of the workout type to retrieve.
     * @return An {@code Optional} containing the retrieved workout type, or empty if not found.
     */
    Optional<WorkoutType> findById(Long id);

    /**
     * Retrieves a workout type by its name.
     *
     * @param name The name of the workout type to retrieve.
     * @return An {@code Optional} containing the retrieved workout type, or empty if not found.
     */
    Optional<WorkoutType> findByName(String name);

    /**
     * Saves a workout type.
     *
     * @param workoutType The workout type to save.
     * @return The saved workout type.
     */
    WorkoutType save(WorkoutType workoutType);

    /**
     * Retrieves all workout types.
     *
     * @return A list of all workout types.
     */
    List<WorkoutType> findAll();
}

