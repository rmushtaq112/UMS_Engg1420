package com.example.ums_engg1420.facultymodule;

// These imports bring in the classes we need
// We import our own data classes for Faculty and FacultySubject
import com.example.ums_engg1420.dataclasses.Faculty;
import com.example.ums_engg1420.dataclasses.FacultySubject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

// This class allows students/users to view information about faculty members and their subjects
// It's a different view from the FacultyController which is for faculty members to log in
public class UserFacultyController {

    // This table shows the list of faculty members
    // It demonstrates the concept of composition - our class contains other objects
    @FXML private TableView<Faculty> facultyTable;

    // These columns display different pieces of information about faculty members
    // Each column will show one property from the Faculty class
    @FXML private TableColumn<Faculty, String> colName, colDegree, colEmail, colOffice, colResearch;

    // This table shows the list of subjects and which faculty teach them
    @FXML private TableView<FacultySubject> facultySubjectTable;

    // These columns display information about subjects and their assigned faculty
    @FXML private TableColumn<FacultySubject, String> colSubjectCode, colSubjectName, colFacultyName;

    // This list stores all the faculty members
    // ObservableList is special - when it changes, the UI updates automatically
    // This is an example of encapsulation - we keep our data private
    private ObservableList<Faculty> facultyList = FXCollections.observableArrayList();

    // This list stores all the subject assignments
    private ObservableList<FacultySubject> facultySubjectList = FXCollections.observableArrayList();

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

        // Now do the same for the faculty subject table
        // Tell each column which property of FacultySubject to display
        colSubjectCode.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSubjectCode()));
        colSubjectName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSubjectName()));
        colFacultyName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFacultyName()));

        // Connect our facultySubjectList to the table
        facultySubjectTable.setItems(facultySubjectList);

        // Load all the data from Excel files when the program starts
        loadFacultyFromExcel();
        loadFacultySubjectsFromExcel();
    }

    // This method reads faculty information from an Excel file
    // It creates Faculty objects from each row in the file
    private void loadFacultyFromExcel() {
        File file = new File("facultymembers.xlsx");

        // Make sure the file exists before trying to read it
        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 Workbook workbook = WorkbookFactory.create(fileIn)) {
                // Open the Excel file

                Sheet sheet = workbook.getSheetAt(0);  // Get the first sheet
                facultyList.clear();  // Remove any existing faculty data

                // Look through each row in the Excel file
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;  // Skip the header row

                    // Get the values from each cell in this row
                    String name = row.getCell(0).getStringCellValue();
                    String degree = row.getCell(1).getStringCellValue();
                    String email = row.getCell(2).getStringCellValue();
                    String office = row.getCell(3).getStringCellValue();
                    String researchInterest = row.getCell(4).getStringCellValue();

                    // Create a new Faculty object with this data
                    // This is an example of instantiation - creating an object from a class
                    Faculty faculty = new Faculty(name, degree, email, office, researchInterest);
                    facultyList.add(faculty);  // Add to our list
                }

                // Update the table to show the new data
                facultyTable.refresh();
            } catch (IOException e) {
                // If something goes wrong, show an error message
                showAlert("Error", "Failed to load faculty data from Excel.");
            }
        }
    }

    // This method reads subject information from an Excel file
    // It's similar to loadFacultyFromExcel but creates FacultySubject objects
    private void loadFacultySubjectsFromExcel() {
        File file = new File("facultysubjects.xlsx");

        // Make sure the file exists before trying to read it
        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 Workbook workbook = WorkbookFactory.create(fileIn)) {
                // Open the Excel file

                Sheet sheet = workbook.getSheetAt(0);  // Get the first sheet
                facultySubjectList.clear();  // Remove any existing subject data

                // Look through each row in the Excel file
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;  // Skip the header row

                    // Get the values from each cell in this row
                    String subjectCode = row.getCell(0).getStringCellValue();
                    String subjectName = row.getCell(1).getStringCellValue();
                    String facultyName = row.getCell(2).getStringCellValue();

                    // Create a new FacultySubject object with this data
                    // This demonstrates using multiple classes together (Faculty and FacultySubject)
                    FacultySubject facultySubject = new FacultySubject(subjectCode, subjectName, facultyName);
                    facultySubjectList.add(facultySubject);  // Add to our list
                }

                // Update the table to show the new data
                facultySubjectTable.refresh();
            } catch (IOException e) {
                // If something goes wrong, show an error message
                showAlert("Error", "Failed to load faculty subject data from Excel.");
            }
        }
    }

    // This is a helper method to show popup messages to the user
    // It's an example of abstraction - hiding the complex details of creating alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);  // Create a warning popup
        alert.setTitle(title);                             // Set the title
        alert.setHeaderText(null);                         // No header text
        alert.setContentText(message);                     // Set the message
        alert.showAndWait();                               // Show popup and wait for user to close it
    }
}