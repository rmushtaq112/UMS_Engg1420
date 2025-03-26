package com.example.ums_engg1420.facultymodule;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class FacultyController {

    @FXML private VBox loginBox;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleFacultyLogin() {
        String password = passwordField.getText();

        if ("faculty".equals(password)) {
            loginBox.setVisible(false);  // Hide login box
            loadFacultyDashboard();
        } else {
            errorLabel.setText("Incorrect password. Try again.");
            errorLabel.setVisible(true);
        }
    }

    private void loadFacultyDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FacultyDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginBox.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600)); // Adjust size as needed
            stage.setTitle("Faculty Dashboard");
        } catch (IOException e) {
            showAlert("Error", "Failed to load Faculty Dashboard.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
