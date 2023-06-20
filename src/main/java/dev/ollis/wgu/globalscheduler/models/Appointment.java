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

/**
 * Appointment model
 * Implements Readable and Writable interfaces
 */
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

    /**
     * Appointment constructor that takes a ResultSet
     * to build the appointment from the database
     * as required by the Readable interface
     * @param rs
     */
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

    /**
     * Appointment constructor that builds a new appointment
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param userId
     * @param contactId
     */
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

    /**
     * Get appointment by id
     * @return Appointment ID
     */
    public int getId() {
        return id;
    }

    /**
     * Set appointment id
     * @param appointmentId
     */
    private void setId(int appointmentId) {
        this.id = appointmentId;
    }

    /**
     * Get appointment title
     * @return Appointment title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set appointment title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get appointment description
     * @return Appointment description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set appointment description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get appointment location
     * @return Appointment location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set appointment location
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get appointment type
     * @return Appointment type
     */
    public String getType() {
        return type;
    }

    /**
     * Set appointment type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get appointment start time
     * @return Appointment start time
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * Set appointment start time
     * @param start
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * Get appointment start time from UTC
     * @return Appointment end time
     */
    public Timestamp getStartUTC() {
        return TimeUtils.toUTC(getStart(), ApplicationController.zone);
    }

    /**
     * Set appointment start time to UTC
     * @param start
     */
    public void setStartUTC(Timestamp start) {
        setStart(TimeUtils.fromUTC(start, ApplicationController.zone));
    }

    /**
     * Get appointment end time
     * @return Appointment end time
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * Set appointment end time
     * @param end
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * Get appointment end time in UTC
     * @return Appointment end time
     */
    public Timestamp getEndUTC() {
        return TimeUtils.toUTC(getEnd(), ApplicationController.zone);
    }

    /**
     * Set appointment end time from UTC
     * @param end
     */
    public void setEndUTC(Timestamp end) {
        setEnd(TimeUtils.fromUTC(end, ApplicationController.zone));
    }

    /**
     * Get appointment customer id
     * @return Appointment customer id
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Set appointment customer id
     * @param customerId
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Get appointment customer
     * @return Appointment customer
     */
    public Customer getCustomer() {
        return Customer.find(getCustomerId());
    }

    /**
     * Get appointment user id
     * @return Appointment user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Set appointment user id
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Get appointment user
     * @return Appointment user
     */
    public User getUser() {
        return User.find(getUserId());
    }

    /**
     * Get appointment contact id
     * @return Appointment contact id
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Set appointment contact id
     * @param contactId
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Get appointment contact
     * @return Appointment contact
     */
    public Contact getContact() {
        return Contact.find(getContactId());
    }

    // Override methods

    /**
     * Get appointment id SQL column name
     * @return
     */
    @Override
    public String getIdColumnName() {
        return "Appointment_ID";
    }

    /**
     * Get appointment SQL table name
     * @return
     */
    @Override
    public String getTableName() {
        return "appointments";
    }

    /**
     * Get appointment SQL column names and a map to their values
     * @return
     */
    @Override
    public Map<String, Supplier<Object>> getDatabaseMap() {
        return Map.of(
                "Title", this::getTitle,
                "Description", this::getDescription,
                "Location", this::getLocation,
                "Type", this::getType,
                // Convert to UTC
                "Start", this::getStartUTC,
                // Convert to UTC
                "End", this::getEndUTC,
                "Customer_ID", this::getCustomerId,
                "User_ID", this::getUserId,
                "Contact_ID", this::getContactId
        );
    }

    /**
     * Validate the appointment before saving
     * Throws an exception if the appointment is invalid
     * Type of exception depends on the validation error
     * @throws SQLException | AppointmentValidationException
     */
    @Override
    public void save() throws Exception {
        if (validate()) {
            Writable.super.save();
        }
    }

    // Instance methods

    /**
     * Checks the appointment against the different validation methods
     * And throws an appropriate exception if the appointment is invalid
     * @return true if the appointment is valid
     * @throws AppointmentValidationException
     */
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

    /**
     * Checks if the appointment overlaps with another appointment
     * @param other Appointment to check against
     * @return true if the appointments overlap
     */
    public boolean isOverlapping(Appointment other) {
        if (other.getId() == getId()) {
            return false;
        }
        return getStart().before(other.getEnd()) && getEnd().after(other.getStart());
    }

    /**
     * Checks if the appointment overlaps with any of the appointments in the list
     * @param others List of appointments to check against
     * @return true if the appointments overlap
     */
    public boolean isOverlapping(List<Appointment> others) {
        return others.stream().anyMatch(this::isOverlapping);
    }

    /**
     * Checks if the appointment overlaps with any of the appointments in the database
     * @return true if the appointments overlap
     */
    public boolean isOverlapping() {
        return isOverlapping(getAll());
    }

    /**
     * Checks if the appointment is on a weekend
     * @return true if the appointment is on a weekend
     */
    public boolean isOnWeekend() {
        return TimeUtils.isWeekend(getStart(), getEnd());
    }

    /**
     * Checks if the appointment is outside of business hours
     * Converts the appointment start and end times to EST
     * @return true if the appointment is outside of business hours
     */
    public boolean isOutsideBusinessHours() {
        Timestamp start = TimeUtils.toEST(getStart(), ApplicationController.zone);
        Timestamp end = TimeUtils.toEST(getEnd(), ApplicationController.zone);
        int open = ApplicationController.businessHours[0];
        int close = ApplicationController.businessHours[1];
        System.out.println(start + " " + end + " " + open + " " + close + " " + getStart() + " " + getEnd());
        return TimeUtils.isOutsideBusinessHours(start, end, open, close);
    }

    /**
     * Checks if the appointment is within 15 minutes
     * @return true if the appointment is within 15 minutes
     */
    public boolean isWithin15Minutes() {
        return TimeUtils.isWithinTheNext(15, getStart());
    }

    // Static methods

    /**
     * Find appointment by id
     * @param id Appointment id
     * @return Appointment
     * @throws NoSuchElementException
     */
    public static Appointment find(int id) throws NoSuchElementException {
        String sql = "SELECT * FROM appointments WHERE Appointment_ID = ?";
        return JDBC.getFirstFromQuery(sql, List.of(id), Appointment.class);
    }

    /**
     * Get all appointments
     * @return List of appointments
     * @throws NoSuchElementException
     */
    public static List<Appointment> getAll() throws NoSuchElementException {
        String sql = "SELECT * FROM appointments";
        return JDBC.getAllFromQuery(sql, null, Appointment.class);
    }

    /**
     * Get all appointments for a contact
     * @param contact Contact to get appointments for
     * @return List of appointments
     * @throws NoSuchElementException
     */
    public static List<Appointment> getAllForContact(Contact contact) throws NoSuchElementException {
        String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
        return JDBC.getAllFromQuery(sql, List.of(contact.getId()), Appointment.class);
    }

    /**
     * Get all appointments for a customer
     * @param customer Customer to get appointments for
     * @return List of appointments
     * @throws NoSuchElementException
     */
    public static List<Appointment> getAllForCustomer(Customer customer) throws NoSuchElementException {
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        return JDBC.getAllFromQuery(sql, List.of(customer.getId()), Appointment.class);
    }

    /**
     * Get all appointments that have a title that contains the given title
     * @param title Title to search for
     * @param forCustomer Customer to get appointments for (Optional)
     * @return List of appointments
     * @throws NoSuchElementException
     */
    public static List<Appointment> getAllForTitle(String title, Customer forCustomer) throws NoSuchElementException {
        String sql = "SELECT * FROM appointments WHERE Title LIKE ?";
        List<Object> params = List.of("%" + title + "%");
        if (forCustomer != null) {
            sql += " AND Customer_ID = ?";
            params.add(forCustomer.getId());
        }
        return JDBC.getAllFromQuery(sql, params, Appointment.class);
    }

    /**
     * Get all appointments for a month
     * @param month Month to get appointments for
     * @return List of appointments
     * @throws NoSuchElementException
     */
    public static List<Appointment> getAllForMonth(int month) throws NoSuchElementException {
        String sql = "SELECT * FROM appointments WHERE MONTH(Start) = ? OR MONTH(End) = ?";
        return JDBC.getAllFromQuery(sql, List.of(month, month), Appointment.class);
    }

    /**
     * Get all appointments for a week
     * @param selectedWeek Week to get appointments for
     * @return List of appointments
     */
    public static List<Appointment> getAllForWeek(int selectedWeek) {
        String sql = "SELECT * FROM appointments WHERE WEEK(Start) = ? OR WEEK(End) = ?";
        return JDBC.getAllFromQuery(sql, List.of(selectedWeek, selectedWeek), Appointment.class);
    }

    /**
     * Get all appointments for a user
     * @param user User to get appointments for
     * @return List of appointments
     * @throws NoSuchElementException
     */
    public static List<Appointment> getAllForUser(User user) throws NoSuchElementException {
        String sql = "SELECT * FROM appointments WHERE User_ID = ?";
        return JDBC.getAllFromQuery(sql, List.of(user.getId()), Appointment.class);
    }

    /**
     * Get all types of appointments
     * @return List of types
     * @throws NoSuchElementException
     */
    public static List<String> getAllTypes() throws NoSuchElementException {
        String sql = "SELECT DISTINCT Type FROM appointments";
        return JDBC.getAllFromQuery(sql, null, Type.class).stream().map(Type::toString).toList();
    }

    /**
     * Generate a report of the number of appointments by type and month
     * @return List of maps used to build the report
     * @throws SQLException
     */
    public static List<Map<String, Object>> getMonthlyReport() throws SQLException {
        String sql = "SELECT MONTHNAME(Start) AS Month, Type, COUNT(*) AS Count\n" +
                "FROM appointments\n" +
                "GROUP BY MONTHNAME(Start), Type\n" +
                "ORDER BY MONTHNAME(Start), Type;";
        return JDBC.getResultSetFromQuery(sql, null);
    }

    /**
     * Generate a report of the number of appointments by customer
     * @param contact
     * @return List of maps used to build the report
     * @throws NoSuchElementException
     */
    public static List<Map<String, Object>> getContactReport(Contact contact) throws NoSuchElementException {
        List<Appointment> appointments = Appointment.getAllForContact(contact);
        return convertAppointmentToMap(appointments);
    }

    /**
     * Generate a report of the number of appointments by customer
     * @param customer
     * @return List of maps used to build the report
     * @throws NoSuchElementException
     */
    public static List<Map<String, Object>> getCustomerReport(Customer customer) throws NoSuchElementException {
        List<Appointment> appointments = Appointment.getAllForCustomer(customer);
        return convertAppointmentToMap(appointments);
    }

    /**
     * Generator to turn the Appointment into a map
     * @param appointments
     * @return List of maps used to build the report
     * @throws NoSuchElementException
     */
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

    /**
     * Type class for appointments
     * Allows us to query only types of appointments
     */
    public static class Type implements Readable {
        private String type;

        /**
         * Constructor to create a type from a SQL results
         * @param rs
         */
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

    /**
     * Exception class for appointment validation
     */
    public static class AppointmentValidationException extends Exception {
        public AppointmentValidationException(String message) {
            super(message == null ? "Appointment validation failed" : message);
        }
    }

    /**
     * Exception class for on weekend validation
     */
    public static class OnWeekendException extends AppointmentValidationException {
        public OnWeekendException() {
            super("Appointment cannot be on a weekend");
        }
    }

    /**
     * Exception class for outside business hours validation
     */
    public static class OutsideBusinessHoursException extends AppointmentValidationException {
        public OutsideBusinessHoursException() {
            super("Appointment cannot be outside of business hours");
        }
    }

    /**
     * Exception class for overlapping appointment validation
     */
    public static class OverlappingAppointmentException extends AppointmentValidationException {
        public OverlappingAppointmentException() {
            super("Appointment cannot overlap with another appointment");
        }
    }
}
