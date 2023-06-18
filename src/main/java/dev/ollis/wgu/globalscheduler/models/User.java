package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class User implements Readable {
    private static User currentUser;
    public static User getCurrentUser() {
        return currentUser;
    }
    private int userId;
    private String userName;

    public User(int userId, String userName) {
        setId(userId);
        setUserName(userName);
    }

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

    public int getId() {
        return userId;
    }

    private void setId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static User login(String userName, String userPassword) throws NoSuchElementException {
        String sql = "SELECT User_ID, User_Name FROM users WHERE User_Name = ? AND Password = ?";
        User user = JDBC.getFirstFromQuery(sql, Arrays.asList(userName, userPassword), User.class);
        currentUser = user;
        return user;
    }

    public static User find(int id) throws NoSuchElementException {
        String sql = "SELECT User_ID, User_Name FROM users WHERE User_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), User.class);
    }

    public static List<User> getAll() throws NoSuchElementException {
        String sql = "SELECT User_ID, User_Name FROM users";
        return JDBC.getAllFromQuery(sql, null, User.class);
    }
}
