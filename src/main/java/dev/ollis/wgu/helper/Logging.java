package dev.ollis.wgu.helper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class Logging {
    private static final String LOGIN_ACTIVITY_FILE = "login_activity.txt";

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

    public static void loginSuccessful(String username) {
        loginMessage(username, true);
    }

    public static void loginFailed(String username) {
        loginMessage(username, false);
    }

    private static void loginMessage(String username, boolean success) {
        String message = String.format("%s: User %s logged in %s", success ? "SUCCESS" : "FAILURE", username, success ? "successfully" : "unsuccessfully");
        message += String.format(" at %s", TimeUtils.now());
        message += String.format(" on %s", TimeUtils.today());
        log(message, LOGIN_ACTIVITY_FILE);
    }
}
