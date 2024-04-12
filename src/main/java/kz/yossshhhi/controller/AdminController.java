package kz.yossshhhi.controller;

import kz.yossshhhi.model.Audit;
import kz.yossshhhi.model.ExtraOption;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.model.WorkoutType;
import kz.yossshhhi.service.AuditService;
import kz.yossshhhi.service.ExtraOptionService;
import kz.yossshhhi.service.WorkoutDiaryService;
import kz.yossshhhi.service.WorkoutTypeService;

import java.util.List;

/**
 * Controller class responsible for handling administrative operations related to workouts.
 */
public class AdminController {

    /** Service for managing workout diary entries. */
    private final WorkoutDiaryService workoutDiaryService;

    /** Service for managing workout types. */
    private final WorkoutTypeService workoutTypeService;

    /** Service for managing extra options. */
    private final ExtraOptionService extraOptionService;

    /**
     * Constructs a new AdminController with the specified services.
     *
     * @param workoutDiaryService Service for managing workout diary entries.
     * @param workoutTypeService Service for managing workout types.
     * @param extraOptionService Service for managing extra options.
     */
    public AdminController(WorkoutDiaryService workoutDiaryService, WorkoutTypeService workoutTypeService,
                           ExtraOptionService extraOptionService) {
        this.workoutDiaryService = workoutDiaryService;
        this.workoutTypeService = workoutTypeService;
        this.extraOptionService = extraOptionService;
    }

    /**
     * Retrieves a string representation of all workout records.
     *
     * @return A string containing details of all workout records.
     * @throws IllegalArgumentException if there are no workout records.
     */
    public String showAllWorkouts() {
        List<Workout> workouts = workoutDiaryService.findAll();
        if (workouts.isEmpty()) {
            throw new IllegalArgumentException("There are no workout records");
        }
        return workoutDiaryService.workoutListToString(workouts);
    }

    /**
     * Adds a new workout type with the specified name.
     *
     * @param name The name of the new workout type.
     * @throws IllegalArgumentException if the name is empty or blank.
     */
    public void addWorkoutType(String name) {
        if (name.isBlank() || name.isEmpty()) {
            throw new IllegalArgumentException("The workout type name cannot be empty");
        }
        workoutTypeService.create(WorkoutType.builder().name(name).build());
    }

    /**
     * Adds a new extra option with the specified name.
     *
     * @param name The name of the new extra option.
     * @throws IllegalArgumentException if the name is empty or blank.
     */
    public void addExtraOption(String name) {
        if (name.isBlank() || name.isEmpty()) {
            throw new IllegalArgumentException("The extra option name cannot be empty");
        }
        extraOptionService.create(ExtraOption.builder().name(name).build());
    }
}

