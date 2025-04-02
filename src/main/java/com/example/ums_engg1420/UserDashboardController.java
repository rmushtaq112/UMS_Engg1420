package com.example.ums_engg1420;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

public class UserDashboardController {

    @FXML private StackPane mainContent;
    @FXML private Label lblWelcome;
    @FXML private Button btnDashboard;
    @FXML private Button btnMyCourses;
    @FXML private Button btnSubjects;
    @FXML private Button btnFacultyLogin;  // Added Faculty Login button
    @FXML private Button btnFaculty;
    @FXML private Button btnEvents;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        lblWelcome.setText("Welcome, User!");

        btnDashboard.setOnAction(e -> loadPage("StudentManagement.fxml"));
        btnMyCourses.setOnAction(e -> loadPage("UserCourseManagement.fxml"));
        btnSubjects.setOnAction(e -> loadPage("UserSubjects.fxml"));
        btnFaculty.setOnAction(e -> loadPage("UserFacultyDashboard.fxml"));
        btnFacultyLogin.setOnAction(e -> loadPage("FacultyDashboard.fxml"));
        btnEvents.setOnAction(e -> loadPage("StudentEventManagement.fxml"));
        btnLogout.setOnAction(e -> logout());
    }

    private void loadPage(String fxmlFile) {
        try {
            // Load the new page (FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newPage = loader.load();

            // Replace the current content in the StackPane with the new page
            mainContent.getChildren().setAll(newPage);
        } catch (IOException e) {
            // Print stack trace for debugging
            e.printStackTrace();

            // Show an alert to notify the user that the page could not be loaded
            showErrorAlert("Page Load Error", "Could not load the page: " + fxmlFile);
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void logout() {
        // Close the current window (dashboard)
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();

        try {
            // Load the login page into a new stage
            Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new javafx.scene.Scene(root, 300, 250));
            loginStage.show();
        } catch (IOException e) {
            // Handle the IOException if the LoginPage.fxml cannot be loaded
            e.printStackTrace();
            showErrorAlert("Login Page Load Error", "Could not load the Login Page.");
        }
    }
}

