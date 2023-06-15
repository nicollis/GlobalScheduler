package dev.ollis.wgu.globalscheduler.models;

import dev.ollis.wgu.helper.JDBC;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface Writable {
    int getId();
    String getIdColumnName();
    String getTableName();
    Map<String, Supplier<Object>> getDatabaseMap();

    default void save() throws SQLException {
        if (getId() == 0) {
            insert(this);
        } else {
            update(this);
        }
    }

    default void delete() throws SQLException {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        JDBC.saveObject(sql, List.of(getId()));
    }

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
