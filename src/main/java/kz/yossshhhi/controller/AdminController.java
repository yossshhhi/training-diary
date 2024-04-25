package kz.yossshhhi.controller;

import kz.yossshhhi.model.ExtraOptionType;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.model.WorkoutType;
import kz.yossshhhi.service.ExtraOptionTypeService;
import kz.yossshhhi.service.WorkoutService;
import kz.yossshhhi.service.WorkoutTypeService;

import java.util.List;

/**
 * Controller class responsible for handling administrative operations related to workouts.
 */
public class AdminController {

    /** Service for managing workout entries. */
    private final WorkoutService workoutService;

    /** Service for managing workout types. */
    private final WorkoutTypeService workoutTypeService;

    /** Service for managing extra option types. */
    private final ExtraOptionTypeService extraOptionTypeService;

    /**
     * Constructs a new AdminController with the specified services.
     *
     * @param workoutService Service for managing workout entries.
     * @param workoutTypeService Service for managing workout types.
     * @param extraOptionTypeService Service for managing extra option types.
     */
    public AdminController(WorkoutService workoutService, WorkoutTypeService workoutTypeService,
                           ExtraOptionTypeService extraOptionTypeService) {
        this.workoutService = workoutService;
        this.workoutTypeService = workoutTypeService;
        this.extraOptionTypeService = extraOptionTypeService;
    }

    /**
     * Retrieves a string representation of all workout records.
     *
     * @return A string containing details of all workout records.
     * @throws IllegalArgumentException if there are no workout records.
     */
    public String showAllWorkouts() {
        List<Workout> workouts = workoutService.findAll();
        if (workouts.isEmpty()) {
            throw new IllegalArgumentException("There are no workout records");
        }
        return workoutService.workoutListToString(workouts);
    }

    /**
     * Adds a new workout type with the specified name.
     *
     * @param name The name of the new workout type.
     * @throws IllegalArgumentException if the name is empty or blank.
     */
    public void addWorkoutType(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("The workout type name cannot be empty");
        }
        workoutTypeService.create(WorkoutType.builder().name(name).build());
    }

    /**
     * Adds a new extra option type with the specified name.
     *
     * @param name The name of the new extra option type.
     * @throws IllegalArgumentException if the name is empty or blank.
     */
    public void addExtraOptionType(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("The extra option type name cannot be empty");
        }
        extraOptionTypeService.create(ExtraOptionType.builder().name(name).build());
    }
}

