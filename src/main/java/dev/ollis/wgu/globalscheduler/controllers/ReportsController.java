package dev.ollis.wgu.globalscheduler.controllers;

import dev.ollis.wgu.helper.Popup;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Map;

public class ReportsController implements Viewable {
    public TableView<Map<String, Object>> table_view;

    public Text title;

    // Instance methods

    public void buildReport(List<Map<String, Object>> report_data, String title) {
        this.title.setText(title);
        buildTable(report_data);
    }

    private void buildTable(List<Map<String, Object>> rs) {
        if (rs == null || rs.isEmpty()) {
            Popup.error("Error", "Could not build report.");
        }

        // Create new columns and add to the table
        for (String column : rs.get(0).keySet()) {
            TableColumn<Map<String, Object>, Object> tableColumn = new TableColumn<>(column);
            tableColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().get(column)));
            table_view.getColumns().add(tableColumn);
        }

        // Populate the table with data
        ObservableList<Map<String, Object>> tableData = FXCollections.observableArrayList(rs);
        table_view.setItems(tableData);
    }

    // Viewable interface
    @Override
    public String getFxmlFileName() {
        return "reports-view.fxml";
    }

    @Override
    public Node windowElement() {
        return table_view;
    }

    // JavaFX event handlers
    public void on_close(MouseEvent mouseEvent) {
        close();
    }
}
