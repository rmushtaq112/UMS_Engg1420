package com.example.ums_engg1420.studentsmodule;

import com.example.ums_engg1420.dataclasses.Student;
import com.example.ums_engg1420.dataparsers.StudentDataHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private void viewProgress() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Progress Report");
        alert.setHeaderText("Your Academic Progress");
        alert.setContentText("Current progress: " + currentStudent.getProgress() + "%");
        alert.showAndWait();
    }

    @FXML
    private void checkTuitionStatus() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tuition Status");
        alert.setHeaderText("Your Tuition Fee Status");
        alert.setContentText("Tuition Status: " + StudentDataHandler.getTuitionStatus(getStudentRowIndex()));
        alert.showAndWait();
    }public void initialize() {
        loadStudentProfile();
    }

    private void loadStudentProfile() {
        // Assuming we fetch the current logged-in student
        currentStudent = StudentDataHandler.getLoggedInStudent(); // Placeholder function

        if (currentStudent != null) {
            lblStudentID.setText(currentStudent.getStudentId());
            lblName.setText(currentStudent.getName());
            lblEmail.setText(currentStudent.getEmail());
            lblTelephone.setText(currentStudent.getTelephone());
            lblAcademicLevel.setText(currentStudent.getAcademicLevel());
            lblProgress.setText(currentStudent.getProgress() + "%");
            lblTuitionStatus.setText(StudentDataHandler.getTuitionStatus(getStudentRowIndex()));
        }
    }


    private int getStudentRowIndex() {
        // Placeholder logic to fetch row index for the logged-in student
        return 1; // This should be dynamically fetched
    }
}

