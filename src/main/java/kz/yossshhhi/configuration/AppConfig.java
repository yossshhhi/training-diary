package kz.yossshhhi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.yossshhhi.aop.AuditAspect;
import kz.yossshhhi.mapper.*;
import kz.yossshhhi.model.*;
import kz.yossshhhi.service.AuditService;
import kz.yossshhhi.util.ResultSetMapper;
import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * Configuration class for setting up beans related to the application.
 * This class is responsible for configuring and provisioning various
 * components used across the application, such as mappers for converting
 * database results into objects, object mappers for JSON serialization,
 * and aspects for audit logging.
 */
@Configuration
@EnableAspectJAutoProxy
@Import({AuditService.class})
public class AppConfig {

    @Bean
    public ResultSetMapper<Audit> auditResultSetMapper() {
        return new ResultSetMapper<>(Audit.class);
    }

    @Bean
    public ResultSetMapper<ExtraOption> extraOptionResultSetMapper() {
        return new ResultSetMapper<>(ExtraOption.class);
    }

    @Bean
    public ResultSetMapper<ExtraOptionType> extraOptionTypeResultSetMapper() {
        return new ResultSetMapper<>(ExtraOptionType.class);
    }

    @Bean
    public ResultSetMapper<User> userResultSetMapper() {
        return new ResultSetMapper<>(User.class);
    }

    @Bean
    public ResultSetMapper<Workout> workoutResultSetMapper() {
        return new ResultSetMapper<>(Workout.class);
    }

    @Bean
    public ResultSetMapper<WorkoutType> workoutTypeResultSetMapper() {
        return new ResultSetMapper<>(WorkoutType.class);
    }

    @Bean
    public ResultSetMapper<AggregateWorkoutData> aggregateWorkoutDataResultSetMapper() {
        return new ResultSetMapper<>(AggregateWorkoutData.class);
    }

    @Bean
    public WorkoutMapper workoutMapper() {
        return new WorkoutMapperImpl();
    }

    @Bean
    public AuditMapper auditMapper() {
        return new AuditMapperImpl();
    }

    @Bean
    public ExtraOptionTypeMapper extraOptionTypeMapper() {
        return new ExtraOptionTypeMapperImpl();
    }

    @Bean
    public WorkoutTypeMapper workoutTypeMapper() {
        return new WorkoutTypeMapperImpl();
    }

    @Bean
    public AggregateWorkoutDataMapper aggregateWorkoutDataMapper() {
        return new AggregateWorkoutDataMapperImpl();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AuditAspect auditAspect(AuditService auditService) {
        AuditAspect auditAspect = Aspects.aspectOf(AuditAspect.class);
        auditAspect.initServices(auditService);
        return auditAspect;
    }
}
