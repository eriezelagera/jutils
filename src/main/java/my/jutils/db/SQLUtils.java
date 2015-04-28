package my.jutils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import my.jutils.Utils;
import org.slf4j.*;

/**
 * Utility for SQL and Database connectivity.
 *
 * @author Erieze Lagera
 */
public class SQLUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SQLUtils.class.getSimpleName());

    /**
     * Test your database connection.
     *
     * @param dbServer Database server that you are using
     * @param serverIP Database server host
     * @param serverPort Database server port
     * @param dbName Database name
     * @param username User of the database
     * @param password Nothing is special here
     * @return True if the test is successful, otherwise false
     */
    public static boolean doTestConnection(String dbServer, String serverIP, String serverPort, String dbName, String username, String password) {
        final AtomicBoolean result = new AtomicBoolean();
        final String url = "jdbc:" + dbServer + "://" + serverIP + ":" + serverPort + "/" + dbName;
        final Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        if (dbServer.equalsIgnoreCase("mysql")) {
            dbServer += ".jdbc";
        }

        try {
            Class.forName("org." + dbServer + ".Driver");
            final Connection db = DriverManager.getConnection(url, props);
            db.close();
            result.set(true);
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
            Utils.setDelay(500);
            result.set(false);
        }
        return result.get();
    }

    /**
     * Get Database server by port number.
     * <p>
     * Note: <i> This only support database server with default port
     * configured. </i>
     *
     * @param port Database server port
     * @return The specified database server
     */
    public static String getDatabaseServerByPort(String port) {
        return port.equals("3306") ? "mysql" : port.equals("5432") ? "postgresql" : "unknown_port";
    }

    /**
     * Create a Connection.
     * <p>
     * This method will return a database connection with
     * success testing, null if error is found on connection.
     *
     * @param dbServer Database server that you are using
     * @param serverIP Database server host
     * @param serverPort Database server port
     * @param dbName Database name
     * @param username User of the database
     * @param password Nothing is special here
     * @return Configured connection
     */
    public static Connection setConnection(String dbServer, String serverIP, String serverPort, String dbName, String username, String password) {
        Connection result = null;
        try {
            final String url = "jdbc:" + dbServer + "://" + serverIP + ":" + serverPort + "/" + dbName;
            final Properties props = new Properties();
            props.setProperty("user", username);
            props.setProperty("password", password);

            if (dbServer.equalsIgnoreCase("mysql")) {
                dbServer += ".jdbc";
            }

            Class.forName("org." + dbServer + ".Driver");
            result = DriverManager.getConnection(url, props);
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        }
        return result;
    }

    /**
     * Generate SQL statement, finding row by id.
     *
     * @param id Id of the row
     * @param table_name Your table/entity name
     * @return Generated SQL <b>SELECT</b> statement
     */
    public static String findById(String id, String table_name) {
        return "SELECT * FROM " + table_name + " WHERE id = " + id;
    }

    /**
     * Generate SQL statement, insert query.
     *
     * @param columns Source of columns in instance of {@code ArrayList<String>}
     * @param table_name Your table/entity name
     * @return Generated SQL <b>INSERT</b> statement
     */
    public static String insertQuery(ArrayList<String> columns, String table_name) {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ").append(table_name).append(" (");
        for (int i = 0; i < columns.size(); i++) {
            if (i != columns.size() - 1) {
                query.append(" ").append(columns.get(i)).append(", ");
            } else {
                query.append(" ").append(columns.get(i)).append(" ");
            }
        }
        query.append(") VALUES (");
        for (int i = 0; i < columns.size(); i++) {
            if (i != columns.size() - 1) {
                query.append(" ?, ");
            } else {
                query.append(" ? ");
            }
        }
        query.append(")");

        return query.toString();
    }

    /**
     * Generate SQL statement, insert query.
     *
     * @param columns Source of columns in instance of {@code LinkedList<String>}
     * @param table_name Your table/entity name
     * @return Generated SQL <b>INSERT</b> statement
     */
    public static String insertQuery(LinkedList<String> columns, String table_name) {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ").append(table_name).append(" (");

        for (int i = 0; i < columns.size(); i++) {
            if (i != columns.size() - 1) {
                query.append(" ").append(columns.get(i)).append(", ");
            } else {
                query.append(" ").append(columns.get(i)).append(" ");
            }
        }
        query.append(") VALUES (");
        for (int i = 0; i < columns.size(); i++) {
            if (i != columns.size() - 1) {
                query.append(" ?, ");
            } else {
                query.append(" ? ");
            }
        }
        query.append(")");
        return query.toString();
    }

}
