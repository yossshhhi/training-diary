package kz.yossshhhi.container;

import kz.yossshhhi.util.ConnectionPool;
import kz.yossshhhi.util.DataSource;
import kz.yossshhhi.util.DatabaseManager;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestContainerInitializer {

    public static void initializeDatabase(PostgreSQLContainer<?> container) {
        String jdbcUrl = container.getJdbcUrl();
        String username = container.getUsername();
        String password = container.getPassword();

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseManager databaseManager(PostgreSQLContainer<?> container) {
        String jdbcUrl = container.getJdbcUrl();
        String username = container.getUsername();
        String password = container.getPassword();
        String driverClassName = container.getDriverClassName();

        ConnectionPool connectionPool = new ConnectionPool(new DataSource(jdbcUrl, username, password, driverClassName));
        return new DatabaseManager(connectionPool);
    }
}

