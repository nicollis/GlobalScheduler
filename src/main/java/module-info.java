module dev.ollis.wgu.globalscheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens dev.ollis.wgu.globalscheduler to javafx.fxml;
    exports dev.ollis.wgu.globalscheduler;
    exports dev.ollis.wgu.globalscheduler.controllers;
    exports dev.ollis.wgu.globalscheduler.models;
}