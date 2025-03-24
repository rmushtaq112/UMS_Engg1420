package com.example.ums_engg1420.studentsmodule;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.time.LocalDate;

public class UserStudentController {

    @FXML private DatePicker calendarPicker; // FXML link for DatePicker
    @FXML private Label selectedDateLabel; // FXML link for the selected date label
    @FXML private VBox eventContainer; // FXML link for event container

    @FXML
    public void initialize() {
        // Initialization logic
        calendarPicker.setValue(LocalDate.now());
        selectedDateLabel.setText("Events for: " + LocalDate.now());
    }

    @FXML
    public void viewEvents() {
        LocalDate selectedDate = calendarPicker.getValue();
        if (selectedDate != null) {
            selectedDateLabel.setText("Events for: " + selectedDate);
            // Additional logic to load events for the selected date
        }
    }
}