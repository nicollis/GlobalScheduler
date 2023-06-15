package dev.ollis.wgu.helper;

import dev.ollis.wgu.globalscheduler.models.Readable;
import dev.ollis.wgu.globalscheduler.models.Writable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public abstract class JDBC {

    // Connection Information
    private static final String DB_NAME = "client_schedule";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private static final String DB_USER = "sq|User";
    private static final String DB_PASS = "PasswOrd!";
    // Driver version 8.0.25
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection openConnection() {
        // Open Connection
        try {
            Class.forName(DB_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Connection to database " + DB_NAME + " established.");
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return null;
        }
    }

    public static void closeConnection(Connection conn) {
        // Close Connection
        try {
            conn.close();
            System.out.println("Connection to database " + DB_NAME + " closed.");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

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

    public static <T extends Writable> void saveObject(String sql, List<Object> params) throws SQLException  {
        int rowChanged =  executeUpdate(sql, params);
        if (rowChanged == 0) {
            throw new SQLException("No rows changed.");
        }
    }

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

    private static int executeUpdate(String sql, List<Object> params) throws SQLException {
        Connection connect = JDBC.openConnection();
        try {
            PreparedStatement stmt = preparedStatement(sql, params, connect);
            return stmt.executeUpdate();
        } finally {
            if (connect != null) JDBC.closeConnection(connect);
        }
    }

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
}
