package kz.yossshhhi.configuration;

import kz.yossshhhi.util.ConnectionPool;
import kz.yossshhhi.util.DataSource;
import kz.yossshhhi.util.DatabaseManager;
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
import java.util.Properties;

import static kz.yossshhhi.util.AppConstants.PROPERTIES_FILE;

/**
 * This class represents the configuration for the application, providing access to various controllers and repositories.
 */
public class DatabaseConfiguration {

    /**
     * Retrieves the {@link DataSource} instance from the context map or creates a new one if not present.
     *
     * @param properties The properties containing database connection information.
     * @return The {@link DataSource} instance.
     */
    public DataSource dataSource(Properties properties) {
        return new DataSource(
                properties.getProperty("database.url"),
                properties.getProperty("database.user"),
                properties.getProperty("database.password"),
                properties.getProperty("database.driver"));
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
    public DatabaseManager databaseManager(Properties properties) {
        DataSource dataSource = dataSource(properties);
        databaseMigration(dataSource);
        return new DatabaseManager(connectionPool(dataSource));
    }

    /**
     * Retrieve or create an instance of the {@link  ConnectionPool}.
     *
     * @return The {@link ConnectionPool} instance.
     */
    public ConnectionPool connectionPool(DataSource dataSource) {
        return new ConnectionPool(dataSource);
    }
}
