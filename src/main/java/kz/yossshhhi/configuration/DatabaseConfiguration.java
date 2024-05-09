package kz.yossshhhi.configuration;

import kz.yossshhhi.util.YamlPropertySourceFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Configuration class for setting up database-related beans. This class is responsible for
 * creating the {@link DataSource}, {@link JdbcTemplate}, and {@link SpringLiquibase} beans
 * necessary for database operations and migrations. It reads database configurations from
 * an application.yaml file using a custom YamlPropertySourceFactory.
 */
@Configuration
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class DatabaseConfiguration {

    @Value("${liquibase.change-log}")
    private String changeLogFile;

    @Value("${datasource.url}")
    private String databaseUrl;

    @Value("${datasource.username}")
    private String databaseUser;

    @Value("${datasource.password}")
    private String databasePassword;

    @Value("${datasource.driver-class-name}")
    private String databaseDriver;

    /**
     * Creates and configures a {@link DataSource} bean with properties specified in the application's
     * configuration file. This bean is essential for providing database connectivity throughout the application.
     *
     * @return a configured instance of {@link DataSource}.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseDriver);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUser);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }

    /**
     * Configures the Liquibase integration with Spring, allowing for database migrations using
     * Liquibase change logs. This method sets up the {@link SpringLiquibase} bean with a {@link DataSource}
     * to manage database schema updates.
     *
     * @param dataSource The {@link DataSource} to use for Liquibase database migrations.
     * @return the configured {@link SpringLiquibase} instance.
     */
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(changeLogFile);
        return liquibase;
    }

    /**
     * Creates a {@link JdbcTemplate} bean that simplifies the development of JDBC operations
     * and helps to avoid common errors. It executes core JDBC workflow, leaving application code
     * to provide SQL and extract results.
     *
     * @param dataSource The {@link DataSource} that provides the database connectivity.
     * @return a fully configured {@link JdbcTemplate} instance.
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
