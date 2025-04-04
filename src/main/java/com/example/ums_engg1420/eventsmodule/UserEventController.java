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

    // Table and column references from FXML UI
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

    private ObservableList<Event> eventList; // Holds all events visible to user

    // Called when the controller is loaded by JavaFX
    @FXML
    public void initialize() {
        // Initialize table columns, set up table columns using base class method (no admin-specific column i.e Registered Students)
        tableInitialize(
                colEventCode, colEventName, colDescription, colLocation,
                colDateTime, colCapacity, colCost, colHeaderImage, null, null
        );

        // Load user event data
        loadUserEventData();
    }

    // Load event data for users without admin controls, (read from Excel) into the observable list and binds to the table, ensures fresh data on every load
    private void loadUserEventData() {
        eventList = FXCollections.observableArrayList(readEvents());
        tblUserEvents.setItems(eventList);
    }

    // Refresh entries when needed
    @FXML
    private void refreshUserEntries() {
        refreshEntries(tblUserEvents);
    }

    // Show events for selected date using DatePicker, filters the full event list to match the selected date.
    @FXML
    private void showEventsForSelectedDate() {
        if (datePicker.getValue() != null) {
            String selectedDate = datePicker.getValue().toString();  // Get date string
            // Filter events where the datetime string starts with the selected date
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

    //allows to register for event, prevent duplicates too
    @FXML
    private void registerForEvent() {
        Event selectedEvent = tblUserEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Dialog<String> dialog = new TextInputDialog(); // prompt name
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

                    // Check for duplicate registration
                    if (!registeredStudents.contains(userName)) {
                        registeredStudents.add(userName);
                        selectedEvent.setRegisteredStudents(registeredStudents);

                        // Get actual Excel row index using event code
                        int excelRowIndex = EventDataHandler.getRowIndexByEventCode(selectedEvent.getEventCode());
                        if (excelRowIndex != -1) {
                            EventDataHandler.updateEvent(selectedEvent, excelRowIndex);
                            refreshEntries(tblUserEvents); //refresh ui
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
    //Prompts the user for their name and then shows a list of all events they are registered for.
    @FXML
    private void viewMyRegisteredEvents() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("View Registered Events");
        dialog.setHeaderText("Enter your name to view your registered events:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(userName -> {
            String inputName = userName.trim();
            if (!inputName.isEmpty()) {
                // Filter events where the user's name appears in the registered student list
                List<Event> registeredEvents = eventList.stream()
                        .filter(event -> {
                            List<String> students = event.getRegisteredStudents();
                            if (students == null) return false;

                            // Clean and normalize the names for comparison
                            List<String> cleanedStudents = students.stream()
                                    .filter(name -> name != null && !name.trim().isEmpty())
                                    .map(String::trim)
                                    .map(String::toLowerCase)
                                    .toList();

                            return cleanedStudents.contains(inputName.toLowerCase());
                        })
                        .toList();

                // show results as an alert
                if (!registeredEvents.isEmpty()) {
                    StringBuilder sb = new StringBuilder("You are registered for the following events:\n\n");
                    for (Event event : registeredEvents) {
                        sb.append("• ").append(event.getEventName())
                                .append(" (").append(event.getDateTime()).append(")\n");
                    }
                    showAlert("Registered Events", sb.toString(), Alert.AlertType.INFORMATION);
                } else {
                    showAlert("No Events Found", "You are not registered for any events.", Alert.AlertType.INFORMATION);
                }
            } else {
                showAlert("Invalid Input", "Name cannot be empty.", Alert.AlertType.ERROR);
            }
        });
    }
}
