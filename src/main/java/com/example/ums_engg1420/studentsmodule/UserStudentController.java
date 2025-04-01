package com.example.ums_engg1420.studentsmodule;

import com.example.ums_engg1420.dataclasses.Student;
import com.example.ums_engg1420.dataparsers.StudentDataHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

public class UserStudentController {

    @FXML private ImageView profileImageView;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private TextField txtCourse;
    @FXML private TextField txtYear;

    @FXML private Button btnChangeProfilePicture;  // Disable this as profile picture can't be changed by the user

    private Student currentStudent;

    @FXML
    public void initialize() {
        // Load the current logged-in student
        currentStudent = StudentDataHandler.getLoggedInStudent();

        if (currentStudent != null) {
            // Display student info in the respective text fields
            txtFirstName.setText(currentStudent.getName());
            txtLastName.setText("");  // Assuming the full name is provided in one field, otherwise you can extract it
            txtEmail.setText(currentStudent.getEmail());
            txtPhone.setText(currentStudent.getTelephone());
            txtAddress.setText(currentStudent.getAddress());
            txtCourse.setText(currentStudent.getAcademicLevel());
            txtYear.setText("");  // If year data is available, add it here

            // Set the profile picture if available
            String profilePicPath = currentStudent.getProfilePhoto();
            if (profilePicPath != null && !profilePicPath.isEmpty()) {
                profileImageView.setImage(new Image("file:" + profilePicPath));
            }

            // Make fields non-editable (since user cannot edit their information)
            txtFirstName.setEditable(false);
            txtLastName.setEditable(false);
            txtEmail.setEditable(false);
            txtPhone.setEditable(false);
            txtAddress.setEditable(false);
            txtCourse.setEditable(false);
            txtYear.setEditable(false);

            // Disable the "Change Profile Picture" button since profile pic can't be changed
            btnChangeProfilePicture.setDisable(true);
        }
    }
}

