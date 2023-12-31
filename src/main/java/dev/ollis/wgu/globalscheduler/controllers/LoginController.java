package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.globalscheduler.ApplicationController;
import dev.ollis.wgu.globalscheduler.models.User;
import dev.ollis.wgu.helper.Logging;
import dev.ollis.wgu.helper.Popup;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.NoSuchElementException;

/**
 * Controller for the login view
 * it implements the Viewable interface to allow for easy loading of the view
 */
public class LoginController implements Viewable {
    public TextField username;
    public PasswordField password;
    public Text application_name;
    public Text label_username;
    public Text label_password;
    public Button btn_login;
    public Button btn_cancel;
    public Text label_location_id;
    public Text label_location;

    /**
     * Initialize the view and set the text based on the language
     */
    public void initialize() {
        application_name.setText(ApplicationController.text.getString("general.application_name"));
        label_username.setText(ApplicationController.text.getString("login.username"));
        label_password.setText(ApplicationController.text.getString("login.password"));
        btn_login.setText(ApplicationController.text.getString("login.login"));
        btn_cancel.setText(ApplicationController.text.getString("general.cancel"));
        label_location.setText(ApplicationController.text.getString("login.location")+":");
        label_location_id.setText(ApplicationController.zone.toString());
    }

    /**
     * Attempt to login the user
     * Logs the attempt and shows an error if the login fails
     * @param mouseEvent
     */
    public void on_login(MouseEvent mouseEvent) {
        try {
            User user = User.login(username.getText(), password.getText());
            Logging.loginSuccessful(user.toString());
            new MainController().show();
            close();
        } catch (NoSuchElementException e) {
            Logging.loginFailed(username.getText());
            Popup.error(ApplicationController.text.getString("error.login_failed.title"),
                    ApplicationController.text.getString("error.login_failed.message"));
        }
    }

    /**
     * Close the window
     * @param mouseEvent
     */
    public void on_cancel(MouseEvent mouseEvent) {
        close();
    }

    @Override
    public String getFxmlFileName() {
        return "login-view.fxml";
    }

    @Override
    public Node windowElement() {
        return application_name;
    }
}
