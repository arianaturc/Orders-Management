package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for managing JDBC database connections.
 */
public class ConnectionFactory {

    private static final String URL = "jdbc:postgresql://localhost:5432/ordersmanagement";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Databases2019";
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "org.postgresql.Driver";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new database connection using the configured credentials.
     *
     * @return a {@link Connection} object, or null if the connection failed.
     */
    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while trying to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Returns a new database connection.
     *
     * @return a {@link Connection} instance
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Closes a {@link Connection}.
     *
     * @param connection the connection to close
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the connection");
            }
        }
    }

    /**
     * Closes a {@link Statement}.
     *
     * @param statement the statement to close
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the statement");
            }
        }
    }

    /**
     * Closes a {@link ResultSet}.
     *
     * @param resultSet the result set to close
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the ResultSet");
            }
        }
    }
}
