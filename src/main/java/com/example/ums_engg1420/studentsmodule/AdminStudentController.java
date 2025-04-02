package com.example.ums_engg1420.studentsmodule;

// Import JavaFX Packages
import com.example.ums_engg1420.dataclasses.Student;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.example.ums_engg1420.dataparsers.StudentDataHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.ums_engg1420.dataparsers.StudentDataHandler.*;
import static com.example.ums_engg1420.dataparsers.SubjectDataHandler.removeSubject;

public class AdminStudentController extends StudentModuleInitializer {

    // Initialize GUI Functions
    @FXML private StackPane mainContent;
    @FXML private TableView<Student> tblStudents;
    @FXML private TableColumn<Student, String> colStuName;
    @FXML private TableColumn<Student, String> colStuID;
    @FXML private TableColumn<Student, String> colStuPic;
    @FXML private TableColumn<Student, String> colStuEmail;
    @FXML private TableColumn<Student, String> colStuPhone;
    @FXML private TableColumn<Student, String> colStuTuition;
    @FXML private TableColumn<Student, String> colStuAcaProg;
    @FXML private Button btnAddStudent;
    @FXML private Button btnDeleteStudent;
    @FXML private TextField txtName;
    @FXML private TextField txtID;
    @FXML private TextField txtPP;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtTuitionStatus;
    @FXML private TextField txtAcademicProg;
    @FXML private TextField txtTuition;
    @FXML private TextField txtSearch;

    // Initialize Column Names and Button Actions
    @FXML
    public void initialize() {
        setupColumns();
        loadStudentDetails((Student) workbook.getSheet("Students "));
        btnAddStudent.setOnAction(e -> addStudent());
        btnDeleteStudent.setOnAction(e -> deleteStudent());
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> loadStudents(newValue)); // Filter students dynamically
    }

    // Set up the columns for student information
    private void setupColumns() {
        colStuName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStuID.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colStuPic.setCellValueFactory(new PropertyValueFactory<>("profilePhoto"));
        colStuEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colStuPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        colStuTuition.setCellValueFactory(new PropertyValueFactory<>("Tuition"));
        colStuAcaProg.setCellValueFactory(new PropertyValueFactory<>("Academic Progress"));

    }

    // Load and filter students based on search criteria
    private void loadStudents(String filter) {
        ObservableList<Student> students = FXCollections.observableArrayList(getLoggedInStudent());
        if (!filter.isEmpty()) {
            students = students.filtered(student -> student.getName().toLowerCase().contains(filter.toLowerCase()) ||
                    student.getStudentId().toLowerCase().contains(filter.toLowerCase()));
        }
        tblStudents.setItems(students);
    }

    // Load student details into the view
    private void loadStudentDetails(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentModuleInitializer.fxml"));
            Parent studentView = loader.load();
            StudentModuleInitializer controller = loader.getController();
            controller.setStudent(student);
            mainContent.getChildren().setAll(studentView);
        } catch (IOException e) {
            showAlert("Error", "Could not load student details. Please try again.");
        }
    }

    // Add a new student
    private void addStudent() {
        String name = txtName.getText().trim();
        String id = txtID.getText().trim();
        String phone = txtTelephone.getText().trim();
        String profile = txtPP.getText().trim();
        String email = txtEmail.getText().trim();
        String tuition = txtTuitionStatus.getText().trim();
        String acaprog = txtAcademicProg.getText().trim();
        String debt = txtTuition.getText().trim();

        if (name.isEmpty() || id.isEmpty() || phone.isEmpty() || profile.isEmpty() || email.isEmpty() || tuition.isEmpty() || acaprog.isEmpty() || debt.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        Student newStudent = new Student(name, id);
        try {
            writeNewStudent(newStudent);
            loadStudents(""); // Refresh the table
            clearInputFields();
        } catch (Exception e) {
            showAlert("Error", "Student already exists or could not be added.");
        }
    }

    private void writeNewStudent(Student newStudent) {
        String filePath = String.valueOf(workbook.getSheet("Students ")); // Replace with the actual file path

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Access the first sheet
            Sheet sheet = workbook.getSheetAt(0);

            // Create a new row at the end of the sheet
            int rowCount = sheet.getLastRowNum();
            Row newRow = sheet.createRow(rowCount + 1);

            // Populate the row with student data
            newRow.createCell(0).setCellValue(newStudent.getName());
            newRow.createCell(1).setCellValue(newStudent.getStudentId());
            newRow.createCell(2).setCellValue(newStudent.getProfilePhoto());
            newRow.createCell(3).setCellValue(newStudent.getEmail());
            newRow.createCell(4).setCellValue(newStudent.getTelephone());
            newRow.createCell(5).setCellValue(newStudent.getTuitionStatus());
            newRow.createCell(6).setCellValue(newStudent.getAcademicLevel());
            newRow.createCell(7).setCellValue(newStudent.getTuitionStatus());

            // Write the updated workbook back to the file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            System.out.println("New student added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Unable to write to the Excel file.");
        }
    }

        // Delete selected student
    private void deleteStudent() {
        Student selected = tblStudents.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                removeSubject(tblStudents.getItems().indexOf(selected) + 1);
                loadStudents(""); // Refresh the table
            } catch (Exception e) {
                showAlert("Error", "Could not delete student.");
            }
        } else {
            showAlert("Error", "Please select a student to delete.");
        }
    }

    // Clear input fields after adding a student
    private void clearInputFields() {
        txtName.clear();
        txtID.clear();
        txtTelephone.clear();
        txtPP.clear();
        txtEmail.clear();
        txtTuitionStatus.clear();
        txtAcademicProg.clear();
        txtTuition.clear();
    }

    // Display alert messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
