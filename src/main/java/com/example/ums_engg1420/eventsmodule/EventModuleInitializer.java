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
    // Observable list that holds all event data for table display and logic
    protected ObservableList<Event> events = FXCollections.observableArrayList();

    // Constructor automatically loads the latest event data into the observable list
    public EventModuleInitializer() {
        refreshList();
    }

    // Refresh the observable list with up-to-date event data read from storage
    protected void refreshList() {
        List<Event> eventData = readEvents(); //read excel
        events.setAll(eventData); //replace current list w new data
    }

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
            TableColumn<Event, Integer> numRegisteredCol
    ) // Bind each column to the corresponding Event property using JavaFX bindings
    {
        codeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventCode()));
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventName()));
        descriptionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        locationCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        dateTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateTime()));
        capacityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCapacity()).asObject());
        costCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCost()));
        headerCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeaderImage()));

        if (registeredByCol != null) {
            registeredByCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                    String.join(", ", cellData.getValue().getRegisteredStudents()) //, as delimiter
            ));
        }

        if (numRegisteredCol != null) {
            numRegisteredCol.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getRegisteredStudents().size()).asObject()
            );
        }
    }

    // Method to refresh event entries and update the table
    public void refreshEntries(TableView<Event> table) {
        refreshList();
        table.setItems(events);
        table.refresh();
    }

    // Show alert
    public void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // no header
        alert.setContentText(message); //main msg content
        alert.showAndWait(); //essentially pops up until dismissed
    }
}

