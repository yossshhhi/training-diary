package kz.yossshhhi.configuration;

import kz.yossshhhi.controller.AdminController;
import kz.yossshhhi.controller.AppController;
import kz.yossshhhi.controller.SecurityController;
import kz.yossshhhi.controller.WorkoutController;
import kz.yossshhhi.dao.*;
import kz.yossshhhi.dao.repository.*;
import kz.yossshhhi.handler.CommandHandler;
import kz.yossshhhi.in.console.ConsoleReader;
import kz.yossshhhi.model.*;
import kz.yossshhhi.out.ConsoleWriter;
import kz.yossshhhi.service.*;
import kz.yossshhhi.util.ConnectionPool;
import kz.yossshhhi.util.DataSource;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class represents the configuration for the application, providing access to various controllers and repositories.
 */
public class Configuration {

    /**
     * The file name of the properties file used for application configuration.
     */
    private static final String PROPERTIES_FILE = "application.properties";
    /**
     * The context map holds references to various objects used in the application.
     */
    private final HashMap<String, Object> context = new HashMap<>();

    /**
     * Initializes the Configuration by loading properties and performing database migration.
     */
    public Configuration() {
        Properties properties = properties();
        DataSource dataSource = dataSource(properties);
        databaseMigration(dataSource);
    }

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
        return new CommandHandler(consoleReader, consoleWriter, workoutController(), adminController(),
                (AuditService) context.get("auditService"), connectionPool());
    }

    /**
     * Retrieves an instance of {@link WorkoutController} configured with necessary services.
     *
     * @return An instance of {@link WorkoutController}.
     */
    public WorkoutController workoutController() {
        return new WorkoutController(
                (WorkoutService) context.get("workoutService"),
                (ExtraOptionService) context.get("extraOptionService"),
                (WorkoutTypeService) context.get("workoutTypeService"),
                (ExtraOptionTypeService) context.get("extraOptionTypeService"));
    }

    /**
     * Retrieves an instance of {@link AdminController} configured with necessary services.
     *
     * @return An instance of {@link AdminController}.
     */
    public AdminController adminController() {
        return new AdminController(
                (WorkoutService) context.get("workoutService"),
                (WorkoutTypeService) context.get("workoutTypeService"),
                (ExtraOptionTypeService) context.get("extraOptionTypeService"));
    }

    /**
     * Retrieves the {@link UserRepository} instance from the context map, or creates a new one if not present.
     *
     * @return The {@link UserRepository} instance.
     */
    public UserRepository userRepository() {
        UserRepository userRepository = (UserRepository) context.getOrDefault("userRepository",
                new UserDAO(databaseManager(), new ResultSetMapper<>(User.class)));
        putIfNotContains("userRepository", userRepository);
        return userRepository;
    }

    /**
     * Retrieves the {@link WorkoutRepository} instance from the context map, or creates a new one if not present.
     *
     * @return The {@link WorkoutRepository} instance.
     */
    public WorkoutRepository workoutRepository() {
        WorkoutRepository workoutRepository = (WorkoutRepository) context.getOrDefault("workoutRepository",
                new WorkoutDAO(databaseManager(),
                        new ResultSetMapper<>(Workout.class),
                        new ResultSetMapper<>(AggregateWorkoutData.class)));
        putIfNotContains("workoutRepository", workoutRepository);
        return workoutRepository;
    }

    /**
     * Retrieves the {@link WorkoutTypeRepository} instance from the context map, or creates a new one if not present.
     *
     * @return The {@link WorkoutTypeRepository} instance.
     */
    public WorkoutTypeRepository workoutTypeRepository() {
        WorkoutTypeRepository workoutTypeRepository = (WorkoutTypeRepository) context.getOrDefault(
                "workoutTypeRepository",
                new WorkoutTypeDAO(databaseManager(), new ResultSetMapper<>(WorkoutType.class)));
        putIfNotContains("workoutTypeRepository", workoutTypeRepository);
        return workoutTypeRepository;
    }

    /**
     * Retrieves the {@link ExtraOptionTypeRepository} instance from the context map, or creates a new one if not present.
     *
     * @return The {@link ExtraOptionTypeRepository} instance.
     */
    public ExtraOptionTypeRepository extraOptionTypeRepository() {
        ExtraOptionTypeRepository extraOptionTypeRepository = (ExtraOptionTypeRepository) context.getOrDefault(
                "extraOptionTypeRepository",
                new ExtraOptionTypeDAO(databaseManager(), new ResultSetMapper<>(ExtraOptionType.class)));
        putIfNotContains("extraOptionTypeRepository", extraOptionTypeRepository);
        return extraOptionTypeRepository;
    }

    /**
     * Retrieve or create an instance of the {@link ExtraOptionRepository}.
     *
     * @return The {@link ExtraOptionRepository} instance.
     */
    public ExtraOptionRepository extraOptionRepository() {
        ExtraOptionRepository extraOptionRepository = (ExtraOptionRepository) context.getOrDefault(
                "extraOptionRepository",
                new ExtraOptionDAO(databaseManager(), new ResultSetMapper<>(ExtraOption.class)));
        putIfNotContains("extraOptionRepository", extraOptionRepository);
        return extraOptionRepository;
    }

    /**
     * Retrieves the {@link AuditRepository} instance from the context map, or creates a new one if not present.
     *
     * @return The {@link AuditRepository} instance.
     */
    public AuditRepository auditRepository() {
        AuditRepository auditRepository = (AuditRepository) context.getOrDefault("auditRepository",
                new AuditDAO(databaseManager(), new ResultSetMapper<>(Audit.class)));
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
        WorkoutService workoutService = (WorkoutService) context.getOrDefault("workoutService",
                new WorkoutService(workoutRepository(), extraOptionRepository()));
        putIfNotContains("workoutService", workoutService);

        ExtraOptionService extraOptionService = (ExtraOptionService) context.getOrDefault("extraOptionService",
                new ExtraOptionService(extraOptionRepository()));
        putIfNotContains("extraOptionService", extraOptionService);

        WorkoutTypeService workoutTypeService = (WorkoutTypeService) context.getOrDefault("workoutTypeService",
                new WorkoutTypeService(workoutTypeRepository()));
        putIfNotContains("workoutTypeService", workoutTypeService);

        AuditService auditService = (AuditService) context.getOrDefault("auditService",
                new AuditService(auditRepository()));
        putIfNotContains("auditService", auditService);

        SecurityService securityService = (SecurityService) context.getOrDefault("securityService",
                new SecurityService(userRepository(), auditService));
        putIfNotContains("securityService", securityService);

        ExtraOptionTypeService extraOptionTypeService = (ExtraOptionTypeService) context.getOrDefault(
                "extraOptionTypeService", new ExtraOptionTypeService(extraOptionTypeRepository()));
        putIfNotContains("extraOptionTypeService", extraOptionTypeService);
    }

    /**
     * Retrieves the {@link DataSource} instance from the context map or creates a new one if not present.
     *
     * @param properties The properties containing database connection information.
     * @return The {@link DataSource} instance.
     */
    public DataSource dataSource(Properties properties) {
        DataSource dataSource = (DataSource) context.getOrDefault("dataSource", new DataSource(
                properties.getProperty("database.url"),
                properties.getProperty("database.user"),
                properties.getProperty("database.password"),
                properties.getProperty("database.driver")));
        putIfNotContains("dataSource", dataSource);
        return dataSource;
    }

    /**
     * Retrieve the application properties from the properties file.
     *
     * @return The application properties.
     * @throws RuntimeException If an error occurs while loading the properties file.
     */
    public Properties properties() {
        if (context.containsKey("properties")) {
            return (Properties) context.get("properties");
        }
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading application properties");
        }
        putIfNotContains("properties", properties);
        return properties;
    }

    /**
     * Perform database migration using Liquibase.
     *
     * @param dataSource The data source for the database.
     * @throws RuntimeException If an error occurs during database migration.
     */
    public void databaseMigration(DataSource dataSource) {
        try {
            Connection connection = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUser(), dataSource.getPassword());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve or create an instance of the {@link DatabaseManager}.
     *
     * @return The {@link DatabaseManager} instance.
     */
    public DatabaseManager databaseManager() {
        DatabaseManager databaseManager = (DatabaseManager) context.getOrDefault(
                "databaseManager",
                new DatabaseManager(connectionPool()));
        putIfNotContains("databaseManager", databaseManager);
        return databaseManager;
    }

    /**
     * Retrieve or create an instance of the {@link  ConnectionPool}.
     *
     * @return The {@link ConnectionPool} instance.
     */
    public ConnectionPool connectionPool() {
        ConnectionPool connectionPool = (ConnectionPool) context.getOrDefault(
                "connectionPool",
                new ConnectionPool(dataSource(properties()))
        );
        putIfNotContains("connectionPool", connectionPool);
        return connectionPool;
    }
}
