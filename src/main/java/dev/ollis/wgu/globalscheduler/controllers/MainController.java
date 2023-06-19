package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.globalscheduler.models.Appointment;
import dev.ollis.wgu.globalscheduler.models.Contact;
import dev.ollis.wgu.globalscheduler.models.Customer;
import dev.ollis.wgu.globalscheduler.models.User;
import dev.ollis.wgu.helper.Popup;
import dev.ollis.wgu.helper.TimeUtils;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public class MainController implements Initializable, Viewable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Appointment appointment = hasAppointmentWithin15Minutes();
        if (appointment != null) {
            String message = "You have an appointment";
            message += "(id: " + appointment.getId() + ")";
            message += " with " + appointment.getCustomer().getName();
            message += " at " + TimeUtils.getTime(appointment.getStart());
            message += " on " + TimeUtils.getDate(appointment.getStart()) + ".";
            Popup.info("Appointment", message);
        } else {
            Popup.info("Appointment", "You have no appointments within 15 minutes.");
        }
    }

    public Appointment hasAppointmentWithin15Minutes() {
        List<Appointment> appointments = Appointment.getAllForUser(User.getCurrentUser());
        for (Appointment appointment : appointments) {
            if (appointment.isWithin15Minutes()) {
                return appointment;
            }
        }
        return null;
    }

    // JavaFX event handlers

    public void on_customers(MouseEvent mouseEvent) {
        new CustomersController().show();
    }

    public void on_monthly_reports(MouseEvent mouseEvent) {
        new ReportsController().show((view) -> {
            ReportsController controller = (ReportsController) view;
            try {
                controller.buildReport(Appointment.getMonthlyReport(), "Monthly Report");
            } catch (SQLException e) {
                Popup.error("Error", "There was an error building the report.");
            }
        });
    }

    public void on_contact_reports(MouseEvent mouseEvent) {
        Popup.showComboBoxDialog(Contact.getAll(), "Select a contact", "Contact", (contact) -> {
            new ReportsController().show((view) -> {
                ReportsController controller = (ReportsController) view;
                try {
                    controller.buildReport(Appointment.getContactReport(contact), "Contact Report");
                } catch (NoSuchElementException e) {
                    Popup.error("Error", "No appointment exist for this contact.");
                }
            });
        });
    }

    public void on_customer_reports(MouseEvent mouseEvent) {
        Popup.showComboBoxDialog(Customer.getAll(), "Select a customer", "Customer", (customer) -> {
            new ReportsController().show((view) -> {
                ReportsController controller = (ReportsController) view;
                try {
                    controller.buildReport(Appointment.getCustomerReport(customer), "Customer Report");
                } catch (NoSuchElementException e) {
                    Popup.error("Error", "No appointment exist for this customer.");
                }
            });
        });
    }

    public void on_exit(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
