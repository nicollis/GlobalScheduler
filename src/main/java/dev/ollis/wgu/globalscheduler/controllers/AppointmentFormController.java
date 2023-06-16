package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.globalscheduler.models.*;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentFormController implements Initializable, Viewable {
    public TextField input_id;
    public TextField input_title;
    public TextField input_description;
    public ComboBox<String> combo_location;
    public ComboBox<Appointment.Type> combo_type;

    public DatePicker date_start;
    public DatePicker date_end;

    public ComboBox<Customer> combo_customer;
    public ComboBox<Contact> combo_contact;
    public ComboBox<User> combo_user;

    public Button btn_save;
    public Button btn_cancel;
    public Button btn_new_customer;
    public ChoiceBox<Integer> choice_hour_start;
    public ChoiceBox<Integer> choice_minute_start;
    public ChoiceBox<Integer> choice_hour_end;
    public ChoiceBox<Integer> choice_minute_end;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choice_hour_start.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8,
                9,10,11,12,13,14,15,16,17,18,19,20,21,22,23));
        choice_minute_start.setItems(FXCollections.observableArrayList(0, 15, 30, 45));
        choice_hour_end.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8,
                9,10,11,12,13,14,15,16,17,18,19,20,21,22,23));
        choice_minute_end.setItems(FXCollections.observableArrayList(0, 15, 30, 45));

        combo_location.setItems(FXCollections.observableArrayList(Location.getInstance().getLocations()));
        combo_type.setItems(FXCollections.observableArrayList(Appointment.getAllTypes()));

        combo_customer.setItems(FXCollections.observableArrayList(Customer.getAll()));
        combo_contact.setItems(FXCollections.observableArrayList(Contact.getAll()));
        combo_user.setItems(FXCollections.observableArrayList(User.getAll()));

        combo_user.setValue(User.getCurrentUser());
    }

    @Override
    public String getFxmlFileName() {
        return "appointment-form-view.fxml";
    }

    @Override
    public Node windowElement() {
        return input_id;
    }

    // JavaFX event handlers

    public void on_save(MouseEvent mouseEvent) {
    }

    public void on_cancel(MouseEvent mouseEvent) {
        close();
    }

    public void on_new_customer(MouseEvent mouseEvent) {
    }
}
