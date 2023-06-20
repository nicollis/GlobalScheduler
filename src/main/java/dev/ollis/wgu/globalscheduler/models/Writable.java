package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Interface for objects that can be saved to the database.
 * Provides save() and delete() methods.
 * <p>
 * Provides database Insert, Update and Delete methods.
 */
public interface Writable {
    /**
     * Get the ID of the object.
     * @return int
     */
    int getId();

    /**
     * Get the name of the ID column in the database.
     * @return String
     */
    String getIdColumnName();

    /**
     * Get the name of the table in the database.
     * @return String
     */
    String getTableName();

    /**
     * Get a map of column names to values.
     * @return Map<String, Supplier<Object>>
     */
    Map<String, Supplier<Object>> getDatabaseMap();

    /**
     * Save the object to the database.
     * If the ID is 0, insert the object.
     * Otherwise, update the object.
     * @throws Exception
     */
    default void save() throws Exception {
        if (getId() == 0) {
            insert(this);
        } else {
            update(this);
        }
    }

    /**
     * Delete the object from the database.
     * @throws SQLException
     */
    default void delete() throws SQLException {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        JDBC.saveObject(sql, List.of(getId()));
    }

    /**
     * Insert the object into the database.
     * Writes the insert query based on the object's database map.
     * @param object The object to insert.
     * @param <T> The type of the object.
     * @throws SQLException If the SQL query fails.
     */
    static <T extends Writable> void insert(T object) throws SQLException {
        Map<String, Supplier<Object>> map = object.getDatabaseMap();
        String columnNames = String.join(", ", map.keySet());
        String placeholders = map.keySet().stream().map(k -> "?").collect(Collectors.joining(", "));
        List<Object> params = map.values().stream()
                .map(Supplier::get)
                .toList();

        String sql = "INSERT INTO " + object.getTableName() + " (" + columnNames + ") VALUES (" + placeholders + ")";

        JDBC.saveObject(sql, params);
    }

    /**
     * Update the object in the database.
     * Writes the update query based on the object's database map.
     * @param object The object to update.
     * @param <T> The type of the object.
     * @throws SQLException If the SQL query fails.
     */
    static <T extends Writable> void update(T object) throws SQLException {
        Map<String, Supplier<Object>> map = object.getDatabaseMap();

        String setClause = map.keySet().stream()
                .map(key -> key + " = ?")
                .collect(Collectors.joining(", "));

        String whereClause = object.getIdColumnName() + " = ?";

        List<Object> params = map.values().stream()
                .map(Supplier::get)
                .collect(Collectors.toList());

        params.add(object.getId());

        String sql = "UPDATE " + object.getTableName() + " SET " + setClause + " WHERE " + whereClause;

        JDBC.saveObject(sql, params);
    }

}
