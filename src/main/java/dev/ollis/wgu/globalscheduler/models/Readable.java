package dev.ollis.wgu.globalscheduler.models;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Readable {
    static <T extends Readable> T fromResultSet(ResultSet rs, Class<T> type) throws SQLException {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor(ResultSet.class);
            return constructor.newInstance(rs);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new SQLException("Unable to construct object from ResultSet", e);
        }
    }
}