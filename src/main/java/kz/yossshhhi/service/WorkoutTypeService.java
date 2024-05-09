package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.WorkoutTypeRepository;
import kz.yossshhhi.dto.WorkoutTypeDTO;
import kz.yossshhhi.mapper.WorkoutTypeMapper;
import kz.yossshhhi.model.WorkoutType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing workout types.
 */
@Service
@RequiredArgsConstructor
public class WorkoutTypeService {
    /**
     * The repository for workout types.
     */
    private final WorkoutTypeRepository workoutTypeRepository;
    private final WorkoutTypeMapper mapper;

    /**
     * Creates a new workout type.
     *
     * @param dto the workout type dto to create
     * @throws IllegalArgumentException if a workout type with the same name already exists
     */
    public WorkoutType create(WorkoutTypeDTO dto) {
        WorkoutType workoutType = mapper.toEntity(dto);
        workoutTypeRepository.findByName(workoutType.getName()).ifPresent(type -> {
            throw new IllegalArgumentException("Workout type with " + type.getName() + " name already exists");
        });
        return workoutTypeRepository.save(workoutType);
    }

    /**
     * Finds a workout type by its ID.
     *
     * @param id the ID of the workout type to find
     * @return the found workout type
     * @throws IllegalArgumentException if the workout type is not found
     */
    public WorkoutType findById(Long id) {
        return workoutTypeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Workout type not found"));
    }

    /**
     * Checks if a workout type exists with the given ID.
     *
     * @param id the ID of the workout type to check
     * @return {@code true} if the workout type exists, {@code false} otherwise
     */
    public boolean existsById(Long id) {
        return workoutTypeRepository.findById(id).isPresent();
    }

    /**
     * Retrieves all workout types.
     *
     * @return a list of all workout types
     */
    public List<WorkoutTypeDTO> findAll() {
        return workoutTypeRepository.findAll().stream().map(mapper::toDTO).toList();
    }

}

