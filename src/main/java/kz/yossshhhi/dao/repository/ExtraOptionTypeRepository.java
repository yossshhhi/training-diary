package kz.yossshhhi.dao.repository;


import kz.yossshhhi.model.ExtraOptionType;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing ExtraOptionType entities.
 */
public interface ExtraOptionTypeRepository {

    /**
     * Finds an ExtraOptionType by its unique identifier.
     *
     * @param id The unique identifier of the ExtraOptionType.
     * @return An Optional containing the ExtraOptionType if found, or empty otherwise.
     */
    Optional<ExtraOptionType> findById(Long id);

    /**
     * Finds an ExtraOptionType by its name.
     *
     * @param name The name of the ExtraOptionType.
     * @return An Optional containing the ExtraOptionType if found, or empty otherwise.
     */
    Optional<ExtraOptionType> findByName(String name);

    /**
     * Saves the given ExtraOptionType.
     *
     * @param extraOptionType The ExtraOptionType to save.
     * @return The saved ExtraOptionType.
     */
    ExtraOptionType save(ExtraOptionType extraOptionType);

    /**
     * Retrieves all ExtraOptionTypes.
     *
     * @return A list of all ExtraOptionTypes.
     */
    List<ExtraOptionType> findAll();
}

