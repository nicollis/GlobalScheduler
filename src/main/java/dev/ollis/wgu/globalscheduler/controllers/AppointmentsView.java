package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.globalscheduler.models.Appointment;
import dev.ollis.wgu.globalscheduler.models.Contact;
import dev.ollis.wgu.globalscheduler.models.Customer;
import dev.ollis.wgu.globalscheduler.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentsView implements Initializable, Viewable {
    public Button btn_customers;
    public Button btn_close;
    public TableView<Appointment> table_view;
    public TableColumn<Appointment, Integer> col_id;
    public TableColumn<Appointment, String> col_title;
    public TableColumn<Appointment, String> col_description;
    public TableColumn<Appointment, String> col_location;
    public TableColumn<Appointment, String> col_type;
    public TableColumn<Appointment, String> col_start;
    public TableColumn<Appointment, String> col_end;
    public TableColumn<Appointment, User> col_user;
    public TableColumn<Appointment, Customer> col_customer;
    public TableColumn<Appointment, Contact> col_contact;
    public TextField input_search;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        col_location.setCellValueFactory(new PropertyValueFactory<>("location"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_start.setCellValueFactory(new PropertyValueFactory<>("start"));
        col_end.setCellValueFactory(new PropertyValueFactory<>("end"));
        col_user.setCellValueFactory(new PropertyValueFactory<>("user"));
        col_customer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        col_contact.setCellValueFactory(new PropertyValueFactory<>("contact"));

        refreshTable();
    }

    @Override
    public String getFxmlFileName() {
        return "appointments-view.fxml";
    }

    @Override
    public Node windowElement() {
        return btn_customers;
    }

    public void refreshTable() {
        ObservableList<Appointment> appointments = FXCollections.observableList(Appointment.getAll());
        table_view.setItems(appointments);
    }

    // JavaFX Event Handlers

    public void on_search_input_changed(KeyEvent keyEvent) {
    }

    public void on_modify(MouseEvent mouseEvent) {
    }

    public void on_delete(MouseEvent mouseEvent) {
    }

    public void on_add(MouseEvent mouseEvent) {
    }

    public void on_closed(MouseEvent mouseEvent) {
        close();
    }

}
