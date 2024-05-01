package kz.yossshhhi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import kz.yossshhhi.aop.AuditAspect;
import kz.yossshhhi.dao.*;
import kz.yossshhhi.dao.repository.*;
import kz.yossshhhi.mapper.*;
import kz.yossshhhi.model.*;
import kz.yossshhhi.security.JwtTokenProvider;
import kz.yossshhhi.service.*;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;
import org.aspectj.lang.Aspects;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static kz.yossshhhi.util.AppConstants.PROPERTIES_FILE;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private DatabaseManager databaseManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Properties properties = properties();
        databaseManager = databaseConfiguration.databaseManager(properties);
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
                properties.getProperty("jwt.secret"),
                Long.parseLong(properties.getProperty("jwt.expiration-time")));
        loadServiceContext(servletContext);
        loadMapperContext(servletContext);

        servletContext.setAttribute("databaseManager", databaseManager);
        servletContext.setAttribute("jwtTokenProvider", jwtTokenProvider);
        servletContext.setAttribute("objectMapper", objectMapper);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    private void loadServiceContext(ServletContext servletContext) {
        WorkoutRepository workoutRepository = new WorkoutDAO(databaseManager,
                new ResultSetMapper<>(Workout.class),
                new ResultSetMapper<>(AggregateWorkoutData.class));
        ExtraOptionRepository extraOptionRepository = new ExtraOptionDAO(databaseManager, new ResultSetMapper<>(ExtraOption.class));
        WorkoutTypeRepository workoutTypeRepository = new WorkoutTypeDAO(databaseManager, new ResultSetMapper<>(WorkoutType.class));
        AuditRepository auditRepository = new AuditDAO(databaseManager, new ResultSetMapper<>(Audit.class));
        UserRepository userRepository = new UserDAO(databaseManager, new ResultSetMapper<>(User.class));
        ExtraOptionTypeRepository extraOptionTypeRepository = new ExtraOptionTypeDAO(databaseManager,
                new ResultSetMapper<>(ExtraOptionType.class));

        AuditService auditService = new AuditService(auditRepository, userRepository);
        AuditAspect auditAspect = Aspects.aspectOf(AuditAspect.class);
        auditAspect.initServices(auditService);

        servletContext.setAttribute("workoutService", new WorkoutService(workoutRepository, extraOptionRepository));
        servletContext.setAttribute("extraOptionService", new ExtraOptionService(extraOptionRepository));
        servletContext.setAttribute("workoutTypeService", new WorkoutTypeService(workoutTypeRepository));
        servletContext.setAttribute("auditService", auditService);
        servletContext.setAttribute("securityService", new SecurityService(userRepository));
        servletContext.setAttribute("extraOptionTypeService", new ExtraOptionTypeService(extraOptionTypeRepository));
    }

    private void loadMapperContext(ServletContext servletContext) {
        servletContext.setAttribute("workoutMapper", Mappers.getMapper(WorkoutMapper.class));
        servletContext.setAttribute("workoutTypeMapper", Mappers.getMapper(WorkoutTypeMapper.class));
        servletContext.setAttribute("extraOptionTypeMapper", Mappers.getMapper(ExtraOptionTypeMapper.class));
        servletContext.setAttribute("auditMapper", Mappers.getMapper(AuditMapper.class));
        servletContext.setAttribute("aggregateWorkoutDataMapper", Mappers.getMapper(AggregateWorkoutDataMapper.class));
    }

    /**
     * Retrieve the application properties from the properties file.
     *
     * @return The application properties.
     * @throws RuntimeException If an error occurs while loading the properties file.
     */
    public Properties properties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading application properties");
        }
        return properties;
    }
}
