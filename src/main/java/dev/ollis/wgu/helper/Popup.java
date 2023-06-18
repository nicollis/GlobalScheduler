package dev.ollis.wgu.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

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
    public static boolean confirm(String title, String message, Runnable onOk) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            onOk.run();
            return true;
        }
        return false;
    }

    /**
     * Shows an info dialog
     * @param title The title of the dialog
     * @param message The message of the dialog
     */
    public static void info(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
