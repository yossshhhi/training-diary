package kz.yossshhhi.dao.repository;


import kz.yossshhhi.model.ExtraOption;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing ExtraOption entities.
 */
public interface ExtraOptionRepository {

    /**
     * Finds an ExtraOption by its unique identifier.
     *
     * @param id The unique identifier of the ExtraOption.
     * @return An Optional containing the ExtraOption if found, or empty otherwise.
     */
    Optional<ExtraOption> findById(Long id);

    /**
     * Finds an ExtraOption by its name.
     *
     * @param name The name of the ExtraOption.
     * @return An Optional containing the ExtraOption if found, or empty otherwise.
     */
    Optional<ExtraOption> findByName(String name);

    /**
     * Saves the given ExtraOption.
     *
     * @param extraOption The ExtraOption to save.
     * @return The saved ExtraOption.
     */
    ExtraOption save(ExtraOption extraOption);

    /**
     * Retrieves all ExtraOptions.
     *
     * @return A list of all ExtraOptions.
     */
    List<ExtraOption> findAll();

    /**
     * Retrieves the total count of ExtraOptions.
     *
     * @return The total count of ExtraOptions.
     */
    Long getCount();
}

