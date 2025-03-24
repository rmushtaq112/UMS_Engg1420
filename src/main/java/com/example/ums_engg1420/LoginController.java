package com.example.ums_engg1420;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
            messageLabel.setText("Login Successful as " + role + "!");
            loadDashboard(role);
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }

    private String authenticateUser(String username, String password) {
        if ("admin".equals(username) && "admin123".equals(password)) {
            return "ADMIN";
        } else if ("user".equals(username) && "user123".equals(password)) {
            return "USER"; // Now always directs to UserDashboard.fxml
        }
        return "INVALID";
    }

    private void loadDashboard(String role) {
        try {
            Stage stage = (Stage) userField.getScene().getWindow();
            Parent root;

            if (role.equals("ADMIN")) {
                root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("UserDashboard.fxml")); // Always loads user dashboard
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
