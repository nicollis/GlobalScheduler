package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.ResultSet;
import java.util.List;

public class Division implements Readable {
    private int id;
    private String division;
    private int countryId;

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

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getDivision() {
        return division;
    }

    private void setDivision(String division) {
        this.division = division;
    }

    public int getCountryId() {
        return countryId;
    }

    private void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public Country getCountry() {
        return Country.fetch(getCountryId());
    }

    public static Division fetch(int id) {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), Division.class);
    }

    public static List<Division> getAll() {
        String sql = "SELECT * FROM first_level_divisions";
        return JDBC.getAllFromQuery(sql, null, Division.class);
    }

    public static List<Division> getAllByCountry(Country country) {
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        return JDBC.getAllFromQuery(sql, List.of(country.getId()), Division.class);
    }
}
