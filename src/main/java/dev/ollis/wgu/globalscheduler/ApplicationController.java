package dev.ollis.wgu.globalscheduler;

import dev.ollis.wgu.globalscheduler.controllers.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main application class
 */
public class ApplicationController extends Application {
    /**
     * The text resource bundle
     */
    public static ResourceBundle text;

    /**
     * The user's time zone
     */
    public static ZoneId zone;

    /**
     * The business hours
     */
    public static int[] businessHours = {8, 22};

    @Override
    public void start(Stage stage) {
        new LoginController().show();
    }

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
//        Locale.setDefault(Locale.FRENCH);
        Locale userLocal = Locale.getDefault();
        text = ResourceBundle.getBundle("lang", userLocal);
//        zone = ZoneId.of(ZoneId.SHORT_IDS.get("CST"));
        zone = ZoneId.systemDefault();
        launch();
    }
}