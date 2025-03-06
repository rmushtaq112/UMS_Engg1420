package com.example.ums_engg1420;

//Import JavaFX Packages

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserCourseManagementController {
    //Initialize Variables

    @FXML private TableView<Course> tblSubjects;
    @FXML private TableColumn<Course, String> colCourseName;
    @FXML private TableColumn<Course, String> colCourseCode;
    @FXML private TableColumn<Course, String> colSubjectCode;
    @FXML private TableColumn<Course, String> colSectionNumber;
    @FXML private TableColumn<Course, String> colCapacity;
    @FXML private TableColumn<Course, String> colLectureTime;
    @FXML private TableColumn<Course, String> colFinalExamDate;
    @FXML private TableColumn<Course, String> colLocation;
    @FXML private TableColumn<Course, String> colTeacherName;

    public void initialize() {
        // Initialize the columns
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        colSubjectCode.setCellValueFactory(new PropertyValueFactory<>("subjectCode"));
        colSectionNumber.setCellValueFactory(new PropertyValueFactory<>("sectionNumber"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colLectureTime.setCellValueFactory(new PropertyValueFactory<>("lectureTime"));
        colFinalExamDate.setCellValueFactory(new PropertyValueFactory<>("finalExamDate"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colTeacherName.setCellValueFactory(new PropertyValueFactory<>("teacherName"));

    }


}//AdminCourseManagement