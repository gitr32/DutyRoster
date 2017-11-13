package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to manage connections to the database
 *
 * @author Alex.Tiang
 */
public class ConnectionManager {

    /**
     * this method is used initialize a connection object
     *
     * @return Connection a connection object or null if an exception occurs.
     */
    public static Connection getConnection() {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pss", "postgres", "admin");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * this method is used to close result set, prepared statement and
     * connection objects.
     *
     * @param rs the result set object
     * @param stmt the prepared statement object
     * @param c the connection object
     *
     * @throws SQLException if an exception occur while closing the three
     * objects
     */
    public static void closeAll(ResultSet rs, PreparedStatement stmt, Connection c) throws SQLException {
        if (stmt != null && c != null && rs != null) {
            rs.close();
            stmt.close();
            c.close();
        }
    }

    /**
     * this method is used to close prepared statement and connection objects.
     *
     * @param stmt the prepared statement object
     * @param c the connection object
     *
     * @throws SQLException if an exception occur while closing the two objects
     */
    public static void close(PreparedStatement stmt, Connection c) throws SQLException {
        if (stmt != null && c != null) {
            stmt.close();
            c.close();
        }
    }
}