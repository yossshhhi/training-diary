package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.model.ExtraOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing ExtraOption entities.
 */
public class ExtraOptionService {

    /**
     * Repository for handling ExtraOption entities in the database.
     */
    private final ExtraOptionRepository extraOptionRepository;

    /**
     * Constructs an ExtraOptionService with the provided ExtraOptionRepository.
     *
     * @param extraOptionRepository the repository for ExtraOption entities
     */
    public ExtraOptionService(ExtraOptionRepository extraOptionRepository) {
        this.extraOptionRepository = extraOptionRepository;
    }

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
        extraOptionRepository.deleteAll(extraOptions);
    }

    /**
     * Saves a list of ExtraOption entities associated with a workout.
     *
     * @param inputMap   a map containing ExtraOption type IDs and their corresponding values
     * @param workoutId  the ID of the associated workout
     * @return the list of saved ExtraOption entities
     */
    public List<ExtraOption> saveAllByWorkout(Map<Long, Integer> inputMap, Long workoutId) {
        List<ExtraOption> extraOptions = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : inputMap.entrySet()) {
            extraOptions.add(save(ExtraOption.builder()
                    .workoutId(workoutId)
                    .typeId(entry.getKey())
                    .value(entry.getValue())
                    .build()));
        }
        return extraOptions;
    }
}