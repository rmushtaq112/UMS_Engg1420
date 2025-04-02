package com.example.ums_engg1420.studentsmodule;

import com.example.ums_engg1420.dataclasses.Student;
import com.example.ums_engg1420.dataparsers.StudentDataHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class UserStudentController extends StudentModuleInitializer {

    // FXML components
    @FXML private StackPane mainContent;
    @FXML private Label lblFullName;
    @FXML private Label lblStudentId;
    @FXML private Label lblEmail;
    @FXML private Label lblPhone;
    @FXML private Label lblAddress;
    @FXML private Label lblTuitionStatus;
    @FXML private Label lblCurrentSemester;
    @FXML private Label lblSubjectsRegistered;
    @FXML private Label lblAcademicLevel;
    @FXML private Label lblThesisTitle;
    @FXML private Label lblProgress;
    @FXML private ImageView profileImageView;

    // Buttons for navigation
    @FXML private Button btnProfile;
    @FXML private Button btnStudent;
    @FXML private Button btnEvents;
    @FXML private Button btnLogout;

    private Student currentStudent;

    @Override
    public void initialize() {
        super.initialize();  // Call the parent method to load student profile

        // Initialize profile and button actions
        loadStudentProfile();

        // Setting up button actions for navigation
        btnProfile.setOnAction(e -> loadPage("UserDashboard.fxml"));
        btnStudent.setOnAction(e -> loadPage("StudentManagement.fxml"));
        btnEvents.setOnAction(e -> loadPage("EventManagement.fxml"));
        btnLogout.setOnAction(e -> logout());
    }

    private void loadStudentProfile() {
        // Fetch the current logged-in student using StudentDataHandler
        currentStudent = StudentDataHandler.getLoggedInStudent(); // Placeholder function to get the logged-in student

        if (currentStudent != null) {
            // Populate the labels with the student data
            lblFullName.setText("Full Name: " + currentStudent.getName());
            lblStudentId.setText("Student ID: " + currentStudent.getStudentId());
            lblEmail.setText("Email Address: " + currentStudent.getEmail());
            lblPhone.setText("Telephone: " + currentStudent.getTelephone());
            lblAddress.setText("Address: " + currentStudent.getAddress());
            lblTuitionStatus.setText("Tuition: " + currentStudent.getTuitionStatus());
            lblCurrentSemester.setText("Current Semester: " + currentStudent.getCurrentSemester());
            lblSubjectsRegistered.setText("Subjects Registered: " + currentStudent.getSubjectsRegistered());
            lblAcademicLevel.setText("Academic Level: " + currentStudent.getAcademicLevel());
            lblProgress.setText("Progress: " + currentStudent.getProgress() + "%");

            // For PhD students, display Thesis Title
            if (currentStudent.getAcademicLevel().equalsIgnoreCase("PhD")) {
                lblThesisTitle.setText("Thesis Title: " + currentStudent.getThesisTitle());
            } else {
                lblThesisTitle.setText("Thesis Title: N/A");
            }

            // Set the profile picture if available
            if (currentStudent.getProfilePhoto() != null && !currentStudent.getProfilePhoto().isEmpty()) {
                profileImageView.setImage(new Image("file:" + currentStudent.getProfilePhoto()));
            } else {
                profileImageView.setImage(new Image("file:default-profile-pic.jpg"));
            }
        }
    }

    @FXML
    private void goBackToDashboard() {
        // Logic for navigating back to the dashboard (can be empty if this is handled by btnProfile)
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newPage = loader.load();
            mainContent.getChildren().setAll(newPage);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Could not load " + fxmlFile);
        }
    }

    private void logout() {
    }
}



