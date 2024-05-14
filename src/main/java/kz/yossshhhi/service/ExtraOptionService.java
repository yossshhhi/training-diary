package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.model.ExtraOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing ExtraOption entities.
 */
@Service
@RequiredArgsConstructor
public class ExtraOptionService {

    /**
     * Repository for handling ExtraOption entities in the database.
     */
    private final ExtraOptionRepository extraOptionRepository;

    /**
     * Saves the given ExtraOption entity.
     *
     * @param extraOption the ExtraOption entity to save
     * @return the saved ExtraOption entity
     */
    public ExtraOption save(ExtraOption extraOption) {
        return extraOptionRepository.save(extraOption);
    }

    /**
     * Deletes all ExtraOption entities in the provided list.
     *
     * @param extraOptions the list of ExtraOption entities to delete
     */
    public void deleteAll(List<ExtraOption> extraOptions) {
        if (!extraOptions.isEmpty())
            extraOptionRepository.deleteAll(extraOptions);
    }
}