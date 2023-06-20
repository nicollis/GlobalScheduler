package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.ResultSet;
import java.util.List;

/**
 * Country model
 * Implements Readable interface
 */
public class Country implements Readable {
    private int id;
    private String name;

    /**
     * Constructor builds Country from SQL results
     * @param rs
     */
    public Country(ResultSet rs) {
        try {
            setId(rs.getInt("Country_ID"));
            setName(rs.getString("Country"));
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
     * Get name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Set name
     * @param name
     */
    private void setName(String name) {
        this.name = name;
    }

    /**
     * Get all countries
     * @return List<Country>
     */
    public static List<Country> getAll() {
        String sql = "SELECT * FROM countries";
        return JDBC.getAllFromQuery(sql, null, Country.class);
    }

    /**
     * Fetch country by ID
     * @param id
     * @return Country
     */
    public static Country fetch(int id) {
        String sql = "SELECT * FROM countries WHERE Country_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), Country.class);
    }
}
