package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.globalscheduler.ApplicationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Interface that allows for easy opening of new windows.
 * As well as closing the current window.
 */
public interface Viewable {
    /**
     * Returns the name of the fxml file that should be loaded.
     * @return the name of the fxml file that should be loaded.
     */
    String getFxmlFileName();

    /**
     * Returns an element of the window, used by the close method.
     * @return an element of the window.
     */
    Node windowElement();

    /**
     * Opens the window.
     */
    default void show() {
        openView(getFxmlFileName(), null);
    }

    /**
     * Opens the window and passes the controller to the view consumer.
     * This allows the consumer to set values on the controller before the window is shown.
     * @param view the consumer that will receive the controller.
     * @param <T> the type of the controller.
     */
    default <T> void show(Consumer<T> view) {
        openView(getFxmlFileName(), view);
    }

    /**
     * Closes the window.
     */
    default void close() {
        Stage stage = (Stage) windowElement().getScene().getWindow();
        stage.close();
    }

    /**
     * Opens a new window.
     * Not typically used due to the show helper methods.
     * @param fxmlFile the name of the fxml file to load.
     * @param view the consumer that will receive the controller.
     * @param <T> the type of the controller.
     */
    static <T> void openView(String fxmlFile, Consumer<T> view) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationController.class.getResource(fxmlFile));
            Scene scene = new Scene(loader.load());

            if (view != null) {
                T controller = loader.getController();
                view.accept(controller);
            }

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
