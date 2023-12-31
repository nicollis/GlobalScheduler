package dev.ollis.wgu.helper;

import dev.ollis.wgu.globalscheduler.models.Readable;
import dev.ollis.wgu.globalscheduler.models.Writable;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

/**
 * JDBC Class
 * Abstract class for handling JDBC connections
 * Provides helper methods for executing queries and updates
 */
public abstract class JDBC {

    // Connection Information
    private static final String DB_NAME = "client_schedule";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private static final String DB_USER = "sqlUser";
    private static final String DB_PASS = "Passw0rd!";
    // Driver version 8.0.25
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Open Connection
     * @return Connection
     */
    public static Connection openConnection() {
        // Open Connection
        try {
            Class.forName(DB_DRIVER);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return null;
        }
    }

    /**
     * Close Connection
     * @param conn Connection
     */
    public static void closeConnection(Connection conn) {
        // Close Connection
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    /**
     * Get First From Query
     * @param sql SQL Query
     * @param params List of parameters
     * @param clazz Class of object to return
     * @return Object
     * @param <T> Class of object to return
     * @throws NoSuchElementException if no results found
     */
    public static <T extends Readable> T getFirstFromQuery(String sql, List<Object> params, Class<T> clazz) throws NoSuchElementException {
        return executeQuery(sql, params, rs -> {
            try {
                if (rs.next()) {
                    return Readable.fromResultSet(rs, clazz);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            throw new NoSuchElementException("No results found.");
        });
    }

    /**
     * Get All From Query
     * @param sql SQL Query
     * @param params List of parameters
     * @param clazz Class of object to return
     * @return List of objects
     * @param <T> Class of object to return
     * @throws NoSuchElementException if no results found
     */
    public static <T extends Readable> List<T> getAllFromQuery(String sql, List<Object> params, Class<T> clazz) throws NoSuchElementException {
        return executeQuery(sql, params, rs -> {
            try {
                List<T> results = new ArrayList<>();
                while (rs.next()) {
                     results.add(
                                Readable.fromResultSet(rs, clazz)
                     );
                }
                if (results.isEmpty()) {
                    throw new NoSuchElementException("No results found.");
                }
                return results;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Save Object
     * @param sql SQL Query
     * @param params List of parameters
     * @param <T> Class of object to return
     * @throws SQLException if no rows changed
     */
    public static <T extends Writable> void saveObject(String sql, List<Object> params) throws SQLException  {
        int rowChanged =  executeUpdate(sql, params);
        if (rowChanged == 0) {
            throw new SQLException("No rows changed.");
        }
    }

    /**
     * Execute Query
     * @param sql SQL Query
     * @param params List of parameters
     * @param handler Function to handle ResultSet
     * @return Object
     * @param <T> Class of object to return
     * @throws RuntimeException if error occurs
     */
    private static <T> T executeQuery(String sql, List<Object> params, Function<ResultSet, T> handler) throws RuntimeException {
        Connection connect = JDBC.openConnection();
        try {
            PreparedStatement stmt = preparedStatement(sql, params, connect);
            var rs = stmt.executeQuery();
            return handler.apply(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connect != null) JDBC.closeConnection(connect);
        }
    }

    /**
     * Execute Update
     * @param sql SQL Query
     * @param params List of parameters
     * @return int number of rows changed
     * @throws SQLException if error occurs
     */
    private static int executeUpdate(String sql, List<Object> params) throws SQLException {
        Connection connect = JDBC.openConnection();
        try {
            PreparedStatement stmt = preparedStatement(sql, params, connect);
            return stmt.executeUpdate();
        } finally {
            if (connect != null) JDBC.closeConnection(connect);
        }
    }

    /**
     * Prepared Statement
     * helper method for creating prepared statements
     * @param sql SQL Query
     * @param params List of parameters
     * @param connect Connection
     * @return PreparedStatement
     * @throws SQLException
     */
    private static PreparedStatement preparedStatement(String sql, List<Object> params, Connection connect) throws SQLException {
        assert connect != null;
        PreparedStatement stmt = connect.prepareStatement(sql);
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
        }
        return stmt;
    }

    /**
     * Get ResultSet From Query
     * @param sql SQL Query
     * @param params List of parameters
     * @return List of Maps
     */
    public static List<Map<String, Object>> getResultSetFromQuery(String sql, List<Object> params) {
        return executeQuery(sql, params, JDBC::resultsSetToList);
    }

    /**
     * Results Set To List
     * @param rs ResultSet
     * @return List of Maps
     */
    private static List<Map<String, Object>> resultsSetToList(ResultSet rs) {
        try {
            // get column names
            List<String> columnNames = new ArrayList<>();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columnNames.add(rsmd.getColumnName(i));
            }

            // fetch data rows
            List<Map<String, Object>> list = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (String col : columnNames) {
                    row.put(col, rs.getObject(col));
                }
                list.add(row);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
