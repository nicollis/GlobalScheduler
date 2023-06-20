package dev.ollis.wgu.globalscheduler.controllers;

/**
 * Interface for controllers that need to be refreshed
 */
public interface Refreshable {
    /**
     * Provides a hook for refreshing elements on the controller
     */
    void refresh();
}
