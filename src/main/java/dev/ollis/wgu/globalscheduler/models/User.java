package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * User model
 * Implements Readable interface
 */
public class User implements Readable {
    private static User currentUser;
    public static User getCurrentUser() {
        return currentUser;
    }
    private int userId;
    private String userName;

    /**
     * Constructor for User
     * @param userId User ID
     * @param userName User name
     */
    public User(int userId, String userName) {
        setId(userId);
        setUserName(userName);
    }

    /**
     * Constructor for User from SQL results
     * @param rs ResultSet
     * @throws RuntimeException
     */
    public User(ResultSet rs) throws RuntimeException {
        try {
            setId(rs.getInt("User_ID"));
            setUserName(rs.getString("User_Name"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getUserName();
    }

    /**
     * Get user ID
     * @return int
     */
    public int getId() {
        return userId;
    }

    /**
     * Set user ID
     * @param userId User ID
     */
    private void setId(int userId) {
        this.userId = userId;
    }

    /**
     * Get user name
     * @return String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set user name
     * @param userName User name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Login user
     * @param userName User name
     * @param userPassword User password
     * @return User
     * @throws NoSuchElementException
     */
    public static User login(String userName, String userPassword) throws NoSuchElementException {
        String sql = "SELECT User_ID, User_Name FROM users WHERE User_Name = ? AND Password = ?";
        User user = JDBC.getFirstFromQuery(sql, Arrays.asList(userName, userPassword), User.class);
        currentUser = user;
        return user;
    }

    /**
     * Find user by ID
     * @param id User ID
     * @return User
     * @throws NoSuchElementException
     */
    public static User find(int id) throws NoSuchElementException {
        String sql = "SELECT User_ID, User_Name FROM users WHERE User_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), User.class);
    }

    /**
     * Get all users
     * @return List<User>
     * @throws NoSuchElementException
     */
    public static List<User> getAll() throws NoSuchElementException {
        String sql = "SELECT User_ID, User_Name FROM users";
        return JDBC.getAllFromQuery(sql, null, User.class);
    }
}
