package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * Customer model
 * Implements Readable and Writable interfaces
 */
public class Customer implements Readable, Writable {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;

    /**
     * Constructor for creating a new customer
     * @param customerName
     * @param address
     * @param postalCode
     * @param phone
     * @param divisionId
     */
    public Customer(String customerName, String address, String postalCode, String phone, int divisionId) {
        setName(customerName);
        setAddress(address);
        setPostalCode(postalCode);
        setPhone(phone);
        setDivisionId(divisionId);
    }

    /**
     * Constructor for creating a customer from a ResultSet
     * @param rs
     * @throws RuntimeException
     */
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

    /**
     * Set the customer ID
     * @param customerId
     */
    private void setId(int customerId) {
        this.id = customerId;
    }

    /**
     * Set the customer name
     * @param customerName
     */
    public void setName(String customerName) {
        this.name = customerName;
    }

    /**
     * Set the customer address
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Set the customer postal code
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Set the customer phone number
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Set the customer division ID
     * @param divisionId
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Get the customer ID
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Get the customer name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Get the customer address
     * @return String
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get the customer postal code
     * @return String
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Get the customer phone number
     * @return String
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Get the customer division ID
     * @return int
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Get the customer division
     * @return Division
     */
    public Division getDivision() {
        return Division.fetch(getDivisionId());
    }

    /**
     * Get the customer country
     * @return Country
     */
    public Country getCountry() { return getDivision().getCountry(); }

    @Override
    public String getIdColumnName() {
        return "Customer_ID";
    }

    @Override
    public String getTableName() {
        return "customers";
    }

    /**
     * Get a map of the database columns and their values
     * @return Map<String, Supplier<Object>>
     */
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

    /**
     * Delete the customer
     * @throws SQLException
     */
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

    /**
     * Get all customers
     * @return List<Customer>
     */
    public static List<Customer> getAll() {
        String sql = "SELECT * FROM customers";
        return JDBC.getAllFromQuery(sql, null, Customer.class);
    }

    /**
     * Get all customers with a name like the given name
     * @param name
     * @return List<Customer>
     */
    public static List<Customer> getAllByName(String name) {
        String sql = "SELECT * FROM customers WHERE Customer_Name LIKE ?";
        return JDBC.getAllFromQuery(sql, List.of("%" + name + "%"), Customer.class);
    }

    /**
     * Get a customer by ID
     * @param id
     * @return Customer
     */
    public static Customer find(int id) {
        String sql = "SELECT * FROM customers WHERE Customer_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), Customer.class);
    }
}
