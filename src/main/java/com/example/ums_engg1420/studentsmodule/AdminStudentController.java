package com.example.ums_engg1420.studentsmodule;

import com.example.ums_engg1420.dataclasses.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.beans.property.SimpleStringProperty;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AdminStudentController extends StudentModuleInitializer {

    @FXML private StackPane mainContent;
    @FXML private Button btnDashboard;
    @FXML private Button btnStudentManagement;
    @FXML private Button btnEvents;
    @FXML private Button btnLogout;
    @FXML private Button btnAddStudent;
    @FXML private Button btnEditStudent;
    @FXML private Button btnDeleteStudent;

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> colStudentId, colName, colAddress, colTelephone, colEmail;
    @FXML private TableColumn<Student, String> colAcademicLevel, colProfilePhoto, colSubjectsRegistered;
    @FXML private TableColumn<Student, String> colThesisTitle, colPassword;
    @FXML private TableColumn<Student, Integer> colCurrentSemester;
    @FXML private TableColumn<Student, Double> colProgress;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @Override
    public void initialize() {
        super.initialize();  // Call the parent method to load student profile

        // Admin-specific initialization
        btnDashboard.setOnAction(e -> loadPage("AdminDashboard.fxml"));
        btnStudentManagement.setOnAction(e -> loadPage("AdminStudentManagement.fxml"));
        btnEvents.setOnAction(e -> loadPage("AdminEventManagement.fxml"));
        btnLogout.setOnAction(e -> logout());

        // Load student data from Excel
        loadStudentsFromExcel();

        // Set up the TableView columns
        setUpStudentTable();

        // Set up button actions
        btnAddStudent.setOnAction(e -> addStudent());
        btnEditStudent.setOnAction(e -> editStudent());
        btnDeleteStudent.setOnAction(e -> deleteStudent());
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
        // Logic for logging out the admin
    }

    private void setUpStudentTable() {
        // Set up table columns
        colStudentId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudentId()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        colTelephone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephone()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colAcademicLevel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAcademicLevel()));
        colCurrentSemester.setCellValueFactory(cellData -> cellData.getValue().currentSemesterProperty().asObject());
        colProfilePhoto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProfilePhoto()));
        colSubjectsRegistered.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectsRegistered()));
        colThesisTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getThesisTitle()));
        colProgress.setCellValueFactory(cellData -> cellData.getValue().progressProperty().asObject());
        colPassword.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));

        // Bind the TableView to the studentList
        studentTable.setItems(studentList);
    }

    private void loadStudentsFromExcel() {
        File file = new File("src/main/resources/com/example/ums_engg1420/student.xlsx");
        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fileIn)) {
                Sheet sheet = workbook.getSheetAt(0); // Assuming student data is in the first sheet

                // Read each row and add students to the studentList
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;  // Skip the header row

                    // Read data for each student
                    String studentId = row.getCell(0).getStringCellValue();
                    String name = row.getCell(1).getStringCellValue();
                    String address = row.getCell(2).getStringCellValue();
                    String telephone = row.getCell(3).getStringCellValue();
                    String email = row.getCell(4).getStringCellValue();
                    String academicLevel = row.getCell(5).getStringCellValue();
                    int currentSemester = (int) row.getCell(6).getNumericCellValue();  // assuming numeric value in Excel
                    String profilePhoto = row.getCell(7).getStringCellValue();
                    String subjectsRegistered = row.getCell(8).getStringCellValue();
                    String thesisTitle = row.getCell(9).getStringCellValue();
                    double progress = row.getCell(10).getNumericCellValue();  // assuming numeric value in Excel
                    String password = row.getCell(11).getStringCellValue();

                    // Create a new Student object and add it to the list
                    Student student = new Student(studentId, name, email, address, telephone, "", currentSemester, academicLevel,
                            thesisTitle, progress, password, profilePhoto, subjectsRegistered);
                    studentList.add(student);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR: Could not load student data from Excel file.");
            }
        }
    }

    // Method to add a new student
    private void addStudent() {
        // Open a new dialog or form for adding a student (can be another FXML file or a simple popup)
        // This can be handled via a simple modal or a new scene.
    }

    // Method to edit the selected student
    private void editStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            // Open a new dialog or form to edit the student details
            // Populate the form with the selected student's data.
        } else {
            showAlert("No student selected", "Please select a student to edit.");
        }
    }

    // Method to delete the selected student
    private void deleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            // Ask for confirmation before deleting
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Student");
            alert.setHeaderText("Are you sure you want to delete this student?");
            alert.setContentText("This action cannot be undone.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    studentList.remove(selectedStudent);
                    // Optionally, delete from the Excel or database here
                }
            });
        } else {
            showAlert("No student selected", "Please select a student to delete.");
        }
    }

    // Helper method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


