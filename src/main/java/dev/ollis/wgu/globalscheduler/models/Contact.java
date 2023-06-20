package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.ResultSet;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Contact model
 * implements Readable interface
 */
public class Contact implements Readable {
    private int id;
    private String name;
    private String email;

    /**
     * Constructor
     * Builds Contact model from the ResultSet of SQL query
     * @param rs
     */
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

    /**
     * Get ID
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Set ID
     * @param id
     */
    private void setId(int id) {
        this.id = id;
    }

    /**
     * Get Name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Set Name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Email
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set Email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get All
     * @return List<Contact>
     * @throws NoSuchElementException
     */
    static public List<Contact> getAll() throws NoSuchElementException {
        String sql = "SELECT * FROM contacts";
        return JDBC.getAllFromQuery(sql, null, Contact.class);
    }

    /**
     * Find by id
     * @param id
     * @return Contact
     * @throws NoSuchElementException
     */
    static public Contact find(int id) throws NoSuchElementException {
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), Contact.class);
    }
}
