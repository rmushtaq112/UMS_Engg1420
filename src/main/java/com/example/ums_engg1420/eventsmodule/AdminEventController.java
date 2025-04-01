package com.example.ums_engg1420.eventsmodule;

import com.example.ums_engg1420.dataclasses.Event;
import com.example.ums_engg1420.dataparsers.EventDataHandler;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ums_engg1420.dataparsers.EventDataHandler.readEvents;

public class AdminEventController extends EventModuleInitializer {

    @FXML
    private TableView<Event> tblEvents;
    @FXML
    private TableColumn<Event, String> colEventCode, colEventName, colDescription, colLocation, colDateTime, colCost, colHeaderImage, colRegisteredStudents;
    @FXML
    private TableColumn<Event, Integer> colCapacity, colNumRegisteredStudents;  // Column for registered count
    @FXML
    private TextField txtEventCode, txtEventName, txtDescription, txtLocation, txtDateTime, txtCapacity, txtCost, txtHeaderImage, txtRegisteredStudents;
    @FXML
    private TextField eventNameField, eventCodeField, locationField, eventTimeField, capacityField, costField;
    @FXML
    private DatePicker eventDateField;
    @FXML
    private TextArea descriptionField;

    private ObservableList<Event> eventList;

    @FXML
    public void initialize() {
        // Initialize table columns using EventModuleInitializer
        tableInitialize(
                colEventCode, colEventName, colDescription, colLocation,
                colDateTime, colCapacity, colCost, colHeaderImage, colRegisteredStudents, colNumRegisteredStudents
        );

        // Set up number of registered students column using comma delimiter
        colNumRegisteredStudents.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(countRegisteredStudents(cellData.getValue().getRegisteredStudents())).asObject()
        );

        // Load event data
        loadEventData();
    }

    // Load data from Excel and populate table
    private void loadEventData() {
        eventList = FXCollections.observableArrayList(readEvents());
        tblEvents.setItems(eventList);
    }

    // Count registered students based on comma as delimiter
    private int countRegisteredStudents(List<String> registeredStudents) {
        if (registeredStudents == null || registeredStudents.isEmpty()) {
            return 0;  // No registered students
        }

        // Join the list to a single string and count based on comma
        String joinedStudents = String.join(", ", registeredStudents);
        return joinedStudents.isEmpty() ? 0 : joinedStudents.split(",").length;
    }

    // Add Event
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

    // Edit Event
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
                showAlert("Error", "All required fields must be filled out.", Alert.AlertType.INFORMATION);
                return;
            }

            int capacity;
            try {
                capacity = Integer.parseInt(capacityStr);
            } catch (NumberFormatException e) {
                showAlert("Error", "Capacity must be a valid number.", Alert.AlertType.INFORMATION);
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

    // Delete Event
    @FXML
    private void deleteEvent(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            int selectedIndex = eventList.indexOf(selectedEvent);

            if (selectedIndex >= 0) {
                eventList.remove(selectedIndex);
                EventDataHandler.deleteEvent(selectedIndex + 1);
                refreshEntries(tblEvents);
                clearForm();
                showAlert("Success", "Event deleted successfully!", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Error", "Please select an event to delete.", Alert.AlertType.ERROR);
        }
        refreshEntries(tblEvents);
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

    @FXML
    private void populateFormFromSelectedRow(MouseEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            txtEventCode.setText(selectedEvent.getEventCode());
            txtEventName.setText(selectedEvent.getEventName());
            txtDescription.setText(selectedEvent.getDescription());
            txtLocation.setText(selectedEvent.getLocation());

            try {
                // Split date and time correctly
                String dateTime = selectedEvent.getDateTime();
                String datePart = dateTime.split(" ")[0];  // Extract date portion
                String timePart = dateTime.split(" ")[1];  // Extract time portion

                // Parse date correctly
                eventDateField.setValue(LocalDate.parse(datePart));  // Set DatePicker
                eventTimeField.setText(timePart);  // Set time field
            } catch (Exception e) {
                System.err.println("Error parsing date for form: " + e.getMessage());
                eventDateField.setValue(null);
                eventTimeField.clear();
            }

            txtCapacity.setText(String.valueOf(selectedEvent.getCapacity()));
            txtCost.setText(selectedEvent.getCost());
            txtHeaderImage.setText(selectedEvent.getHeaderImage());
            txtRegisteredStudents.setText(String.join(", ", selectedEvent.getRegisteredStudents()));
        }
    }
}
