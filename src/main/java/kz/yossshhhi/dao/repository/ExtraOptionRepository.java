package kz.yossshhhi.dao.repository;

import kz.yossshhhi.model.ExtraOption;

import java.util.List;

/**
 * Repository interface for managing {@link ExtraOption} entities.
 */
public interface ExtraOptionRepository {

    /**
     * Saves an ExtraOption entity to the database.
     *
     * @param extraOption The ExtraOption to save.
     * @return The saved ExtraOption entity.
     */
    ExtraOption save(ExtraOption extraOption);

    /**
     * Finds all ExtraOption entities associated with a given Workout ID.
     *
     * @param workoutId The ID of the Workout to find ExtraOptions for.
     * @return A list of ExtraOption entities associated with the given Workout ID.
     */
    List<ExtraOption> findAllByWorkoutId(Long workoutId);

    /**
     * Deletes a list of ExtraOption entities from the database.
     *
     * @param extraOptions The list of ExtraOptions to delete.
     */
    void deleteAll(List<ExtraOption> extraOptions);

}

