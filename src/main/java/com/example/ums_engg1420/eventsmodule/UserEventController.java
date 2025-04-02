package com.example.ums_engg1420.eventsmodule;

import com.example.ums_engg1420.dataclasses.Event;
import com.example.ums_engg1420.dataparsers.EventDataHandler;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.ums_engg1420.dataparsers.EventDataHandler.readEvents;

public class UserEventController extends EventModuleInitializer {

    @FXML
    private TableView<Event> tblUserEvents;

    @FXML
    private TableColumn<Event, String> colEventCode, colEventName, colDescription, colLocation, colDateTime, colCost, colHeaderImage;

    @FXML
    private TableColumn<Event, Integer> colCapacity;

    @FXML
    private DatePicker datePicker;  // For Calendar View

    @FXML
    private TextArea eventDetailsArea;  // To show details for selected date

    private ObservableList<Event> eventList;

    @FXML
    public void initialize() {
        // Initialize table columns without Registered Students column
        tableInitialize(
                colEventCode, colEventName, colDescription, colLocation,
                colDateTime, colCapacity, colCost, colHeaderImage, null, null
        );

        // Load user event data
        loadUserEventData();
    }

    // Load event data for users without admin controls
    private void loadUserEventData() {
        eventList = FXCollections.observableArrayList(readEvents());
        tblUserEvents.setItems(eventList);
    }

    // Refresh entries when needed
    @FXML
    private void refreshUserEntries() {
        refreshEntries(tblUserEvents);
    }

    // Show events for selected date using DatePicker
    @FXML
    private void showEventsForSelectedDate() {
        if (datePicker.getValue() != null) {
            String selectedDate = datePicker.getValue().toString();  // Get date string
            List<Event> filteredEvents = eventList.stream()
                    .filter(event -> event.getDateTime().startsWith(selectedDate))
                    .toList();

            if (!filteredEvents.isEmpty()) {
                StringBuilder eventDetails = new StringBuilder();
                for (Event event : filteredEvents) {
                    eventDetails.append("Event Name: ").append(event.getEventName()).append("\n")
                            .append("Description: ").append(event.getDescription()).append("\n")
                            .append("Location: ").append(event.getLocation()).append("\n")
                            .append("Date & Time: ").append(event.getDateTime()).append("\n")
                            .append("Capacity: ").append(event.getCapacity()).append("\n")
                            .append("Cost: ").append(event.getCost()).append("\n\n");
                }
                eventDetailsArea.setText(eventDetails.toString());
            } else {
                eventDetailsArea.setText("No events found for the selected date.");
            }
        }
    }

    @FXML
    private void registerForEvent() {
        Event selectedEvent = tblUserEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Dialog<String> dialog = new TextInputDialog();
            dialog.setTitle("Register for Event");
            dialog.setHeaderText("Enter your name to register:");
            dialog.setContentText("Name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(userName -> {
                userName = userName.trim();
                if (!userName.isEmpty()) {
                    List<String> registeredStudents = new ArrayList<>();
                    if (selectedEvent.getRegisteredStudents() != null) {
                        // Filter out empty strings (like "")
                        registeredStudents = selectedEvent.getRegisteredStudents().stream()
                                .filter(name -> name != null && !name.trim().isEmpty())
                                .collect(Collectors.toList());
                    }

                    if (!registeredStudents.contains(userName)) {
                        registeredStudents.add(userName);
                        selectedEvent.setRegisteredStudents(registeredStudents);

                        // Get actual Excel row index using event code
                        int excelRowIndex = EventDataHandler.getRowIndexByEventCode(selectedEvent.getEventCode());
                        if (excelRowIndex != -1) {
                            EventDataHandler.updateEvent(selectedEvent, excelRowIndex);
                            refreshEntries(tblUserEvents);
                            showAlert("Success", "You have registered for " + selectedEvent.getEventName() + ".", Alert.AlertType.INFORMATION);
                        } else {
                            showAlert("Error", "Failed to locate event in Excel file.", Alert.AlertType.ERROR);
                        }
                    } else {
                        showAlert("Error", "You are already registered for this event.", Alert.AlertType.ERROR);
                    }
                } else {
                    showAlert("Error", "Name cannot be empty.", Alert.AlertType.ERROR);
                }
            });
        } else {
            showAlert("Error", "Please select an event to register.", Alert.AlertType.ERROR);
        }
    }
}
