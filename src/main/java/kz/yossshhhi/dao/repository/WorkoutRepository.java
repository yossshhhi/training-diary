package kz.yossshhhi.dao.repository;

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
     * Updates an existing Workout entity.
     *
     * @param workout The Workout entity to update.
     */
    void edit(Workout workout);

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
}

