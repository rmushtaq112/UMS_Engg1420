package com.example.ums_engg1420;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

//student's, not faculty!!

public class UserDashboardController {

    @FXML private Label lblWelcome;
    @FXML private StackPane mainContent;
    @FXML private Button btnDashboard;
    @FXML private Button btnMyCourses;
    @FXML private Button btnSubjects;
    @FXML private Button btnFaculty;
    @FXML private Button btnEvents;
    @FXML private Button btnStudentManagement;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        lblWelcome.setText("Welcome, Student!");

        //Button Functionalities
        //btnDashboard.setOnAction((e -> loadPage()));
        btnMyCourses.setOnAction((e -> loadPage("UserCourseManagement.fxml")));
        btnSubjects.setOnAction((e -> loadPage("UserSubjects.fxml")));
        //btnFaculty.setOnAction((e -> loadPage()));
        //btnEvents.setOnAction((e -> loadPage()));
        // Event handler for Student Management button
        btnStudentManagement.setOnAction(event -> openStudentManagement());
        btnLogout.setOnAction(event -> logout());
    }

    //Calls on the fxml file to load a page
    private void loadPage(String fxmlFile) {
        try {
            Parent newPage = FXMLLoader.load(getClass().getResource(fxmlFile));
            mainContent.getChildren().setAll(newPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openStudentManagement() {
        try {
            Stage stage = (Stage) btnStudentManagement.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ums_engg1420/StudentManagement.fxml"));
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Student Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not load StudentManagement.fxml. Check the file path.");
        }
    }

    private void logout() {
        try {
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
