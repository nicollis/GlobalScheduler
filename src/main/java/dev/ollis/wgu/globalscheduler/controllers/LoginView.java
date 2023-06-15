package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.globalscheduler.ApplicationController;
import dev.ollis.wgu.globalscheduler.models.User;
import dev.ollis.wgu.helper.Popup;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.NoSuchElementException;

public class LoginView implements Viewable {
    public TextField username;
    public PasswordField password;
    public Text application_name;
    public Text label_username;
    public Text label_password;
    public Button btn_login;
    public Button btn_cancel;
    public Text label_location_id;
    public Text label_location;

    public void initialize() {
        application_name.setText(ApplicationController.text.getString("general.application_name"));
        label_username.setText(ApplicationController.text.getString("login.username"));
        label_password.setText(ApplicationController.text.getString("login.password"));
        btn_login.setText(ApplicationController.text.getString("login.login"));
        btn_cancel.setText(ApplicationController.text.getString("general.cancel"));
        label_location.setText(ApplicationController.text.getString("login.location")+":");
        label_location_id.setText(ApplicationController.zone.toString());
    }

    public void on_login(MouseEvent mouseEvent) {
        try {
            User user = User.login(username.getText(), password.getText());
            System.out.println(user);
            new AppointmentsView().show();
            close();
        } catch (NoSuchElementException e) {
            Popup.error(ApplicationController.text.getString("error.login_failed.title"),
                    ApplicationController.text.getString("error.login_failed.message"));
        }
    }

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
