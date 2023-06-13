package dev.ollis.wgu.helper;

import dev.ollis.wgu.globalscheduler.ApplicationController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * A class to handle popup dialogs
 * @author Nicholas Ollis
 */

// Although independent of the rest of the application, this class is expects a "lang" resource bundle to be present
public class Popup {
//    private static final ResourceBundle text = ResourceBundle.getBundle("lang", Locale.getDefault());
//    private static final ButtonType okButton = new ButtonType(text.getString("general.ok"), ButtonBar.ButtonData.OK_DONE);

    /**
     * Shows an error dialog
     * @param title The title of the dialog
     * @param message The message of the dialog
     */
    public static void error(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Shows a conformation dialog
     * @param title The title of the dialog
     * @param message The message of the dialog
     * @return True if the user clicked OK, false otherwise
     */
    public static boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
