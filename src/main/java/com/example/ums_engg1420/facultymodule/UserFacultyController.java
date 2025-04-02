package com.example.ums_engg1420.facultymodule;

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

public class UserFacultyController {

    @FXML private TableView<Faculty> facultyTable;
    @FXML private TableColumn<Faculty, String> colName, colDegree, colEmail, colOffice, colResearch;

    @FXML private TableView<FacultySubject> facultySubjectTable;
    @FXML private TableColumn<FacultySubject, String> colSubjectCode, colSubjectName, colFacultyName;

    private ObservableList<Faculty> facultyList = FXCollections.observableArrayList();
    private ObservableList<FacultySubject> facultySubjectList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up faculty table columns to display faculty data
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colDegree.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDegree()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colOffice.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOffice()));
        colResearch.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getResearchInterest()));

        facultyTable.setItems(facultyList);

        // Set up faculty subject table columns to display subject data
        colSubjectCode.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSubjectCode()));
        colSubjectName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSubjectName()));
        colFacultyName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFacultyName()));

        facultySubjectTable.setItems(facultySubjectList);

        // Load faculty data from Excel when the application starts
        loadFacultyFromExcel();
        // Load faculty subject data from Excel when the application starts
        loadFacultySubjectsFromExcel();
    }

    // Load faculty data from Excel file (facultymembers.xlsx)
    private void loadFacultyFromExcel() {
        File file = new File("facultymembers.xlsx");
        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file); Workbook workbook = WorkbookFactory.create(fileIn)) {
                Sheet sheet = workbook.getSheetAt(0);
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
                    facultyList.add(faculty);
                }

                facultyTable.refresh();  // Refresh the table to display updated data
            } catch (IOException e) {
                showAlert("Error", "Failed to load faculty data from Excel.");
            }
        }
    }

    // Load faculty subject data from Excel file (facultysubjects.xlsx)
    private void loadFacultySubjectsFromExcel() {
        File file = new File("facultysubjects.xlsx");
        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file); Workbook workbook = WorkbookFactory.create(fileIn)) {
                Sheet sheet = workbook.getSheetAt(0);
                facultySubjectList.clear();  // Clear the existing list

                // Read each row in the sheet and add to the faculty subject list
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;  // Skip the header row
                    String subjectCode = row.getCell(0).getStringCellValue();
                    String subjectName = row.getCell(1).getStringCellValue();
                    String facultyName = row.getCell(2).getStringCellValue();

                    FacultySubject facultySubject = new FacultySubject(subjectCode, subjectName, facultyName);
                    facultySubjectList.add(facultySubject);
                }

                facultySubjectTable.refresh();  // Refresh the table to display updated data
            } catch (IOException e) {
                showAlert("Error", "Failed to load faculty subject data from Excel.");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
