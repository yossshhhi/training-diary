package kz.yossshhhi.handler;

import kz.yossshhhi.controller.AdminController;
import kz.yossshhhi.controller.WorkoutController;
import kz.yossshhhi.in.console.ConsoleReader;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.model.enums.AuditAction;
import kz.yossshhhi.model.enums.AuditType;
import kz.yossshhhi.out.ConsoleWriter;
import kz.yossshhhi.service.AuditService;
import kz.yossshhhi.util.ConnectionPool;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles commands from the user interface and delegates them to the appropriate controllers.
 */
public class CommandHandler {
    /**
     * The reader for receiving user input.
     */
    @Getter
    private final ConsoleReader reader;

    /**
     * The writer for displaying messages to the user.
     */
    @Getter
    private final ConsoleWriter writer;

    /**
     * The controller for managing workout operations.
     */
    private final WorkoutController workoutController;

    /**
     * The controller for administrative operations.
     */
    private final AdminController adminController;

    /**
     * The service for auditing user actions.
     */
    private final AuditService auditService;

    /**
     * The connection pool used for managing database connections.
     */
    private final ConnectionPool connectionPool;

    /**
     * Constructs a CommandHandler instance with the necessary dependencies.
     *
     * @param reader                 The console reader.
     * @param writer                 The console writer.
     * @param workoutController      The workout controller.
     * @param adminController        The admin controller.
     * @param auditService           The audit service.
     * @param connectionPool         The connection pool for managing database connections.
     */
    public CommandHandler(ConsoleReader reader, ConsoleWriter writer, WorkoutController workoutController, AdminController adminController, AuditService auditService, ConnectionPool connectionPool) {
        this.reader = reader;
        this.writer = writer;
        this.workoutController = workoutController;
        this.adminController = adminController;
        this.auditService = auditService;
        this.connectionPool = connectionPool;
    }

    /**
     * Retrieves workout data from the user and creates a new workout entry.
     *
     * @param userId The ID of the user.
     */
    public void getWorkoutDataAndCreate(Long userId) {
        Long workoutTypeId = getWorkoutTypeId();
        Integer duration = getDuration();
        Integer burnedCalories = getCalories();
        Map<Long, Integer> extraOptionsInput = getExtraOptions();

        workoutController.create(userId, duration, burnedCalories, workoutTypeId, extraOptionsInput);
        auditService.audit(userId, AuditAction.RECORD_WORKOUT, AuditType.SUCCESS);
    }

    /**
     * Shows the list of workouts recorded by a specific user.
     *
     * @param userId The ID of the user.
     */
    public void showWorkoutListByUser(Long userId) {
        editingWorkoutList(workoutController.getUserWorkoutList(userId), userId);
    }

    /**
     * Displays a list of workouts for all users.
     *
     * @param userId The ID of the user.
     */
    public void showAllWorkoutList(Long userId) {
        editingWorkoutList(adminController.showAllWorkouts(), userId);
    }

    /**
     * Adds a new workout type.
     */
    public void addWorkoutType() {
        writer.write("Enter workout type name: ");
        adminController.addWorkoutType(reader.read());
    }

    /**
     * Adds a new extra option type.
     */
    public void addExtraOptionType() {
        writer.write("Enter extra option type name: ");
        adminController.addExtraOptionType(reader.read());
    }

    /**
     * Displays statistics for the user's workout.
     *
     * @param userId The ID of the user.
     */
    public void getWorkoutStatistics(Long userId) {
        writer.write("Enter the desired number of days for which you want to view statistics: ");
        int days = Math.toIntExact(parseLong(reader.read()));
        writer.write(workoutController.getStatistics(userId, days));
    }

    /**
     * Edits the provided workout list for the given user.
     *
     * @param workoutList The workout list to display and edit.
     * @param userId      The ID of the user associated with the workout list.
     */
    private void editingWorkoutList(String workoutList, Long userId) {
        String editMenu = """
                To edit(1) or delete(2), enter the entry ID and action number separated by a space, or enter 0 to return to the menu.
                Example:
                To edit: 1 1
                To delete: 2 1
                To exit: 0""";
        writer.write(workoutList);
        int action;
        long record;
        while (true) {
            writer.write(editMenu);
            String[] split = reader.read().split(" ");
            action = Math.toIntExact(parseLong(split[0]));
            if (action == 0) {
                break;
            }
            record = parseLong(split[1]);

            switch (action) {
                case 1 -> editRecord(record, userId);
                case 2 -> deleteRecord(record, userId);
                default -> writer.write("Unknown action");
            }
        }
    }

    /**
     * Retrieves the ID of the workout type selected by the user.
     *
     * @return The ID of the selected workout type.
     * @throws IllegalArgumentException If the entered workout type does not exist.
     */
    private Long getWorkoutTypeId() {
        String workoutTypesToString = workoutController.getWorkoutTypesToString() + "Select the workout type:";
        Long workoutTypeId = 0L;
        while (workoutTypeId == 0) {
            workoutTypeId = choseActionNumber(workoutTypesToString);
        }
        if (!workoutController.existsWorkoutTypeById(workoutTypeId)) {
            throw new IllegalArgumentException("The entered workout type does not exist");
        }
        return workoutTypeId;
    }

    /**
     * Retrieves the duration of the workout entered by the user.
     *
     * @return The duration of the workout.
     */
    private Integer getDuration() {
        writer.write("Enter workout duration:");
        return Math.toIntExact(parseLong(reader.read()));
    }

    /**
     * Retrieves the number of calories burned during the workout entered by the user.
     *
     * @return The number of calories burned.
     */
    private Integer getCalories() {
        writer.write("Enter calories burned:");
        return Math.toIntExact(parseLong(reader.read()));
    }

    /**
     * Retrieves the extra options entered by the user for the workout.
     *
     * @return A map of extra options and their values.
     */
    private Map<Long, Integer> getExtraOptions() {
        String extraOptionsToString = workoutController.getExtraOptionsToString() + 0 + ". Exit current menu\nSelect the parameter you want to add:";
        Map<Long, Integer> extraOptionsInput = new HashMap<>();
        while (true) {
            Long number = choseActionNumber(extraOptionsToString);
            if (number == 0) {
                break;
            }
            writer.write("Enter the value of the selected parameter: ");
            extraOptionsInput.put(number, Math.toIntExact(parseLong(reader.read())));
        }
        return extraOptionsInput;
    }

    /**
     * Retrieves the number entered by the user to choose an action.
     *
     * @param toString The string representation of available options.
     * @return The number chosen by the user.
     */
    private Long choseActionNumber(String toString) {
        writer.write(toString);
        return parseLong(reader.read());
    }

    /**
     * Deletes a workout record.
     *
     * @param workoutId The ID of the workout to delete.
     * @param userId    The ID of the user performing the action.
     */
    private void deleteRecord(Long workoutId, Long userId) {
        try {
            workoutController.delete(workoutId);
            auditService.audit(userId, AuditAction.DELETE_WORKOUT, AuditType.SUCCESS);
        } catch (RuntimeException ex) {
            auditService.audit(userId, AuditAction.DELETE_WORKOUT, AuditType.FAIL);
            throw new RuntimeException("Deleting workout record failed. " + ex.getMessage());
        }
    }

    /**
     * Edits a workout record based on user input.
     *
     * @param workoutId The ID of the workout to edit.
     * @param userId    The ID of the user performing the action.
     */
    private void editRecord(Long workoutId, Long userId) {
        Workout workout = workoutController.findById(workoutId);
        Map<Long, Integer> extraOptions = new HashMap<>();
        String choseRecordItem = """
                1. Workout type
                2. Duration
                3. Calories
                4. Extra options (all parameters are completely overwritten)
                5. Edit
                6. Cancel
                Enter the number of the item you want to change:""";
        int action;
        while (true) {
            writer.write(choseRecordItem);
            action = Math.toIntExact(parseLong(reader.read()));
            if (action == 5) {
                workoutController.update(workout);
                if (!extraOptions.isEmpty())
                    workoutController.updateExtraOptions(workout.getExtraOptions(), extraOptions, workoutId);
                auditService.audit(userId, AuditAction.EDIT_WORKOUT, AuditType.SUCCESS);
                break;
            } else if (action == 6) {
                auditService.audit(userId, AuditAction.EDIT_WORKOUT, AuditType.FAIL);
                break;
            }
            switch (action) {
                case 1 -> workout.setWorkoutTypeId(getWorkoutTypeId());
                case 2 -> workout.setDuration(getDuration());
                case 3 -> workout.setBurnedCalories(getCalories());
                case 4 -> extraOptions = getExtraOptions();
                default -> writer.write("Unknown item. Try again");
            }
        }
    }

    /**
     * Exits the application.
     */
    public void exit() {
        connectionPool.close();
        System.exit(0);
    }

    /**
     * Parses a string input to a long number.
     *
     * @param input The input string to parse.
     * @return The parsed long number.
     * @throws IllegalArgumentException If the input string cannot be parsed to a long number.
     */
    private Long parseLong(String input) {
        long number;
        try {
            number = Long.parseLong(input);
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Enter numbers only. Please try again.");
        }
        return number;
    }
}
