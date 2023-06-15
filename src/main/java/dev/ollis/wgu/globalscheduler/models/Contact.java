package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.ResultSet;
import java.util.List;
import java.util.NoSuchElementException;

public class Contact implements Readable {
    private int id;
    private String name;
    private String email;

    public Contact(ResultSet rs) {
        try {
            setId(rs.getInt("Contact_ID"));
            setName(rs.getString("Contact_Name"));
            setEmail(rs.getString("Email"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    static public List<Contact> getAll() throws NoSuchElementException {
        String sql = "SELECT * FROM contacts";
        return JDBC.getAllFromQuery(sql, null, Contact.class);
    }

    static public Contact find(int id) throws NoSuchElementException {
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), Contact.class);
    }
}
