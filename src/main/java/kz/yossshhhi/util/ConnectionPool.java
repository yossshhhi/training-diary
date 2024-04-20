package kz.yossshhhi.util;

import kz.yossshhhi.exception.DatabaseManagerException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/**
 * A connection pool implementation to manage database connections.
 */
public class ConnectionPool {

    /** The data source used by the connection pool. */
    private final DataSource dataSource;

    /** The maximum number of connections allowed in the pool. */
    private static final int MAX_CONNECTIONS = 10;

    /** The pool of database connections. */
    private final BlockingQueue<Connection> pool;

    /**
     * Constructs a connection pool with the specified data source.
     *
     * @param dataSource The data source used to obtain connections.
     */
    public ConnectionPool(DataSource dataSource) {
        this.dataSource = dataSource;
        pool = new ArrayBlockingQueue<>(MAX_CONNECTIONS);
        initializePool();
    }

    /**
     * Initializes the connection pool by creating and adding connections to the pool.
     */
    private void initializePool() {
        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            try {
                Connection connection = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUser(), dataSource.getPassword());
                pool.offer(connection);
            } catch (SQLException e) {
                throw new RuntimeException("Connection Pool initialization error", e);
            }
        }
    }

    /**
     * Retrieves a connection from the pool. If no connections are available, waits until one becomes available.
     *
     * @return A database connection.
     * @throws SQLException If an error occurs while obtaining a connection.
     */
    public Connection getConnection() throws SQLException {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Failed to obtain connection from pool", e);
        }
    }

    /**
     * Releases a connection back to the pool for reuse.
     *
     * @param connection The connection to release.
     */
    public void releaseConnection(Connection connection) {
        pool.offer(connection);
    }

    /**
     * Closes all connections in the pool.
     *
     * @throws DatabaseManagerException If an error occurs while closing connections.
     */
    public void close() {
        for (Connection connection : pool) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing connection pool", e);
            }
        }
    }
}

