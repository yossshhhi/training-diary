package kz.yossshhhi.util;

import kz.yossshhhi.exception.DatabaseManagerException;

import java.sql.*;
import java.util.List;

/**
 * A utility class for executing SQL queries and updates.
 */
public class DatabaseManager {

    /**
     * A connection pool managing database connections.
     */
    private final ConnectionPool connectionPool;

    /**
     * Constructs a new DatabaseManager with the given connection pool.
     *
     * @param connectionPool the connection pool to use
     */
    public DatabaseManager(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Executes a SQL query and maps the result set to a list of objects using the provided ResultSetMapper.
     *
     * @param <T>    the type of objects in the resulting list
     * @param sql    the SQL query to execute
     * @param mapper the ResultSetMapper to map the result set to objects
     * @param params the parameters to be set in the prepared statement
     * @return a list of objects mapped from the result set
     * @throws DatabaseManagerException if an error occurs while executing the query
     */
    public <T> List<T> executeQuery(String sql, ResultSetMapper<T> mapper, Object... params) {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                setParameters(statement, params);
                resultSet = statement.executeQuery();
                return mapper.mapResultSetToList(resultSet);
            }
        } catch (SQLException ex) {
            throw new DatabaseManagerException("Error executing query. " + ex.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    throw new DatabaseManagerException("Error closing ResultSet: " + ex.getMessage());
                }
            }
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }

    /**
     * Executes a SQL update (insert, update, delete) and returns the generated key if available.
     *
     * @param sql    the SQL update statement to execute
     * @param params the parameters to be set in the prepared statement
     * @return the generated key if available
     * @throws DatabaseManagerException if an error occurs while executing the update
     */
    public long executeUpdate(String sql, Object... params) {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                setParameters(statement, params);
                statement.executeUpdate();
                resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                } else {
                    throw new SQLException("Creating entity failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseManagerException("Error executing update. " + ex.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    throw new DatabaseManagerException("Error closing ResultSet: " + ex.getMessage());
                }
            }
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }

    /**
     * Sets the parameters of a PreparedStatement based on the given parameter values.
     *
     * @param statement the PreparedStatement to set parameters for
     * @param params    the parameter values to set
     * @throws SQLException if an error occurs while setting parameters
     */
    private void setParameters(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}
