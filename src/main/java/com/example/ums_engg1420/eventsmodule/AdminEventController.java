package com.example.ums_engg1420.eventsmodule;

import com.example.ums_engg1420.dataclasses.Event;
import com.example.ums_engg1420.dataparsers.EventDataHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ums_engg1420.dataparsers.EventDataHandler.readEvents;
import static com.example.ums_engg1420.dataparsers.SubjectDataHandler.removeSubject;

// Extends EventModuleInitializer
public class AdminEventController extends EventModuleInitializer {

    @FXML
    private TableView<Event> tblEvents;
    @FXML
    private TableColumn<Event, String> colEventCode, colEventName, colDescription, colLocation, colDateTime, colCost, colHeaderImage, colRegisteredStudents;
    @FXML
    private TableColumn<Event, Integer> colCapacity;
    @FXML
    private TextField txtEventCode, txtEventName, txtDescription, txtLocation, txtDateTime, txtCapacity, txtCost, txtHeaderImage, txtRegisteredStudents;
    @FXML
    private TextField eventNameField, eventCodeField, locationField, eventTimeField, capacityField, costField;
    @FXML
    private DatePicker eventDateField;
    @FXML TextArea descriptionField;

    private ObservableList<Event> eventList;

    @FXML
    public void initialize() {
        // initialize table columns using EventModuleInitializer
        tableInitialize(
                colEventCode, colEventName, colDescription, colLocation,
                colDateTime, colCapacity, colCost, colHeaderImage, colRegisteredStudents
        );

        // Load event data
        loadEventData();
    }

    // Load data from Excel and populate table
    private void loadEventData() {
        eventList = FXCollections.observableArrayList(readEvents());
        tblEvents.setItems(eventList);
    }

    // Add
    @FXML
    private void addEvent(ActionEvent event) {
        String eventCode = eventCodeField.getText();
        String eventName = eventNameField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();

        // Corrected DatePicker handling
        String date = (eventDateField.getValue() != null) ? eventDateField.getValue().toString() : "";
        String time = eventTimeField.getText();
        String dateTime = date + " " + time;

        String capacityStr = capacityField.getText();
        String cost = costField.getText();

        // Validate required fields
        if (eventCode.isEmpty() || eventName.isEmpty() || dateTime.isEmpty() || capacityStr.isEmpty() || cost.isEmpty()) {
            showAlert("Error", "All required fields must be filled out.", Alert.AlertType.INFORMATION);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
        } catch (NumberFormatException e) {
            showAlert("Error", "Capacity must be a valid integer.", Alert.AlertType.ERROR);
            return;
        }

        // Create new event object
        Event newEvent = new Event(eventCode, eventName, description, location, dateTime, capacity, cost, "default", List.of());

        try {
            EventDataHandler.writeEvent(newEvent);
            eventList.add(newEvent);
        } catch (Exception DuplicateEntryException) {
            showAlert("Error", "Event already exists with this code.", Alert.AlertType.INFORMATION);
            return;
        }

        refreshEntries(tblEvents);
        clearForm();
        showAlert("Success", "Event added successfully!", Alert.AlertType.INFORMATION);
    }


    // edit
    @FXML
    private void editEvent(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            int selectedIndex = tblEvents.getSelectionModel().getSelectedIndex();

            // Get updated values from the form
            String eventCode = txtEventCode.getText();
            String eventName = txtEventName.getText();
            String description = txtDescription.getText();
            String location = txtLocation.getText();
            String dateTime = txtDateTime.getText();
            String capacityStr = txtCapacity.getText();
            String cost = txtCost.getText();
            String headerImage = txtHeaderImage.getText();
            String registeredStudentsStr = txtRegisteredStudents.getText();

            // Validate required fields
            if (eventCode.isEmpty() || eventName.isEmpty() || dateTime.isEmpty() || capacityStr.isEmpty()) {
                showAlert("Error", "All required fields must be filled out.",Alert.AlertType.INFORMATION);
                return;
            }

            int capacity;
            try {
                capacity = Integer.parseInt(capacityStr);
            } catch (NumberFormatException e) {
                showAlert("Error", "Capacity must be a valid number.",Alert.AlertType.INFORMATION);
                return;
            }



            // Parse registered students from a comma-separated string
            List<String> registeredStudents = Arrays.stream(registeredStudentsStr.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());

            // Create updated event object
            Event updatedEvent = new Event(
                    eventCode, eventName, description, location, dateTime,
                    capacity, cost, headerImage, registeredStudents
            );

            // Update ObservableList and Excel
            eventList.set(selectedIndex, updatedEvent);
            EventDataHandler.updateEvent(updatedEvent, selectedIndex + 1); // Update Excel

            // Refresh table and clear form
            refreshEntries(tblEvents);
            clearForm();
            showAlert("Success", "Event updated successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Please select an event to edit.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void uploadImage(){
        System.out.println("uploading");
    }

    // delete
    @FXML
    private void deleteEvent(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            // Get the correct index of the selected event
            int selectedIndex = eventList.indexOf(selectedEvent);

            // Remove the event from the ObservableList and TableView
            if (selectedIndex >= 0) {
                eventList.remove(selectedIndex);

                // Delete the corresponding entry from the Excel sheet
                EventDataHandler.deleteEvent(selectedIndex + 1);  // Pass the correct rowIndex to deleteEvent()

                // Refresh the entries to reflect changes
                refreshEntries(tblEvents);

                // Clear form and show confirmation
                clearForm();
                showAlert("Success", "Event deleted successfully!", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Error", "Please select an event to delete.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void viewEvents() {
        System.out.println("Viewing events...");
    }

    @FXML
    public void searchEvents() {
        System.out.println("Searching events...");
    }

    @FXML
    public void manageRegistrations() {
        System.out.println("Managing registrations...");
    }

    // event details
    private Event getEventFromForm() {
        String eventCode = txtEventCode.getText();
        String eventName = txtEventName.getText();
        String description = txtDescription.getText();
        String location = txtLocation.getText();
        String dateTime = txtDateTime.getText();
        int capacity = Integer.parseInt(txtCapacity.getText());
        String cost = txtCost.getText();
        String headerImage = txtHeaderImage.getText();

        List<String> registeredStudents = Arrays.stream(txtRegisteredStudents.getText().split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        return new Event(eventCode, eventName, description, location, dateTime,
                capacity, cost, headerImage, registeredStudents);
    }

    // Validate
    private boolean validateInput() {
        if (txtEventCode.getText().isEmpty() || txtEventName.getText().isEmpty() || txtDescription.getText().isEmpty()
                || txtLocation.getText().isEmpty() || txtDateTime.getText().isEmpty()
                || txtCapacity.getText().isEmpty() || txtCost.getText().isEmpty() || txtHeaderImage.getText().isEmpty()
                || txtRegisteredStudents.getText().isEmpty()) {
            showAlert("Error", "Please fill in all fields.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            Integer.parseInt(txtCapacity.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Capacity must be a valid number.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    // Clear form
    private void clearForm() {
        txtEventCode.clear();
        txtEventName.clear();
        txtDescription.clear();
        txtLocation.clear();
        txtDateTime.clear();
        txtCapacity.clear();
        txtCost.clear();
        txtHeaderImage.clear();
        txtRegisteredStudents.clear();
    }

    // Alert helper
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
