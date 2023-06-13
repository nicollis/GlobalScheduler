package dev.ollis.wgu.globalscheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ApplicationController extends Application {
    public static ResourceBundle text;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationController.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(text.getString("login.title"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
//        Locale.setDefault(Locale.FRENCH);
        Locale userLocal = Locale.getDefault();
        text = ResourceBundle.getBundle("lang", userLocal);
        launch();
    }
}