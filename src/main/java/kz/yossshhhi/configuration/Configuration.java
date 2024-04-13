package kz.yossshhhi.configuration;

import kz.yossshhhi.controller.AdminController;
import kz.yossshhhi.controller.AppController;
import kz.yossshhhi.controller.SecurityController;
import kz.yossshhhi.controller.WorkoutDiaryController;
import kz.yossshhhi.dao.*;
import kz.yossshhhi.dao.repository.*;
import kz.yossshhhi.handler.CommandHandler;
import kz.yossshhhi.in.console.ConsoleReader;
import kz.yossshhhi.out.ConsoleWriter;
import kz.yossshhhi.service.*;

import java.util.HashMap;

/**
 * This class represents the configuration for the application, providing access to various controllers and repositories.
 */
public class Configuration {

    /**
     * The context map holds references to various objects used in the application.
     */
    private final HashMap<String, Object> context = new HashMap<>();

    /**
     * Retrieves an instance of {@link AppController} configured with necessary dependencies.
     *
     * @return An instance of {@link AppController}.
     */
    public AppController appController() {
        loadServices();
        return new AppController(securityController(), commandHandler(), (AuditService) context.get("auditService"));
    }

    /**
     * Retrieves an instance of {@link SecurityController} configured with necessary dependencies.
     *
     * @return An instance of {@link SecurityController}.
     */
    public SecurityController securityController() {
        return new SecurityController((SecurityService) context.get("securityService"));
    }

    /**
     * Retrieves an instance of {@link CommandHandler} configured with necessary dependencies.
     *
     * @return An instance of {@link CommandHandler}.
     */
    public CommandHandler commandHandler() {
        ConsoleReader consoleReader = (ConsoleReader) context.getOrDefault("consoleReader", new ConsoleReader());
        ConsoleWriter consoleWriter = (ConsoleWriter) context.getOrDefault("consoleWriter", new ConsoleWriter());

        putIfNotContains("consoleReader", consoleReader);
        putIfNotContains("consoleWriter", consoleWriter);
        return new CommandHandler(consoleReader, consoleWriter, workoutDiaryController(), adminController(), (AuditService) context.get("auditService"));
    }

    /**
     * Retrieves an instance of {@link WorkoutDiaryController} configured with necessary services.
     * <p>
     * This method retrieves or creates instances of {@link WorkoutDiaryService}, {@link ExtraOptionService},
     * and {@link WorkoutTypeService} from the context map and constructs a new {@link WorkoutDiaryController}
     * with these services.
     *
     * @return An instance of {@link WorkoutDiaryController}.
     */
    public WorkoutDiaryController workoutDiaryController() {
        return new WorkoutDiaryController(
                (WorkoutDiaryService) context.get("workoutDiaryService"),
                (ExtraOptionService) context.get("extraOptionService"),
                (WorkoutTypeService) context.get("workoutTypeService"));
    }

    /**
     * Retrieves an instance of {@link AdminController} configured with necessary services.
     * <p>
     * This method retrieves or creates instances of {@link WorkoutDiaryService}, {@link WorkoutTypeService},
     * and {@link ExtraOptionService} from the context map and constructs a new {@link AdminController}
     * with these services.
     *
     * @return An instance of {@link AdminController}.
     */
    public AdminController adminController() {
        return new AdminController(
                (WorkoutDiaryService) context.get("workoutDiaryService"),
                (WorkoutTypeService) context.get("workoutTypeService"),
                (ExtraOptionService) context.get("extraOptionService"));
    }


    /**
     * Retrieves the {@link UserRepository} instance from the context map, or creates a new one if not present.
     *
     * @return The {@link UserRepository} instance.
     */
    public UserRepository userRepository() {
        UserRepository userRepository = (UserRepository) context.getOrDefault("userRepository", new UserDAO());
        putIfNotContains("userRepository", userRepository);
        return userRepository;
    }

    /**
     * Retrieves the {@link WorkoutRepository} instance from the context map, or creates a new one if not present.
     *
     * @return The {@link WorkoutRepository} instance.
     */
    public WorkoutRepository workoutRepository() {
        WorkoutRepository workoutRepository = (WorkoutRepository) context.getOrDefault("workoutRepository", new WorkoutDAO());
        putIfNotContains("workoutRepository", workoutRepository);
        return workoutRepository;
    }

    /**
     * Retrieves the {@link WorkoutTypeRepository} instance from the context map, or creates a new one if not present.
     *
     * @return The {@link WorkoutTypeRepository} instance.
     */
    public WorkoutTypeRepository workoutTypeRepository() {
        WorkoutTypeRepository workoutTypeRepository = (WorkoutTypeRepository) context.getOrDefault("workoutTypeRepository", new WorkoutTypeDAO());
        putIfNotContains("workoutTypeRepository", workoutTypeRepository);
        return workoutTypeRepository;
    }

    /**
     * Retrieves the {@link WorkoutDiaryRepository} instance from the context map, or creates a new one if not present.
     *
     * @return The {@link WorkoutDiaryRepository} instance.
     */
    public WorkoutDiaryRepository workoutDiaryRepository() {
        WorkoutDiaryRepository workoutDiaryRepository = (WorkoutDiaryRepository) context.getOrDefault("workoutDiaryRepository", new WorkoutDiaryDAO());
        putIfNotContains("workoutDiaryRepository", workoutDiaryRepository);
        return workoutDiaryRepository;
    }

    /**
     * Retrieves the {@link ExtraOptionRepository} instance from the context map, or creates a new one if not present.
     *
     * @return The {@link ExtraOptionRepository} instance.
     */
    public ExtraOptionRepository extraOptionRepository() {
        ExtraOptionRepository extraOptionRepository = (ExtraOptionRepository) context.getOrDefault("extraOptionRepository", new ExtraOptionDAO());
        putIfNotContains("extraOptionRepository", extraOptionRepository);
        return extraOptionRepository;
    }

    /**
     * Retrieves the {@link AuditRepository} instance from the context map, or creates a new one if not present.
     *
     * @return The {@link AuditRepository} instance.
     */
    public AuditRepository auditRepository() {
        AuditRepository auditRepository = (AuditRepository) context.getOrDefault("auditRepository", new AuditDAO());
        putIfNotContains("auditRepository", auditRepository);
        return auditRepository;
    }

    /**
     * Puts the object into the context map if it's not already present.
     *
     * @param key The key under which the object is stored.
     * @param object The object to be stored in the context map.
     */
    private void putIfNotContains(String key, Object object) {
        if (!context.containsKey(key)) {
            context.put(key, object);
        }
    }

    /**
     * Loads necessary services into the context map if they are not already present.
     */
    private void loadServices() {
        WorkoutDiaryService workoutDiaryService = (WorkoutDiaryService) context.getOrDefault("workoutDiaryService",
                new WorkoutDiaryService(workoutRepository(), workoutDiaryRepository(), extraOptionRepository(), workoutTypeRepository()));
        ExtraOptionService extraOptionService = (ExtraOptionService) context.getOrDefault("extraOptionService", new ExtraOptionService(extraOptionRepository()));
        WorkoutTypeService workoutTypeService = (WorkoutTypeService) context.getOrDefault("workoutTypeService", new WorkoutTypeService(workoutTypeRepository()));
        AuditService auditService = (AuditService) context.getOrDefault("auditService", new AuditService(auditRepository()));
        SecurityService securityService = (SecurityService) context.getOrDefault("securityService", new SecurityService(userRepository(), auditService));
        putIfNotContains("workoutDiaryService", workoutDiaryService);
        putIfNotContains("extraOptionService", extraOptionService);
        putIfNotContains("workoutTypeService", workoutTypeService);
        putIfNotContains("securityService", securityService);
        putIfNotContains("auditService", auditService);
    }
}
