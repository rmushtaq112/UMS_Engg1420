package com.example.ums_engg1420.facultymodule;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
    @FXML private Label emailLabel; // Display email for login
    @FXML private Label welcomeLabel;

    private String facultyEmail; // Stores faculty email from Excel

    @FXML
    public void initialize() {
        loadFacultyEmail();
    }

    private void loadFacultyEmail() {
        String filePath = "facultymembers.xlsx";
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Assume first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip header row

            if (rowIterator.hasNext()) {
                Row row = rowIterator.next(); // Read first faculty entry
                Cell emailCell = row.getCell(2); // 3rd column (index 2)

                if (emailCell != null) {
                    facultyEmail = emailCell.getStringCellValue();
                    // Ensure the emailLabel displays the text 'Enter Email:' not the email itself
                    emailLabel.setText("Enter Email:");
                } else {
                    showError("No email found in faculty records.");
                }
            } else {
                showError("Faculty list is empty.");
            }
        } catch (IOException e) {
            showError("Error reading faculty data.");
        }
    }

    @FXML
    private void handleFacultyLogin() {
        String password = passwordField.getText();

        if ("faculty".equals(password)) {
            System.out.println("Login successful for email: " + facultyEmail); // Console log on successful login
            loginBox.setVisible(false);
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

            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome, " + facultyEmail + "!");
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
