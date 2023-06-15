package dev.ollis.wgu.globalscheduler.controllers;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MainView implements Viewable {
    public Button btn_customers;
    public AnchorPane appointment_view;

    @Override
    public String getFxmlFileName() {
        return "main-view.fxml";
    }

    @Override
    public Node windowElement() {
        return btn_customers;
    }

    // JavaFX event handlers

    public void on_customers(MouseEvent mouseEvent) {
        new CustomersView().show();
    }

    public void on_exit(MouseEvent mouseEvent) {
        close();
    }
}
