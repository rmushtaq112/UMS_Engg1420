package com.example.ums_engg1420.facultymodule;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class FacultyController {

    @FXML private VBox loginBox;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private TextField emailField; // TextField for email input
    @FXML private Label welcomeLabel;

    private String facultyName; // Stores faculty name from Excel
    private String facultyEmail; // Stores faculty email from Excel

    @FXML
    public void initialize() {
        // No need to load faculty email anymore, since we are using the email input field
    }

    @FXML
    private void handleFacultyLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty()) {
            showError("Please enter a valid email.");
            return;
        }

        if ("faculty".equals(password)) {
            if (loadFacultyInfo(email)) {
                System.out.println("Login successful for email: " + facultyEmail); // Console log on successful login
                loginBox.setVisible(false);
                showWelcomeMessage();
            } else {
                errorLabel.setText("Email not found. Try again.");
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Incorrect password. Try again.");
            errorLabel.setVisible(true);
        }
    }

    private boolean loadFacultyInfo(String email) {
        String filePath = "facultymembers.xlsx";
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Assume first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell emailCell = row.getCell(2); // 3rd column (index 2) for email
                if (emailCell != null && emailCell.getStringCellValue().equalsIgnoreCase(email)) {
                    facultyName = row.getCell(0).getStringCellValue(); // 1st column (index 0) for name
                    facultyEmail = email;
                    return true;
                }
            }
        } catch (IOException e) {
            showError("Error reading faculty data.");
        }
        return false;
    }

    private void showWelcomeMessage() {
        // Show the welcome message in the center of the screen
        welcomeLabel.setText("Welcome, " + facultyName + "!");
        welcomeLabel.setVisible(true);

        // Center the welcome label in the scene
        Stage stage = (Stage) loginBox.getScene().getWindow();
        stage.setTitle("Faculty Login");

        // Optionally, delay the transition to the dashboard to show the welcome label for a few seconds
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Show welcome message for 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loadFacultyDashboard(); // Then load the faculty dashboard
        }).start();
    }

    private void loadFacultyDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FacultyDashboard.fxml"));
            Parent root = loader.load();

            // Optional: Set the welcome message if it was not already set
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome, " + facultyName + "!");
            }

            Stage stage = (Stage) loginBox.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Faculty Dashboard");
        } catch (IOException e) {
            showError("Failed to load Faculty Dashboard.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
