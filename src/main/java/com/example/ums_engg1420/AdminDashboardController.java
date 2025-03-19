package com.example.ums_engg1420;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminDashboardController {

    @FXML private StackPane mainContent;
    @FXML private Label lblWelcome;
    @FXML private Button btnDashboard;
    @FXML private Button btnStudentManagement;
    @FXML private Button btnCourseManagement;
    @FXML private Button btnFacultyManagement;
    @FXML private Button btnEventManagement;
    @FXML private Button btnSubjectManagement;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        lblWelcome.setText("Welcome, Admin!");

        btnDashboard.setOnAction(e -> loadPage("Dashboard.fxml"));
        btnStudentManagement.setOnAction(e -> loadPage("AdminStudentManagement.fxml"));
        btnCourseManagement.setOnAction(e -> loadPage("CourseManagement.fxml"));
        btnFacultyManagement.setOnAction(e -> loadPage("FacultyManagement.fxml"));
        btnEventManagement.setOnAction(e -> loadPage("EventManagement.fxml"));
        btnSubjectManagement.setOnAction(e -> loadPage("SubjectManagement.fxml"));

        btnLogout.setOnAction(e -> logout());
    }

    private void loadPage(String fxmlFile) {
        try {
            Parent newPage = FXMLLoader.load(getClass().getResource(fxmlFile));
            mainContent.getChildren().setAll(newPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new javafx.scene.Scene(root, 300, 250));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
