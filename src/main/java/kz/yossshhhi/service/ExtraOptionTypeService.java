package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionTypeRepository;
import kz.yossshhhi.model.ExtraOptionType;

import java.util.List;

/**
 * Service class for managing {@link ExtraOptionType} entities.
 */
public class ExtraOptionTypeService {

    /**
     * The repository for managing {@link ExtraOptionType} entities.
     */
    private final ExtraOptionTypeRepository extraOptionTypeRepository;

    /**
     * Constructs an {@link ExtraOptionTypeService} with the specified {@link ExtraOptionTypeRepository}.
     *
     * @param extraOptionTypeRepository the repository to be used by the service
     */
    public ExtraOptionTypeService(ExtraOptionTypeRepository extraOptionTypeRepository) {
        this.extraOptionTypeRepository = extraOptionTypeRepository;
    }

    /**
     * Creates a new extra option type.
     *
     * @param extraOptionType the extra option type to create
     * @return the created extra option type
     * @throws IllegalArgumentException if an extra option type with the same name already exists
     */
    public ExtraOptionType create(ExtraOptionType extraOptionType) {
        extraOptionTypeRepository.findByName(extraOptionType.getName()).ifPresent(option -> {
            throw new IllegalArgumentException("Extra option type with " + option.getName() + " name already exists");
        });
        return extraOptionTypeRepository.save(extraOptionType);
    }

    /**
     * Retrieves a string representation of all extra option types.
     *
     * @return A string containing the IDs and names of all extra option types.
     */
    public List<ExtraOptionType> findAll() {
        return extraOptionTypeRepository.findAll();
    }
}

