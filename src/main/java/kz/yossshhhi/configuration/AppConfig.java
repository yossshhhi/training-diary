package kz.yossshhhi.configuration;

import kz.yossshhhi.model.*;
import kz.yossshhhi.security.JwtTokenFilter;
import kz.yossshhhi.util.ResultSetMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration class for setting up beans related to the application.
 * This class is responsible for configuring and provisioning various
 * components used across the application, such as mappers for converting
 * database results into objects, object mappers for JSON serialization,
 * and aspects for audit logging.
 */
@Configuration
@Import({JwtTokenFilter.class})
public class AppConfig {

    @Bean
    public FilterRegistrationBean<JwtTokenFilter> authenticationFilter(JwtTokenFilter jwtTokenFilter) {
        FilterRegistrationBean<JwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtTokenFilter);
        registrationBean.addUrlPatterns("/admin/*", "/user/*");

        return registrationBean;
    }

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
}
