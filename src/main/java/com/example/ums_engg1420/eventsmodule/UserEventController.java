package com.example.ums_engg1420.eventsmodule;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;

public class UserEventController {

    @FXML
    private DatePicker calendarPicker;

    @FXML
    private Label selectedDateLabel;

    @FXML
    private VBox eventContainer;

    @FXML
    private Button btnViewEvents;

    // Example data source: Replace with API or database integration in a real project
    private static final List<Event> EVENT_DATA = List.of(
            new Event("Math Workshop", LocalDate.of(2023, 11, 1), "10:30 AM"),
            new Event("Science Fair", LocalDate.of(2023, 11, 1), "2:00 PM"),
            new Event("Sports Day", LocalDate.of(2023, 11, 5), "9:00 AM")
    );

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        // Set initial date (today)
        calendarPicker.setValue(LocalDate.now());
        selectedDateLabel.setText("Events for: " + LocalDate.now());
        loadEventsForDate(LocalDate.now());
    }

    /**
     * Triggered when the "View Events" button is clicked.
     * Updates the event display based on the selected date.
     */
    @FXML
    public void viewEvents() {
        // Get the selected date from DatePicker
        LocalDate selectedDate = calendarPicker.getValue();
        if (selectedDate != null) {
            selectedDateLabel.setText("Events for: " + selectedDate);
            loadEventsForDate(selectedDate);
        }
    }

    /**
     * Loads and displays the events for a specific date.
     *
     * @param date the date for which events are loaded
     */
    private void loadEventsForDate(LocalDate date) {
        // Clear the event container before adding new events
        eventContainer.getChildren().clear();

        // Filter events for the specified date
        List<Event> eventsForDate = EVENT_DATA.stream()
                .filter(event -> event.getDate().equals(date))
                .toList();

        // Populate the events dynamically
        if (eventsForDate.isEmpty()) {
            Label noEventsLabel = new Label("No events scheduled for this date.");
            noEventsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: gray;");
            eventContainer.getChildren().add(noEventsLabel);
        } else {
            for (Event event : eventsForDate) {
                HBox eventCard = createEventCard(event);
                eventContainer.getChildren().add(eventCard);
            }
        }
    }

    /**
     * Creates a visual card (HBox) to display an individual event.
     *
     * @param event the event to display
     * @return an HBox representing the event card
     */
    private HBox createEventCard(Event event) {
        HBox card = new HBox(10);
        card.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #dcdcdc; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label timeLabel = new Label("Time: " + event.getTime());
        timeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label eventLabel = new Label("Event: " + event.getName());
        eventLabel.setStyle("-fx-font-size: 14px;");

        card.getChildren().addAll(timeLabel, eventLabel);
        return card;
    }

    /**
     * Inner class to represent an Event.
     * Replace this with your actual data model for events in real applications.
     */
    private static class Event {
        private final String name;
        private final LocalDate date;
        private final String time;

        public Event(String name, LocalDate date, String time) {
            this.name = name;
            this.date = date;
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }
    }
}