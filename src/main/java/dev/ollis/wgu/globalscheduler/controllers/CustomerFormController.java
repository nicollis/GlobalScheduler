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
import java.util.ResourceBundle;

/** 
 * This is the controller for the customer form view. It is used for both creating and editing customers.
 * It implements the Initializable interface so that it can be used as a controller for an FXML view.
 * It also implements the Viewable interface so it has helpers for showing and closing the view.
 */
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

    /**
     * This method is used to set the parent view. It is used to refresh the parent view after saving a customer.
     * @param parentView
     */
    public void setParentView(Refreshable parentView) {
        this.parentView = parentView;
    }

    /**
     * This method is used to set the customer to edit. It is used when editing a customer.
     * @param customer
     */
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

    /**
     * This method is called when the view is initialized. It is used to populate the country and division dropdowns.
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resourceBundle
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the country dropdown
        ObservableList<Country> countries = FXCollections.observableList(Country.getAll());
        dropdown_country.setItems(countries);

        // Populate the division dropdown
        ObservableList<Division> divisions = FXCollections.observableList(Division.getAll());
        dropdown_division.setItems(divisions);
    }

    /**
     * This method is called when the save button is clicked. It is used to save the customer.
     * It also provides validation that nothing is empty
     * @param mouseEvent
     */
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

        // Handles if we are creating a new customer or editing an existing one
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
            // refreshes the parent view
            parentView.refresh();
            close();
        } catch (Exception e) {
            Popup.error("Error saving customer", e.getMessage());
        }
    }

    /**
     * This method is called when the cancel button is clicked. It is used to close the view.
     * @param mouseEvent
     */
    public void on_cancel(MouseEvent mouseEvent) {
        close();
    }

    /**
     * This method is called when a country is selected. It is used to populate the division dropdown.
     * @param event
     */
    public void on_country_selected(ActionEvent event) {
        Country selected_country = dropdown_country.getValue();
        ObservableList<Division> divisions = FXCollections.observableList(Division.getAllByCountry(selected_country));
        dropdown_division.setItems(divisions);
        dropdown_division.setValue(null);
        dropdown_division.setDisable(false);
    }
}
