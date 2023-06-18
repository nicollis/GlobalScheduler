package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class Customer implements Readable, Writable {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;

    public Customer(String customerName, String address, String postalCode, String phone, int divisionId) {
        setName(customerName);
        setAddress(address);
        setPostalCode(postalCode);
        setPhone(phone);
        setDivisionId(divisionId);
    }

    public Customer(ResultSet rs) throws RuntimeException {
        try {
            setId(rs.getInt("Customer_ID"));
            setName(rs.getString("Customer_Name"));
            setAddress(rs.getString("Address"));
            setPostalCode(rs.getString("Postal_Code"));
            setPhone(rs.getString("Phone"));
            setDivisionId(rs.getInt("Division_ID"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    private void setId(int customerId) {
        this.id = customerId;
    }

    public void setName(String customerName) {
        this.name = customerName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public Division getDivision() {
        return Division.fetch(getDivisionId());
    }

    public Country getCountry() { return getDivision().getCountry(); }

    @Override
    public String getIdColumnName() {
        return "Customer_ID";
    }

    @Override
    public String getTableName() {
        return "customers";
    }

    @Override
    public Map<String, Supplier<Object>> getDatabaseMap() {
        return Map.of(
                "Customer_Name", this::getName,
                "Address", this::getAddress,
                "Postal_Code", this::getPostalCode,
                "Phone", this::getPhone,
                "Division_ID", this::getDivisionId
        );
    }

    @Override
    public void delete() throws SQLException {
        //Delete all appointments first
        try {
            Appointment.getAllForCustomer(this).forEach(appointment -> {
                try {
                    appointment.delete();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (NoSuchElementException e) {
            //No appointments for this customer
        }

        Writable.super.delete();
    }

    public static List<Customer> getAll() {
        String sql = "SELECT * FROM customers";
        return JDBC.getAllFromQuery(sql, null, Customer.class);
    }

    public static List<Customer> getAllByName(String name) {
        String sql = "SELECT * FROM customers WHERE Customer_Name LIKE ?";
        return JDBC.getAllFromQuery(sql, List.of("%" + name + "%"), Customer.class);
    }

    public static Customer find(int id) {
        String sql = "SELECT * FROM customers WHERE Customer_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), Customer.class);
    }
}
