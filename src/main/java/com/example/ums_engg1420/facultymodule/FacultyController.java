package com.example.ums_engg1420.facultymodule;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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

    @FXML private VBox facultyTableBox;
    @FXML private TableView<String> facultyTable;
    @FXML private TableColumn<String, String> subjectColumn;

    @FXML private Button uploadButton;  // Reference to the "Upload Profile Picture" button

    private String facultyName;
    private String facultyEmail;
    private String facultyDegree;
    private String facultyOffice;
    private String facultyResearchInterest;

    @FXML
    private void handleFacultyLogin() {
        System.out.println("Login button clicked!");
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
                org.apache.poi.ss.usermodel.Cell emailCell = row.getCell(2);
                if (emailCell != null && emailCell.getStringCellValue().equalsIgnoreCase(email)) {
                    facultyName = row.getCell(0).getStringCellValue();
                    facultyDegree = row.getCell(1).getStringCellValue();
                    facultyEmail = email;
                    facultyOffice = row.getCell(3).getStringCellValue();
                    facultyResearchInterest = row.getCell(4).getStringCellValue();
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

        // Configure subjectColumn to display subjects
        subjectColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));

        loadFacultySubjects();
        facultyTableBox.setVisible(true);
        facultyTable.setVisible(true);

        // Make the "Upload Profile Picture" button visible
        uploadButton.setVisible(true);

        Stage stage = (Stage) loginBox.getScene().getWindow();
        stage.setTitle("Faculty Dashboard");
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

    private void loadFacultySubjects() {
        String filePath = "facultysubjects.xlsx";
        ObservableList<String> subjects = FXCollections.observableArrayList();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                org.apache.poi.ss.usermodel.Cell facultyNameCell = row.getCell(2);
                org.apache.poi.ss.usermodel.Cell subjectCell = row.getCell(1);

                if (facultyNameCell != null && subjectCell != null &&
                        facultyNameCell.getStringCellValue().equalsIgnoreCase(facultyName)) {
                    subjects.add(subjectCell.getStringCellValue());
                }
            }
            facultyTable.setItems(subjects);
        } catch (IOException e) {
            showError("Error reading faculty subjects.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @FXML
    private void handleUploadProfilePicture() {
        // Create a FileChooser instance
        FileChooser fileChooser = new FileChooser();

        // Set the extension filter for image files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the file chooser dialog
        Stage stage = (Stage) loginBox.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // If a file is selected, update the profile image view
        if (selectedFile != null) {
            try {
                // Load and display the selected image
                Image image = new Image(selectedFile.toURI().toString());
                profileImageView.setImage(image);
            } catch (Exception e) {
                showError("Error loading the image.");
            }
        }
    }
}
