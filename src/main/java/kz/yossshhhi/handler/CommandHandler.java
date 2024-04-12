package kz.yossshhhi.handler;

import kz.yossshhhi.controller.AdminController;
import kz.yossshhhi.controller.WorkoutDiaryController;
import kz.yossshhhi.in.console.ConsoleReader;
import kz.yossshhhi.model.ExtraOption;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.model.enums.AuditAction;
import kz.yossshhhi.model.enums.AuditType;
import kz.yossshhhi.out.ConsoleWriter;
import kz.yossshhhi.service.AuditService;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    @Getter
    private final ConsoleReader reader;
    @Getter
    private final ConsoleWriter writer;
    private final WorkoutDiaryController workoutDiaryController;
    private final AdminController adminController;
    private final AuditService auditService;

    public CommandHandler(ConsoleReader reader, ConsoleWriter writer, WorkoutDiaryController workoutDiaryController, AdminController adminController, AuditService auditService) {
        this.reader = reader;
        this.writer = writer;
        this.workoutDiaryController = workoutDiaryController;
        this.adminController = adminController;
        this.auditService = auditService;
    }

    public void getWorkoutDataAndCreate(Long userId) {
        Long workoutTypeId = getWorkoutTypeId();
        Integer duration = getDuration();
        Integer burnedCalories = getCalories();
        Map<ExtraOption, Integer> extraOptionsInput = getExtraOptions();

        workoutDiaryController.create(userId, duration, burnedCalories, workoutTypeId, extraOptionsInput);
        auditService.audit(userId, AuditAction.RECORD_WORKOUT, AuditType.SUCCESS);
    }

    public void showWorkoutListByUser(Long userId) {
        editingWorkoutList(workoutDiaryController.getUserWorkoutList(userId), userId);
    }

    public void showAllWorkoutList(Long userId) {
        editingWorkoutList(adminController.showAllWorkouts(), userId);
    }

    public void addWorkoutType() {
        writer.write("Enter workout type name: ");
        adminController.addWorkoutType(reader.read());
    }

    public void addExtraOption() {
        writer.write("Enter extra option name: ");
        adminController.addExtraOption(reader.read());
    }

    public void getDiaryStatistics(Long userId) {
        writer.write("Enter the desired number of days for which you want to view statistics: ");
        int days = Math.toIntExact(parseLong(reader.read()));
        writer.write(workoutDiaryController.getStatistics(days, userId));
    }

    private void editingWorkoutList(String workoutList, Long userId) {
        String editMenu = """
                To edit(1) or delete(2), enter the entry number and action number separated by a space, or enter 0 to return to the menu.
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

    private Long getWorkoutTypeId() {
        String workoutTypesToString = workoutDiaryController.getWorkoutTypesToString() + "Select the workout type:";
        Long workoutTypeId = 0L;
        while (workoutTypeId == 0) {
            workoutTypeId = choseActionNumber(workoutTypesToString);
        }
        if (!workoutDiaryController.existsWorkoutTypeById(workoutTypeId)) {
            throw new IllegalArgumentException("The entered workout type does not exist");
        }
        return workoutTypeId;
    }

    private Integer getDuration() {
        writer.write("Enter workout duration:");
        return Math.toIntExact(parseLong(reader.read()));
    }

    private Integer getCalories() {
        writer.write("Enter calories burned:");
        return Math.toIntExact(parseLong(reader.read()));
    }

    private Map<ExtraOption, Integer> getExtraOptions() {
        String extraOptionsToString = workoutDiaryController.getExtraOptionsToString() + 0 + ". Exit current menu\nSelect the parameter you want to add:";
        Map<Long, String> extraOptionsInput = new HashMap<>();
        while (true) {
            Long number = choseActionNumber(extraOptionsToString);
            if (number == 0) {
                break;
            }
            writer.write("Enter the value of the selected parameter: ");
            extraOptionsInput.put(number, reader.read());
        }
        return workoutDiaryController.createExtraOptionMap(extraOptionsInput);
    }

    private Long choseActionNumber(String toString) {
        writer.write(toString);
        return parseLong(reader.read());
    }

    private void deleteRecord(Long workoutId, Long userId) {
        workoutDiaryController.delete(workoutId, userId);
        auditService.audit(userId, AuditAction.DELETE_WORKOUT, AuditType.SUCCESS);
    }

    private void editRecord(Long workoutId, Long userId) {
        Workout workout = workoutDiaryController.findById(workoutId);
        String choseRecordItem = """
                1. Workout type
                2. Duration
                3. Calories
                4. Extra options (all parameters are completely overwritten)
                5. Cancel
                Enter the number of the item you want to change:""";
        int action;
        while (true) {
            writer.write(choseRecordItem);
            action = Math.toIntExact(parseLong(reader.read()));
            if (action == 5) {
                break;
            }
            switch (action) {
                case 1 -> workout.setWorkoutTypeId(getWorkoutTypeId());
                case 2 -> workout.setDuration(getDuration());
                case 3 -> workout.setBurnedCalories(getCalories());
                case 4 -> workout.setExtraOptions(getExtraOptions());
                default -> writer.write("Unknown item. Try again");
            }
        }
        auditService.audit(userId, AuditAction.EDIT_WORKOUT, AuditType.SUCCESS);
    }

    public void exit() {
        System.exit(0);
    }

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
