package com.example.ums_engg1420.coursesmodule;
import com.example.ums_engg1420.dataclasses.Course;

//Import JavaFX Packages
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AdminCourseController extends CourseModuleInitializer{
    //Initialize Variables

    @FXML
    private Button btnAddSubject1;

    @FXML
    private Button btnDeleteSubject;

    @FXML
    private Button btnEditSubject;

    @FXML
    private Button btnSearch;

    @FXML
    private TableColumn<Course, String> colCapacity;

    @FXML
    private TableColumn<Course, String> colCourseCode;

    @FXML
    private TableColumn<Course, String> colCourseName;

    @FXML
    private TableColumn<Course, String> colFinalExamDate;

    @FXML
    private TableColumn<Course, String> colLectureTime;

    @FXML
    private TableColumn<Course, String> colLocation;

    @FXML
    private TableColumn<Course, String> colSectionNumber;

    @FXML
    private TableColumn<Course, String> colSubjectCode;

    @FXML
    private TableColumn<Course, String> colTeacherName;

    @FXML
    private TableView<Course> tblCourses;

    @FXML
    private TextField txtCapacity;

    @FXML
    private TextField txtCourseCode;

    @FXML
    private TextField txtCourseName;

    @FXML
    private TextField txtFinalExamDate;

    @FXML
    private TextField txtLectureTime;

    @FXML
    private TextField txtLocation;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtSectionNumber;

    @FXML
    private TextField txtSubjectCode;

    @FXML
    private TextField txtTeacherName;

    public void initialize() {
        tableInitialize(colCourseName, colCourseCode, colSubjectCode, colSectionNumber, colCapacity, colLectureTime, colFinalExamDate, colLocation, colTeacherName);
        tblCourses.setItems(courses);

    }


}