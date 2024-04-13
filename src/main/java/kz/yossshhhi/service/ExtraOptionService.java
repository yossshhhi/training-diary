package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.model.ExtraOption;

import java.util.List;

/**
 * Service class for managing {@link ExtraOption} entities.
 */
public class ExtraOptionService {

    /**
     * The repository for managing {@link ExtraOption} entities.
     */
    private final ExtraOptionRepository extraOptionRepository;

    /**
     * Constructs an {@code ExtraOptionService} with the specified {@link ExtraOptionRepository}.
     *
     * @param extraOptionRepository the repository to be used by the service
     */
    public ExtraOptionService(ExtraOptionRepository extraOptionRepository) {
        this.extraOptionRepository = extraOptionRepository;
    }

    /**
     * Creates a new extra option.
     *
     * @param extraOption the extra option to create
     * @return the created extra option
     * @throws IllegalArgumentException if an extra option with the same name already exists
     */
    public ExtraOption create(ExtraOption extraOption) {
        extraOptionRepository.findByName(extraOption.getName()).ifPresent(option -> {
            throw new IllegalArgumentException("Extra option with " + option.getName() + " name already exists");
        });
        return extraOptionRepository.save(extraOption);
    }

    /**
     * Finds an extra option by its ID.
     *
     * @param id the ID of the extra option to find
     * @return the found extra option
     * @throws IllegalArgumentException if no extra option with the specified ID is found
     */
    public ExtraOption findById(Long id) {
        return extraOptionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Extra option not found"));
    }

    /**
     * Retrieves all extra options.
     *
     * @return a list of all extra options
     */
    public List<ExtraOption> findAll() {
        return extraOptionRepository.findAll();
    }
}

