package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.globalscheduler.models.Country;
import dev.ollis.wgu.globalscheduler.models.Customer;
import dev.ollis.wgu.globalscheduler.models.Division;
import dev.ollis.wgu.helper.Popup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class CustomersController implements Initializable, Viewable, Refreshable {

    public TableView<Customer> table_view;
    public TableColumn<Customer, Integer> col_id;
    public TableColumn<Customer, String> col_name;
    public TableColumn<Customer, String> col_address;
    public TableColumn<Customer, String> col_postalCode;
    public TableColumn<Customer, String> col_phone;
    public TextField input_customer_search;

    public TableColumn<Customer, Division> col_division;
    public TableColumn<Customer, Country> col_country;

    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_division.setCellValueFactory(new PropertyValueFactory<>("division"));
        col_country.setCellValueFactory(new PropertyValueFactory<>("country"));

        refresh();
    }
    @Override
    public String getFxmlFileName() {
        return "customers-view.fxml";
    }

    @Override
    public Node windowElement() {
        return table_view;
    }

    public void on_search_input_changed(KeyEvent inputMethodEvent) {
        String value = input_customer_search.getText();
        ObservableList<Customer> customers;

        try {
            if (value.isBlank()) {
                customers = FXCollections.observableList(Customer.getAll());
            } else if (value.matches("\\d+")) {
                customers = FXCollections.observableList(List.of(Customer.find(Integer.parseInt(value))));
            } else {
                customers = FXCollections.observableList(Customer.getAllByName(value));
            }
        } catch (NoSuchElementException e) {
            Popup.error("No Results", "No Customers found for the given search term.");
            input_customer_search.setText("");
            customers = FXCollections.observableList(Customer.getAll());
        }
        table_view.setItems(customers);
    }

    public void on_modify(MouseEvent mouseEvent) {
        Customer customer = table_view.getSelectionModel().getSelectedItem();
        if (customer == null) {
            Popup.error("No Customer Selected", "Please select a customer to modify.");
            return;
        }
        new CustomerFormController().show((formView) -> {
            CustomerFormController view = (CustomerFormController) formView;
            view.setCustomer(customer);
            view.setParentView(this);
        });
    }

    public void on_delete(MouseEvent mouseEvent) {
        Customer customer = table_view.getSelectionModel().getSelectedItem();
        if (customer == null) {
            Popup.error("No Customer Selected", "Please select a customer to delete.");
            return;
        }

        Popup.confirm("Delete Customer", "Are you sure you want to delete this customer?", () -> {
            try {
                customer.delete();
            } catch (SQLException e) {
                Popup.error("Error Deleting Customer", "There was an error deleting the customer.");
            }
            refresh();
            Popup.info("Customer Deleted", "The customer was deleted successfully.");
        });
    }

    public void on_add(MouseEvent mouseEvent) {
        new CustomerFormController().show((formView) -> {
            CustomerFormController view = (CustomerFormController) formView;
            view.setParentView(this);
        });
    }

    public void on_closed(MouseEvent mouseEvent) {
        close();
    }

    @Override
    public void refresh() {
        ObservableList<Customer> customers = FXCollections.observableList(Customer.getAll());
        table_view.setItems(customers);
    }

    public void on_appointments(MouseEvent mouseEvent) {
        Customer customer = table_view.getSelectionModel().getSelectedItem();
        if (customer == null) {
            Popup.error("No Customer Selected", "Please select a customer to modify.");
            return;
        }

        try {
            new AppointmentsController().show((view) -> {
                AppointmentsController appointmentsController = (AppointmentsController) view;
                appointmentsController.setCustomer(customer);
            });
        } catch (NoSuchElementException e) {
            Popup.error("No appointments", "There was no appointment found for the selected customer.");
            // TODO: make this a prompt to create an appointment if desired
        }
    }
}
