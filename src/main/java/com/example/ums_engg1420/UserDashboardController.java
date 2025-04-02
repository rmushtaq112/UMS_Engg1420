package com.example.ums_engg1420;

import com.example.ums_engg1420.studentsmodule.UserStudentController;
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
    @FXML private Button btnFacultyLogin;
    @FXML private Button btnFaculty;
    @FXML private Button btnEvents;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        lblWelcome.setText("Welcome, User!");

        // Set up button actions
        btnDashboard.setOnAction(e -> loadPage("StudentInfo.fxml"));
        btnMyCourses.setOnAction(e -> loadPage("UserCourseManagement.fxml"));
        btnSubjects.setOnAction(e -> loadPage("UserSubjects.fxml"));
        btnFacultyLogin.setOnAction(e -> loadPage("FacultyDashboard.fxml"));
        btnFaculty.setOnAction(e -> loadPage("FacultyDashboard.fxml"));
        btnEvents.setOnAction(e -> loadPage("StudentEventManagement.fxml"));
        btnLogout.setOnAction(e -> logout());
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

            // Check if the page is StudentInfo.fxml, and if so, load with UserStudentController
            if (fxmlFile.equals("StudentInfo.fxml")) {
                loader.setController(new UserStudentController());  // Set UserStudentController for StudentInfo page
            }

            Parent newPage = loader.load();
            mainContent.getChildren().setAll(newPage);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Could not load " + fxmlFile);
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

