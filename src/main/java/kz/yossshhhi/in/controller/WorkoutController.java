package kz.yossshhhi.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kz.yossshhhi.dto.*;
import kz.yossshhhi.service.ExtraOptionTypeService;
import kz.yossshhhi.service.WorkoutService;
import kz.yossshhhi.service.WorkoutTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing user operations related to workouts.
 * Provides endpoints for creating, deleting, updating workouts, recording workouts,
 * and retrieving workout statistics and types.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Operations", description = "User operations for managing workout diary")
public class WorkoutController {
    private final WorkoutService workoutService;
    private final ExtraOptionTypeService extraOptionTypeService;
    private final WorkoutTypeService workoutTypeService;

    /**
     * Deletes a workout based on the provided ID.
     *
     * @param id The ID of the workout to delete.
     * @return A {@link ResponseEntity} containing a success message if the workout is successfully deleted.
     */
    @DeleteMapping("/workouts/delete")
    @Operation(summary = "Deleting a workout by id")
    public ResponseEntity<SuccessResponse> deleteWorkout(@RequestParam(name = "id") Long id) {
        workoutService.delete(id);
        return ResponseEntity.ok(new SuccessResponse("Workout successfully deleted"));
    }

    /**
     * Updates the details of an existing workout.
     *
     * @param workoutDTO The updated workout details.
     * @return A {@link ResponseEntity} with a success message if the update is successful.
     */
    @PatchMapping("/workouts/edit")
    @Operation(summary = "Editing a workout")
    public ResponseEntity<SuccessResponse> editWorkout(@Valid @RequestBody WorkoutDTO workoutDTO) {
        workoutService.update(workoutDTO);
        return ResponseEntity.ok(new SuccessResponse("Workout successfully updated"));
    }

    /**
     * Retrieves workout statistics for a user over a specified number of days.
     *
     * @param days The number of days over which to gather statistics.
     * @param request The HTTP request containing the user ID as an attribute.
     * @return A {@link ResponseEntity} containing aggregated workout data.
     */
    @GetMapping("/statistic")
    @Operation(summary = "Gives statistics on training for the desired period (in days)")
    public ResponseEntity<AggregateWorkoutDataDTO> getStatistic(@RequestParam(name = "days") Integer days, HttpServletRequest request) {
        long userId = Long.parseLong((String) request.getAttribute("user_id"));
        return ResponseEntity.ok(workoutService.getStatistics(userId, days));
    }

    /**
     * Records a workout for the specified user.
     *
     * @param workoutDTO the workout data transfer object containing workout details
     * @param request the HTTP servlet request containing user identification attributes
     * @return a ResponseEntity containing a success message if the workout is recorded successfully
     */
    @PostMapping("/record")
    @Operation(summary = "Recording a workout")
    public ResponseEntity<SuccessResponse> recordWorkout(@RequestBody WorkoutDTO workoutDTO, HttpServletRequest request) {
        long userId = Long.parseLong((String) request.getAttribute("user_id"));
        workoutService.create(workoutDTO, userId);
        return ResponseEntity.ok(new SuccessResponse("Training successfully recorded"));
    }

    /**
     * Retrieves all extra option types available.
     *
     * @return a ResponseEntity containing a list of all extra option types
     */
    @GetMapping("/extra-option-types")
    @Operation(summary = "Show all extra option types")
    public ResponseEntity<List<ExtraOptionTypeDTO>> showExtraOptionTypes() {
        return ResponseEntity.ok(extraOptionTypeService.findAll());
    }

    /**
     * Retrieves all workouts recorded by a specific user.
     *
     * @param request the HTTP servlet request that contains the user's ID as an attribute
     * @return a ResponseEntity containing a list of all workouts for the user
     */
    @GetMapping("/workouts")
    @Operation(summary = "Show all user's workouts")
    public ResponseEntity<List<WorkoutDTO>> showUserWorkouts(HttpServletRequest request) {
        long userId = Long.parseLong((String) request.getAttribute("user_id"));
        return ResponseEntity.ok(workoutService.findAllByUserId(userId));
    }

    /**
     * Retrieves all workout types defined in the system.
     *
     * @return a ResponseEntity containing a list of all workout types
     */
    @GetMapping("/workout-types")
    @Operation(summary = "Show all workout types")
    public ResponseEntity<List<WorkoutTypeDTO>> showWorkoutTypes() {
        return ResponseEntity.ok(workoutTypeService.findAll());
    }
}
