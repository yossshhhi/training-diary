package kz.yossshhhi.controller;

import kz.yossshhhi.handler.CommandHandler;
import kz.yossshhhi.model.User;
import kz.yossshhhi.model.enums.AuditAction;
import kz.yossshhhi.model.enums.AuditType;
import kz.yossshhhi.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Controller responsible for handling user interactions and controlling the flow of the application.
 */
public class AppController {

    /** Controller for security-related operations. */
    private final SecurityController securityController;

    /** Handler for executing commands and I/O operations. */
    private final CommandHandler commandHandler;

    /** Service for auditing user actions. */
    private final AuditService auditService;

    /** Logger for logging application events. */
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    /**
     * Constructs a new AppController with the specified dependencies.
     * @param securityController Controller for security-related operations.
     * @param commandHandler Handler for executing commands and I/O operations.
     * @param auditService Service for auditing user actions.
     */
    public AppController(SecurityController securityController, CommandHandler commandHandler, AuditService auditService) {
        this.securityController = securityController;
        this.commandHandler = commandHandler;
        this.auditService = auditService;
    }

    /**
     * Starts the application and enters the main loop.
     */
    public void run() {
        while (true) {
            startMethod();
        }
    }

    /**
     * Displays the main menu and handles user input.
     */
    private void startMethod() {
        String menu = """
                1. Register
                2. Authenticate
                3. Exit
                Enter the action number:""";
        
        commandHandler.getWriter().write(menu);
        String menuItem = commandHandler.getReader().read();
        try {
            switch (menuItem) {
                case "1", "2" -> getCredentials(menuItem);
                case "3" -> commandHandler.exit();
                default -> commandHandler.getWriter().write("Invalid choice. Please try again.");
            }
        } catch (RuntimeException ex) {
            logger.warn(ex.getMessage());
        }
    }

    /**
     * Prompts the user for credentials and performs registration or authentication.
     * @param menuItem The menu item selected by the user.
     */
    private void getCredentials(String menuItem) {
        commandHandler.getWriter().write("Enter username:");
        String username = commandHandler.getReader().read();
        commandHandler.getWriter().write("Enter password:");
        String password = commandHandler.getReader().read();

        User user = User.builder().build();

        try {
            switch (menuItem) {
                case "1" -> user = securityController.register(username, password);
                case "2" -> user = securityController.authenticate(username, password);
            }
        } catch (RuntimeException ex) {
            logger.error(ex.getMessage());
        }

        switch (user.getRole().ordinal()) {
            case 0 -> adminMenu(user.getId());
            case 1 -> userMenu(user);
        }
    }

    /**
     * Displays the admin menu and handles admin actions.
     *
     * @param userId The ID of the admin user.
     */
    private void adminMenu(Long userId) {
        boolean inSystem = true;
        String menu = """
                1. Show all user workouts
                2. Add workout type
                3. Add an extra option to workouts
                4. Show all audits
                5. Log out
                Enter the action number:""";

        while (inSystem) {
            commandHandler.getWriter().write(menu);
            try {
                switch (commandHandler.getReader().read()) {
                    case "1" -> commandHandler.showAllWorkoutList(userId);
                    case "2" -> commandHandler.addWorkoutType();
                    case "3" -> commandHandler.addExtraOption();
                    case "4" -> commandHandler.getWriter().write(auditService.showAll());
                    case "5" -> {
                        inSystem = false;
                        auditService.audit(userId, AuditAction.LOG_OUT, AuditType.SUCCESS);
                    }
                    default -> commandHandler.getWriter().write("Invalid choice. Please try again.");
                }
            } catch (RuntimeException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    /**
     * Displays the user menu and handles user actions.
     *
     * @param user The user object.
     */
    private void userMenu(User user) {
        boolean inSystem = true;
        String menu = """
                1. Record a workout
                2. Open my workout list (you can edit the list)
                3. Get training statistics
                4. Log out
                Enter the action number:""";

        while (inSystem) {
            commandHandler.getWriter().write(menu);

            try {
                switch (commandHandler.getReader().read()) {
                    case "1" -> commandHandler.getWorkoutDataAndCreate(user.getId());
                    case "2" -> commandHandler.showWorkoutListByUser(user.getId());
                    case "3" -> commandHandler.getDiaryStatistics(user.getId());
                    case "4" -> {
                        inSystem = false;
                        auditService.audit(user.getId(), AuditAction.LOG_OUT, AuditType.SUCCESS);
                    }
                    default -> commandHandler.getWriter().write("Invalid choice. Please try again.");
                }
            } catch (RuntimeException ex) {
                logger.error(ex.getMessage());
            }
        }
    }
}
