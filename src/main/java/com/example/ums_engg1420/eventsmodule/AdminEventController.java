package com.example.ums_engg1420.eventsmodule;

import com.example.ums_engg1420.dataclasses.Event;
import com.example.ums_engg1420.dataparsers.EventDataHandler;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

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
    //for edit
    private Event editingEvent = null;
    //for print attendee list
    private void printNode(Node node) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
            boolean success = job.printPage(node);
            if (success) {
                job.endJob();
            }
        }
    }


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
        editingEvent = tblEvents.getSelectionModel().getSelectedItem();
        if (editingEvent != null) {
            eventCodeField.setText(editingEvent.getEventCode());
            eventNameField.setText(editingEvent.getEventName());
            descriptionField.setText(editingEvent.getDescription());
            locationField.setText(editingEvent.getLocation());

            try {
                String[] parts = editingEvent.getDateTime().split(" ");
                eventDateField.setValue(LocalDate.parse(parts[0]));
                eventTimeField.setText(parts[1]);
            } catch (Exception e) {
                eventDateField.setValue(null);
                eventTimeField.clear();
            }

            capacityField.setText(String.valueOf(editingEvent.getCapacity()));
            costField.setText(editingEvent.getCost());
        } else {
            showAlert("Error", "Please select an event to edit.", Alert.AlertType.ERROR);
        }

    }
    @FXML
    private void saveEditedEvent(ActionEvent event) {
        if (editingEvent == null) {
            showAlert("Error", "No event selected to update.", Alert.AlertType.ERROR);
            return;
        }

        // Copy existing data
        String eventCode = editingEvent.getEventCode();
        String eventName = !eventNameField.getText().isEmpty() ? eventNameField.getText() : editingEvent.getEventName();
        String description = !descriptionField.getText().isEmpty() ? descriptionField.getText() : editingEvent.getDescription();
        String location = !locationField.getText().isEmpty() ? locationField.getText() : editingEvent.getLocation();

        String date = (eventDateField.getValue() != null) ? eventDateField.getValue().toString() : editingEvent.getDateTime().split(" ")[0];
        String time = !eventTimeField.getText().isEmpty() ? eventTimeField.getText() : editingEvent.getDateTime().split(" ")[1];
        String dateTime = date + " " + time;

        int capacity = editingEvent.getCapacity();
        if (!capacityField.getText().isEmpty()) {
            try {
                capacity = Integer.parseInt(capacityField.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "Capacity must be a number.", Alert.AlertType.ERROR);
                return;
            }
        }

        String cost = !costField.getText().isEmpty() ? costField.getText() : editingEvent.getCost();
        String headerImage = editingEvent.getHeaderImage(); // Keeping as is
        List<String> registeredStudents = editingEvent.getRegisteredStudents(); // Keeping as is

        Event updatedEvent = new Event(eventCode, eventName, description, location, dateTime, capacity, cost, headerImage, registeredStudents);

        int excelRowIndex = EventDataHandler.getRowIndexByEventCode(eventCode);
        if (excelRowIndex != -1) {
            EventDataHandler.updateEvent(updatedEvent, excelRowIndex);
            eventList.set(tblEvents.getSelectionModel().getSelectedIndex(), updatedEvent);
            refreshEntries(tblEvents);
            clearForm();
            editingEvent = null;
            showAlert("Success", "Event updated successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Could not find the event in the database.", Alert.AlertType.ERROR);
        }
    }


    // Delete Event
    @FXML
    private void deleteEvent(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            int selectedIndex = eventList.indexOf(selectedEvent);

            if (selectedIndex >= 0) {
                // Delete from Excel sheet (with +1 offset due to header row)
                EventDataHandler.deleteEvent(selectedEvent.getEventCode());

                // Reload list from Excel and update table
                eventList.setAll(EventDataHandler.readEvents());
                tblEvents.refresh();

                clearForm();
                showAlert("Success", "Event deleted successfully!", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Error", "Please select an event to delete.", Alert.AlertType.ERROR);
        }
    }

    // Clear form
    private void clearForm() {
        if (eventCodeField != null) eventCodeField.clear();
        if (eventNameField != null) eventNameField.clear();
        if (locationField != null) locationField.clear();
        if (eventDateField != null) eventDateField.setValue(null);
        if (eventTimeField != null) eventTimeField.clear();
        if (capacityField != null) capacityField.clear();
        if (costField != null) costField.clear();
        if (descriptionField != null) descriptionField.clear();
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
    //attendee list
    @FXML
    private void viewAttendeeList(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            showAlert("Error", "Please select an event to view attendees.", Alert.AlertType.ERROR);
            return;
        }

        List<String> registeredStudents = selectedEvent.getRegisteredStudents();

        // Create a new window (Stage)
        Stage printStage = new Stage();
        printStage.setTitle("Attendee List - " + selectedEvent.getEventName());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Label header = new Label("Attendees for: " + selectedEvent.getEventName());
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextArea attendeeArea = new TextArea();
        attendeeArea.setEditable(false);
        attendeeArea.setWrapText(true);

        if (registeredStudents == null || registeredStudents.isEmpty()) {
            attendeeArea.setText("No students have registered for this event.");
        } else {
            StringBuilder sb = new StringBuilder();
            int count = 1;
            for (String student : registeredStudents) {
                if (!student.trim().isEmpty()) {
                    sb.append(count++).append(". ").append(student.trim()).append("\n");
                }
            }
            attendeeArea.setText(sb.toString());
        }

        Button printButton = new Button("Print");
        printButton.setOnAction(e -> printNode(attendeeArea));

        layout.getChildren().addAll(header, attendeeArea, printButton);

        Scene scene = new Scene(layout, 500, 500);
        printStage.setScene(scene);
        printStage.show();
    }
}
