package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.globalscheduler.models.Country;
import dev.ollis.wgu.globalscheduler.models.Customer;
import dev.ollis.wgu.globalscheduler.models.Division;
import dev.ollis.wgu.helper.Popup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable, Viewable {
    public TextField input_id;
    public TextField input_name;
    public TextField input_address;
    public TextField input_postalCode;
    public TextField input_phone;
    public ComboBox<Country> dropdown_country;
    public ComboBox<Division> dropdown_division;

    public Customer customer;
    public Text title;
    private Refreshable parentView;

    public void setParentView(Refreshable parentView) {
        this.parentView = parentView;
    }

    public void setCustomer(Customer customer) {
        title.setText("Edit Customer");
        this.customer = customer;
        input_id.setText(String.valueOf(customer.getId()));
        input_name.setText(customer.getName());
        input_address.setText(customer.getAddress());
        input_postalCode.setText(customer.getPostalCode());
        input_phone.setText(customer.getPhone());
        dropdown_country.setValue(customer.getDivision().getCountry());
        dropdown_division.setValue(customer.getDivision());
        dropdown_division.setDisable(false);
    }

    @Override
    public String getFxmlFileName() {
        return "customer-form-view.fxml";
    }

    @Override
    public Node windowElement() {
        return input_id;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the country dropdown
        ObservableList<Country> countries = FXCollections.observableList(Country.getAll());
        dropdown_country.setItems(countries);

        // Populate the division dropdown
        ObservableList<Division> divisions = FXCollections.observableList(Division.getAll());
        dropdown_division.setItems(divisions);
    }

    public void on_save(MouseEvent mouseEvent) {
        String name = input_name.getText();
        String address = input_address.getText();
        String postalCode = input_postalCode.getText();
        String phone = input_phone.getText();
        Division division = dropdown_division.getValue();

        if (name == null || name.isEmpty() ||
                address == null || address.isEmpty() ||
                postalCode == null || postalCode.isEmpty() ||
                phone == null || phone.isEmpty() ||
                division == null) {
            Popup.error("Error saving customer", "All fields are required!");
            return;
        }

        if (customer == null) {
            customer = new Customer(name, address, postalCode, phone, division.getId());
        } else {
            customer.setName(name);
            customer.setAddress(address);
            customer.setPostalCode(postalCode);
            customer.setPhone(phone);
            customer.setDivisionId(division.getId());
        }

        try {
            customer.save();
            parentView.refresh();
            close();
        } catch (SQLException e) {
            Popup.error("Error saving customer", e.getMessage());
        }
    }

    public void on_cancel(MouseEvent mouseEvent) {
        close();
    }

    public void on_country_selected(ActionEvent event) {
        Country selected_country = dropdown_country.getValue();
        ObservableList<Division> divisions = FXCollections.observableList(Division.getAllByCountry(selected_country));
        dropdown_division.setItems(divisions);
        dropdown_division.setValue(null);
        dropdown_division.setDisable(false);
    }
}
