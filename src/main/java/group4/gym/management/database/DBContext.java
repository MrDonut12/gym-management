package group4.gym.management.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provide connection to database (shared interface, SRS 9.1).
 * Connection settings are read from db.properties on the classpath (CL-10); the real file is git-ignored,
 * copy src/main/resources/db.properties.example to create it.
 * Every call returns a NEW connection: close it with try-with-resources. Services that need a transaction
 * (CL-06, QT-06) call setAutoCommit(false) on it and pass it to the DAO methods that take a Connection.
 *
 * @author Nguyen Sy Nguyen
 */
public final class DBContext {

    private static final Logger LOGGER = Logger.getLogger(DBContext.class.getName());
    private static final String CONFIG_FILE = "db.properties";
    private static final Properties CONFIG = loadConfig();

    private DBContext() {
    }

    public static Connection getConnection() throws SQLException {
        String url = CONFIG.getProperty("db.url");
        if (url == null || url.isBlank()) {
            throw new SQLException(CONFIG_FILE + " is missing or does not define db.url");
        }
        return DriverManager.getConnection(url, CONFIG.getProperty("db.user"), CONFIG.getProperty("db.password"));
    }

    /**
     * Roll back a transaction without throwing (used in catch blocks before rethrowing the original error).
     */
    public static void rollbackQuietly(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.rollback();
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, "Rollback failed", ex);
        }
    }

    private static Properties loadConfig() {
        Properties props = new Properties();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "SQL Server JDBC driver not found", ex);
        }
        try (InputStream in = DBContext.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                LOGGER.severe(CONFIG_FILE + " not found on the classpath; copy db.properties.example to create it");
            } else {
                props.load(in);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Cannot read " + CONFIG_FILE, ex);
        }
        return props;
    }
}
