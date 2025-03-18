package com.example.ums_engg1420;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;

public class EventManagementController {
    @FXML private VBox eventList;
    @FXML private TextField eventNameField, eventCodeField, locationField, dateTimeField, capacityField, costField;
    @FXML private TextArea descriptionField;
    @FXML private Button addEventButton, btnEditEvent, btnDeleteEvent, btnManageRegistrations, btnSaveEvent, btnCancel;
    @FXML private ComboBox<String> filterOptions;

    public void initialize() {
        refreshEventList();
    }

    @FXML
    private void addEvent() {
        try {
            String name = eventNameField.getText();
            String code = eventCodeField.getText();
            String location = locationField.getText();
            String dateTime = dateTimeField.getText();
            int capacity = Integer.parseInt(capacityField.getText().trim());
            double cost = Double.parseDouble(costField.getText().trim());
            String description = descriptionField.getText();

            if (name.isEmpty() || code.isEmpty() || location.isEmpty() || dateTime.isEmpty()) {
                showAlert("Error", "Please fill in all required fields.");
                return;
            }

            Event event = new Event(0, name, code, description, "default.jpg", location, dateTime, capacity, cost, "");
            DatabaseHelper.addEvent(event);
            refreshEventList();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Invalid number format for capacity or cost.");
        }
    }

    @FXML
    private void editEvent() {
        showAlert("Edit Event", "Edit event functionality to be implemented.");
    }

    @FXML
    private void deleteEvent() {
        showAlert("Delete Event", "Delete event functionality to be implemented.");
    }

    @FXML
    private void manageRegistrations() {
        showAlert("Manage Registrations", "Manage registrations functionality to be implemented.");
    }

    @FXML
    private void saveEvent() {
        showAlert("Save Event", "Save event functionality to be implemented.");
    }

    @FXML
    private void cancelEvent() {
        showAlert("Cancel Event", "Cancel event functionality to be implemented.");
    }

    private void refreshEventList() {
        if (eventList == null) return;

        eventList.getChildren().clear();
        List<Event> events = DatabaseHelper.getAllEvents();
        for (Event event : events) {
            Label eventLabel = new Label(event.getEventName() + " - " + event.getDateTime());
            eventList.getChildren().add(eventLabel);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        eventNameField.clear();
        eventCodeField.clear();
        locationField.clear();
        dateTimeField.clear();
        capacityField.clear();
        costField.clear();
        descriptionField.clear();
    }
}