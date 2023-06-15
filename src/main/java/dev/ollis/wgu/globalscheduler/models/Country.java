package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.ResultSet;
import java.util.List;

public class Country implements Readable {
    private int id;
    private String name;

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

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public static List<Country> getAll() {
        String sql = "SELECT * FROM countries";
        return JDBC.getAllFromQuery(sql, null, Country.class);
    }

    public static Country fetch(int id) {
        String sql = "SELECT * FROM countries WHERE Country_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), Country.class);
    }
}
