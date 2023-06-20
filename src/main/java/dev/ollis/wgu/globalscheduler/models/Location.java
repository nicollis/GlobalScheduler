package dev.ollis.wgu.globalscheduler.models;

import java.util.List;

/**
 * Singleton class to hold the list of locations
 */
public class Location {
    private static Location instance;

    /**
     * Private constructor to prevent instantiation
     */
    private Location() {
        locations = List.of("Phoenix, Arizona", "White Plains, New York", "Montreal, Canada", "London, England");
    }

    /**
     * Get the instance of the class
     * @return the instance
     */
    public static Location getInstance() {
        if (instance == null) {
            instance = new Location();
        }
        return instance;
    }
    private final List<String> locations;

    /**
     * Get the list of locations
     * @return the list of locations
     */
    public List<String> getLocations() {
        return locations;
    }
}
