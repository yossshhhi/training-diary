package kz.yossshhhi.util;

import lombok.Getter;

/**
 * Represents a data source for connecting to a database.
 */
@Getter
public class DataSource {
    /**
     * The JDBC URL of the database.
     */
    private final String url;

    /**
     * The username used to authenticate to the database.
     */
    private final String user;

    /**
     * The password used to authenticate to the database.
     */
    private final String password;

    /**
     * Constructs a new DataSource with the specified connection details.
     *
     * @param url      the URL of the database
     * @param user     the username for accessing the database
     * @param password the password for accessing the database
     * @param driver   the fully qualified name of the JDBC driver class
     * @throws RuntimeException if an error occurs while loading the JDBC driver
     */
    public DataSource(String url, String user, String password, String driver) {
        this.url = url;
        this.user = user;
        this.password = password;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading PostgreSQL JDBC driver", e);
        }
    }
}

