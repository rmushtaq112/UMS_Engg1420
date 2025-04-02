package com.example.ums_engg1420.studentsmodule;

import com.example.ums_engg1420.dataclasses.Student;
import com.example.ums_engg1420.dataparsers.StudentDataHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class StudentModuleInitializer {

    @FXML private Label lblStudentID;
    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblTelephone;
    @FXML private Label lblAcademicLevel;
    @FXML private Label lblProgress;
    @FXML private Label lblTuitionStatus;

    @FXML private Button btnViewProgress;
    @FXML private Button btnCheckTuition;

    private Student currentStudent;

    @FXML
    public void initialize() {
        loadStudentProfile();
    }

    private void loadStudentProfile() {
        // Fetch the current logged-in student using StudentDataHandler
        currentStudent = StudentDataHandler.getLoggedInStudent(); // Placeholder function to retrieve student data

        if (currentStudent != null) {
            lblStudentID.setText("Student ID: " + currentStudent.getStudentId());
            lblName.setText("Name: " + currentStudent.getName());
            lblEmail.setText("Email: " + currentStudent.getEmail());
            lblTelephone.setText("Telephone: " + currentStudent.getTelephone());
            lblAcademicLevel.setText("Academic Level: " + currentStudent.getAcademicLevel());
            lblProgress.setText("Progress: " + currentStudent.getProgress() + "%");
            lblTuitionStatus.setText("Tuition Status: " + StudentDataHandler.getTuitionStatus(getStudentRowIndex()));
        }
    }

    @FXML
    private void viewProgress() {
        // Show the student's progress in an alert box
        if (currentStudent != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Progress Report");
            alert.setHeaderText("Your Academic Progress");
            alert.setContentText("Current progress: " + currentStudent.getProgress() + "%");
            alert.showAndWait();
        }
    }

    @FXML
    private void checkTuitionStatus() {
        // Show the student's tuition status in an alert box
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tuition Status");
        alert.setHeaderText("Your Tuition Fee Status");
        alert.setContentText("Tuition Status: " + StudentDataHandler.getTuitionStatus(getStudentRowIndex()));
        alert.showAndWait();
    }

    private int getStudentRowIndex() {
        // Placeholder logic to fetch the row index for the logged-in student (this should be dynamically fetched)
        return 1; // For now, assuming we fetch the student at index 1
    }

    protected void setStudent(Student student) {
        this.currentStudent = student;
        loadStudentProfile(); // Update profile with new student data
    }
}


