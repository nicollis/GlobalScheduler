package dev.ollis.wgu.helper;

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

    public static <T> T getFirstFromQuery(String sql, List<Object> params, Function<ResultSet, T> handler) {
        return executeQuery(sql, params, rs -> {
            try {
                if (rs.next()) {
                    return handler.apply(rs);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            throw new NoSuchElementException("No results found.");
        });
    }

    public static <T> List<T> getAllFromQuery(String sql, List<Object> params, Function<ResultSet, T> handler) {
        return executeQuery(sql, params, rs -> {
            try {
                List<T> results = new ArrayList<>();
                while (rs.next()) {
                     results.add(handler.apply(rs));
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

    private static <T> T executeQuery(String sql, List<Object> params, Function<ResultSet, T> handler) {
        Connection connect = JDBC.openConnection();
        try {
            assert connect != null;
            PreparedStatement stmt = connect.prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            var rs = stmt.executeQuery();
            return handler.apply(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connect != null) JDBC.closeConnection(connect);
        }
    }

}
