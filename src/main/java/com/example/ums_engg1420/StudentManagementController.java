package com.example.ums_engg1420;

import com.example.ums_engg1420.dataclasses.Student;
import com.example.ums_engg1420.dataparsers.StudentDataHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class StudentManagementController {

    @FXML private Label lblStudentID;
    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private TextField txtAddress;
    @FXML private TextField txtTelephone;
    @FXML private ImageView imgProfilePhoto;
    @FXML private Button btnUploadPhoto;
    @FXML private Button btnSaveChanges;
    @FXML private Button btnBack;

    private Student currentStudent;
    private String profilePhotoPath = "";

    @FXML
    public void initialize() {
        loadStudentProfile();

        btnUploadPhoto.setOnAction(event -> uploadProfilePhoto());
        btnSaveChanges.setOnAction(event -> saveChanges());
        btnBack.setOnAction(event -> goBackToDashboard());
    }

    private void loadStudentProfile() {
        currentStudent = StudentDataHandler.getLoggedInStudent();

        if (currentStudent != null) {
            lblStudentID.setText(currentStudent.getStudentId());
            lblName.setText(currentStudent.getName());
            lblEmail.setText(currentStudent.getEmail());
            txtAddress.setText(currentStudent.getAddress());
            txtTelephone.setText(currentStudent.getTelephone());

            // Load profile photo if exists
            if (currentStudent.getProfilePhoto() != null && !currentStudent.getProfilePhoto().isEmpty()) {
                imgProfilePhoto.setImage(new Image("file:" + currentStudent.getProfilePhoto()));
            }
        }
    }

    private void uploadProfilePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            profilePhotoPath = file.getAbsolutePath();
            imgProfilePhoto.setImage(new Image("file:" + profilePhotoPath));
        }
    }

    private void saveChanges() {
        if (currentStudent != null) {
            currentStudent.setAddress(txtAddress.getText());
            currentStudent.setTelephone(txtTelephone.getText());

            if (!profilePhotoPath.isEmpty()) {
                currentStudent.setProfilePhoto(profilePhotoPath);
            }

            // Save updated data to Excel
            StudentDataHandler.updateProfile(currentStudent, StudentDataHandler.getStudentRowIndex(currentStudent.getStudentId()));

            // Confirmation alert
            System.out.println("Profile updated successfully!");
        }
    }

    private void goBackToDashboard() {
        try {
            Stage stage = (Stage) btnBack.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("UserDashboard.fxml"));
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle("User Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
