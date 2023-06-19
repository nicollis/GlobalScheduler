package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.globalscheduler.ApplicationController;
import dev.ollis.wgu.helper.JDBC;
import dev.ollis.wgu.helper.TimeUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

    // Constructors

    public Appointment(ResultSet rs) {
        try {
            setId(rs.getInt("Appointment_ID"));
            setTitle(rs.getString("Title"));
            setDescription(rs.getString("Description"));
            setLocation(rs.getString("Location"));
            setType(rs.getString("Type"));
            setStartUTC(rs.getTimestamp("Start"));
            setEndUTC(rs.getTimestamp("End"));
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

    // Getters and setters

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

    public Timestamp getStartUTC() {
        return TimeUtils.toUTC(getStart(), ApplicationController.zone);
    }

    public void setStartUTC(Timestamp start) {
        setStart(TimeUtils.fromUTC(start, ApplicationController.zone));
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public Timestamp getEndUTC() {
        return TimeUtils.toUTC(getEnd(), ApplicationController.zone);
    }

    public void setEndUTC(Timestamp end) {
        setEnd(TimeUtils.fromUTC(end, ApplicationController.zone));
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

    // Override methods

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
                "Start", this::getStartUTC,
                "End", this::getEndUTC,
                "Customer_ID", this::getCustomerId,
                "User_ID", this::getUserId,
                "Contact_ID", this::getContactId
        );
    }

    @Override
    public void save() throws Exception {
        if (validate()) {
            Writable.super.save();
        }
    }

    // Instance methods

    public boolean validate() throws AppointmentValidationException {
        if (isOnWeekend()) {
            throw new OnWeekendException();
        }
        if (isOutsideBusinessHours()) {
            throw new OutsideBusinessHoursException();
        }
        if (isOverlapping()) {
            throw new OverlappingAppointmentException();
        }
        return true;
    }

    public boolean isOverlapping(Appointment other) {
        if (other.getId() == getId()) {
            return false;
        }
        return getStart().before(other.getEnd()) && getEnd().after(other.getStart());
    }

    public boolean isOverlapping(List<Appointment> others) {
        return others.stream().anyMatch(this::isOverlapping);
    }

    public boolean isOverlapping() {
        return isOverlapping(getAll());
    }

    public boolean isOnWeekend() {
        return TimeUtils.isWeekend(getStart(), getEnd());
    }

    public boolean isOutsideBusinessHours() {
        Timestamp start = TimeUtils.toEST(getStart(), ApplicationController.zone);
        Timestamp end = TimeUtils.toEST(getEnd(), ApplicationController.zone);
        int open = ApplicationController.businessHours[0];
        int close = ApplicationController.businessHours[1];
        System.out.println(start + " " + end + " " + open + " " + close + " " + getStart() + " " + getEnd());
        return TimeUtils.isOutsideBusinessHours(start, end, open, close);
    }

    public boolean isWithin15Minutes() {
        return TimeUtils.isWithinTheNext(15, getStart());
    }

    // Static methods

    public static Appointment find(int id) throws NoSuchElementException {
        String sql = "SELECT * FROM appointments WHERE Appointment_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), Appointment.class);
    }

    public static List<Appointment> getAll() throws NoSuchElementException {
        String sql = "SELECT * FROM appointments";
        return JDBC.getAllFromQuery(sql, null, Appointment.class);
    }

    public static List<Appointment> getAllForContact(Contact contact) throws NoSuchElementException {
        String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
        return JDBC.getAllFromQuery(sql, List.of(contact.getId()), Appointment.class);
    }

    public static List<Appointment> getAllForCustomer(Customer customer) throws NoSuchElementException {
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        return JDBC.getAllFromQuery(sql, List.of(customer.getId()), Appointment.class);
    }

    public static List<Appointment> getAllForTitle(String title, Customer forCustomer) throws NoSuchElementException {
        String sql = "SELECT * FROM appointments WHERE Title LIKE ?";
        List<Object> params = List.of("%" + title + "%");
        if (forCustomer != null) {
            sql += " AND Customer_ID = ?";
            params.add(forCustomer.getId());
        }
        return JDBC.getAllFromQuery(sql, params, Appointment.class);
    }

    public static List<Appointment> getAllForMonth(int month) throws NoSuchElementException {
        String sql = "SELECT * FROM appointments WHERE MONTH(Start) = ? OR MONTH(End) = ?";
        return JDBC.getAllFromQuery(sql, List.of(month, month), Appointment.class);
    }

    public static List<Appointment> getAllForWeek(int selectedWeek) {
        String sql = "SELECT * FROM appointments WHERE WEEK(Start) = ? OR WEEK(End) = ?";
        return JDBC.getAllFromQuery(sql, List.of(selectedWeek, selectedWeek), Appointment.class);
    }

    public static List<Appointment> getAllForUser(User user) throws NoSuchElementException {
        String sql = "SELECT * FROM appointments WHERE User_ID = ?";
        return JDBC.getAllFromQuery(sql, List.of(user.getId()), Appointment.class);
    }

    public static List<String> getAllTypes() throws NoSuchElementException {
        String sql = "SELECT DISTINCT Type FROM appointments";
        return JDBC.getAllFromQuery(sql, null, Type.class).stream().map(Type::toString).toList();
    }

    public static List<Map<String, Object>> getMonthlyReport() throws SQLException {
        String sql = "SELECT MONTHNAME(Start) AS Month, Type, COUNT(*) AS Count\n" +
                "FROM appointments\n" +
                "GROUP BY MONTHNAME(Start), Type\n" +
                "ORDER BY MONTHNAME(Start), Type;";
        return JDBC.getResultSetFromQuery(sql, null);
    }

    public static List<Map<String, Object>> getContactReport(Contact contact) throws NoSuchElementException {
        List<Appointment> appointments = Appointment.getAllForContact(contact);
        return convertAppointmentToMap(appointments);
    }

    public static List<Map<String, Object>> getCustomerReport(Customer customer) throws NoSuchElementException {
        List<Appointment> appointments = Appointment.getAllForCustomer(customer);
        return convertAppointmentToMap(appointments);
    }

    private static List<Map<String, Object>> convertAppointmentToMap(List<Appointment> appointments) {
        return appointments.stream().map(appointment -> {
            Map<String, Object> map = new HashMap<>();
            map.put("ID", appointment.getId());
            map.put("Title", appointment.getTitle());
            map.put("Description", appointment.getDescription());
            map.put("Location", appointment.getLocation());
            map.put("Type", appointment.getType());
            map.put("Start", appointment.getStart());
            map.put("End", appointment.getEnd());
            map.put("Customer", appointment.getCustomer());
            map.put("User", appointment.getUser());
            map.put("Contact", appointment.getContact());
            return map;
        }).toList();
    }

    // Subclasses and exceptions

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

    public static class AppointmentValidationException extends Exception {
        public AppointmentValidationException(String message) {
            super(message == null ? "Appointment validation failed" : message);
        }
    }

    public static class OnWeekendException extends AppointmentValidationException {
        public OnWeekendException() {
            super("Appointment cannot be on a weekend");
        }
    }

    public static class OutsideBusinessHoursException extends AppointmentValidationException {
        public OutsideBusinessHoursException() {
            super("Appointment cannot be outside of business hours");
        }
    }

    public static class OverlappingAppointmentException extends AppointmentValidationException {
        public OverlappingAppointmentException() {
            super("Appointment cannot overlap with another appointment");
        }
    }
}
