package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.globalscheduler.ApplicationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public interface Viewable {
    String getFxmlFileName();
    Node windowElement();

    default void show() {
        openView(getFxmlFileName(), null);
    }

    default <T> void show(Consumer<T> view) {
        openView(getFxmlFileName(), view);
    }

    default void close() {
        Stage stage = (Stage) windowElement().getScene().getWindow();
        stage.close();
    }
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
