package com.example.ums_engg1420.facultymodule;

// Imports for our data classes (Faculty and Subject) and other needed tools
import com.example.ums_engg1420.dataclasses.Faculty;
import com.example.ums_engg1420.dataclasses.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

// This class is for administrators to manage faculty members and subjects
// It allows adding, editing, deleting faculty, and assigning them to subjects
// This is an example of a controller class in the MVC (Model-View-Controller) pattern
public class AdminFacultyController {

    // These are the input fields for adding/editing faculty information
    // They're connected to the UI elements in the FXML file
    @FXML private TextField nameField, degreeField, emailField, officeField, researchField;

    // This table shows the list of faculty members
    @FXML private TableView<Faculty> facultyTable;

    // These columns display different pieces of information about faculty members
    @FXML private TableColumn<Faculty, String> colName, colDegree, colEmail, colOffice, colResearch;

    // Buttons for deleting and editing faculty
    @FXML private Button deleteButton, editButton; // Added editButton

    // This table shows the list of subjects
    @FXML private TableView<Subject> subjectTable;

    // These columns display information about subjects
    @FXML private TableColumn<Subject, String> colSubject, colSubjectCode;

    // We use a HashMap to store faculty members with email as the key
    // This is an example of using a collection class from Java
    private Map<String, Faculty> facultyDatabase = new HashMap<>();

    // These lists store faculty and subject data in a format JavaFX can display
    // ObservableList is special because it automatically updates the UI when changed
    private ObservableList<Faculty> facultyList = FXCollections.observableArrayList();
    private ObservableList<Subject> subjectList = FXCollections.observableArrayList(); // ObservableList for subjects

    // This method runs automatically when the window opens
    // It sets up both tables and loads data from Excel files
    @FXML
    public void initialize() {
        // Tell each column which property of Faculty to display
        // This connects our data (Faculty objects) to what the user sees
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colDegree.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDegree()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colOffice.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOffice()));
        colResearch.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getResearchInterest()));

        // Connect our facultyList to the table so it displays our data
        facultyTable.setItems(facultyList);
        deleteButton.setDisable(true);  // Disable delete button until a faculty is selected
        editButton.setDisable(true); // Disable edit button until a faculty is selected

        // This is an event listener - it watches for when a faculty is selected
        // When selection changes, it enables or disables the delete and edit buttons
        // This demonstrates the Observer pattern, a common OOP design pattern
        facultyTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteButton.setDisable(newValue == null);
            editButton.setDisable(newValue == null); // Enable edit button if an item is selected
        });

        // Load faculty data from Excel when the application starts
        loadFacultyFromExcel();

        // Tell each column which property of Subject to display
        colSubject.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSubjectName()));
        colSubjectCode.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSubjectCode()));

        // Connect our subjectList to the subject table
        subjectTable.setItems(subjectList); // Bind subject list to subject table

        // Load subjects from the UMS_Data.xlsx file
        loadSubjectsFromExcel();
    }

    // This method runs when the "Add Faculty" button is clicked
    // It creates a new Faculty object from the form fields and adds it to our lists
    @FXML
    private void handleAddFaculty() {
        // Get values from the text fields
        String name = nameField.getText();
        String degree = degreeField.getText();
        String email = emailField.getText();
        String office = officeField.getText();
        String researchInterest = researchField.getText();

        // Check if the email is valid and not already in use
        // Email is our unique identifier for faculty members
        if (!email.isEmpty() && !facultyDatabase.containsKey(email)) {
            // Create a new Faculty object - this is an example of object instantiation
            Faculty faculty = new Faculty(name, degree, email, office, researchInterest);
            facultyDatabase.put(email, faculty);  // Add to our HashMap
            facultyList.add(faculty);             // Add to our observable list for the table
            saveFacultyToExcel();  // Save to Excel file right away
            clearFields();         // Clear the form fields
        } else {
            showAlert("Error", "Email already exists or empty");
        }
    }

    // This method runs when the "Delete Faculty" button is clicked
    // It removes the selected faculty from our lists and Excel file
    @FXML
    private void handleDeleteFaculty() {
        // Get the faculty member selected in the table
        Faculty selectedFaculty = facultyTable.getSelectionModel().getSelectedItem();
        if (selectedFaculty != null) {
            facultyDatabase.remove(selectedFaculty.getEmail());  // Remove from HashMap
            facultyList.remove(selectedFaculty);                 // Remove from observable list
            saveFacultyToExcel();  // Save changes to Excel file
        }
    }

    // This method runs when the "Edit Faculty" button is clicked
    // It fills the form fields with the selected faculty's information for editing
    @FXML
    private void handleEditFaculty() {
        // Get the faculty member selected in the table
        Faculty selectedFaculty = facultyTable.getSelectionModel().getSelectedItem();
        if (selectedFaculty != null) {
            // Fill the form fields with the faculty's current data
            nameField.setText(selectedFaculty.getName());
            degreeField.setText(selectedFaculty.getDegree());
            emailField.setText(selectedFaculty.getEmail());
            officeField.setText(selectedFaculty.getOffice());
            researchField.setText(selectedFaculty.getResearchInterest());

            // Disable delete button while editing to prevent confusion
            deleteButton.setDisable(true); // Disable delete button while editing
        }
    }

    // Helper method to clear all form fields
    // This shows good OOP practice by reusing code instead of repeating it
    private void clearFields() {
        nameField.clear();
        degreeField.clear();
        emailField.clear();
        officeField.clear();
        researchField.clear();
    }

    // Helper method to show alert messages to the user
    // This is an example of abstraction - hiding the complex details behind a simple method
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);  // Create a warning popup
        alert.setTitle(title);                      // Set the title
        alert.setHeaderText(null);                  // No header text
        alert.setContentText(message);              // Set the message
        alert.showAndWait();                        // Show popup and wait for user to close it
    }

    // This method saves all faculty data to an Excel file
    // It creates a new Excel file each time with the current faculty data
    private void saveFacultyToExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create a new Excel workbook and sheet
            Sheet sheet = workbook.createSheet("Faculty Members");

            // Create the header row with column names
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Name", "Degree", "Email", "Office", "Research Interest"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Write all faculty data into rows in the sheet
            int rowNum = 1;
            for (Faculty faculty : facultyDatabase.values()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(faculty.getName());
                row.createCell(1).setCellValue(faculty.getDegree());
                row.createCell(2).setCellValue(faculty.getEmail());
                row.createCell(3).setCellValue(faculty.getOffice());
                row.createCell(4).setCellValue(faculty.getResearchInterest());
            }

            // Save the workbook to a file
            File file = new File("facultymembers.xlsx");
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

            // Reload the faculty data to refresh the table
            // This ensures the table and our data stay in sync
            loadFacultyFromExcel();

            showAlert("Success", "Faculty data has been saved to facultymembers.xlsx.");
        } catch (IOException e) {
            showAlert("Error", "Failed to save faculty data to Excel.");
        }
    }

    // This method loads faculty data from the Excel file
    // It reads each row and creates Faculty objects
    private void loadFacultyFromExcel() {
        File file = new File("facultymembers.xlsx");
        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 Workbook workbook = WorkbookFactory.create(fileIn)) {
                // Open the existing Excel file

                Sheet sheet = workbook.getSheetAt(0);  // Get the first sheet
                facultyDatabase.clear();  // Clear existing data
                facultyList.clear();      // Clear existing list

                // Read each row in the sheet and add to our lists
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;  // Skip the header row
                    String name = row.getCell(0).getStringCellValue();
                    String degree = row.getCell(1).getStringCellValue();
                    String email = row.getCell(2).getStringCellValue();
                    String office = row.getCell(3).getStringCellValue();
                    String researchInterest = row.getCell(4).getStringCellValue();

                    // Create Faculty object and add to our lists
                    Faculty faculty = new Faculty(name, degree, email, office, researchInterest);
                    facultyDatabase.put(email, faculty);
                    facultyList.add(faculty);
                }

                facultyTable.refresh();  // Update the table display

            } catch (IOException e) {
                showAlert("Error", "Failed to load faculty data from Excel.");
            }
        }
    }

    // This method loads subject data from an Excel file
    // It reads each row and creates Subject objects
    private void loadSubjectsFromExcel() {
        File file = new File("src/main/resources/com/example/ums_engg1420/UMS_Data.xlsx");
        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 Workbook workbook = WorkbookFactory.create(fileIn)) {
                // Open the Excel file with subject data

                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                // Read each row and create Subject objects
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;  // Skip the header row

                    String subjectCode = row.getCell(0).getStringCellValue();
                    String subjectName = row.getCell(1).getStringCellValue();

                    // Create Subject object and add to our list
                    Subject subject = new Subject(subjectName, subjectCode);
                    subjectList.add(subject);
                }

                subjectTable.refresh();  // Update the table display

            } catch (IOException e) {
                showAlert("Error", "Failed to load subject data from UMS_Data.xlsx.");
            }
        }
    }

    // This method runs when the "Assign Faculty" button is clicked
    // It assigns a faculty member to teach a selected subject
    @FXML
    public void handleAssignFaculty(ActionEvent actionEvent) {
        // Get the subject selected in the table
        Subject selectedSubject = subjectTable.getSelectionModel().getSelectedItem();
        if (selectedSubject == null) {
            showAlert("Error", "Please select a subject.");
            return;
        }

        // Show a dialog to enter faculty name
        // This demonstrates creating UI elements programmatically
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Assign Faculty");
        dialog.setHeaderText("Enter Faculty Member Name:");
        dialog.setContentText("Faculty Name:");
        dialog.showAndWait().ifPresent(facultyName -> {
            // This code runs when the user enters a name and clicks OK

            // Find the faculty member by name (not case sensitive)
            // This uses Java 8 streams, a modern way to process collections
            Faculty assignedFaculty = facultyDatabase.values().stream()
                    .filter(faculty -> faculty.getName().equalsIgnoreCase(facultyName))
                    .findFirst()
                    .orElse(null);

            if (assignedFaculty == null) {
                showAlert("Error", "Faculty member not found.");
                return;
            }

            // Now we'll save the assignment to an Excel file
            File file = new File("facultysubjects.xlsx");
            Workbook workbook;
            Sheet sheet;

            try {
                if (file.exists()) {
                    // Open the existing file
                    try (FileInputStream fileIn = new FileInputStream(file)) {
                        workbook = WorkbookFactory.create(fileIn);
                        sheet = workbook.getSheetAt(0);
                    }
                } else {
                    // Create a new file if it doesn't exist
                    workbook = new XSSFWorkbook();
                    sheet = workbook.createSheet("Faculty Assignments");
                    // Create header row for the new file
                    Row headerRow = sheet.createRow(0);
                    headerRow.createCell(0).setCellValue("Subject Code");
                    headerRow.createCell(1).setCellValue("Subject Name");
                    headerRow.createCell(2).setCellValue("Faculty Name");
                    headerRow.createCell(3).setCellValue("Faculty Email");
                }

                // Add this assignment as a new row in the Excel file
                int rowNum = sheet.getLastRowNum() + 1;
                Row row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(selectedSubject.getSubjectCode());
                row.createCell(1).setCellValue(selectedSubject.getSubjectName());
                row.createCell(2).setCellValue(assignedFaculty.getName());
                row.createCell(3).setCellValue(assignedFaculty.getEmail());

                // Save the updated Excel file
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }

                showAlert("Success", "Faculty member assigned to subject successfully.");
            } catch (IOException e) {
                showAlert("Error", "Failed to assign faculty member to subject.");
            }
        });
    }

}