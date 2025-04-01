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
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Register for Event");
            dialog.setHeaderText("Please enter your name and email to register.");

            // Set the button types
            ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

            // Create text fields for name and email
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField nameField = new TextField();
            TextField emailField = new TextField();

            grid.add(new Label("Name:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Email:"), 0, 1);
            grid.add(emailField, 1, 1);

            dialog.getDialogPane().setContent(grid);

            // Get the result when the button is pressed
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == registerButtonType) {
                    return new Pair<>(nameField.getText().trim(), emailField.getText().trim());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(userData -> {
                String userName = userData.getKey();
                String userEmail = userData.getValue();

                if (!userName.isEmpty() && !userEmail.isEmpty()) {
                    // Check if registered students list is null and initialize it
                    List<String> registeredStudents = selectedEvent.getRegisteredStudents() != null
                            ? new ArrayList<>(selectedEvent.getRegisteredStudents())
                            : new ArrayList<>();

                    // Check if registered emails list is null and initialize it
                    List<String> registeredEmails = selectedEvent.getRegisteredEmails() != null
                            ? new ArrayList<>(selectedEvent.getRegisteredEmails())
                            : new ArrayList<>();

                    // Check if the user is already registered
                    if (!registeredStudents.contains(userName)) {
                        registeredStudents.add(userName);
                        registeredEmails.add(userEmail);

                        // Update the event object
                        selectedEvent.setRegisteredStudents(registeredStudents);
                        selectedEvent.setRegisteredEmails(registeredEmails);

                        // Update Excel file
                        int selectedIndex = eventList.indexOf(selectedEvent);
                        EventDataHandler.updateEvent(selectedEvent, selectedIndex + 1);

                        // Refresh and confirm registration
                        refreshEntries(tblUserEvents);
                        showAlert("Success", "You have registered successfully for " + selectedEvent.getEventName() + "!", Alert.AlertType.INFORMATION);
                    } else {
                        showAlert("Error", "You are already registered for this event.", Alert.AlertType.ERROR);
                    }
                } else {
                    showAlert("Error", "Please provide valid name and email.", Alert.AlertType.ERROR);
                }
            });
        } else {
            showAlert("Error", "Please select an event to register.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void unregisterFromEvent() {
        Event selectedEvent = tblUserEvents.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Unregister from Event");
            dialog.setHeaderText("Enter your name to unregister:");
            dialog.setContentText("Name:");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent() && !result.get().trim().isEmpty()) {
                String userName = result.get().trim();

                // Check if registered students list is null
                List<String> registeredStudents = selectedEvent.getRegisteredStudents() != null
                        ? new ArrayList<>(selectedEvent.getRegisteredStudents())
                        : new ArrayList<>();

                // Check if registered emails list is null
                List<String> registeredEmails = selectedEvent.getRegisteredEmails() != null
                        ? new ArrayList<>(selectedEvent.getRegisteredEmails())
                        : new ArrayList<>();

                // Find and remove the name and corresponding email
                int indexToRemove = registeredStudents.indexOf(userName);

                // Check if the index is valid before attempting to remove
                if (indexToRemove != -1 && indexToRemove < registeredStudents.size()) {
                    registeredStudents.remove(indexToRemove);

                    // Only remove the corresponding email if the list of emails is not empty
                    if (indexToRemove < registeredEmails.size()) {
                        registeredEmails.remove(indexToRemove);
                    }

                    selectedEvent.setRegisteredStudents(registeredStudents);
                    selectedEvent.setRegisteredEmails(registeredEmails);

                    // Update Excel
                    int selectedIndex = eventList.indexOf(selectedEvent);
                    EventDataHandler.updateEvent(selectedEvent, selectedIndex + 1);

                    // Refresh and show confirmation
                    refreshEntries(tblUserEvents);
                    showAlert("Success", "You have unregistered from " + selectedEvent.getEventName() + ".", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Your name was not found in the registered list.", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "Please provide your name to unregister.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Please select an event to unregister from.", Alert.AlertType.ERROR);
        }
    }

}
