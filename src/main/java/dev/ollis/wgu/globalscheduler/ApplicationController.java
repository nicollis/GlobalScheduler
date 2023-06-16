package dev.ollis.wgu.globalscheduler;

import dev.ollis.wgu.globalscheduler.controllers.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class ApplicationController extends Application {
    public static ResourceBundle text;
    public static ZoneId zone;
    @Override
    public void start(Stage stage) {
        new LoginController().show();
    }

    public static void main(String[] args) {
//        Locale.setDefault(Locale.FRENCH);
        Locale userLocal = Locale.getDefault();
        text = ResourceBundle.getBundle("lang", userLocal);
        zone = ZoneId.systemDefault();
        launch();
    }
}