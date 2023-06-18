package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.globalscheduler.models.*;
import dev.ollis.wgu.helper.Popup;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentFormController implements Initializable, Viewable, Refreshable {

    public Text window_title;
    public TextField input_id;
    public TextField input_title;
    public TextField input_description;
    public ComboBox<String> combo_location;
    public ComboBox<String> combo_type;

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

    private AppointmentsController parentController;
    private Appointment appointment;

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

    @Override
    public void refresh() {
        List<Customer> customers = Customer.getAll();
        combo_customer.setItems(FXCollections.observableArrayList(customers));
        combo_customer.setValue(customers.get(customers.size()-1));
    }

    public void setAppointment(Appointment appointment) {
        this.window_title.setText("Edit Appointment");
        this.appointment = appointment;

        input_id.setText(String.valueOf(appointment.getId()));
        input_title.setText(appointment.getTitle());
        input_description.setText(appointment.getDescription());
        combo_location.setValue(appointment.getLocation());
        combo_type.setValue(appointment.getType());
        combo_customer.setValue(Customer.find(appointment.getCustomerId()));
        combo_contact.setValue(Contact.find(appointment.getContactId()));
        combo_user.setValue(User.find(appointment.getUserId()));

        date_start.setValue(appointment.getStart().toLocalDateTime().toLocalDate());
        date_end.setValue(appointment.getEnd().toLocalDateTime().toLocalDate());

        choice_hour_start.setValue(appointment.getStart().toLocalDateTime().getHour());
        choice_minute_start.setValue(appointment.getStart().toLocalDateTime().getMinute());
        choice_hour_end.setValue(appointment.getEnd().toLocalDateTime().getHour());
        choice_minute_end.setValue(appointment.getEnd().toLocalDateTime().getMinute());
    }

    public void setParentController(AppointmentsController parentController) {
        this.parentController = parentController;
    }

    public void getParentController(AppointmentsController parentController) {
        this.parentController = parentController;
    }

    public Timestamp getStart() {
        return Timestamp.valueOf(date_start.getValue().atTime(choice_hour_start.getValue(), choice_minute_start.getValue()));
    }

    public Timestamp getEnd() {
        return Timestamp.valueOf(date_end.getValue().atTime(choice_hour_end.getValue(), choice_minute_end.getValue()));
    }

    // JavaFX event handlers

    public void on_save(MouseEvent mouseEvent) {
        // Nothing can be blank
        if (input_title.getText().isBlank() ||
                input_description.getText().isBlank() ||
                combo_location.getValue() == null ||
                combo_type.getValue() == null ||
                combo_customer.getValue() == null ||
                combo_contact.getValue() == null ||
                combo_user.getValue() == null ||
                date_start.getValue() == null ||
                date_end.getValue() == null ||
                choice_hour_start.getValue() == null ||
                choice_minute_start.getValue() == null ||
                choice_hour_end.getValue() == null ||
                choice_minute_end.getValue() == null) {
            Popup.error("Error saving appointment", "All fields must be filled in");
            return;
        }

        if (appointment == null) {
            appointment = new Appointment(
                    input_title.getText(),
                    input_description.getText(),
                    combo_location.getValue(),
                    combo_type.getValue(),
                    getStart(),
                    getEnd(),
                    combo_customer.getValue().getId(),
                    combo_user.getValue().getId(),
                    combo_contact.getValue().getId()
            );
        } else {
            appointment.setTitle(input_title.getText());
            appointment.setDescription(input_description.getText());
            appointment.setLocation(combo_location.getValue());
            appointment.setType(combo_type.getValue());
            appointment.setStart(getStart());
            appointment.setEnd(getEnd());
            appointment.setCustomerId(combo_customer.getValue().getId());
            appointment.setUserId(combo_user.getValue().getId());
            appointment.setContactId(combo_contact.getValue().getId());
        }

        try {
            appointment.save();
        } catch (SQLException e) {
            Popup.error("Error saving appointment", e.getMessage());
        }

        parentController.refreshTable();
        close();
    }

    public void on_cancel(MouseEvent mouseEvent) {
        close();
    }

    public void on_new_customer(MouseEvent mouseEvent) {
        new CustomerFormController().show((view) -> {
            CustomerFormController controller = (CustomerFormController) view;
            controller.setParentView(this);
        });
    }
}
