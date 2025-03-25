package com.example.ums_engg1420.facultymodule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;

public class AdminFacultyController {

    @FXML private TextField nameField, degreeField, emailField, officeField, researchField;
    @FXML private TableView<Faculty> facultyTable;
    @FXML private TableColumn<Faculty, String> colName, colDegree, colEmail, colOffice, colResearch;

    private Map<String, Faculty> facultyDatabase = new HashMap<>();
    private ObservableList<Faculty> facultyList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colDegree.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDegree()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colOffice.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOffice()));
        colResearch.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getResearchInterest()));

        facultyTable.setItems(facultyList);
    }

    @FXML
    private void handleAddFaculty() {
        String name = nameField.getText();
        String degree = degreeField.getText();
        String email = emailField.getText();
        String office = officeField.getText();
        String researchInterest = researchField.getText();

        if (!email.isEmpty() && !facultyDatabase.containsKey(email)) {
            Faculty faculty = new Faculty(name, degree, email, office, researchInterest);
            facultyDatabase.put(email, faculty);
            facultyList.add(faculty);
            clearFields();
        } else {
            showAlert("Error", "Email already exists or empty");
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
