package com.example.ums_engg1420;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLogin() {
        String username = userField.getText();
        String password = passField.getText();

        if (isValidUser(username, password)) {
            messageLabel.setText("Login Successful!");
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }

    private boolean isValidUser(String username, String password) {
        return "admin".equals(username) && "password123".equals(password);
    }
}
