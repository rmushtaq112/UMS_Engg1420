package com.example.ums_engg1420.facultymodule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminFacultyController {

    @FXML private TextField nameField, degreeField, emailField, officeField, researchField;
    @FXML private TableView<Faculty> facultyTable;
    @FXML private TableColumn<Faculty, String> colName, colDegree, colEmail, colOffice, colResearch;
    @FXML private Button deleteButton;

    private Map<String, Faculty> facultyDatabase = new HashMap<>();
    private ObservableList<Faculty> facultyList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colDegree.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDegree()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colOffice.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOffice()));
        colResearch.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getResearchInterest()));

        facultyTable.setItems(facultyList);
        deleteButton.setDisable(true);  // Disable delete button initially

        // Enable delete button when an item is selected
        facultyTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteButton.setDisable(newValue == null);
        });

        // Load faculty data from Excel when the application starts
        loadFacultyFromExcel();
    }

    @FXML
    private void handleAddFaculty() {
        String name = nameField.getText();
        String degree = degreeField.getText();
        String email = emailField.getText();
        String office = officeField.getText();
        String researchInterest = researchField.getText();

        // Check if email is not empty and doesn't exist in the database
        if (!email.isEmpty() && !facultyDatabase.containsKey(email)) {
            Faculty faculty = new Faculty(name, degree, email, office, researchInterest);
            facultyDatabase.put(email, faculty);
            facultyList.add(faculty);
            saveFacultyToExcel();  // Save to Excel when added
            clearFields();
        } else {
            showAlert("Error", "Email already exists or empty");
        }
    }

    @FXML
    private void handleDeleteFaculty() {
        Faculty selectedFaculty = facultyTable.getSelectionModel().getSelectedItem();
        if (selectedFaculty != null) {
            facultyDatabase.remove(selectedFaculty.getEmail());
            facultyList.remove(selectedFaculty);
            saveFacultyToExcel();  // Save after deletion
        }
    }

    private void clearFields() {
        nameField.clear();
        degreeField.clear();
        emailField.clear();
        officeField.clear();
        researchField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Save faculty data to Excel file (facultymembers.xlsx)
    private void saveFacultyToExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Faculty Members");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Name", "Degree", "Email", "Office", "Research Interest"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Write faculty data
            int rowNum = 1;
            for (Faculty faculty : facultyDatabase.values()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(faculty.getName());
                row.createCell(1).setCellValue(faculty.getDegree());
                row.createCell(2).setCellValue(faculty.getEmail());
                row.createCell(3).setCellValue(faculty.getOffice());
                row.createCell(4).setCellValue(faculty.getResearchInterest());
            }

            // Save to file in project directory
            File file = new File("facultymembers.xlsx");
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

            // Reload the faculty data from the updated file to refresh the table
            loadFacultyFromExcel();

            showAlert("Success", "Faculty data has been saved to facultymembers.xlsx.");
        } catch (IOException e) {
            showAlert("Error", "Failed to save faculty data to Excel.");
        }
    }

    // Load faculty data from Excel file (facultymembers.xlsx)
    private void loadFacultyFromExcel() {
        File file = new File("facultymembers.xlsx");
        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file); Workbook workbook = WorkbookFactory.create(fileIn)) {
                Sheet sheet = workbook.getSheetAt(0);
                facultyDatabase.clear();  // Clear the existing faculty data
                facultyList.clear();  // Clear the existing list

                // Read each row in the sheet and add to the faculty list
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;  // Skip the header row
                    String name = row.getCell(0).getStringCellValue();
                    String degree = row.getCell(1).getStringCellValue();
                    String email = row.getCell(2).getStringCellValue();
                    String office = row.getCell(3).getStringCellValue();
                    String researchInterest = row.getCell(4).getStringCellValue();

                    Faculty faculty = new Faculty(name, degree, email, office, researchInterest);
                    facultyDatabase.put(email, faculty);
                    facultyList.add(faculty);
                }

                facultyTable.refresh();  // Refresh the table to display updated data

            } catch (IOException e) {
                showAlert("Error", "Failed to load faculty data from Excel.");
            }
        }
    }

    // Faculty class to hold faculty details
    public static class Faculty {
        private String name;
        private String degree;
        private String email;
        private String office;
        private String researchInterest;

        public Faculty(String name, String degree, String email, String office, String researchInterest) {
            this.name = name;
            this.degree = degree;
            this.email = email;
            this.office = office;
            this.researchInterest = researchInterest;
        }

        public String getName() { return name; }
        public String getDegree() { return degree; }
        public String getEmail() { return email; }
        public String getOffice() { return office; }
        public String getResearchInterest() { return researchInterest; }
    }
}
