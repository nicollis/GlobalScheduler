package dev.ollis.wgu.globalscheduler.models;

import java.util.List;

public class Location {
    private static Location instance;

    private Location() {
        locations = List.of("Phoenix, Arizona", "White Plains, New York", "Montreal, Canada", "London, England");
    }

    public static Location getInstance() {
        if (instance == null) {
            instance = new Location();
        }
        return instance;
    }
    private final List<String> locations;

    public List<String> getLocations() {
        return locations;
    }
}
