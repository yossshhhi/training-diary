package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.*;
import kz.yossshhhi.dto.AggregateWorkoutDataDTO;
import kz.yossshhhi.dto.WorkoutDTO;
import kz.yossshhhi.mapper.AggregateWorkoutDataMapper;
import kz.yossshhhi.mapper.WorkoutMapper;
import kz.yossshhhi.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Service class for managing workout.
 */
@Service
@RequiredArgsConstructor
public class WorkoutService {

    /**
     * Repository for managing workout entities.
     */
    private final WorkoutRepository workoutRepository;

    /**
     * Repository for managing extra option entities.
     */
    private final ExtraOptionRepository extraOptionRepository;
    private final WorkoutMapper workoutMapper;
    private final AggregateWorkoutDataMapper dataMapper;

    /**
     * Creates a new workout.
     *
     * @param workoutDTO The workout DTO to create.
     * @param userId The ID of the user.
     * @return The created workout.
     * @throws IllegalArgumentException If a workout with the same date and workout type already exists.
     */
    public Workout create(WorkoutDTO workoutDTO, Long userId) {
        Workout workout = mapWorkoutToCreate(workoutDTO, userId);
        if (workoutRepository.existsByUserIdAndDateAndWorkoutTypeId(workout.getUserId(), workout.getCreatedAt(), workout.getWorkoutTypeId())) {
            throw new IllegalArgumentException("Workout with the same date and workout type already exists in the diary");
        }
        Workout save = workoutRepository.save(workout);
        saveExtraOptions(workout.getExtraOptions(), save.getId());
        return save;
    }

    /**
     * Finds all workouts recorded by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of workouts recorded by the user.
     */
    public List<WorkoutDTO> findAllByUserId(Long userId) {
        List<Workout> workouts = workoutRepository.findAllByUserId(userId);
        fillExtraOptions(workouts);
        return workouts.stream().map(workoutMapper::toDTO).toList();
    }

    /**
     * Finds all workouts.
     *
     * @return A list of all workouts.
     */
    public List<WorkoutDTO> findAll() {
        List<Workout> workouts = workoutRepository.findAll();
        fillExtraOptions(workouts);
        return workouts.stream().map(workoutMapper::toDTO).toList();
    }

    /**
     * Finds a workout by its ID.
     *
     * @param id The ID of the workout.
     * @return The found workout.
     * @throws IllegalArgumentException If the workout is not found.
     */
    public Workout findById(Long id) {
        Workout workout = workoutRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Workout not found"));
        workout.setExtraOptions(extraOptionRepository.findAllByWorkoutId(workout.getId()));
        return workout;
    }

    /**
     * Deletes a workout by its ID.
     *
     * @param id The ID of the workout to delete.
     */
    public boolean delete(Long id) {
        List<ExtraOption> options = extraOptionRepository.findAllByWorkoutId(id);
        if (!options.isEmpty())
            extraOptionRepository.deleteAll(options);
        return workoutRepository.delete(id);
    }

    /**
     * Updates a workout.
     *
     * @param workoutDTO The workout DTO to update.
     */
    public void update(WorkoutDTO workoutDTO) {
        Workout mapperEntity = workoutMapper.toEntity(workoutDTO);
        Workout find = findById(mapperEntity.getId());
        Workout workout = mapWorkoutToUpdate(workoutDTO, find);
        workoutRepository.save(workout);
        saveExtraOptions(workout.getExtraOptions(), workout.getId());
    }

    /**
     * Generates a statistics report for the specified user based on the aggregate workout data
     * obtained for the last specified number of days.
     *
     * @param userId The ID of the user for whom the statistics report is generated.
     * @param days   The number of days for which the statistics report is calculated.
     * @return A formatted string containing the statistics report.
     */
    public AggregateWorkoutDataDTO getStatistics(Long userId, Integer days) {
        LocalDate dayAfter = LocalDate.now().minusDays(days);
        AggregateWorkoutData data = workoutRepository.getAggregateDataByUserIdAndAfterDate(userId, dayAfter);
        return dataMapper.toDTO(data);
    }

    /**
     * Fills the extra options for each workout by querying the repository.
     * For each workout in the provided list, the method retrieves the corresponding
     * extra options from the repository and sets them in the workout object.
     * @param workouts The list of workouts for which to fill the extra options.
     */
    private void fillExtraOptions(List<Workout> workouts) {
        for (Workout workout : workouts) {
            workout.setExtraOptions(extraOptionRepository.findAllByWorkoutId(workout.getId()));
        }
    }

    /**
     * Saves a list of {@link ExtraOption} objects to the database with the specified workout ID.
     * Each option's workout ID is set before saving.
     *
     * @param options the list of extra options to save
     * @param workoutId the workout ID to be associated with each extra option
     */
    private void saveExtraOptions(List<ExtraOption> options, Long workoutId) {
        for (ExtraOption option : options) {
            option.setWorkoutId(workoutId);
            extraOptionRepository.save(option);
        }
    }

    /**
     * Maps properties from a {@link WorkoutDTO} to an existing {@link Workout} object.
     * Updates properties such as duration, burned calories, workout type ID, and extra options.
     * If extra options are provided, it first deletes the existing options, then maps and sets the new ones.
     *
     * @param workoutDTO the DTO containing new values for the workout properties
     * @param workout the existing Workout entity to update
     * @return the updated Workout entity
     */
    private Workout mapWorkoutToUpdate(WorkoutDTO workoutDTO, Workout workout) {
        if (workoutDTO.duration() != null) {
            workout.setDuration(workoutDTO.duration());
        }
        if (workoutDTO.burnedCalories() != null) {
            workout.setBurnedCalories(workoutDTO.burnedCalories());
        }
        if (workoutDTO.workoutTypeId() != null) {
            workout.setWorkoutTypeId(workoutDTO.workoutTypeId());
        }
        if (workoutDTO.extraOptions() != null) {
            extraOptionRepository.deleteAll(workout.getExtraOptions());
            List<ExtraOption> options = workoutDTO.extraOptions().stream().map(workoutMapper::toExtraOption).toList();
            workout.setExtraOptions(options);
        }
        return workout;
    }

    /**
     * Creates a new {@link Workout} entity from a {@link WorkoutDTO} and sets the user ID and current date.
     * This method is intended to be used when creating a new workout record in the database.
     *
     * @param workoutDTO the DTO with data for creating a new workout
     * @param userId the user ID to associate with the new workout
     * @return the newly created Workout entity
     */
    private Workout mapWorkoutToCreate(WorkoutDTO workoutDTO, long userId) {
        Workout entity = workoutMapper.toEntity(workoutDTO);
        entity.setUserId(userId);
        entity.setCreatedAt(LocalDate.now());
        return entity;
    }
}
