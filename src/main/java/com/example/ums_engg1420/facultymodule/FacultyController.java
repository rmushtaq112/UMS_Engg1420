package com.example.ums_engg1420.facultymodule;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

public class FacultyController {

    @FXML private VBox loginBox;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private TextField emailField;
    @FXML private Label welcomeLabel;

    @FXML private ImageView profileImageView;
    @FXML private Label degreeLabel;
    @FXML private Label facultyEmailLabel;
    @FXML private Label officeLabel;
    @FXML private Label researchInterestLabel;

    private String facultyName;
    private String facultyEmail;
    private String facultyDegree;
    private String facultyOffice;
    private String facultyResearchInterest;

    @FXML
    private void handleFacultyLogin() {
        System.out.println("Login button clicked!"); // Debugging

        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty()) {
            showError("Please enter a valid email.");
            return;
        }

        if ("faculty".equals(password)) {
            if (loadFacultyInfo(email)) {
                System.out.println("Login successful for email: " + facultyEmail);
                loginBox.setVisible(false);
                showWelcomeMessage();
            } else {
                showError("Email not found. Try again.");
            }
        } else {
            showError("Incorrect password. Try again.");
        }
    }

    private boolean loadFacultyInfo(String email) {
        String filePath = "facultymembers.xlsx";
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell emailCell = row.getCell(2); // Email column index 2
                if (emailCell != null && emailCell.getStringCellValue().equalsIgnoreCase(email)) {
                    facultyName = row.getCell(0).getStringCellValue(); // Name column index 0
                    facultyDegree = row.getCell(1).getStringCellValue(); // Degree column index 1
                    facultyEmail = email;
                    facultyOffice = row.getCell(3).getStringCellValue(); // Office column index 3
                    facultyResearchInterest = row.getCell(4).getStringCellValue(); // Research Interest column index 4
                    return true;
                }
            }
        } catch (IOException e) {
            showError("Error reading faculty data.");
        }
        return false;
    }

    private void showWelcomeMessage() {
        welcomeLabel.setText("Welcome, " + facultyName + "!");
        welcomeLabel.setVisible(true);

        loadProfileImage();

        degreeLabel.setText("Degree: " + facultyDegree);
        facultyEmailLabel.setText("Email: " + facultyEmail);
        officeLabel.setText("Office: " + facultyOffice);
        researchInterestLabel.setText("Research Interest: " + facultyResearchInterest);

        degreeLabel.setVisible(true);
        facultyEmailLabel.setVisible(true);
        officeLabel.setVisible(true);
        researchInterestLabel.setVisible(true);

        Stage stage = (Stage) loginBox.getScene().getWindow();
        stage.setTitle("Faculty Dashboard");

        // Pause for 2 seconds before loading dashboard
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> loadFacultyDashboard());
        delay.play();
    }

    private void loadProfileImage() {
        URL imageUrl = getClass().getResource("/com/example/ums_engg1420/images/Default.jpg");
        if (imageUrl != null) {
            profileImageView.setImage(new Image(imageUrl.toExternalForm()));
        } else {
            System.out.println("Profile image not found!");
        }
        profileImageView.setVisible(true);
    }

    private void loadFacultyDashboard() {
        System.out.println("Navigating to Faculty Dashboard...");
        // Add logic to load the Faculty Dashboard scene if needed
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
