package com.example.ums_engg1420.studentsmodule;

import com.example.ums_engg1420.dataclasses.Student;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    private void addStudent() {
        try {
            // Open a new dialog or form for adding a student
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddStudentForm.fxml"));
            Parent addStudentPage = loader.load();
            Scene scene = new Scene(addStudentPage);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add New Student");
            stage.show();

            // Simulate a new student addition (replace with real form data)
            String studentId = generateStudentId(); // Auto-generate student ID
            String name = "New Student"; // Example form data
            String address = "New Address";
            String telephone = "123456789";
            String email = "newstudent@example.com";
            String academicLevel = "Undergraduate";
            int currentSemester = 1;
            String profilePhoto = ""; // Add logic to upload a photo
            String subjectsRegistered = "Math, Science";
            String thesisTitle = "";
            double progress = 0.0;
            String password = "password123";

            // Create a new student and add to the list
            Student newStudent = new Student(studentId, name, email, address, telephone, "", currentSemester, academicLevel,
                    thesisTitle, progress, password, profilePhoto, subjectsRegistered);

            studentList.add(newStudent);
            saveStudentsToExcel(); // Save the new student data to Excel

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Add Student form.");
        }
    }

    private String generateStudentId() {
        return "S" + System.currentTimeMillis(); // Generates an ID based on current timestamp
    }

    private void saveStudentsToExcel() {
        File file = new File("src/main/resources/com/example/ums_engg1420/student.xlsx");
        try (FileInputStream fileIn = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fileIn)) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming student data is in the first sheet
            int rowCount = sheet.getPhysicalNumberOfRows();

            // Add a new row for the new student
            Row row = sheet.createRow(rowCount);

            // Write student data to the new row
            Student student = studentList.get(rowCount - 1); // Get the last student added
            row.createCell(0).setCellValue(student.getStudentId());
            row.createCell(1).setCellValue(student.getName());
            row.createCell(2).setCellValue(student.getAddress());
            row.createCell(3).setCellValue(student.getTelephone());
            row.createCell(4).setCellValue(student.getEmail());
            row.createCell(5).setCellValue(student.getAcademicLevel());
            row.createCell(6).setCellValue(student.getCurrentSemester());
            row.createCell(7).setCellValue(student.getProfilePhoto());
            row.createCell(8).setCellValue(student.getSubjectsRegistered());
            row.createCell(9).setCellValue(student.getThesisTitle());
            row.createCell(10).setCellValue(student.getProgress());
            row.createCell(11).setCellValue(student.getPassword());

            // Write to the file
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save student data to Excel file.");
        }
    }

    private void editStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            // Open a new dialog or form to edit the student details
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditStudentForm.fxml"));
            try {
                Parent editStudentPage = loader.load();
                Scene scene = new Scene(editStudentPage);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Edit Student");
                stage.show();

                // Assuming EditStudentForm.fxml is where you edit the student info
                // Once the form is filled, retrieve updated values and update the student

                selectedStudent.setName("Updated Name"); // Example update
                selectedStudent.setAddress("Updated Address");
                selectedStudent.setEmail("updated@example.com");
                // Update other fields as necessary...

                saveStudentsToExcel(); // Save the updated data back to Excel
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open Edit Student form.");
            }
        } else {
            showAlert("No student selected", "Please select a student to edit.");
        }
    }

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
                    deleteStudentFromExcel(selectedStudent); // Remove from Excel
                }
            });
        } else {
            showAlert("No student selected", "Please select a student to delete.");
        }
    }

    private void deleteStudentFromExcel(Student student) {
        File file = new File("src/main/resources/com/example/ums_engg1420/student.xlsx");
        try (FileInputStream fileIn = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fileIn)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Iterate over rows and find the row corresponding to the student
            for (Row row : sheet) {
                if (row.getCell(0).getStringCellValue().equals(student.getStudentId())) {
                    // Delete the student row
                    sheet.removeRow(row);
                    break;
                }
            }

            // Save the updated Excel file
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete student data from Excel file.");
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



