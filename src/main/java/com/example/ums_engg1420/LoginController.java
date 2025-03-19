package com.example.ums_engg1420;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LoginController {

    @FXML
    private TextField userField;
    @FXML
    private PasswordField passField;
    @FXML
    private Label messageLabel;

    @FXML
    private void handleLogin() {
        String username = userField.getText();
        String password = passField.getText();
        String role = authenticateUser(username, password); // Determines user role

        if (!role.equals("INVALID")) {
            if (role.equals("USER")) {
                promptUserType(); // Ask if they are a student or faculty
            } else {
                messageLabel.setText("Login Successful as " + role + "!");
                loadDashboard(role);
            }
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }

    private String authenticateUser(String username, String password) {
        if ("admin".equals(username) && "admin123".equals(password)) {
            return "ADMIN";
        } else if ("user".equals(username) && "user123".equals(password)) {
            return "USER"; // Generic user (will later choose student or faculty)
        }
        return "INVALID";
    }

    private void promptUserType() {
        // Create an Alert with choices
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("User Selection");
        alert.setHeaderText("Are you a Student or Faculty?");
        alert.setContentText("Choose your role:");

        ButtonType btnStudent = new ButtonType("Student");
        ButtonType btnFaculty = new ButtonType("Faculty");
        ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnStudent, btnFaculty, btnCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == btnStudent) {
                loadDashboard("USER"); // Redirect student to UserDashboard.fxml
            } else if (result.get() == btnFaculty) {
                loadDashboard("FACULTY");
            }
        }
    }

    private void loadDashboard(String role) {
        try {
            Stage stage = (Stage) userField.getScene().getWindow();
            Parent root;

            if (role.equals("ADMIN")) {
                root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
            } else if (role.equals("USER")) { // Student redirects to UserDashboard
                root = FXMLLoader.load(getClass().getResource("UserDashboard.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("UserDashboard.fxml")); //not faculty yet
            }

            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.setTitle("Dashboard - " + role);
            stage.show();
        } catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "Error loading dashboard", e);
        }
    }
}
