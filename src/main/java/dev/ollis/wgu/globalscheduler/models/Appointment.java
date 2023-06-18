package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Appointment implements Readable, Writable {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private int customerId;
    private int userId;
    private int contactId;

    public Appointment(ResultSet rs) {
        try {
            setId(rs.getInt("Appointment_ID"));
            setTitle(rs.getString("Title"));
            setDescription(rs.getString("Description"));
            setLocation(rs.getString("Location"));
            setType(rs.getString("Type"));
            setStart(rs.getTimestamp("Start"));
            setEnd(rs.getTimestamp("End"));
            setCustomerId(rs.getInt("Customer_ID"));
            setUserId(rs.getInt("User_ID"));
            setContactId(rs.getInt("Contact_ID"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Appointment(String title, String description, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId) {
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setType(type);
        setStart(start);
        setEnd(end);
        setCustomerId(customerId);
        setUserId(userId);
        setContactId(contactId);
    }

    public int getId() {
        return id;
    }

    private void setId(int appointmentId) {
        this.id = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return Customer.find(getCustomerId());
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return User.find(getUserId());
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public Contact getContact() {
        return Contact.find(getContactId());
    }

    @Override
    public String getIdColumnName() {
        return "Appointment_ID";
    }

    @Override
    public String getTableName() {
        return "appointments";
    }

    @Override
    public Map<String, Supplier<Object>> getDatabaseMap() {
        return Map.of(
                "Title", this::getTitle,
                "Description", this::getDescription,
                "Location", this::getLocation,
                "Type", this::getType,
                "Start", this::getStart,
                "End", this::getEnd,
                "Customer_ID", this::getCustomerId,
                "User_ID", this::getUserId,
                "Contact_ID", this::getContactId
        );
    }

    public static Appointment find(int id) {
        String sql = "SELECT * FROM appointments WHERE Appointment_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), Appointment.class);
    }

    public static List<Appointment> getAll() {
        String sql = "SELECT * FROM appointments";
        return JDBC.getAllFromQuery(sql, null, Appointment.class);
    }

    public static List<Appointment> getAllForCustomer(Customer customer) {
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        return JDBC.getAllFromQuery(sql, List.of(customer.getId()), Appointment.class);
    }

    public static List<Appointment> getAllForTitle(String title, Customer forCustomer) {
        String sql = "SELECT * FROM appointments WHERE Title LIKE ?";
        List<Object> params = List.of("%" + title + "%");
        if (forCustomer != null) {
            sql += " AND Customer_ID = ?";
            params.add(forCustomer.getId());
        }
        return JDBC.getAllFromQuery(sql, params, Appointment.class);
    }

    public static List<String> getAllTypes() {
        String sql = "SELECT DISTINCT Type FROM appointments";
        return JDBC.getAllFromQuery(sql, null, Type.class).stream().map(Type::toString).toList();
    }

    public static class Type implements Readable {
        private String type;

        public Type(ResultSet rs) {
            try {
                setType(rs.getString("Type"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return getType();
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
