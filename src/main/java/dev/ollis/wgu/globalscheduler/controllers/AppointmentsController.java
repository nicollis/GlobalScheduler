package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.globalscheduler.models.Appointment;
import dev.ollis.wgu.globalscheduler.models.Contact;
import dev.ollis.wgu.globalscheduler.models.Customer;
import dev.ollis.wgu.globalscheduler.models.User;
import dev.ollis.wgu.helper.Popup;
import dev.ollis.wgu.helper.TimeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

/**
 * This class represents the controller for the appointments view.
 * It implements the Initializable, Viewable, and Refreshable interfaces.
 */
public class AppointmentsController implements Initializable, Viewable, Refreshable {
    public Button btn_close;
    public Button btn_last;
    public Button btn_next;
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
    public TableColumn<Appointment, Integer> col_user_id;
    public TableColumn<Appointment, Integer> col_customer_id;

    public TextField input_search;
    public TabPane tabs;
    public Text text_view_indicator;

    private Customer customer;

    private Refreshable parentView;

    private int selectedYear = TimeUtils.getCurrentYear();
    private int selectedMonth = 1;
    private int selectedWeek = 1;
    private ViewType selectedView = ViewType.ALL;

    /**
     * This enum represents the different types of views that can be selected.
     */
    public enum ViewType {
        ALL,
        MONTHLY,
        WEEKLY
    }

    /**
     * This method initializes the controller.
     * It sets up the table view with the appropriate data and cell factories.
     * It also sets up the search input field with a listener for filtering the appointments.
     * @param url The URL of the FXML file.
     * @param resourceBundle The resource bundle for the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up the table view with the appropriate data and cell factories
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
        col_user_id.setCellValueFactory(new PropertyValueFactory<>("userId"));
        col_customer_id.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        refresh();

        // Listen to tab events
        tabs.getSelectionModel().selectedItemProperty().addListener((observableValue, oldTab, newTab) -> {
            selectedView = ViewType.valueOf(newTab.getText().toUpperCase());
            switch (selectedView) {
                case ALL -> viewAll();
                case MONTHLY -> viewMonthly();
                case WEEKLY -> viewWeekly();
            }
            refresh();
        });
    }

    @Override
    public String getFxmlFileName() {
        return "appointments-view.fxml";
    }

    @Override
    public Node windowElement() {
        return table_view;
    }

    /**
     * This method refreshes the appointments view.
     */
    @Override
    public void refresh() {
        List<Appointment> appointments = null;
        try {
            if (customer != null) {
                appointments = Appointment.getAllForCustomer(customer);
            } else if (selectedView == ViewType.MONTHLY) {
                appointments = Appointment.getAllForMonth(selectedMonth);
            } else if (selectedView == ViewType.WEEKLY) {
                appointments = Appointment.getAllForWeek(selectedWeek);
            } else {
                appointments = Appointment.getAll();
            }
            table_view.setItems(FXCollections.observableList(appointments));
        } catch (NoSuchElementException e) {
            table_view.getItems().clear();
        }


        if (parentView != null) {
            parentView.refresh();
        }
    }

    /**
     * This method sets the ui to view all appointments.
     */
    public void viewAll() {
        btn_last.setDisable(true);
        btn_next.setDisable(true);
        text_view_indicator.setText("");

    }

    /**
     * This method sets the ui to view appointments for the selected month.
     */
    public void viewMonthly() {
        viewMonthlyOrWeekly();
        text_view_indicator.setText(Month.of(selectedMonth).getDisplayName(TextStyle.FULL, Locale.getDefault()));
    }

    /** 
     * This method sets the ui to view appointments for the selected week.
     */
    public void viewWeekly() {
        viewMonthlyOrWeekly();
        text_view_indicator.setText(TimeUtils.getWeekRangeString(selectedYear, selectedWeek));
    }

    /**
     * This method sets the ui to view appointments for the selected month or week.
     */
    public void viewMonthlyOrWeekly() {
        btn_last.setDisable(false);
        btn_next.setDisable(false);
    }

    /**
     * This method handles setting the UI to view appointments for one customer
     * @param customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
        btn_close.setVisible(true);
        input_search.setVisible(false);
        tabs.setVisible(false);
        btn_last.setVisible(false);
        btn_next.setVisible(false);
        
        refresh();
    }

    /**
     * This method sets the parent view.
     */
    public void setParentView(Refreshable parentView) {
        this.parentView = parentView;
    }

    // JavaFX Event Handlers

    /**
     * This method handles the event when a user is searching for an appointment.
     * @param keyEvent
     */
    public void on_search_input_changed(KeyEvent keyEvent) {
        String value = input_search.getText();
        ObservableList<Appointment> appointments;

        try {
            // Resets if a valid is blank
            if (value.isBlank()) {
                refresh();
                return;
            // Handles searching an ID
            } else if (value.matches("\\d+")) {
                appointments = FXCollections.observableList(
                        List.of(Appointment.find(Integer.parseInt(value))));
            // Handles searching a name
            } else {
                appointments = FXCollections.observableList(Appointment.getAllForTitle(value, customer));
            }
        // When no appointments are found for the given search term an error is displayed
        } catch (NoSuchElementException e) {
            Popup.error("No Results", "No Customers found for the given search term.");
            input_search.setText("");
            refresh();
            return;
        }
        table_view.setItems(appointments);
    }

    /**
     * This method handles the button when users refresh the data
     */
    public void on_refresh(MouseEvent mouseEvent) {
        refresh();
    }

    /**
     * This method handles changing the view to the previous month or week
     * @param mouseEvent
     */
    public void on_last(MouseEvent mouseEvent) {
        if (selectedView == ViewType.MONTHLY) {
            selectedMonth = selectedMonth == 1 ? 12 : selectedMonth - 1;
            text_view_indicator.setText(Month.of(selectedMonth).getDisplayName(TextStyle.FULL, Locale.getDefault()));
        } else if (selectedView == ViewType.WEEKLY) {
            if (selectedWeek == 1) {
                selectedYear--;
                selectedWeek = 52;
            } else {
                selectedWeek--;
            }
            text_view_indicator.setText(TimeUtils.getWeekRangeString(selectedYear, selectedWeek));
        }

        refresh();
    }

    /**
     * This method handles changing the view to the next month or week
     * @param mouseEvent
     */
    public void on_next(MouseEvent mouseEvent) {
        if (selectedView == ViewType.MONTHLY) {
            selectedMonth = selectedMonth == 12 ? 1 : selectedMonth + 1;
            text_view_indicator.setText(Month.of(selectedMonth).getDisplayName(TextStyle.FULL, Locale.getDefault()));
        } else if (selectedView == ViewType.WEEKLY) {
            if (selectedWeek == 52) {
                selectedYear++;
                selectedWeek = 1;
            } else {
                selectedWeek++;
            }
            text_view_indicator.setText(TimeUtils.getWeekRangeString(selectedYear, selectedWeek));
        }

        refresh();
    }

    /**
     * This method handles the even when a user wants to modify an appointment
     * This method uses a lambda to pass the appointment to the form controller
     * after it is created as well as sets the parent controller to this controller.
     * @param mouseEvent
     */
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

    /**
     * This method handles the event when a user wants to delete an appointment.
     * This method uses a lambda to handle the "onOk" event of the confirmation popup.
     * @param mouseEvent
     */
    public void on_delete(MouseEvent mouseEvent) {
        Appointment appointment = table_view.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            Popup.error("No Selection", "Please select an appointment to delete.");
            return;
        }
        
        // Get user confirmation before deleting
        Popup.confirm("Delete Appointment", "Are you sure you want to delete this appointment?", () -> {
            try {
                appointment.delete();
                refresh();
            } catch (SQLException e) {
                Popup.error("Error", "There was an error deleting the appointment.");
                return;
            }
            Popup.info("Success", "Appointment " + appointment.getId() + " of type " + appointment.getType() + " deleted successfully.");
        });
    }

    /**
     * This method handles the event when a user wants to add an appointment.
     * This method uses a lambda to pass the parent controller to the form controller.
     * @param mouseEvent
     */
    public void on_add(MouseEvent mouseEvent) {
        new AppointmentFormController().show((view) -> {
            AppointmentFormController controller = (AppointmentFormController) view;
            controller.setParentController(this);
        });
    }

    /**
     * This method handles the event when a user wants to close the view.
     * @param mouseEvent
     */
    public void on_closed(MouseEvent mouseEvent) {
        close();
    }
}
