package com.example.ums_engg1420;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    @FXML private Button btnFaculty;
    @FXML private Button btnEvents;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        lblWelcome.setText("Welcome, User!");

        btnDashboard.setOnAction(e -> loadPage("UserDashboardContent.fxml"));
        btnMyCourses.setOnAction(e -> loadPage("MyCourses.fxml"));
        btnSubjects.setOnAction(e -> loadPage("Subjects.fxml"));
        btnFaculty.setOnAction(e -> loadPage("Faculty.fxml"));
        btnEvents.setOnAction(e -> loadPage("Events.fxml"));
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
