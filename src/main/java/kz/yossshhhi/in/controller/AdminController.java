package kz.yossshhhi.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.yossshhhi.dto.*;
import kz.yossshhhi.service.AuditService;
import kz.yossshhhi.service.ExtraOptionTypeService;
import kz.yossshhhi.service.WorkoutService;
import kz.yossshhhi.service.WorkoutTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling administrative operations related to workouts.
 * Provides endpoints for managing workout records, audit logs, and configuring workout-related metadata.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin Operations", description = "Administrative operations for managing workouts")
public class AdminController {
    private final WorkoutService workoutService;
    private final WorkoutTypeService workoutTypeService;
    private final ExtraOptionTypeService extraOptionTypeService;
    private final AuditService auditService;

    /**
     * Retrieves all existing workout records.
     *
     * @return a {@link ResponseEntity} containing a list of {@link WorkoutDTO} representing all workouts
     */
    @GetMapping("/workouts")
    @Operation(summary = "Show all workout records")
    public ResponseEntity<List<WorkoutDTO>> showWorkouts() {
        return ResponseEntity.ok(workoutService.findAll());
    }

    /**
     * Retrieves all audit records.
     *
     * @return a {@link ResponseEntity} containing a list of {@link AuditDTO} representing all audit records
     */
    @GetMapping("/audits")
    @Operation(summary = "Show all audits")
    public ResponseEntity<List<AuditDTO>> showAudits() {
        return ResponseEntity.ok(auditService.findAll());
    }

    /**
     * Creates a new extra option type for workouts.
     *
     * @param extraOptionTypeDTO the {@link ExtraOptionTypeDTO} containing the details of the new extra option type
     * @return a {@link ResponseEntity} with a {@link SuccessResponse} indicating the result of the creation process
     */
    @PostMapping("/add-extra-option-type")
    @Operation(summary = "Creating new extra option type")
    public ResponseEntity<SuccessResponse> addExtraOptionType(@Valid @RequestBody ExtraOptionTypeDTO extraOptionTypeDTO) {
        extraOptionTypeService.create(extraOptionTypeDTO);
        return ResponseEntity.ok(new SuccessResponse("Extra option type successfully created"));
    }

    /**
     * Creates a new workout type.
     *
     * @param workoutTypeDTO the {@link WorkoutTypeDTO} containing the details of the new workout type
     * @return a {@link ResponseEntity} with a {@link SuccessResponse} indicating the result of the creation process
     */
    @PostMapping("/add-workout-type")
    @Operation(summary = "Creating new workout type")
    public ResponseEntity<SuccessResponse> addWorkoutType(@Valid @RequestBody WorkoutTypeDTO workoutTypeDTO) {
        workoutTypeService.create(workoutTypeDTO);
        return ResponseEntity.ok(new SuccessResponse("Workout type successfully created"));
    }
}
