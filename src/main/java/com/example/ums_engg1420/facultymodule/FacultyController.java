package com.example.ums_engg1420.facultymodule;

// These are import statements that bring in tools we need for our program
// JavaFX is for making the user interface (buttons, text fields, etc.)
// Apache POI is for working with Excel files
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

// This is our main class - it controls the faculty module of our university system
// It's an example of encapsulation because it keeps related data and methods together
public class FacultyController {

    // These variables with @FXML are connected to elements in our user interface
    // They're private (encapsulation) so other classes can't mess with our UI directly
    @FXML private VBox loginBox;           // Container for login elements
    @FXML private PasswordField passwordField;  // Where faculty enters password
    @FXML private Label errorLabel;        // Shows error messages
    @FXML private TextField emailField;    // Where faculty enters email
    @FXML private Label welcomeLabel;      // Displays welcome message after login

    @FXML private ImageView profileImageView;    // Shows faculty profile picture
    @FXML private Label degreeLabel;             // Shows faculty degree
    @FXML private Label facultyEmailLabel;       // Shows faculty email
    @FXML private Label officeLabel;             // Shows faculty office location
    @FXML private Label researchInterestLabel;   // Shows faculty research interests

    @FXML private VBox facultyTableBox;    // Container for the subjects table
    @FXML private TableView<String> facultyTable;  // Table showing faculty's subjects
    @FXML private TableColumn<String, String> subjectColumn;  // Column for subject names

    @FXML private Button uploadButton;          // Button to upload profile picture
    @FXML private Button changePasswordButton;  // Button to change password
    @FXML private HBox buttonBox;               // Container for buttons

    // These variables store information about the faculty member
    // They're private (encapsulation) - we control how this data is accessed
    private String facultyName;
    private String facultyEmail;
    private String facultyDegree;
    private String facultyOffice;
    private String facultyResearchInterest;

    // This method runs when the login button is clicked
    // It checks if the email and password are correct
    @FXML
    private void handleFacultyLogin() {
        System.out.println("Login button clicked!");
        String email = emailField.getText().trim();  // Get email and remove spaces
        String password = passwordField.getText();   // Get password

        // Check if email is empty
        if (email.isEmpty()) {
            showError("Please enter a valid email.");
            return;
        }

        // Check if password is "faculty" (very simple security!)
        if ("faculty".equals(password)) {
            // Try to load faculty information using the email
            if (loadFacultyInfo(email)) {
                System.out.println("Login successful for email: " + facultyEmail);
                loginBox.setVisible(false);  // Hide login form
                showWelcomeMessage();        // Show faculty dashboard
            } else {
                showError("Email not found. Try again.");
            }
        } else {
            showError("Incorrect password. Try again.");
        }
    }

    // This method tries to find faculty information in an Excel file
    // It demonstrates how we can read data from external sources
    private boolean loadFacultyInfo(String email) {
        String filePath = "facultymembers.xlsx";  // Path to Excel file
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            // Open the Excel file

            Sheet sheet = workbook.getSheetAt(0);  // Get first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();  // Skip header row

            // Look through each row to find matching email
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                org.apache.poi.ss.usermodel.Cell emailCell = row.getCell(2);
                if (emailCell != null && emailCell.getStringCellValue().equalsIgnoreCase(email)) {
                    // Found matching email! Save faculty information
                    facultyName = row.getCell(0).getStringCellValue();
                    facultyDegree = row.getCell(1).getStringCellValue();
                    facultyEmail = email;
                    facultyOffice = row.getCell(3).getStringCellValue();
                    facultyResearchInterest = row.getCell(4).getStringCellValue();
                    return true;  // Success!
                }
            }
        } catch (IOException e) {
            showError("Error reading faculty data.");
        }
        return false;  // Email not found
    }

    // This method updates the UI to show the faculty dashboard after login
    // It demonstrates how we can update many UI elements at once
    private void showWelcomeMessage() {
        welcomeLabel.setText("Welcome, " + facultyName + "!");
        welcomeLabel.setVisible(true);
        loadProfileImage();  // Load default profile picture

        // Update labels with faculty information
        degreeLabel.setText("Degree: " + facultyDegree);
        facultyEmailLabel.setText("Email: " + facultyEmail);
        officeLabel.setText("Office: " + facultyOffice);
        researchInterestLabel.setText("Research Interest: " + facultyResearchInterest);

        // Make labels visible
        degreeLabel.setVisible(true);
        facultyEmailLabel.setVisible(true);
        officeLabel.setVisible(true);
        researchInterestLabel.setVisible(true);

        // Set up the subjects table
        subjectColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));

        loadFacultySubjects();  // Load subjects from Excel file
        facultyTableBox.setVisible(true);
        facultyTable.setVisible(true);

        // Show buttons for profile management
        buttonBox.setVisible(true);

        // Update window title
        Stage stage = (Stage) loginBox.getScene().getWindow();
        stage.setTitle("Faculty Dashboard");
    }

    // This method loads a default profile image
    // It shows how to load resources from your project
    private void loadProfileImage() {
        URL imageUrl = getClass().getResource("/com/example/ums_engg1420/images/Default.jpg");
        if (imageUrl != null) {
            profileImageView.setImage(new Image(imageUrl.toExternalForm()));
        } else {
            System.out.println("Profile image not found!");
        }
        profileImageView.setVisible(true);
    }

    // This method loads the subjects taught by this faculty member
    // It reads from another Excel file and shows inheritance - XSSFWorkbook inherits from Workbook
    private void loadFacultySubjects() {
        String filePath = "facultysubjects.xlsx";
        // ObservableList is a special list that notifies the UI when it changes
        ObservableList<String> subjects = FXCollections.observableArrayList();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();  // Skip header row

            // Look through each row for subjects taught by this faculty
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                org.apache.poi.ss.usermodel.Cell facultyNameCell = row.getCell(2);
                org.apache.poi.ss.usermodel.Cell subjectCell = row.getCell(1);

                // If faculty name matches, add subject to our list
                if (facultyNameCell != null && subjectCell != null &&
                        facultyNameCell.getStringCellValue().equalsIgnoreCase(facultyName)) {
                    subjects.add(subjectCell.getStringCellValue());
                }
            }
            facultyTable.setItems(subjects);  // Update table with subjects
        } catch (IOException e) {
            showError("Error reading faculty subjects.");
        }
    }

    // This is a helper method to show error messages
    // It's an example of abstraction - hiding complex details behind a simple method
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    // This method runs when the "Upload Profile Picture" button is clicked
    // It shows polymorphism - the FileChooser can work with different types of files
    @FXML
    private void handleUploadProfilePicture() {
        // Create a file picker dialog
        FileChooser fileChooser = new FileChooser();

        // Only allow image files (.png, .jpg, etc.)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the dialog and get the selected file
        Stage stage = (Stage) loginBox.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // If a file was selected, update the profile picture
        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                profileImageView.setImage(image);
            } catch (Exception e) {
                showError("Error loading the image.");
            }
        }
    }

    // This method runs when the "Change Password" button is clicked
    // It creates a new window for changing password
    @FXML
    private void handleChangePassword() {
        // Create a container for our password change form
        VBox changePasswordBox = new VBox();
        changePasswordBox.setSpacing(10);

        // Create text field for new password
        TextField newPasswordField = new TextField();
        newPasswordField.setPromptText("Enter new password");

        // Create button to confirm password change
        Button confirmButton = new Button("Confirm");

        // Set what happens when confirm button is clicked
        // This is an example of abstraction - hiding complex code in a simple action
        confirmButton.setOnAction(event -> {
            String newPassword = newPasswordField.getText().trim();

            if (newPassword.isEmpty()) {
                showError("Please enter a valid new password.");
            } else {
                // In a real app, we would save the new password somewhere
                showConfirmation("Password changed successfully.");
                newPasswordField.clear();
            }
        });

        // Add components to our container
        changePasswordBox.getChildren().addAll(newPasswordField, confirmButton);

        // Create and show a new window
        Stage stage = new Stage();
        stage.setTitle("Change Password");
        stage.setScene(new javafx.scene.Scene(changePasswordBox, 300, 150));
        stage.show();
    }

    // This method shows a confirmation message in a popup
    // It's another example of abstraction - hiding details behind a simple method
    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}