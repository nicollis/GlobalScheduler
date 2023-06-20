package dev.ollis.wgu.globalscheduler.models;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface for objects that can be constructed from a ResultSet
 * Although constructors are not inherited, this interface provides a static method
 * that is used by the JDBC to construct objects from a ResultSet
 * <p>
 * Provides Database Read functionality for objects
 */
public interface Readable {
    /**
     * Called by the JDBC helper to construct an object from a ResultSet
     * The object must have a constructor that takes a ResultSet as its only argument
     * @param rs The ResultSet to construct the object from
     * @param type The class of the object to construct
     * @return The constructed object
     * @param <T> The type of the object to construct
     * @throws SQLException If the object cannot be constructed
     */
    static <T extends Readable> T fromResultSet(ResultSet rs, Class<T> type) throws SQLException {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor(ResultSet.class);
            return constructor.newInstance(rs);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new SQLException("Unable to construct object from ResultSet", e);
        }
    }
}