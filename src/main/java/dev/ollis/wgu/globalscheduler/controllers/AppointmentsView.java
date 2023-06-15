package dev.ollis.wgu.globalscheduler.controllers;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class AppointmentsView implements Viewable {
    public Button btn_customers;

    public void on_customers_clicked(MouseEvent mouseEvent) {
        new CustomersView().show();
    }

    @Override
    public String getFxmlFileName() {
        return "appointments-view.fxml";
    }

    @Override
    public Node windowElement() {
        return btn_customers;
    }
}
