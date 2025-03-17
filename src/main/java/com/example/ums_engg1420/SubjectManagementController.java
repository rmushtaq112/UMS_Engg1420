package com.example.ums_engg1420;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SubjectManagementController {

    @FXML private TextField txtSubjectName;
    @FXML private TextField txtSubjectCode;
    @FXML private TextField txtSearch;
    @FXML private TableView<Subject> tblSubjects;
    @FXML private TableColumn<Subject, String> colSubjectName;
    @FXML private TableColumn<Subject, String> colSubjectCode;
    @FXML private Button btnAddSubject;
    @FXML private Button btnSearch;
    @FXML private Button btnEditSubject;
    @FXML private Button btnDeleteSubject;

    private ObservableList<Subject> subjectList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Load data from file when the app starts
        subjectList.addAll(DataPersistence.loadData());

        colSubjectName.setCellValueFactory(cellData -> cellData.getValue().subjectNameProperty());
        colSubjectCode.setCellValueFactory(cellData -> cellData.getValue().subjectCodeProperty());

        tblSubjects.setItems(subjectList);

        btnAddSubject.setOnAction(e -> addSubject());
        btnDeleteSubject.setOnAction(e -> deleteSubject());
        btnEditSubject.setOnAction(e -> editSubject());
        btnSearch.setOnAction(e -> searchSubjects());
    }

    private void addSubject() {
        String name = txtSubjectName.getText();
        String code = txtSubjectCode.getText();

        if (name.isEmpty() || code.isEmpty()) {
            showAlert("Error", "Subject Name and Code cannot be empty!");
            return;
        }

        for (Subject subject : subjectList) {
            if (subject.getSubjectCode().equals(code)) {
                showAlert("Error", "Subject Code must be unique!");
                return;
            }
        }

        Subject newSubject = new Subject(name, code);
        subjectList.add(newSubject);
        DataPersistence.saveData(subjectList); // Save data to file after adding

        txtSubjectName.clear();
        txtSubjectCode.clear();
    }

    private void deleteSubject() {
        Subject selected = tblSubjects.getSelectionModel().getSelectedItem();
        if (selected != null) {
            subjectList.remove(selected);
            DataPersistence.saveData(subjectList); // Save data to file after deletion
        } else {
            showAlert("Error", "No subject selected for deletion.");
        }
    }

    private void editSubject() {
        Subject selected = tblSubjects.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setSubjectName(txtSubjectName.getText());
            selected.setSubjectCode(txtSubjectCode.getText());
            tblSubjects.refresh();
            DataPersistence.saveData(subjectList); // Save data to file after editing
        } else {
            showAlert("Error", "No subject selected for editing.");
        }
    }

    private void searchSubjects() {
        String keyword = txtSearch.getText().toLowerCase();
        if (keyword.isEmpty()) {
            tblSubjects.setItems(subjectList);
            return;
        }

        ObservableList<Subject> filteredList = FXCollections.observableArrayList();
        for (Subject subject : subjectList) {
            if (subject.getSubjectName().toLowerCase().contains(keyword) ||
                    subject.getSubjectCode().toLowerCase().contains(keyword)) {
                filteredList.add(subject);
            }
        }
        tblSubjects.setItems(filteredList);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
