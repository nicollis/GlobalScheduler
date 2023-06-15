package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class User implements Readable {
    private int userId;
    private String userName;

    public User(int userId, String userName) {
        setUserId(userId);
        setUserName(userName);
    }

    public User(ResultSet rs) throws RuntimeException {
        try {
            setUserId(rs.getInt("User_ID"));
            setUserName(rs.getString("User_Name"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static User login(String userName, String userPassword) throws NoSuchElementException {
        String sql = "SELECT User_ID, User_Name FROM users WHERE User_Name = ? AND Password = ?";
        return JDBC.getFirstFromQuery(sql, Arrays.asList(userName, userPassword), User.class);
    }
}
