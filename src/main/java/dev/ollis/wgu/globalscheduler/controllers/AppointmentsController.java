package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.globalscheduler.models.Appointment;
import dev.ollis.wgu.globalscheduler.models.Contact;
import dev.ollis.wgu.globalscheduler.models.Customer;
import dev.ollis.wgu.globalscheduler.models.User;
import dev.ollis.wgu.helper.Popup;
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
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public class AppointmentsController implements Initializable, Viewable {
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

    public Customer customer;

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
        return table_view;
    }

    public void refreshTable() {
        List<Appointment> appointments = null;
        if (customer == null) {
            appointments = Appointment.getAll();
        } else {
            appointments = Appointment.getAllForCustomer(customer);
        }
        table_view.setItems(FXCollections.observableList(appointments));
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.btn_close.setVisible(true);
        this.input_search.setVisible(false);
        refreshTable();
    }

    // JavaFX Event Handlers

    public void on_search_input_changed(KeyEvent keyEvent) {
        String value = input_search.getText();
        ObservableList<Appointment> appointments;

        try {
            if (value.isBlank()) {
                refreshTable();
                return;
            } else if (value.matches("\\d+")) {
                appointments = FXCollections.observableList(
                        List.of(Appointment.find(Integer.parseInt(value))));
            } else {
                appointments = FXCollections.observableList(Appointment.getAllForTitle(value, customer));
            }
        } catch (NoSuchElementException e) {
            Popup.error("No Results", "No Customers found for the given search term.");
            input_search.setText("");
            refreshTable();
            return;
        }
        table_view.setItems(appointments);
    }

    public void on_modify(MouseEvent mouseEvent) {
        Appointment appointment = table_view.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            Popup.error("No Selection", "Please select an appointment to modify.");
            return;
        }
        new AppointmentFormController().show((view) -> {
            AppointmentFormController controller = (AppointmentFormController) view;
            controller.setParentController(this);
            controller.setAppointment(appointment);
        });
    }

    public void on_delete(MouseEvent mouseEvent) {
        Appointment appointment = table_view.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            Popup.error("No Selection", "Please select an appointment to delete.");
            return;
        }
        Popup.confirm("Delete Appointment", "Are you sure you want to delete this appointment?", () -> {
            try {
                appointment.delete();
                refreshTable();
            } catch (SQLException e) {
                Popup.error("Error", "There was an error deleting the appointment.");
                return;
            }
            Popup.info("Success", "Appointment " + appointment.getId() + " of type " + appointment.getType() + " deleted successfully.");
        });
    }

    public void on_add(MouseEvent mouseEvent) {
        new AppointmentFormController().show((view) -> {
            AppointmentFormController controller = (AppointmentFormController) view;
            controller.setParentController(this);
        });
    }

    public void on_closed(MouseEvent mouseEvent) {
        close();
    }

}
