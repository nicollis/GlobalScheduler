package dev.ollis.wgu.helper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Logging class for logging messages to files.
 */
public abstract class Logging {
    private static final String LOGIN_ACTIVITY_FILE = "login_activity.txt";

    /**
     * Logs a message to a file.
     * @param message The message to log
     * @param filename The name of the file to log to
     */
    public static void log(String message, String filename) {
        try {
            FileWriter fw = new FileWriter(filename, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(message);
            pw.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Logs a successful login to the login activity file.
     * @param username
     */
    public static void loginSuccessful(String username) {
        loginMessage(username, true);
    }

    /**
     * Logs a failed login to the login activity file.
     * @param username
     */
    public static void loginFailed(String username) {
        loginMessage(username, false);
    }

    /**
     * Logs a login message to the login activity file.
     * @param username
     * @param success
     */
    private static void loginMessage(String username, boolean success) {
        String message = String.format("%s: User %s logged in %s", success ? "SUCCESS" : "FAILURE", username, success ? "successfully" : "unsuccessfully");
        message += String.format(" at %s", TimeUtils.now());
        message += String.format(" on %s", TimeUtils.today());
        log(message, LOGIN_ACTIVITY_FILE);
    }
}
