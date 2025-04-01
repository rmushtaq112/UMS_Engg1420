package com.example.ums_engg1420.eventsmodule;

import com.example.ums_engg1420.dataclasses.Event;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

import static com.example.ums_engg1420.dataparsers.EventDataHandler.readEvents;

public class EventModuleInitializer {

    // Read initial data from the event data handler
    protected List<Event> eventData = readEvents();
    protected ObservableList<Event> events = FXCollections.observableArrayList(eventData);

    // Method to initialize event table columns
    public void tableInitialize(
            TableColumn<Event, String> codeCol,
            TableColumn<Event, String> nameCol,
            TableColumn<Event, String> descriptionCol,
            TableColumn<Event, String> locationCol,
            TableColumn<Event, String> dateTimeCol,
            TableColumn<Event, Integer> capacityCol,
            TableColumn<Event, String> costCol,
            TableColumn<Event, String> headerCol,
            TableColumn<Event, String> registeredByCol,
            TableColumn<Event, Integer> numRegisteredCol // New column for registered count
    ) {
        codeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventCode()));
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventName()));
        descriptionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        locationCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        dateTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateTime()));
        capacityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCapacity()).asObject());
        costCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCost()));
        headerCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeaderImage()));

        // Registered students list
        if (registeredByCol != null) {
            registeredByCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                    String.join(", ", cellData.getValue().getRegisteredStudents())
            ));
        }

        // Number of registered students
        if (numRegisteredCol != null) {
            numRegisteredCol.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getRegisteredStudents().size()).asObject()
            );
        }
    }

    // Method to refresh event entries and update the table
    public void refreshEntries(TableView<Event> table) {
        eventData = readEvents();  // Read updated event data
        events = FXCollections.observableArrayList(eventData);
        table.setItems(events);
        table.refresh();  // Force refresh
    }

    // Show alert
    public void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
