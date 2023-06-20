package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.ResultSet;
import java.util.List;

/**
 * Division class
 * Implements Readable interface
 */
public class Division implements Readable {
    private int id;
    private String division;
    private int countryId;

    /**
     * Constructor for Division from SQL Data
     * @param rs
     */
    public Division(ResultSet rs) {
        try {
            setId(rs.getInt("Division_ID"));
            setDivision(rs.getString("Division"));
            setCountryId(rs.getInt("Country_ID"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return getDivision();
    }

    /**
     * Gets the id of the division
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the division
     * @param id
     */
    private void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the division
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets the name of the division
     * @param division
     */
    private void setDivision(String division) {
        this.division = division;
    }

    /**
     * Gets the id of the country
     * @return countryId
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the id of the country
     * @param countryId
     */
    private void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Gets the country object of the division
     * @return country
     */
    public Country getCountry() {
        return Country.fetch(getCountryId());
    }

    /**
     * Fetches a division from the database by id
     * @param id
     * @return division
     */
    public static Division fetch(int id) {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), Division.class);
    }

    /**
     * Fetches all divisions from the database
     * @return divisions
     */
    public static List<Division> getAll() {
        String sql = "SELECT * FROM first_level_divisions";
        return JDBC.getAllFromQuery(sql, null, Division.class);
    }

    /**
     * Fetches all divisions from the database by country
     * @param country
     * @return divisions
     */
    public static List<Division> getAllByCountry(Country country) {
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        return JDBC.getAllFromQuery(sql, List.of(country.getId()), Division.class);
    }
}
